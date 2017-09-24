package com.example.loso.friendtracker.http;

import android.app.Activity;
import android.os.AsyncTask;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Lettisia George on 24/09/2017.
 */

public class HttpAsyncTask extends AsyncTask {
    public static final String LOG_TAG = "HttpAsyncTask";
    Activity activity = null;
    private String url;

    public HttpAsyncTask(Activity activity, String url) {
        this.activity = activity;
        this.url = url;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(url);

        try {
            // get the result
            String responseBody = httpclient.execute(getRequest,
                    new BasicResponseHandler());

            // send the response somewhere
            // activity.someMethod(responseBody);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
