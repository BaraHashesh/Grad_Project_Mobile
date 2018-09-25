package com.grad_project_mobile.AsyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.grad_project_mobile.client.models.connection.BrowsingClient;

public class BrowseAsyncTask extends AsyncTask<String, String, String> {

    private Activity activity;
    private String serverIP;

    public BrowseAsyncTask(Activity activity, String IP) {
        this.activity=activity;
        this.serverIP = IP;
    }

    @Override
    protected String doInBackground(String... strings) {
        new BrowsingClient(this.serverIP).browse(strings[0]);

        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//        Toast.makeText(activity.getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
