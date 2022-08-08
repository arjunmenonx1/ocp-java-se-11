package labs.pm.app;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import labs.pm.data.Product;
import labs.pm.data.ProductManager;
import labs.pm.data.Rating;

public class Shop {

  public static void main(String[] args) {
    ProductManager pm = ProductManager.getInstance();
    AtomicInteger clientCount = new AtomicInteger(0);
    Callable<String> client = () -> {
      String clientId = "Client " + clientCount.incrementAndGet();
      String threadName = Thread.currentThread().getName();
      int productId = ThreadLocalRandom.current().nextInt(63) + 101;
      String languageTag = ProductManager.getSupportedLocales().stream()
          .skip(ThreadLocalRandom.current().nextInt(4)).findFirst().get();
      StringBuilder log = new StringBuilder();
      log.append(clientId + " " + threadName + "\n \tstart of log\t \n");
      log.append("\n \t end of log \t \n");
      log.append(pm.getDiscounts(languageTag).entrySet().stream()
          .map(entry -> entry.getKey() + "\t" + entry.getValue()).collect(
              Collectors.joining("\n")));
      Product product = pm.reviewProduct(productId, Rating.FOUR_STAR, "Yet another review");
      log.append((product != null) ? "\nProduct " + productId + " reviewed\n"
          : "\nProduct " + productId + " not reviewed\n");
      pm.printProductReport(productId, languageTag, clientId);
      log.append(clientId + " generated report for " + productId + " product");
      return log.toString();
    };
    List<Callable<String>> clients = Stream.generate(() -> client).limit(5)
        .collect(Collectors.toList());
    ExecutorService executorService = Executors.newFixedThreadPool(3);
    try {
      List<Future<String>> results = executorService.invokeAll(clients);
      executorService.shutdown();
      results.stream().forEach(result -> {
        try {
          System.out.println(result.get());
        } catch (InterruptedException | ExecutionException e) {
          Logger.getLogger(Shop.class.getName())
              .log(Level.SEVERE, "Error retrieving client log", e);
        }
      });
    } catch (InterruptedException e) {
      Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, "Error invoking clients", e);
    }

//    pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
//    pm.parseProduct("D,101,Tea,1.99,0,2021-09-21");
//    pm.parseReview("101,4,Nice hot cup of tea");
//    pm.parseReview("101,3,Ok cup of tea");
//    pm.parseReview("101,5,Great cup of tea");
//    pm.parseReview("101,1,Horrible cup of tea");
//    pm.parseReview("101,4,Nice hot cup of tea");
//
//    pm.printProductReport(101);
    pm.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
    pm.reviewProduct(102, Rating.FOUR_STAR, "Nice hot cup of coffee");
    pm.reviewProduct(102, Rating.THREE_STAR, "Ok cup of coffee");
    pm.reviewProduct(102, Rating.THREE_STAR, "Nice coffee, mate!");
    pm.reviewProduct(102, Rating.ONE_STAR, "Horrible cup of coffee");
    pm.reviewProduct(102, Rating.ONE_STAR, "I asked for tea");
    //pm.printProductReport(102, "en-GB");

//    pm.parseProduct("F,103,Cake,3.99,0,2021-09-21");
//    pm.createProduct(103, "Cake", BigDecimal.valueOf(3.99), Rating.FIVE_STAR,
//        LocalDate.now().plusDays(2));
//    pm.reviewProduct(103, Rating.FOUR_STAR, "Cake Cake Cake!");
//    pm.reviewProduct(103, Rating.THREE_STAR, "Just fine");
//    pm.reviewProduct(103, Rating.THREE_STAR, "Needs more cream");
//    pm.reviewProduct(103, Rating.ONE_STAR, "Not good at all");
//    pm.reviewProduct(103, Rating.ONE_STAR, "I asked for tea again");
//    pm.printProductReport(103);
//    pm.createProduct(104, "Cookie", BigDecimal.valueOf(3.99), Rating.TWO_STAR,
//        LocalDate.now());
//    pm.reviewProduct(104, Rating.FOUR_STAR, "Nice and fresh");
//    pm.reviewProduct(104, Rating.FIVE_STAR, "Perfect");
////    pm.printProductReport(104);
////    pm.changeLocale("es-US");
//    pm.createProduct(105, "Hot Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
//    pm.reviewProduct(105, Rating.FOUR_STAR, "Delicious");
//    pm.reviewProduct(105, Rating.FIVE_STAR, "Amazing");
//    pm.reviewProduct(105, Rating.FIVE_STAR, "Made my day!");
//    pm.reviewProduct(105, Rating.THREE_STAR, "Portion size is too small");
////    pm.printProductReport(105);
//    pm.createProduct(106, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
//    pm.reviewProduct(106, Rating.FOUR_STAR, "My favorite");
//    pm.reviewProduct(106, Rating.THREE_STAR, "Sweet and decadent");
//    pm.printProductReport(106);
////---------------------------------------------------------------------------
//// Logic for using comparator alone in printProducts method
////    pm.printProducts((p1, p2) -> p2.getRating().ordinal() - p1.getRating()
////        .ordinal()); // lambda functions to sort based on rating
////    pm.printProducts((p1, p2) -> p2.getPrice().compareTo(p1.getPrice()));
////
////    Comparator<Product> ratingSorter = (p1, p2) -> p2.getRating().ordinal() - p1.getRating()
////        .ordinal();
////    Comparator<Product> priceSorter = (p1, p2) -> p2.getPrice().compareTo(p1.getPrice());
////    pm.printProducts(ratingSorter.thenComparing(priceSorter)); // Chaining comparators/sorters
////---------------------------------------------------------------------------
    pm.printProducts(p -> p.getPrice().floatValue() < 2,
        (p1, p2) -> p2.getRating().ordinal() - p1.getRating().ordinal(),
        "en-GB"); // (Predicate, Comparator)
//    pm.getDiscounts().forEach((rating, discount) -> System.out.println(rating + "\t" + discount));
  }

}
