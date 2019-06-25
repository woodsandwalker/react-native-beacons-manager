package com.mackentoch.beaconsandroid;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.HeadlessJsTaskService;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.jstasks.HeadlessJsTaskConfig;

import static com.mackentoch.beaconsandroid.BeaconsAndroidModule.LOG_TAG;

public class BeaconsAndroidTransitionService extends HeadlessJsTaskService {

    @Override
    @Nullable
    protected HeadlessJsTaskConfig getTaskConfig(Intent intent)
    {
        Log.d(LOG_TAG, "BeaconsAndroidTransitionService START...");

        WritableMap jsArgs = Arguments.createMap();
        return new HeadlessJsTaskConfig(BeaconsAndroidModule.TRANSITION_TASK_NAME, jsArgs, 0, true);
    }
}
