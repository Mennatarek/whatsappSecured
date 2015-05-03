package com.whatsapp.activities;

import android.app.Activity;

/**
 * Created by hesham on 02/05/15.
 */
public abstract class ActivityTracker extends Activity {

    public final void updateActivityFromBgThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateActivity();
            }});
    }

    public abstract void updateActivity();

    @Override
    protected void onStart() {
        super.onStart();
//        BgThread app = (BgThread)getApplication();
//        app.setUpdatable(this);
    }

    @Override
    protected void onStop() {
//        BgThread app = (BgThread)getApplication();
//        app.setUpdatable(null);
        super.onStop();
    }
}
