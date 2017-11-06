package at.technikumwien.maps.util.managers;


import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

import at.technikumwien.maps.AppDependencyManager;
import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.OnOperationSuccessfulCallback;
import at.technikumwien.maps.data.local.DrinkingFountainRepo;
import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.data.remote.DrinkingFountainApi;
import at.technikumwien.maps.service.SyncDrinkingFountainJobService;
import retrofit2.Call;

public class SyncManager {

    private static int SYNC_JOB_ID = 732547;

    private DrinkingFountainRepo drinkingFountainRepo;
    private DrinkingFountainApi drinkingFountainApi;
    private Context context;

    public SyncManager(AppDependencyManager manager) {
        drinkingFountainRepo = manager.getDrinkingFountainRepo();
        drinkingFountainApi = manager.getDrinkingFountainApi();
        context = manager.getAppContext();
    }


    public void scheduleSync() {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        JobInfo jobInfo = new JobInfo.Builder(SYNC_JOB_ID, new ComponentName(context, SyncDrinkingFountainJobService.class))
                .setPersisted(true)
                .setPeriodic(7L*24L*60L*60L*1000L) // weekly
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setRequiresDeviceIdle(true)
                .setRequiresCharging(true)
                .build();

        jobScheduler.schedule(jobInfo);
    }

    public Cancelable syncDrinkingFountains(final OnOperationSuccessfulCallback callback,
                                      final OnDataLoadedCallback<List<DrinkingFountain>> dataLoadedCallback) {
        /* TODO:
         *       Load drinking fountains from web service
         *       and then save it in the repository
         *
         *       callback is used for notifying the caller of the status
         *       of the whole sync operation
         *
         *       dataLoadedCallback is used for notifying the caller when
         *       new drinking fountain was loaded from the web service
         *
         *       The error callbacks of both callback parameters should be
         *       called accordingly
         *
         *       The caller of this method should be able to cancel data loading.
         *       Therefore save the Call<> object returned by Retrofit and use
         *       this to return a CallCancelable.
         */
        return null;
    }

    public void loadDrinkingFountains(final OnDataLoadedCallback<List<DrinkingFountain>> dataLoadedCallback) {
        /* TODO:
         *       Load drinking fountains from local cache.
         *
         *       If data is available in the local cache, notify the caller
         *       via the callback onDataLoaded().
         *
         *       If no data is available, use syncDrinkingFountains() to sync
         *       the data. You can pass through the OnDataLoadedCallback<> parameter.
         */
    }

    private static class CallCancelable implements Cancelable {

        private final Call<?> call;

        public CallCancelable(Call<?> call) {
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
