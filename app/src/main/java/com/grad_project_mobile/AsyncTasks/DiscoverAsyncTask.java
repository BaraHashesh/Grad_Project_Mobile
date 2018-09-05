package com.grad_project_mobile.AsyncTasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.grad_project_mobile.activities.MainActivity;
import com.grad_project_mobile.client.models.connection.DiscoverySender;

public class DiscoverAsyncTask extends AsyncTask<String, String, String> {

    private Activity activity;

    public DiscoverAsyncTask(Activity activity) {
        this.activity=activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... strings) {
        DiscoverySender discoverySender = new DiscoverySender(strings[0]);
        discoverySender.start();

        return discoverySender.getResults().toString();
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(this.activity.getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}
