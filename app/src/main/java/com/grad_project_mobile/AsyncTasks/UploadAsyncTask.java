package com.grad_project_mobile.AsyncTasks;

import android.os.AsyncTask;

import com.grad_project_mobile.activities.BrowserUpdater;
import com.grad_project_mobile.client.models.connection.UploadClient;

import java.io.File;

public class UploadAsyncTask extends AsyncTask<String, String, String> {

    private BrowserUpdater activity;
    private String serverIP;

    public UploadAsyncTask(BrowserUpdater browserUpdater, String serverIP){
        this.activity = browserUpdater;
        this.serverIP = serverIP;
    }

    @Override
    protected String doInBackground(String... strings) {
        UploadClient uploadClient = new UploadClient(activity, this.serverIP);

        uploadClient.upload(new File(strings[0]), strings[1]);

        return "";
    }
}