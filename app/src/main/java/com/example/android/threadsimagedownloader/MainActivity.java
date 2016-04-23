package com.example.android.threadsimagedownloader;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    private EditText editText;
    private ListView listView;
    private String[] listOfImages;
    private ProgressBar progressBar;
    private LinearLayout loadingSection = null;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.urlList);
        listView.setOnItemClickListener(this);
        listOfImages = getResources().getStringArray(R.array.imageUrls);
        progressBar = (ProgressBar) findViewById(R.id.downloadProgress);
        loadingSection = (LinearLayout) findViewById(R.id.loadingSection);
        handler = new Handler();
    }

    public void downloadImage(View view) {
        String url = editText.getText().toString();
        Thread thread = new Thread(new DownloadThread(url));
        thread.start();
    }

    public boolean downloadImageUsingThreads(String url) {
        boolean successful = false;
        HttpURLConnection connection = null;
        URL downloadURL = null;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        File file = null;
        try {
            downloadURL = new URL(url);
            connection = (HttpURLConnection) downloadURL.openConnection();
            inputStream = connection.getInputStream();
            file = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            .getAbsolutePath() + "/" + Uri.parse(url).getLastPathSegment());
            fileOutputStream = new FileOutputStream(file);
            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, read);
            }
            successful = true;
        } catch (MalformedURLException e) {
            L.m(e+"");
        } catch (IOException e) {
            L.m(e+"");
        } finally {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    loadingSection.setVisibility(View.GONE);
                    MainActivity.this
                            .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            });
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                     L.m(e+"");
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                     L.m(e+"");
                }
            }
        }
        return successful;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        editText.setText(listOfImages[i]);
    }

    private class DownloadThread implements Runnable {

        private String url;
        private DownloadThread(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //This ode will run on the main thread
                    loadingSection.setVisibility(View.VISIBLE);
                }
            });
            downloadImageUsingThreads(url);
        }
    }
}
