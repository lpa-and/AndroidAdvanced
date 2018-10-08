package at.technikumwien.maps.ui.maps;

import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

import at.technikumwien.maps.AppDependencyManager;
import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.OnOperationSuccessfulCallback;
import at.technikumwien.maps.data.local.DrinkingFountain;
import at.technikumwien.maps.data.local.RoomDrinkingFountainRepo;
import at.technikumwien.maps.data.remote.retrofit.DrinkingFountainApi;
import at.technikumwien.maps.data.remote.retrofit.response.DrinkingFountainResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class MapsPresenter extends MvpBasePresenter<MapsView> {

    private final RoomDrinkingFountainRepo roomDrinkingFountainRepo;
    private final DrinkingFountainApi drinkingFountainApi;

    public MapsPresenter(AppDependencyManager manager) {
        roomDrinkingFountainRepo = manager.getRoomDrinkingFountainRepo();
        drinkingFountainApi = manager.getDrinkingFountainApi();
    }

    public void loadDrinkingFountains() {
        roomDrinkingFountainRepo.getAllDrinkingFountains(new OnDataLoadedCallback<List<DrinkingFountain>>() {

            @Override
            public void onDataLoaded(List<DrinkingFountain> data) {
                if(data.isEmpty()) {
                    Log.d("MapsPresenter", "No local data, loading drinking fountains from web service");
                    refreshDrinkingFountains();
                } else {
                    Log.d("MapsPresenter", "Drinking fountains loaded from local storage");
                    if(isViewAttached()) {
                        getView().showDrinkingFountains(data);
                    }
                }
            }

            @Override
            public void onDataLoadError(Throwable throwable) {
                if(isViewAttached()) {
                    getView().showLoadingError(throwable);
                }
            }
        });
    }

    public void refreshDrinkingFountains() {
        drinkingFountainApi.getDrinkingFountains().enqueue(new Callback<DrinkingFountainResponse>() {
            @Override
            public void onResponse(Call<DrinkingFountainResponse> call, Response<DrinkingFountainResponse> response) {
                if(response.isSuccessful()) {
                    DrinkingFountainResponse drinkingFountainResponse = response.body();
                    List<DrinkingFountain> drinkingFountainList = drinkingFountainResponse.getDrinkingFountainList();
                    if(isViewAttached()) {
                        getView().showDrinkingFountains(drinkingFountainList);
                    }
                    saveDrinkingFountainList(drinkingFountainList);
                } else {
                    if(isViewAttached()) {
                        getView().showLoadingError(new HttpException(response));
                    }
                }
            }

            @Override
            public void onFailure(Call<DrinkingFountainResponse> call, Throwable t) {
                if(isViewAttached()) {
                    getView().showLoadingError(t);
                }
            }
        });
    }

    private void saveDrinkingFountainList(List<DrinkingFountain> drinkingFountainList) {
        roomDrinkingFountainRepo.insertDrinkingFountains(new OnOperationSuccessfulCallback() {
            @Override
            public void onOperationSuccessful() {
                Log.d("MapsPresenter", "Drinking fountains saved successfully");
            }

            @Override
            public void onOperationError(Throwable throwable) {
                Log.e("MapsPresenter", "Error saving drinking fountains", throwable);
            }

        }, drinkingFountainList.toArray(new DrinkingFountain[]{}));
    }
}
