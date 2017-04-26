package com.chatimie.arthurcouge.chatimie.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Quentin for ChatIMIE on 26/04/2017.
 */

public class ReceiverWifi extends BroadcastReceiver {
    public ReceiverWifi() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Toast.makeText(context,"Wi-Fi : " + mWifi.isConnected(),Toast.LENGTH_SHORT).show();
    }
}
