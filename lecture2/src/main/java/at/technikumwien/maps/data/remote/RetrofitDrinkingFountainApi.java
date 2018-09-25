package at.technikumwien.maps.data.remote;

import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.data.remote.response.DrinkingFountainResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitDrinkingFountainApi {

    @GET(DrinkingFountainRepo.GET_DRINKING_FOUNTAINS)
    Call<DrinkingFountainResponse> getDrinkingFountains();

    // @GET("drinkingfountain/{id}")
    // Call<DrinkingFountain> getDrinkingFountain(@Path("id") String id);

}
