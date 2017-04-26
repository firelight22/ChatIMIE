package com.chatimie.arthurcouge.chatimie.bg;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Quentin for ChatIMIE on 26/04/2017.
 */

//com.chatimie.arthurcouge.chatimie.bg.MyIntentService
public class MyIntentService extends IntentService {
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }

    public MyIntentService(String name) {
        super(name);
    }
    public MyIntentService() {
        super("myNewIntentServiceName");
    }
}
