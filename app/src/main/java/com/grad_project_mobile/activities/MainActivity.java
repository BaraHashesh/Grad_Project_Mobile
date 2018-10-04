package com.grad_project_mobile.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.grad_project_mobile.AsyncTasks.BrowseAsyncTask;
import com.grad_project_mobile.AsyncTasks.DiscoverAsyncTask;
import com.grad_project_mobile.BrowserUpdater;
import com.grad_project_mobile.R;
import com.grad_project_mobile.adapters.ServerInfoAdapter;
import com.grad_project_mobile.client.models.models.ServerRowInfo;
import com.grad_project_mobile.shared.Constants;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements ServerInfoAdapter.ServerInfoClickListener {


    Button searchButton;
    EditText ipEditText;
    ProgressBar loadingProgressBar;
    TextView loadingTextView;
    RecyclerView serverRecyclerView;
    ServerInfoAdapter serverInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("javax.net.ssl.trustStore", "/res/raw/keystore_server");
        System.setProperty("javax.net.ssl.trustStorePassword", "password");

        searchButton = findViewById(R.id.activity_main_search_button);
        ipEditText = findViewById(R.id.activity_main_ip_edit_text);
        loadingProgressBar = findViewById(R.id.activity_main_loading_bar);
        loadingTextView = findViewById(R.id.activity_main_loading_text_view);
        serverRecyclerView = findViewById(R.id.activity_main_server_recycler_view);

        serverRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        serverRecyclerView.setHasFixedSize(true);

        serverInfoAdapter = new ServerInfoAdapter(this, new ArrayList<ServerRowInfo>());
        serverInfoAdapter.setListener(this);

        serverRecyclerView.setAdapter(serverInfoAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = ipEditText.getText().toString();

                boolean valid = Constants.IPV4_REGEX.matcher(ip).matches();

                /*
                Check if IP is valid
                 */
                if(valid) {
                    DiscoverAsyncTask discoverAsyncTask = new DiscoverAsyncTask(MainActivity.this);
                    discoverAsyncTask.execute(ip);

                    serverRecyclerView.setVisibility(View.INVISIBLE);

                    loadingProgressBar.setVisibility(View.VISIBLE);
                    loadingTextView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Valid IP Address", Toast.LENGTH_SHORT).show();
                }

                /*
                remove keyboard
                 */
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext()
                            .INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                } catch (Exception ignore){

                }
            }
        });

        findViewById(R.id.activity_main_root).setOnClickListener(v -> {
            /*
            remove keyboard
             */
            try {
                InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext()
                        .INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            } catch (Exception ignore){

            }

        });
    }

    /**
     * Method called after async discover task is done
     * @param serverIPs The list of available server
     */
    public void onDiscoverEnd(String[] serverIPs) {
        /*
        Check if servers where found
         */
        if (serverIPs.length == 0) {
            Toast.makeText(getApplicationContext(), "No devices where found", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), Arrays.toString(serverIPs), Toast.LENGTH_SHORT).show();
        }

        ArrayList<ServerRowInfo> serverRowInfo = new ArrayList<>(10);

        /*
        add all servers to server list of the recycler view
         */
        for(String ele : serverIPs){
            serverRowInfo.add(new ServerRowInfo(ele));
        }

        serverInfoAdapter.setServers(serverRowInfo);
        serverInfoAdapter.notifyDataSetChanged();

        serverRecyclerView.setVisibility(View.VISIBLE);

        loadingProgressBar.setVisibility(View.INVISIBLE);
        loadingTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onServerSelected(ServerRowInfo server, View v) {
        Intent myIntent = new Intent(MainActivity.this, BrowseActivity.class);
        myIntent.putExtra("serverIP", server.getIp());
        startActivity(myIntent);
        finish();
    }
}
