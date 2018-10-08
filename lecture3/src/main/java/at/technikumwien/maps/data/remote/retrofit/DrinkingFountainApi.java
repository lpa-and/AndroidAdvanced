package at.technikumwien.maps.data.remote.retrofit;

import at.technikumwien.maps.data.remote.retrofit.response.DrinkingFountainResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface DrinkingFountainApi {

    String BASE_URL = "https://data.wien.gv.at/daten/";
    String GET_DRINKING_FOUNTAINS_PATH = "geo?service=WFS&request=GetFeature&version=1.1.0&typeName=ogdwien:TRINKBRUNNENOGD&srsName=EPSG:4326&outputFormat=json";

    @GET(GET_DRINKING_FOUNTAINS_PATH)
    Call<DrinkingFountainResponse> getDrinkingFountains();
}
