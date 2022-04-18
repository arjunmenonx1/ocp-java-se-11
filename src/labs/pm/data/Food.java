package labs.pm.data;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Food extends Product{
    private LocalDate bestBefore;
    public Food(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        super(id, name, price, rating);
        this.bestBefore = bestBefore;
    }

    public LocalDate getBestBefore() {
        return bestBefore;
    }

    @Override
    public BigDecimal getDiscount() {
        // apply 10% discount rate if bestBefore date is today
        return (bestBefore.isEqual(LocalDate.now())) ? super.getDiscount(): BigDecimal.ZERO ;
    }

    @Override
    public Product applyRating(Rating newRating) {
        return new Food(getId(),getName(),getPrice(),newRating,bestBefore);
    }

    @Override
    public String toString() {
        return super.toString()+", " + bestBefore;
    }
}
