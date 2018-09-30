package com.grad_project_mobile.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.grad_project_mobile.AsyncTasks.BrowseAsyncTask;
import com.grad_project_mobile.BrowserUpdater;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        Intent myIntent = getIntent();

        serverIP = myIntent.getStringExtra("serverIP");

        pathTextView = findViewById(R.id.browser_activity_path_text_view);
        recyclerView = findViewById(R.id.browser_activity_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        fileInfoAdapter = new FileInfoAdapter(this, new ArrayList<>());
        fileInfoAdapter.setListener(this);

        recyclerView.setAdapter(fileInfoAdapter);

        new BrowseAsyncTask(this, serverIP).execute("");

        pathList = new ArrayList<>(10);
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

    }

    @Override
    public void onFileDelete(FileRowData file, View v) {

    }

    @Override
    public void onFileDownload(FileRowData file, View v) {

    }

    @Override
    public void onFolderBrowse(FileRowData file, View v) {
        pathList.add(file.getParent());
        pathTextView.setText(file.getPath());

        new BrowseAsyncTask(this, serverIP).execute(file.getPath());
    }
}


