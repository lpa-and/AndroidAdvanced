package at.technikumwien.maps.ui.maps;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

import at.technikumwien.maps.AppDependencyManager;
import at.technikumwien.maps.data.NoOpOnOperationSuccessfulCallback;
import at.technikumwien.maps.data.local.DrinkingFountainRepo;
import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.data.remote.DrinkingFountainApi;
import at.technikumwien.maps.data.remote.response.DrinkingFountainResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class MapsPresenter extends MvpBasePresenter<MapsView> {

    private final DrinkingFountainApi drinkingFountainApi;
    private final DrinkingFountainRepo drinkingFountainRepo;

    private LiveData<List<DrinkingFountain>> liveData;
    private Observer<List<DrinkingFountain>> observer = new Observer<List<DrinkingFountain>>() {
        @Override
        public void onChanged(@Nullable List<DrinkingFountain> drinkingFountains) {
            Log.i("MapsPresenter", "Drinking fountain list changed, size=" + drinkingFountains.size());
            if(isViewAttached()) { getView().showDrinkingFountains(drinkingFountains); }
        }
    };

    public MapsPresenter(AppDependencyManager manager) {
        drinkingFountainApi = manager.getDrinkingFountainApi();
        drinkingFountainRepo = manager.getDrinkingFountainRepo();
        liveData = manager.getDrinkingFountainRepo().loadAllWithChanges();
    }

    @Override
    public void attachView(MapsView view) {
        super.attachView(view);
        liveData.observeForever(observer);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        liveData.removeObserver(observer);
    }

    public void loadDrinkingFountains() {
        drinkingFountainApi.getDrinkingFountains().enqueue(new Callback<DrinkingFountainResponse>() {
            @Override
            public void onResponse(Call<DrinkingFountainResponse> call, Response<DrinkingFountainResponse> response) {
                if(response.isSuccessful()) {
                    drinkingFountainRepo.refreshList(new NoOpOnOperationSuccessfulCallback(), response.body().getDrinkingFountainList());
                }
            }

            @Override
            public void onFailure(Call<DrinkingFountainResponse> call, Throwable throwable) {

            }
        });
    }
}
