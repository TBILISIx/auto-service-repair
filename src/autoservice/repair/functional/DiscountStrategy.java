package autoservice.repair.functional;

@FunctionalInterface
public interface DiscountStrategy {

    double apply(double price);

}