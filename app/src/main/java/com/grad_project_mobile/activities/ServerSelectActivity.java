package com.grad_project_mobile.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.grad_project_mobile.R;
import com.grad_project_mobile.adapters.ServerInfoAdapter;
import com.grad_project_mobile.client.models.models.ServerRowInfo;
import com.grad_project_mobile.shared.JsonParser;

import java.util.ArrayList;

public class ServerSelectActivity extends AppCompatActivity implements ServerInfoAdapter.ServerInfoClickListener {

    ServerInfoAdapter serverInfoAdapter;
    RecyclerView serverRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_select);

        serverRecyclerView = findViewById(R.id.activity_server_select);
        serverRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        serverRecyclerView.setHasFixedSize(true);

        ArrayList<ServerRowInfo> serverRowInfo = new ArrayList<>();

       serverRowInfo.add(new ServerRowInfo("1.1.1"));

        serverRowInfo.add(new ServerRowInfo("1.2.1"));

        serverInfoAdapter = new ServerInfoAdapter(this, serverRowInfo);
        serverInfoAdapter.setListener(this);

        serverRecyclerView.setAdapter(serverInfoAdapter);
    }

    @Override
    public void onServerSelected(ServerRowInfo server, View v) {
        Toast.makeText(getApplicationContext(), server.getIp(), Toast.LENGTH_SHORT).show();
    }
}
