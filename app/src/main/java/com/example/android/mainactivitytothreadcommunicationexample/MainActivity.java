package com.example.android.mainactivitytothreadcommunicationexample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    MyThread myThread;
    Button sendMessageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendMessageButton = (Button) findViewById(R.id.sendMessage);
        myThread = new MyThread();
        myThread.start();
    }

    public void sendMessage(View view) {
        myThread.handler.post(new Runnable() {
            @Override
            public void run() {
                L.m(Thread.currentThread().getName());
            }
        });
    }

    class MyThread extends Thread {
        Handler handler;
        public MyThread() {

        }

        @Override
        public void run() {
            Looper.prepare();
            handler = new Handler();
            Looper.loop();
        }
    }
}
