package com.example.android.asynctaskimagedownloaderexample;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText selectionText;
    ListView chooseImagesList;
    String[] listOfImages;
    ProgressBar downloadImagesProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectionText = (EditText) findViewById(R.id.editText);
        chooseImagesList = (ListView) findViewById(R.id.listView);
        listOfImages = getResources().getStringArray(R.array.imageUrls);
        downloadImagesProgress = (ProgressBar) findViewById(R.id.progressBar);
        chooseImagesList.setOnItemClickListener(this);
    }

    public void downloadImage(View view) {
        if (selectionText.getText().toString().length() > 0) {
            String url = selectionText.getText().toString();
            MyTask myTask = new MyTask();
            myTask.execute(url);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectionText.setText(listOfImages[i]);
    }

    class MyTask extends AsyncTask<String, Integer, Boolean> {

        private int contentLenght = -1;
        private int counter = 0;
        private int calculatedProgress = 0;

        @Override
        protected void onPreExecute() {
            downloadImagesProgress.setVisibility(View.VISIBLE);
            if (MainActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }

        @Override
        protected Boolean doInBackground(String... voids) {
            boolean successful = false;
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            FileOutputStream fileOutputStream = null;
            File file = null;

            try {
                URL url = new URL(voids[0]);
                try {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    contentLenght = httpURLConnection.getContentLength();
                    inputStream = httpURLConnection.getInputStream();
                    file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile() + "/"
                            + Uri.parse(voids[0]).getLastPathSegment());
                    fileOutputStream = new FileOutputStream(file);
                    int read = -1;
                    byte[] buffer = new byte[1024];
                    while ((read = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, read);
                        counter += read;
                        publishProgress(counter);
                    }
                    successful = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return successful;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            calculatedProgress = (int)(((double)values[0]/contentLenght)*100);
            downloadImagesProgress.setProgress(calculatedProgress);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            downloadImagesProgress.setVisibility(View.GONE);
            MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
    }
}

































