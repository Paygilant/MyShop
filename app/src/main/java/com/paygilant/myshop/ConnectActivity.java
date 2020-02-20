package com.paygilant.myshop;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.paygilant.PG_FraudDetection_SDK.Biometric.PaygilantScreenListener;
import com.paygilant.PG_FraudDetection_SDK.Communication.PaygilantCommunication;
import com.paygilant.PG_FraudDetection_SDK.PaygilantManager;
import com.paygilant.myshop.OnLine.ByOnlineActivity;
import com.paygilant.myshop.R;
import com.paygilant.pgdata.CheckPoint.Login;
import com.paygilant.pgdata.CheckPoint.Registration;
import com.paygilant.pgdata.CheckPoint.ScreenListenerType;
import com.paygilant.pgdata.CheckPoint.param.Address;
import com.paygilant.pgdata.CheckPoint.param.User;
import com.paygilant.pgdata.CheckPoint.param.VerificationType;

import java.io.FileOutputStream;
import java.util.Locale;


public class ConnectActivity extends AppCompatActivity implements TextWatcher {

    Button buttonRegister,buttonLogin,buttonConnect;
    EditText[] editText = new EditText[4];
    //    PaygilantManager paygilantHandler;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private PaygilantScreenListener listener;

    public static final int READ_PHONE_STATE_PERMISSION = 100;
    boolean isReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ui_appBar_start)));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        isReg = true;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        LocaleHelper.setLocale(this,"en");



        final String userID = preferences.getString("USER_NAME", "");
        if (!userID.equals("")) {
            loginProcess(userID);
//            editor = preferences.edit();
//            editor.putString("USER_NAME", userID);
//            editor.apply();
//            Intent intent = new Intent(ConnectActivity.this, Amount.class);
//            intent.putExtra("IF_NOT_FIRST_TIME" , true);
//            startActivity(intent);
//            finish();
        } else {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.CAMERA}
                        , READ_PHONE_STATE_PERMISSION);
            }
            long startTime = System.currentTimeMillis();
            long endTme = System.currentTimeMillis();
            Log.d("Instruction", "time to init paygilant manager :" + (endTme - startTime));
        }
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonConnect = (Button) findViewById(R.id.buttonConnect);
        editText[0] = findViewById(R.id.editTextConnect);
        editText[1] = findViewById(R.id.editTextPassword);
        editText[2] = findViewById(R.id.editTextEmail);
        editText[3] = findViewById(R.id.editTextPhone);

        for (int i = 0; i<4 ;i++){
            editText[i].addTextChangedListener(this);
        }




        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                LocaleHelper.updateLanguage("en",ConnectActivity.this);
                isReg = true;
                buttonRegister.setBackgroundColor(getResources().getColor(R.color.selected_button));
                buttonLogin.setBackgroundColor(getResources().getColor(R.color.white_Color));
                buttonRegister.setTextColor(getResources().getColor(R.color.white_Color));
                buttonLogin.setTextColor(getResources().getColor(R.color.unselected_text));
                for (int i =1; i<4; i++) {
                    editText[i].setVisibility(View.VISIBLE);
                }
//                regProcces();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                LocaleHelper.updateLanguage("ru",ConnectActivity.this);

                isReg = false;
                buttonLogin.setBackgroundColor(getResources().getColor(R.color.selected_button));
                buttonRegister.setBackgroundColor(getResources().getColor(R.color.white_Color));
                buttonLogin.setTextColor(getResources().getColor(R.color.white_Color));
                buttonRegister.setTextColor(getResources().getColor(R.color.unselected_text));
                for (int i =1; i<4; i++) {
                    editText[i].setVisibility(View.INVISIBLE);
                }
