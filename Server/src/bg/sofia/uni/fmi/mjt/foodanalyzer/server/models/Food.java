package bg.sofia.uni.fmi.mjt.foodanalyzer.server.models;

import java.util.Objects;

public class Food {
    private static final String NO_GTIN_UPC = "No GTIN OR UPC.";

    private final String fdcId;
    private final String description;
    private final String ingredients;
    private final String brandOwner;
    private final String gtinUpc;

    public Food(String fdcId, String description, String ingredients, String brandOwner, String gtinUpc) {
        this.fdcId = fdcId;
        this.description = description;
        this.ingredients = ingredients;
        this.brandOwner = brandOwner;
        this.gtinUpc = gtinUpc;
    }

    public String getFdcId() {
        return fdcId;
    }

    public String getDescription() {
        return description;
    }

    public String getGtinUpc() {
        return gtinUpc;
    }

    @Override
    public String toString() {
        String gtinUpcString = this.gtinUpc;
        if(gtinUpcString == null || gtinUpcString.isBlank()) {
            gtinUpcString = NO_GTIN_UPC;
        }

        return String.format("{ Food Id: \"%s\", Description: \"%s\", GTIN or UPC: \"%s\" }\n",
                fdcId, description, gtinUpcString);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return Objects.equals(getFdcId(), food.getFdcId()) &&
                Objects.equals(getDescription(), food.getDescription()) &&
                Objects.equals(getIngredients(), food.getIngredients()) &&
                Objects.equals(getBrandOwner(), food.getBrandOwner()) &&
                Objects.equals(getGtinUpc(), food.getGtinUpc());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFdcId(), getDescription(), getIngredients(), getBrandOwner(), getGtinUpc());
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getBrandOwner() {
        return brandOwner;
    }
}
