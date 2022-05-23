package labs.pm.app;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;
import labs.pm.data.Product;
import labs.pm.data.ProductManager;
import labs.pm.data.Rating;

public class Shop {

  public static void main(String[] args) {
    ProductManager pm = new ProductManager(Locale.UK);
    Product p1 = pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
    p1 = pm.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cup of tea");
    p1 = pm.reviewProduct(101, Rating.THREE_STAR, "Ok cup of tea");
    p1 = pm.reviewProduct(101, Rating.FIVE_STAR, "Great cup of tea");
    p1 = pm.reviewProduct(101, Rating.ONE_STAR, "Horrible cup of tea");
    p1 = pm.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cup of tea");
    pm.printProductReport(101);
    Product p2 = pm.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
    p2 = pm.reviewProduct(102, Rating.FOUR_STAR, "Nice hot cup of coffee");
    p2 = pm.reviewProduct(102, Rating.THREE_STAR, "Ok cup of coffee");
    p2 = pm.reviewProduct(102, Rating.THREE_STAR, "Nice coffee, mate!");
    p2 = pm.reviewProduct(102, Rating.ONE_STAR, "Horrible cup of coffee");
    p2 = pm.reviewProduct(102, Rating.ONE_STAR, "I asked for tea");
    pm.printProductReport(102);
    Product p3 = pm.createProduct(103, "Cake", BigDecimal.valueOf(3.99), Rating.FIVE_STAR,
        LocalDate.now().plusDays(2));
    p3 = pm.reviewProduct(103, Rating.FOUR_STAR, "Cake Cake Cake!");
    p3 = pm.reviewProduct(103, Rating.THREE_STAR, "Just fine");
    p3 = pm.reviewProduct(103, Rating.THREE_STAR, "Needs more cream");
    p3 = pm.reviewProduct(103, Rating.ONE_STAR, "Not good at all");
    p3 = pm.reviewProduct(103, Rating.ONE_STAR, "I asked for tea again");
    pm.printProductReport(103);
    Product p4 = pm.createProduct(104, "Cookie", BigDecimal.valueOf(3.99), Rating.TWO_STAR,
        LocalDate.now());
    p4 = pm.reviewProduct(104, Rating.FOUR_STAR, "Nice and fresh");
    p4 = pm.reviewProduct(104, Rating.FIVE_STAR, "Perfect");
    pm.printProductReport(104);
    Product p5 = pm.createProduct(105, "Hot Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
    p5 = pm.reviewProduct(105, Rating.FOUR_STAR, "Delicious");
    p5 = pm.reviewProduct(105, Rating.FIVE_STAR, "Amazing");
    p5 = pm.reviewProduct(105, Rating.FIVE_STAR, "Made my day!");
    p5 = pm.reviewProduct(105, Rating.THREE_STAR, "Portion size is too small");
    pm.printProductReport(105);
    Product p6 = pm.createProduct(106, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
    p6 = pm.reviewProduct(106, Rating.FOUR_STAR, "My favorite");
    p6 = pm.reviewProduct(106, Rating.THREE_STAR, "Sweet and decadent");
    pm.printProductReport(106);

  }

}
