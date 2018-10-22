package at.technikumwien.maps.service;

import android.app.job.JobParameters;
import android.app.job.JobService;

import at.technikumwien.maps.MyApplication;
import at.technikumwien.maps.data.OnOperationSuccessfulCallback;
import at.technikumwien.maps.util.manager.Cancelable;
import at.technikumwien.maps.util.manager.SyncManager;

public class SyncDrinkingFountainsJobService extends JobService {

    private Cancelable cancelable = null;

    @Override
    public boolean onStartJob(final JobParameters params) {
        // Start syncing
        SyncManager syncManager = ((MyApplication)getApplicationContext()).getAppDependencyManager().getSyncManager();

        cancelable = syncManager.syncDrinkingFountains(new OnOperationSuccessfulCallback() {
            @Override
            public void onOperationSuccessful() {
                jobFinished(params, false);
            }

            @Override
            public void onOperationError(Throwable throwable) {
                jobFinished(params, true);
            }
        });
        // or use:
        // new JobServiceOnOperationSuccessFulCallback(this, params);

        // work is still done in a background thread!
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if(cancelable != null && !cancelable.isCanceled()) { cancelable.cancel(); }
        cancelable = null;
        // please reschedule!
        return true;
    }

}
