package bg.sofia.uni.fmi.mjt.foodanalyzer.server;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.FoodDetails;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.LabelNutrients;

public class TestObjects {
    public static FoodDetails getFoodDetails(String foodId, String gtinUpc) {
        final var description = "desc";
        final var ingredients = "ingr";
        final var servingSize = 3;
        final var servingUnit = "mg";

        return new FoodDetails(foodId, gtinUpc, description, ingredients,
                servingSize, servingUnit, LabelNutrients.nullObject());
    }

    public static Food getFood(String description) {
        final var fdcId = "1";
        final var ingredient = "salt";
        final var brand = "nestle";
        final var gtinUpc = "123";
        return new Food(fdcId, description, ingredient, brand, gtinUpc);
    }
}
