package com.grad_project_mobile.activities;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.grad_project_mobile.AsyncTasks.BrowseAsyncTask;
import com.grad_project_mobile.AsyncTasks.DownloadAsyncTask;
import com.grad_project_mobile.R;
import com.grad_project_mobile.adapters.FileInfoAdapter;
import com.grad_project_mobile.client.models.models.FileRowData;

import java.util.ArrayList;
import java.util.Collections;

public class BrowseActivity extends AppCompatActivity
        implements BrowserUpdater, FileInfoAdapter.FileInfoClickListener {

    RecyclerView recyclerView;
    FileInfoAdapter fileInfoAdapter;
    ArrayList<String> pathList;
    String serverIP;
    TextView pathTextView;
    String downloadPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        Button refreshButton = findViewById(R.id.browser_activity_refresh_button);
        Button backButton = findViewById(R.id.browser_activity_back_button);

        Intent myIntent = getIntent();

        serverIP = myIntent.getStringExtra("serverIP");

        pathTextView = findViewById(R.id.browser_activity_path_text_view);
        recyclerView = findViewById(R.id.browser_activity_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        fileInfoAdapter = new FileInfoAdapter(this, new ArrayList<>());
        fileInfoAdapter.setListener(this);

        recyclerView.setAdapter(fileInfoAdapter);

        backButton.setOnClickListener(v -> {
            /*
            Check if the pathList is empty
            */
            if (this.pathList.size() != 0) {
                // get previous path and remove if from the path list
                String path = this.pathList.remove(this.pathList.size() - 1);

                new BrowseAsyncTask(this, serverIP, false).execute(path);

                pathTextView.setText(path);
            }
        });

        refreshButton.setOnClickListener(v -> {
            new BrowseAsyncTask(this, serverIP, false).execute(pathTextView.getText().toString());
        });


        new BrowseAsyncTask(this, serverIP, false).execute("");

        pathList = new ArrayList<>(10);
        
        downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/";
        
        //// TODO: 10/4/18 Add upload function

    }

    @Override
    public void update(FileRowData[] files, boolean isSuccessful) {
        runOnUiThread(() -> {
            if(isSuccessful){
                ArrayList<FileRowData> result = new ArrayList<>(files.length);

                Collections.addAll(result, files);

                fileInfoAdapter.setFiles(result);

                fileInfoAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void update(String pathToUpdate) {
        runOnUiThread(() -> {
            /*
            Check if the folder which was update is of importance to the current folder
             */
            if (pathTextView.getText().toString().contains(pathToUpdate)) {
                new BrowseAsyncTask(this, serverIP, false).execute(pathTextView.getText().toString());
            }
        });
    }

    @Override
    public void makeMessages(String msg) {
        runOnUiThread(() -> {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onFileDelete(FileRowData file, View v) {
        new BrowseAsyncTask(this, serverIP, true).execute(file.getPath());
    }

    @Override
    public void onFileDownload(FileRowData file, View v) {
        new DownloadAsyncTask(this, serverIP).execute(downloadPath, file.getPath());
    }

    @Override
    public void onFolderBrowse(FileRowData file, View v) {
        pathList.add(file.getParent());
        pathTextView.setText(file.getPath());

        new BrowseAsyncTask(this, serverIP, false).execute(file.getPath());
    }
}


