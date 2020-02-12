package bg.sofia.uni.fmi.mjt.foodanalyzer.server.models;

import java.util.Objects;

public class LabelNutrients {
    private final double fat;
    private final double carbohydrates;
    private final double protein;
    private final double fiber;
    private final double calories;

    public LabelNutrients(double fat, double carbohydrates, double protein,
                          double fiber, double calories) {
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.protein = protein;
        this.fiber = fiber;
        this.calories = calories;
    }

    public double getFat() {
        return fat;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public double getProtein() {
        return protein;
    }

    public double getFiber() {
        return fiber;
    }

    public double getCalories() {
        return calories;
    }


    @Override
    public String toString() {
        return String.format("\n\tFat: %.2f,\n\tCarbohydrates: %.2f,\n\tProtein: %.2f,\n\tFiber: %.2f,\n\t" +
                "Calories: %.2f\n", fat, carbohydrates, protein, fiber, calories);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LabelNutrients that = (LabelNutrients) o;
        return Double.compare(that.getFat(), getFat()) == 0 &&
                Double.compare(that.getCarbohydrates(), getCarbohydrates()) == 0 &&
                Double.compare(that.getProtein(), getProtein()) == 0 &&
                Double.compare(that.getFiber(), getFiber()) == 0 &&
                Double.compare(that.getCalories(), getCalories()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFat(), getCarbohydrates(), getProtein(), getFiber(), getCalories());
    }

    public static LabelNutrients nullObject() {
        return new LabelNutrients(0,0,0,0,0);
    }
}