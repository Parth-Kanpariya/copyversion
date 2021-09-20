package com.example.copyversion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class broadcast extends BroadcastReceiver {

    private boolean t = true;

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            if (!isOnline(context)) {
                Toast.makeText(context, "No internet", Toast.LENGTH_SHORT).show();
                t = false;
            } else {
                if (t == false) {
                    Toast.makeText(context, "Internet connected", Toast.LENGTH_SHORT).show();
                    t = true;
                }
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());

        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;

        }
    }
}
