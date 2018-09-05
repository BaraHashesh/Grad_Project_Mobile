package com.grad_project_mobile.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.grad_project_mobile.AsyncTasks.DiscoverAsyncTask;
import com.grad_project_mobile.R;
import com.grad_project_mobile.shared.Constants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = findViewById(R.id.activity_main_search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ipEditText = findViewById(R.id.activity_main_ip_edit_text);

                String ip = ipEditText.getText().toString();

                boolean valid = Constants.IPV4_REGEX.matcher(ip).matches();

                /*
                Check if IP is valid
                 */
                if(valid) {
//                    DiscoverAsyncTask discoverAsyncTask = new DiscoverAsyncTask(MainActivity.this);
//                    discoverAsyncTask.execute(ip);

                    Intent intent = new Intent(MainActivity.this, ServerSelectActivity.class);
                    startActivity(intent);
                    finish();
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
}
