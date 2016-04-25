package com.example.asynctaskexample;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private final boolean CANCELAR_SI_MAS_DE_100_IMAGENES = false;
    private final String TAG_LOG = "test";
    private TextView TV_mensaje;


    private Button button;
    private EditText time;
    private TextView finalResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        time = (EditText) findViewById(R.id.in_time);
        button = (Button) findViewById(R.id.btn_run);
        finalResult = (TextView) findViewById(R.id.tv_result);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = time.getText().toString();
                runner.execute(sleepTime);
            }
        });
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "ProgressDialog",
                    "Wait for "+time.getText().toString()+ " seconds");
        }

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            int seconds = Integer.parseInt(params[0]);
            resp = "Slept for 0 seconds";
            int i = 0;
            while (!isCancelled() && i < seconds){
                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    cancel(true);
                    e.printStackTrace();
                } catch (Exception e) {
                    cancel(true);
                    e.printStackTrace();
                }

                resp = "Slept " + (i+1) + " seconds";

                //If the given time is more than 3 seconds is going to be cancelled
                if (i == 3) {
                    cancel(true);
                }

                publishProgress(resp);
                i++;
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            finalResult.setText(result);
        }


        @Override
        protected void onCancelled(String result) {
            progressDialog.dismiss();
            finalResult.setText(result + "\n before being cancelled");
        }


        @Override
        protected void onProgressUpdate(String... text) {
            progressDialog.setMessage(text[0]);

        }
    }
}