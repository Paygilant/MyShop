package com.paygilant.myshop;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ui_appBar_start)));
        getSupportActionBar().setDisplayShowTitleEnabled(false);


//        LocaleHelper.updateLanguage("ru",this);

        Thread timer=new Thread()
        {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                finally
                {

                    Intent i=new Intent(SplashActivity.this,PrivacyPolicyActivity.class);
                    finish();
                    startActivity(i);
                }
            }
        };
        timer.start();
    }

}


