package labs.pm.data;


import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProductManager {

  private static final Logger logger = Logger.getLogger(ProductManager.class.getName());

  private Product product;
  private final Map<Product, List<Review>> products = new HashMap<>();

  private ResourceBundle config = ResourceBundle.getBundle("labs.pm.data.config");
  private MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));
  private MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));
  private ResourceFormatter formatter;

  private static final Map<String, ResourceFormatter> formatters = Map.of("en-GB",
      new ResourceFormatter(Locale.UK),
      "en-US", new ResourceFormatter(Locale.US),
      "es-US", new ResourceFormatter(new Locale("es", "US")),
      "fr-FR", new ResourceFormatter(Locale.FRANCE),
      "zh-CN", new ResourceFormatter(Locale.CHINA)
  );

  public void changeLocale(String languageTag) {
    formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));
  }

  public static Set<String> getSupportedLocales() {
    return formatters.keySet();
  }

  public ProductManager(String languageTag) {
    changeLocale(languageTag);
  }

  public ProductManager(Locale locale) {
    this(locale.toLanguageTag());
  }

  public void createProduct(int id, String name, BigDecimal price, Rating rating,
      LocalDate bestBefore) {
    product = new Food(id, name, price, rating, bestBefore);
    products.putIfAbsent(product, new ArrayList<>());
  }

  public void createProduct(int id, String name, BigDecimal price, Rating rating) {
    product = new Drink(id, name, price, rating);
    products.putIfAbsent(product, new ArrayList<>());
  }

  public void parseReview(String text) {
    try {
      Object[] values = reviewFormat.parse(text);
      reviewProduct(Integer.parseInt((String) values[0]),
          Rateable.convert(Integer.parseInt((String) values[1])), (String) values[2]);
    } catch (ParseException | NumberFormatException e) {
      logger.log(Level.WARNING, "Error parsing review " + text, e);
    }
  }

  public void parseProduct(String text) {
    try {
      Object[] values = productFormat.parse(text);
      int id = Integer.parseInt((String) values[1]);
      String name = (String) values[2];
      BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String) values[3]));
      Rating rating = Rateable.convert(Integer.parseInt((String) values[4]));
      switch ((String) values[0]) {
        case "D" -> createProduct(id, name, price, rating);
        case "F" -> {
          LocalDate bestBefore = LocalDate.parse((String) values[5]);
          createProduct(id, name, price, rating, bestBefore);
        }
      }

    } catch (ParseException | NumberFormatException | DateTimeException e) {
      logger.log(Level.WARNING, "Error parsing product " + text + " " + e.getMessage());
    }
  }

  public void reviewProduct(Product product, Rating rating, String comments) {
    List<Review> reviews = products.get(product);
    products.remove(product, reviews);
    reviews.add(new Review(rating, comments));
//    int sum = 0;
//    for (Review review : reviews) {
//      sum += review.getRating().ordinal();
//    }
//    product = product.applyRating(Rateable.convert(Math.round((float) sum / reviews.size())));
    product = product.applyRating(Rateable.convert((int) Math.round(
        reviews.stream().mapToInt(r -> r.getRating().ordinal()).average().orElse(0))));
    products.put(product, reviews);
  }

  public void printProductReport(Product product) {
    List<Review> reviews = products.get(product);
    Collections.sort(reviews);
    StringBuilder txt = new StringBuilder();
    txt.append(formatter.formatProduct(product));
    txt.append('\n');

//    for (Review review : reviews) {
//      txt.append(formatter.formatReview(review));
//      txt.append('\n');
//    }
//
//    if (reviews.isEmpty()) {
//      txt.append(formatter.getText("no.reviews"));
//      txt.append('\n');
//    }
    if (reviews.isEmpty()) {
      txt.append(formatter.getText("no.reviews")).append('\n');
    } else {
      txt.append(reviews.stream().map(r -> formatter.formatReview(r) + '\n')
          .collect(Collectors.joining()));
    }
    System.out.println(txt);
  }

  public void printProductReport(int id) {
    try {
      printProductReport(findProduct(id));
    } catch (ProductManagerException e) {
      logger.log(Level.SEVERE, e.getMessage());
    }
  }

  public void printProducts(Predicate<Product> filter, Comparator<Product> sorter) {
//    List<Product> productList = new ArrayList<>(products.keySet());
//    productList.sort(sorter);
    StringBuilder txt = new StringBuilder();

//    for (Product product : productList) {
//      txt.append(formatter.formatProduct(product));
//      txt.append('\n');
//    }
    products.keySet().stream().sorted(sorter).filter(filter)
        .forEach(p -> txt.append(formatter.formatProduct(p))
            .append('\n'));
    System.out.println(txt);
  }

  public Map<String, String> getDiscounts() {
    return products.keySet().stream().collect(
        Collectors.groupingBy(product -> product.getRating().getStars(),
            Collectors.collectingAndThen(
                Collectors.summingDouble(product -> product.getDiscount().doubleValue()),
                discount -> formatter.moneyFormat.format(discount))));
  }

  public Product findProduct(int id) throws ProductManagerException {
//    Product result = null;
//    for (Product product : products.keySet()) {
//      if (product.getId() == id) {
//        result = product;
//        break;
//      }
//    }
//    return result;
    return products.keySet().stream().filter(p -> p.getId() == id).findFirst()
        .orElseThrow(() -> new ProductManagerException("Product with id " + id + " not found"));
  }

  public void reviewProduct(int id, Rating rating, String comments) {
    try {
      reviewProduct(findProduct(id), rating, comments);
    } catch (ProductManagerException e) {
      logger.log(Level.SEVERE, e.getMessage());
    }
  }


  private static class ResourceFormatter {

    private final ResourceBundle resources;
    private final DateTimeFormatter dateFormat;
    private final NumberFormat moneyFormat;

    private ResourceFormatter(Locale locale) {
      resources = ResourceBundle.getBundle("labs.pm.data.resources", locale);
      dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
      moneyFormat = NumberFormat.getCurrencyInstance(locale);
    }

    private String formatProduct(Product product) {
      return MessageFormat.format(resources.getString("product"), product.getName(),
          moneyFormat.format(product.getPrice()), product.getRating().getStars(),
          dateFormat.format(product.getBestBefore()));
    }

    private String formatReview(Review review) {
      return MessageFormat.format(resources.getString("review"), review.getRating().getStars(),
          review.getComments());
    }

    private String getText(String key) {
      return resources.getString(key);
    }
  }
}

