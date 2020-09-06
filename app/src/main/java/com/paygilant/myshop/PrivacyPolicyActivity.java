package com.paygilant.myshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.paygilant.PG_FraudDetection_SDK.PaygilantManager;

import java.io.IOException;
import java.io.InputStream;

public class PrivacyPolicyActivity extends AppCompatActivity {
    SharedPreferences preferences;
    private StringBuilder text = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Terms & Privacy");
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(! preferences.getBoolean("FIRSTTIME", true)){
            PaygilantManager.getInstance(this).initializeDeviceId();
            Intent intent = new Intent(PrivacyPolicyActivity.this, ConnectActivity.class);
            startActivity(intent);
            finish();
        }


        WebView web = findViewById(R.id.web);
        WebView web2 = findViewById(R.id.web2);

        web.getSettings().setJavaScriptEnabled(true);
        web2.getSettings().setJavaScriptEnabled(true);

//        String prompt = "";
//        try {
//            InputStream inputStream = getResources().openRawResource(R.raw.privacy);
//            byte[] buffer = new byte[inputStream.available()];
//            inputStream.read(buffer);
//            prompt = new String(buffer);
//            inputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String prompt2 = "";
//        try {
//            InputStream inputStream = getResources().openRawResource(R.raw.privacy2);
//            byte[] buffer = new byte[inputStream.available()];
//            inputStream.read(buffer);
//            prompt2 = new String(buffer);
//            inputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        web.loadUrl("file:///android_asset/privacy.html");
        web2.loadUrl("file:///android_asset/privacy2.html");

//
//        web.loadData(prompt,"text/html","utf-8");
//
//        web2.loadData(prompt2,"text/html","utf-8");

        findViewById(R.id.buttonAgree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("FIRSTTIME", false);
                editor.apply();

                PaygilantManager.getInstance(PrivacyPolicyActivity.this).initializeDeviceId();
                Intent intent = new Intent(PrivacyPolicyActivity.this, ConnectActivity.class);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.buttonNotAgree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
}
