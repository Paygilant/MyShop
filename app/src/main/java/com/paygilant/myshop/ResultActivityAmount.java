package com.paygilant.myshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.paygilant.PG_FraudDetection_SDK.PaygilantManager;
import com.paygilant.myshop.OnLine.ByOnlineActivity;


import java.util.ArrayList;


public class ResultActivityAmount extends AppCompatActivity {

    String [] results = {"LOW","MEDUIM","HIGH","VERY HIGH"};
    Button buttonStartAgain;
    ImageView imageResult1,imageResult2,imageResult3,imageResult4,imageResult5,imageSumResult;
    TextView textNumResult, textScoreHigh,paymentAppText;
    Integer whichItemPress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.inflateMenu(R.menu.menu);

        ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
        paymentAppText = findViewById(R.id.paymentAppText);
        paymentAppText.setText(getResources().getString(R.string.rejected));

        buttonStartAgain = findViewById(R.id.buttonStartAgain);
//        imageResult1 = findViewById(R.id.imageResult1);
        textNumResult = findViewById(R.id.textNumResult);
        textScoreHigh = findViewById(R.id.textScoreHigh);
        imageSumResult = findViewById(R.id.imageSumResult);

        int result = getIntent().getIntExtra("RISK_RESULT",-1);
        if ((result>=0) && (result<=3)){
            textNumResult.setText( result+"");
            textScoreHigh.setText(results[result]);

            if (result == 0) {
                imageSumResult.setImageResource(R.drawable.approved);
                paymentAppText.setText(getResources().getString(R.string.payment_approved));

            }else{
                imageSumResult.setImageResource(R.drawable.rejected);
                paymentAppText.setText(getResources().getString(R.string.rejected));


            }
        }else  if (result == -2){
            findViewById(R.id.viewResult).setVisibility(View.INVISIBLE);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getApplicationContext().getResources().getString(R.string.app_name));
            alertDialogBuilder.setMessage(getResources().getString(R.string.expired_message));
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {

                            dialog.dismiss();
                            finish();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }


//        textTryAnotherFlow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent  intent = new Intent(ResultActivityAmount.this, MainActivity.class);
//                startActivity(intent);
//                finish();                        // User clicked OK button
//
//            }
//        });
        buttonStartAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                // User clicked OK button

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPreferences preferences;
                SharedPreferences.Editor editor;
                preferences = PreferenceManager.getDefaultSharedPreferences(ResultActivityAmount.this);
                editor = preferences.edit();
                editor.putString("USER_NAME", "");
                editor.apply();
                PaygilantManager.getInstance(this).logout();

                Intent intent = new Intent(ResultActivityAmount.this, ConnectActivity.class);
                startActivity(intent);
                finish();


//                addSomething();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.back, menu);
//        return true;
//    }

//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        Intent intent;
//        switch (id) {
//            case R.id.icon_back:
//                onBackPressed();
//                return true;
//            case R.id.logout:
//                SharedPreferences preferences;
//                SharedPreferences.Editor editor;
//                preferences = PreferenceManager.getDefaultSharedPreferences(ResultActivityAmount.this);
//                editor = preferences.edit();
//                editor.putString("USER_NAME", "");
//                editor.apply();
//                Singlton.getInstance().setAmount("0");
//                Singlton.getInstance().setContactModel(null);
//                intent = new Intent(ResultActivityAmount.this, ConnectActivity.class);
//                startActivity(intent);
//                finish();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        Intent intent;
//        switch (id) {
////            case R.id.main_screen:
////                intent = new Intent(this,MainActivity.class);
////                startActivity(intent);
////                finish();
////                break;
////            case R.id.send_money:
//////                intent = new Intent(this,Amount.class);
//////                startActivity(intent);
//////                finish();
////            case R.id.log_out:
////                final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
////                SharedPreferences.Editor editor = preferences.edit();
////                editor.putString("PHONECONNECT", "");
////                editor.apply();
////                intent = new Intent(this,ConnectActivity.class);
////                startActivity(intent);
////                finish();
////                break;
////            case R.id.buy_online:
////                intent = new Intent(this,ByOnlineActivity.class);
////                startActivity(intent);
////                finish();
//////                Toast.makeText(getApplicationContext(), "Help menu item pressed", Toast.LENGTH_SHORT).show();
////                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ResultActivityAmount.this, ByOnlineActivity.class);
        startActivity(intent);
        finish();

    }
}
