package labs.pm.app;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import labs.pm.data.Product;
import labs.pm.data.ProductManager;
import labs.pm.data.Rating;

public class Shop {

  public static void main(String[] args) {
    ProductManager pm = new ProductManager("en-GB");
    pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
    pm.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cup of tea");
    pm.reviewProduct(101, Rating.THREE_STAR, "Ok cup of tea");
    pm.reviewProduct(101, Rating.FIVE_STAR, "Great cup of tea");
    pm.reviewProduct(101, Rating.ONE_STAR, "Horrible cup of tea");
    pm.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cup of tea");
    pm.printProductReport(101);
    pm.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
    pm.reviewProduct(102, Rating.FOUR_STAR, "Nice hot cup of coffee");
    pm.reviewProduct(102, Rating.THREE_STAR, "Ok cup of coffee");
    pm.reviewProduct(102, Rating.THREE_STAR, "Nice coffee, mate!");
    pm.reviewProduct(102, Rating.ONE_STAR, "Horrible cup of coffee");
    pm.reviewProduct(102, Rating.ONE_STAR, "I asked for tea");
    pm.printProductReport(102);
    pm.createProduct(103, "Cake", BigDecimal.valueOf(3.99), Rating.FIVE_STAR,
        LocalDate.now().plusDays(2));
    pm.reviewProduct(103, Rating.FOUR_STAR, "Cake Cake Cake!");
    pm.reviewProduct(103, Rating.THREE_STAR, "Just fine");
    pm.reviewProduct(103, Rating.THREE_STAR, "Needs more cream");
    pm.reviewProduct(103, Rating.ONE_STAR, "Not good at all");
    pm.reviewProduct(103, Rating.ONE_STAR, "I asked for tea again");
    pm.printProductReport(103);
    pm.createProduct(104, "Cookie", BigDecimal.valueOf(3.99), Rating.TWO_STAR,
        LocalDate.now());
    pm.reviewProduct(104, Rating.FOUR_STAR, "Nice and fresh");
    pm.reviewProduct(104, Rating.FIVE_STAR, "Perfect");
    pm.printProductReport(104);
    pm.changeLocale("es-US");
    pm.createProduct(105, "Hot Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
    pm.reviewProduct(105, Rating.FOUR_STAR, "Delicious");
    pm.reviewProduct(105, Rating.FIVE_STAR, "Amazing");
    pm.reviewProduct(105, Rating.FIVE_STAR, "Made my day!");
    pm.reviewProduct(105, Rating.THREE_STAR, "Portion size is too small");
    pm.printProductReport(105);
    pm.createProduct(106, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
    pm.reviewProduct(106, Rating.FOUR_STAR, "My favorite");
    pm.reviewProduct(106, Rating.THREE_STAR, "Sweet and decadent");
    pm.printProductReport(106);
    pm.printProducts((p1, p2) -> p2.getRating().ordinal() - p1.getRating()
        .ordinal()); // lambda functions to sort based on rating
    pm.printProducts((p1, p2) -> p2.getPrice().compareTo(p1.getPrice()));

    Comparator<Product> ratingSorter = (p1, p2) -> p2.getRating().ordinal() - p1.getRating()
        .ordinal();
    Comparator<Product> priceSorter = (p1, p2) -> p2.getPrice().compareTo(p1.getPrice());
    pm.printProducts(ratingSorter.thenComparing(priceSorter)); // Chaining comparators/sorters
  }

}
