package com.grad_project_mobile.AsyncTasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.grad_project_mobile.activities.MainActivity;
import com.grad_project_mobile.client.models.connection.DiscoverySender;
import com.grad_project_mobile.shared.JsonParser;

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
        ((MainActivity)activity).onDiscoverEnd(JsonParser.getInstance().fromJson(s, String[].class));
    }
}
