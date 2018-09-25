package at.technikumwien.maps.data.remote;

import java.io.IOException;
import java.util.List;

import at.technikumwien.maps.AppDependencyManager;
import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.data.remote.response.DrinkingFountainResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitDrinkingFountainRepo implements DrinkingFountainRepo {

    private final RetrofitDrinkingFountainApi drinkingFountainApi;

    public RetrofitDrinkingFountainRepo(AppDependencyManager dependencyManager) {
        drinkingFountainApi = dependencyManager.getDrinkingFountainApi();
    }

    @Override
    public void loadDrinkingFountains(final OnDataLoadedCallback<List<DrinkingFountain>> onDataLoadedCallback) {
        Callback<DrinkingFountainResponse> retrofitCallback = new Callback<DrinkingFountainResponse>() {
            @Override
            public void onResponse(Call<DrinkingFountainResponse> call, Response<DrinkingFountainResponse> response) {
                if(response.isSuccessful()) {
                    DrinkingFountainResponse drinkingFountainResponse = response.body();
                    List<DrinkingFountain> drinkingFountains = drinkingFountainResponse.asDrinkingFountainList();
                    onDataLoadedCallback.onDataLoaded(drinkingFountains);
                } else {
                    onDataLoadedCallback.onDataLoadError(new IOException("Could not load drinking fountains"));
                }
            }

            @Override
            public void onFailure(Call<DrinkingFountainResponse> call, Throwable t) {
                onDataLoadedCallback.onDataLoadError(t);
            }
        };

        drinkingFountainApi.getDrinkingFountains().enqueue(retrofitCallback);

        // drinkingFountainApi.getDrinkingFountains().execute();
    }

}
