package at.technikumwien.maps;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;

import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.data.remote.AsyncTaskDrinkingFountainRepo;
import at.technikumwien.maps.data.remote.DrinkingFountainRepo;
import at.technikumwien.maps.data.remote.MockDrinkingFountainRepo;
import at.technikumwien.maps.data.remote.RetrofitDrinkingFountainApi;
import at.technikumwien.maps.data.remote.RetrofitDrinkingFountainRepo;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppDependencyManager {

    private final Context appContext;
    private DrinkingFountainRepo drinkingFountainRepo;
    private RetrofitDrinkingFountainApi drinkingFountainApi;
    private Gson gson;
    private OkHttpClient okHttpClient;

    public AppDependencyManager(Context appContext) {
        this.appContext = appContext;
    }

    public Context getAppContext() {
        return appContext;
    }

    public Resources getResources() {
        return appContext.getResources();
    }

    public Gson getGson() {
        if(gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public OkHttpClient getOkHttpClient() {
        if(okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        return okHttpClient;
    }

    public RetrofitDrinkingFountainApi getDrinkingFountainApi() {
        if(drinkingFountainApi == null) {
            GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(getGson());

            OkHttpClient.Builder okHttpClientBuilder = getOkHttpClient().newBuilder();

            if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClientBuilder.addInterceptor(loggingInterceptor);
            }

            OkHttpClient loggingOkHttpClient = okHttpClientBuilder.build();

            Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(DrinkingFountainRepo.BASE_URL)
                                .addConverterFactory(gsonConverterFactory)
                                .callFactory(loggingOkHttpClient)
                                .build();

            drinkingFountainApi = retrofit.create(RetrofitDrinkingFountainApi.class);
        }
        return drinkingFountainApi;
    }

    public DrinkingFountainRepo getDrinkingFountainRepo() {
        if(drinkingFountainRepo == null) {
            //drinkingFountainRepo = new MockDrinkingFountainRepo();
            //drinkingFountainRepo = new AsyncTaskDrinkingFountainRepo();
            drinkingFountainRepo = new RetrofitDrinkingFountainRepo(this);
        }

        return drinkingFountainRepo;
    }
}
