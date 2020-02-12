package bg.sofia.uni.fmi.mjt.foodanalyzer.server.models;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils.Constants;

import java.util.Objects;

public class FoodDetails {
    private final String fdcId;
    private final String gtinUpc;
    private final String description;
    private final String ingredients;

    private final double servingSize;
    private final String servingSizeUnit;

    private final LabelNutrients labelNutrients;

    public FoodDetails(String fdcId, String gtinUpc, String description, String ingredients,
                       double servingSize, String servingSizeUnit, LabelNutrients labelNutrients) {
        this.fdcId = fdcId;
        this.gtinUpc = gtinUpc;
        this.description = description;
        this.ingredients = ingredients;
        this.servingSize = servingSize;
        this.servingSizeUnit = servingSizeUnit;
        this.labelNutrients = labelNutrients;
    }

    public String getFdcId() {
        return fdcId;
    }

    public String getGtinUpc() {
        return gtinUpc;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public double getServingSize() {
        return servingSize;
    }

    public String getServingSizeUnit() {
        return servingSizeUnit;
    }

    public LabelNutrients getLabelNutrients() {
        return labelNutrients;
    }

    @Override
    public String toString() {
        return String.format("Food id: %s,\nGTIN or UPC: %s,\nDescription: %s,\nIngredients: %s,\n" +
                        "Nutrients per serving of %.2f%s: %s\n",
                getFdcId(), getGtinUpc(), getDescription(), getIngredients(),
                getServingSize(), getServingSizeUnit(), getLabelNutrients().toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodDetails that = (FoodDetails) o;
        return Double.compare(that.getServingSize(), getServingSize()) == 0 &&
                Objects.equals(getFdcId(), that.getFdcId()) &&
                Objects.equals(getGtinUpc(), that.getGtinUpc()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getIngredients(), that.getIngredients()) &&
                Objects.equals(getServingSizeUnit(), that.getServingSizeUnit()) &&
                Objects.equals(getLabelNutrients(), that.getLabelNutrients());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFdcId(), getGtinUpc(), getDescription(), getIngredients(),
                getServingSize(), getServingSizeUnit(), getLabelNutrients());
    }

    public static FoodDetails nullObject() {
        return new FoodDetails(Constants.EMPTY_STRING, Constants.EMPTY_STRING,
                Constants.EMPTY_STRING, Constants.EMPTY_STRING,
                0, Constants.EMPTY_STRING,  LabelNutrients.nullObject());
    }
}
