package com.paygilant.myshop;

import android.app.Application;
import android.content.res.Configuration;

import com.paygilant.PG_FraudDetection_SDK.PaygilantException;
import com.paygilant.PG_FraudDetection_SDK.PaygilantManager;


public class application extends Application {
    // Overriding this method is totally optional!

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            PaygilantManager.init(this,getApplicationContext().getResources().getString(R.string.PGServerURL));
        } catch (PaygilantException e) {
            e.printStackTrace();
        }
    }

}
