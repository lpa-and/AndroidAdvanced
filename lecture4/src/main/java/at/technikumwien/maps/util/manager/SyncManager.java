package at.technikumwien.maps.util.manager;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import at.technikumwien.maps.AppDependencyManager;
import at.technikumwien.maps.data.NoOpOnOperationSuccessfulCallback;
import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.OnOperationSuccessfulCallback;
import at.technikumwien.maps.data.local.DrinkingFountainRepo;
import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.data.remote.DrinkingFountainApi;
import at.technikumwien.maps.data.remote.response.DrinkingFountainResponse;
import at.technikumwien.maps.service.SyncDrinkingFountainsJobService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class SyncManager {

    private static String TAG = SyncManager.class.getSimpleName();

    private static final int SYNC_JOB_ID = 271;

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
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        JobInfo jobInfo = new JobInfo.Builder(SYNC_JOB_ID, new ComponentName(context, SyncDrinkingFountainsJobService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(24*60*60*1000) // daily
                .setPersisted(true)
                .build();

        jobScheduler.schedule(jobInfo);
    }

    // Sync drinking fountains without getting the data in a callback
    public Cancelable syncDrinkingFountains(@NonNull final OnOperationSuccessfulCallback callback) {
        return syncDrinkingFountains(callback, null);
    }

    // Sync drinking fountains and get the data in a callback
    public Cancelable syncDrinkingFountains(
            @NonNull final OnOperationSuccessfulCallback callback,
            final OnDataLoadedCallback<List<DrinkingFountain>> loadedCallback
    ) {
        Call<DrinkingFountainResponse> call = drinkingFountainApi.getDrinkingFountains();
        call.enqueue(new Callback<DrinkingFountainResponse>() {
            @Override
            public void onResponse(Call<DrinkingFountainResponse> call, Response<DrinkingFountainResponse> response) {
                if(response.isSuccessful()) {
                    // Response contains our data
                    List<DrinkingFountain> drinkingFountainList = response.body().getDrinkingFountainList();
                    if(loadedCallback != null) { loadedCallback.onDataLoaded(drinkingFountainList); }
                    drinkingFountainRepo.refreshList(callback, drinkingFountainList);
                } else {
                    if(loadedCallback != null) { loadedCallback.onDataLoadError(new HttpException(response)); }
                    callback.onOperationError(new HttpException(response));
                }
            }

            @Override
            public void onFailure(Call<DrinkingFountainResponse> call, Throwable t) {
                if(loadedCallback != null) { loadedCallback.onDataLoadError(t); }
                callback.onOperationError(t);
            }
        });

        return new CallCancelable(call);
    }

    // Load drinking fountains locally and sync if no local data is present
    public void loadDrinkingFountains(final OnDataLoadedCallback<List<DrinkingFountain>> callback) {
        drinkingFountainRepo.loadAll(new OnDataLoadedCallback<List<DrinkingFountain>>() {
            @Override
            public void onDataLoaded(List<DrinkingFountain> data) {
                if(data.isEmpty()) {
                    syncDrinkingFountains(new NoOpOnOperationSuccessfulCallback(), callback);
                } else {
                    callback.onDataLoaded(data);
                }
            }

            @Override
            public void onDataLoadError(Throwable throwable) {
                callback.onDataLoadError(throwable);
            }
        });
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
