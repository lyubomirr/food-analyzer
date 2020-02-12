package bg.sofia.uni.fmi.mjt.foodanalyzer.server.models;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.utils.ServerMessages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class FoodSearchResult {
    private final Collection<Food> foods;

    public FoodSearchResult() {
        this.foods = new ArrayList<>();
    }

    public FoodSearchResult(Collection<Food> foods) {
        this.foods = foods;
    }

    public Collection<Food> getFoods() {
        return foods;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format(ServerMessages.RESULTS_FOUND, foods.size()));
        for(var food : foods) {
            builder.append(food.toString());
        }

        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodSearchResult that = (FoodSearchResult) o;
        return Objects.equals(getFoods(), that.getFoods());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFoods());
    }

    public boolean isEmpty() {
        return foods.size() == 0;
    }
}