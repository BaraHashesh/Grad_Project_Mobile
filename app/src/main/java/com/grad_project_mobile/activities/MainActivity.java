package com.grad_project_mobile.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.grad_project_mobile.AsyncTasks.DiscoverAsyncTask;
import com.grad_project_mobile.R;
import com.grad_project_mobile.shared.Constants;
import com.grad_project_mobile.shared.JsonParser;

public class MainActivity extends AppCompatActivity {


    Button searchButton;
    EditText ipEditText;
    ProgressBar loadingProgressBar;
    TextView loadingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = findViewById(R.id.activity_main_search_button);
        ipEditText = findViewById(R.id.activity_main_ip_edit_text);
        loadingProgressBar = findViewById(R.id.activity_main_loading_bar);
        loadingTextView = findViewById(R.id.activity_main_loading_text_view);

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

                    searchButton.setVisibility(View.INVISIBLE);
                    ipEditText.setVisibility(View.INVISIBLE);

                    loadingProgressBar.setVisibility(View.VISIBLE);
                    loadingTextView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Valid IP Address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.activity_main_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext()
                            .INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                } catch (Exception ignore){

                }

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
            searchButton.setVisibility(View.VISIBLE);
            ipEditText.setVisibility(View.VISIBLE);

            loadingProgressBar.setVisibility(View.INVISIBLE);
            loadingTextView.setVisibility(View.INVISIBLE);

            Toast.makeText(getApplicationContext(), "No devices where found", Toast.LENGTH_SHORT).show();
        } else {
            Intent myIntent = new Intent(this, ServerSelectActivity.class);
            myIntent.putExtra("servers", serverIPs);
            startActivity(myIntent);
            finish();

        }
    }
}
