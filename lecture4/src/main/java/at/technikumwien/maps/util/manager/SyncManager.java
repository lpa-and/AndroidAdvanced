package at.technikumwien.maps.util.manager;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import at.technikumwien.maps.AppDependencyManager;
import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.OnOperationSuccessfulCallback;
import at.technikumwien.maps.data.local.DrinkingFountainRepo;
import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.data.remote.DrinkingFountainApi;
import retrofit2.Call;

public class SyncManager {

    private static String TAG = SyncManager.class.getSimpleName();

    private final Context context;
    private final DrinkingFountainRepo drinkingFountainRepo;
    private final DrinkingFountainApi drinkingFountainApi;

    public SyncManager(AppDependencyManager manager) {
        context = manager.getAppContext();
        drinkingFountainApi = manager.getDrinkingFountainApi();
        drinkingFountainRepo = manager.getDrinkingFountainRepo();
    }

    // Schedule a periodic sync via our JobService
    public void schedulePeriodicSync() {

    }

    // Sync drinking fountains without getting the data in a callback
    public Cancelable syncDrinkingFountains(@NonNull final OnOperationSuccessfulCallback callback) {
        return syncDrinkingFountains(callback, null);
    }

    // Sync drinking fountains and get the data in a callback
    public Cancelable syncDrinkingFountains(@NonNull  final OnOperationSuccessfulCallback callback, final OnDataLoadedCallback<List<DrinkingFountain>> loadedCallback) {
        return null;
    }

    // Load drinking fountains locally and sync if no local data is present
    public void loadDrinkingFountains(final OnDataLoadedCallback<List<DrinkingFountain>> callback) {

    }

    private static class CallCancelable implements Cancelable {
        private final Call<?> call;

        CallCancelable(Call<?> call) {
            this.call = call;
        }

        @Override
        public void cancel() {
            call.cancel();
        }

        @Override
        public boolean isCanceled() {
            return call.isCanceled();
        }
    }
}
