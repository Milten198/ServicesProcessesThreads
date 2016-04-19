package com.example.android.threadsimagedownloader;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.File;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.urlList);
        listView.setOnItemClickListener(this);
        listOfImages = getResources().getStringArray(R.array.imageUrls);
        progressBar = (ProgressBar) findViewById(R.id.downloadProgress);
    }

    public void downloadImage(View view) {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String url = listOfImages[0];
        Uri uri = Uri.parse(url);
        L.s(this, uri.getLastPathSegment());
        L.m("dupa");
//        Thread thread = new Thread(new DownloadThread());
//        thread.start();
    }

    public boolean downloadImageUsingThreads(String url) {
        boolean successful = false;
        HttpURLConnection connection = null;
        URL downloadURL = null;
        InputStream inputStream = null;
        try {
            downloadURL = new URL(url);
            connection = (HttpURLConnection) downloadURL.openConnection();
            inputStream = connection.getInputStream();
            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = inputStream.read(buffer)) != -1) {
                L.m("  sada" +  read);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
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

        @Override
        public void run() {
            downloadImageUsingThreads(listOfImages[0]);
        }
    }
}
