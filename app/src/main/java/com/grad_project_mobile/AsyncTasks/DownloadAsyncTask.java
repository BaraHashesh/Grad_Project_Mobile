package com.grad_project_mobile.AsyncTasks;

import android.os.AsyncTask;

import com.grad_project_mobile.activities.BrowserUpdater;

import com.grad_project_mobile.client.models.connection.DownloadClient;

public class DownloadAsyncTask extends AsyncTask<String, String, String> {

    private BrowserUpdater activity;
    private String serverIP;

    public DownloadAsyncTask(BrowserUpdater browserUpdater, String serverIP){
        this.activity = browserUpdater;
        this.serverIP = serverIP;
    }

    @Override
    protected String doInBackground(String... strings) {
        DownloadClient downloadClient = new DownloadClient(activity, this.serverIP);

        downloadClient.download(strings[0], strings[1]);

        return "";
    }
}
