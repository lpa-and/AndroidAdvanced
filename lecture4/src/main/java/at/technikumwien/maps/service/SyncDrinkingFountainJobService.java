package at.technikumwien.maps.service;


import android.app.job.JobParameters;
import android.app.job.JobService;

import java.util.List;

import at.technikumwien.maps.MyApplication;
import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.OnOperationSuccessfulCallback;
import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.util.managers.Cancelable;
import at.technikumwien.maps.util.managers.SyncManager;

public class SyncDrinkingFountainJobService extends JobService {

    private Cancelable cancelable = null;

    @Override
    public boolean onStartJob(final JobParameters params) {
        SyncManager syncManager = ((MyApplication)this.getApplicationContext()).getAppDependencyManager().getSyncManager();
        cancelable = syncManager.syncDrinkingFountains(
                new JobServiceOnOperationSuccessFulCallback(this, params),
                new OnDataLoadedCallback<List<DrinkingFountain>>() {
            @Override
            public void onDataLoaded(List<DrinkingFountain> data) { }

            @Override
            public void onDataLoadError(Throwable throwable) { }
        }
        );

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if(cancelable != null) {
            cancelable.cancel();
            cancelable = null;
        }
        return true;
    }
}
