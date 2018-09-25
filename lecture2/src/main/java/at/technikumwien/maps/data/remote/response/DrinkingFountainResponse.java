package at.technikumwien.maps.data.remote.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import at.technikumwien.maps.data.model.DrinkingFountain;

public class DrinkingFountainResponse {

    List<Feature> features;

    public class Feature {
        String id;
        Properties properties;
        Geometry geometry;
    }

    public class Properties {
        @SerializedName("NAME")
        String name;
    }

    public class Geometry {
        List<Double> coordinates;
    }

    public List<DrinkingFountain> asDrinkingFountainList() {
        List<DrinkingFountain> drinkingFountains = new ArrayList<>(features.size());

        for(Feature feature : features) {
            DrinkingFountain df = DrinkingFountain.create(
                    feature.id, feature.properties.name,
                    feature.geometry.coordinates.get(1), feature.geometry.coordinates.get(0)
            );
            drinkingFountains.add(df);
        }

        return drinkingFountains;
    }
}
