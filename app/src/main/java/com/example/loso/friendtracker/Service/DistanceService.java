package com.example.loso.friendtracker.Service;

import android.app.IntentService;
import android.content.Intent;


/**
 * Created by letti on 29/09/2017.
 */

public class DistanceService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DistanceService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
