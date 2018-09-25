package at.technikumwien.maps.data.remote;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.util.DownloadAsyncTask;

public class AsyncTaskDrinkingFountainRepo implements DrinkingFountainRepo {

    @Override
    public void loadDrinkingFountains(OnDataLoadedCallback<List<DrinkingFountain>> callback) {
        new DownloadAsyncTask<List<DrinkingFountain>>(DrinkingFountainRepo.BASE_URL + DrinkingFountainRepo.GET_DRINKING_FOUNTAINS, callback) {

            @Override
            protected List<DrinkingFountain> parseJson(String json) throws JSONException {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray features = jsonObject.getJSONArray("features");

                List<DrinkingFountain> drinkingFountains = new ArrayList<>(features.length());

                for(int i=0; i<features.length(); i++) {
                    JSONObject feature = features.getJSONObject(i);
                    String id = feature.getString("id");
                    JSONObject properties = feature.getJSONObject("properties");
                    String name = properties.getString("NAME");
                    JSONObject geometry = feature.getJSONObject("geometry");
                    JSONArray coordinates = geometry.getJSONArray("coordinates");
                    double lat = coordinates.getDouble(1);
                    double lng = coordinates.getDouble(0);
                    DrinkingFountain df = DrinkingFountain.create(id, name, lat, lng);
                    drinkingFountains.add(df);
                }


                return drinkingFountains;
            }

        }.execute();
    }

}