//                loginProcces();
            }
        });

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReg) {
                    if ((editText[0].length() > 0) && (editText[1].length() > 0) && (editText[2].length() > 0) && (editText[3].length() > 0)) {
                        regProcess();
                    } else {
                        String message = "";

                        if (editText[0].length() <= 0){
                            message =message+ "* Insert User name\n";
                        }
                        if (editText[1].length() <= 0){
                            message =message+ "* Insert password\n";
                        }
                        if (editText[2].length() <= 0){
                            message =message+ "* Insert Email\n";
                        }
                        if (editText[3].length() <= 0){
                            message =message+ "* Insert phone number";
                        }
                        Toast.makeText(ConnectActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                }else{
                    if (editText[0].length() > 0){
                        loginProcess(editText[0].getText().toString());
                    } else {
                        Toast.makeText(ConnectActivity.this, "* Insert User name", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }



    /**
     * collect data from register status and get risk level with message
     */
    private void regProcess() {
        final long startTime = System.currentTimeMillis();
        Log.d("REGISTER", "START");


        final String userName = editText[0].getText().toString();
        final String password = editText[1].getText().toString();
        final String email = editText[2].getText().toString();
        final String phoneNumber = editText[3].getText().toString();
        editor = preferences.edit();

        editor.putString("USER_NAME", userName);
        editor.putString("PASSWORD", password);
        editor.putString("EMAIL", email);
        editor.putString("PHONE_NUMBER", phoneNumber);
        editor.apply();

        Address user_address = new Address("Alexander", "Delarge", "314 Wall street",
                "",  "New York",  "NY", "US", "10001", "+12885550153");
        User user = new User(userName,"tylerd@gmail.com", VerificationType.VERIFIED,"+12885550153",VerificationType.UNKNOWN,user_address);
        Registration currReg = new Registration(user);
        PaygilantManager.getInstance(this).arriveToCheckPoint(currReg);
        boolean isPress = true;
        Intent intent = new Intent(ConnectActivity.this, ByOnlineActivity.class);
        startActivity(intent);
        finish();


        // User clicked OK button

    }
    /**
     * collect data from login status and get risk level with message
     */
    private void loginProcess(String userID) {
        final String userName = userID;
        final String email = preferences.getString("EMAIL", "");
        final String phoneNumber = preferences.getString("PHONE_NUMBER", "");
        editor = preferences.edit();
        editor.putString("USER_NAME", userName);
        editor.apply();
        Address user_address = new Address("Alexander", "Delarge", "314 Wall street",
                "",  "New York",  "NY", "US", "10001", "+12885550153");
        User user = new User(userName,"tylerd@gmail.com", VerificationType.VERIFIED,"+12885550153",VerificationType.UNKNOWN,user_address);
        Login login = new Login(user);
        PaygilantManager.getInstance(this).arriveToCheckPoint(login);
        boolean isPress = true;
        Intent intent = new Intent(ConnectActivity.this, ByOnlineActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {

        PaygilantManager.getInstance(this).onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    @Override
    protected void onResume(){
        super.onResume();
        listener = PaygilantManager.getInstance(this).startNewScreenListener(ScreenListenerType.LOGIN_FORM,1,this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (listener!=null)
            listener.pauseListenToSensors();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
    void saveImage(String pthAndFylTtlVar, Bitmap iptBmjVar)
    {
        try
        {
            FileOutputStream fylBytWrtrVar = new FileOutputStream(pthAndFylTtlVar);
            iptBmjVar.compress(Bitmap.CompressFormat.PNG, 100, fylBytWrtrVar);
            fylBytWrtrVar.close();
        }
        catch (Exception errVar) { errVar.printStackTrace(); }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String str = editText[0].getText().toString();
        if(str.length() > 0 && str.contains(" "))
        {
            editText[0].setText(editText[0].getText().toString().replaceAll(" ",""));
            editText[0].setSelection(editText[0].getText().length());
        }
        if (isReg) {
            if ((editText[0].length() > 0) && (editText[1].length() > 0) && (editText[2].length() > 0) && (editText[3].length() > 0)) {
                buttonConnect.setBackgroundColor(getResources().getColor(R.color.ui_appBar_start));
            } else {
                buttonConnect.setBackgroundColor(getResources().getColor(R.color.colorConnectButton));
            }
        }else{
            if (editText[0].length() > 0) {
                buttonConnect.setBackgroundColor(getResources().getColor(R.color.ui_appBar_start));
            } else {
                buttonConnect.setBackgroundColor(getResources().getColor(R.color.colorConnectButton));
            }
        }

    }
}



