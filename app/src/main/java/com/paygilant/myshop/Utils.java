package com.paygilant.myshop;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AlertDialog;

import com.paygilant.PG_FraudDetection_SDK.PaygilantManager;

public class Utils {

    public static void logOut(Activity activity) {

        setStringShareData(activity,"USER_NAME","");
        PaygilantManager.getInstance(activity).logout();
        Intent intent = new Intent(activity, ConnectActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
    public static String getStringShareData(Context context, String key,String defaultValue){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, defaultValue);
    }
    public static void setStringShareData(Context context, String key,String data) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key, data);
        editor.apply();
    }
}
