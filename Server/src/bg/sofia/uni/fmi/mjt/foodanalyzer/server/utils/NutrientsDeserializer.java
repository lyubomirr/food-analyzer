package bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.models.LabelNutrients;
import com.google.gson.*;

import java.lang.reflect.Type;

public class NutrientsDeserializer implements JsonDeserializer<LabelNutrients> {
    private static final String FAT = "fat";
    private static final String CARBOHYDRATES = "carbohydrates";
    private static final String PROTEIN = "protein";
    private static final String FIBER = "fiber";
    private static final String CALORIES = "calories";
    private static final String VALUE = "value";


    @Override
    public LabelNutrients deserialize(JsonElement jsonElement, Type type,
                              JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        var jsonObject = jsonElement.getAsJsonObject();

        var fat = getValue(jsonObject, FAT);
        var carbs = getValue(jsonObject, CARBOHYDRATES);
        var protein = getValue(jsonObject, PROTEIN);
        var fiber = getValue(jsonObject, FIBER);
        var calories = getValue(jsonObject, CALORIES);

        return new LabelNutrients(fat, carbs, protein, fiber, calories);
    }

    private Double getValue(JsonObject jsonObject, String memberName) {
        var member = jsonObject.get(memberName).getAsJsonObject();
        var value = member.get(VALUE).getAsDouble();
        return value;
    }
}
