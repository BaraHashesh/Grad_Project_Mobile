package com.grad_project_mobile.AsyncTasks;

import android.os.AsyncTask;

import com.grad_project_mobile.activities.BrowserUpdater;
import com.grad_project_mobile.client.models.connection.BrowsingClient;

public class BrowseAsyncTask extends AsyncTask<String, String, String> {

    private BrowserUpdater activity;
    private String serverIP;
    private static BrowsingClient browsingClient;

    public BrowseAsyncTask(BrowserUpdater activity, String IP) {
        this.activity=activity;
        this.serverIP = IP;
    }

    @Override
    protected String doInBackground(String... strings) {
        /*
        Construct connection if not already connected
         */
        if(browsingClient == null)
            browsingClient = new BrowsingClient(activity,this.serverIP);

        browsingClient.browse(strings[0]);

        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//        Toast.makeText(activity.getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
