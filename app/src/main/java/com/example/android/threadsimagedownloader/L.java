package com.example.android.threadsimagedownloader;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class L {
    public static void m(String message) {
        Log.d("MILT", message);
    }
    public static void s(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
