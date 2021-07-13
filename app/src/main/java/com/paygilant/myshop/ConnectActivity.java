package com.paygilant.myshop;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paygilant.PG_FraudDetection_SDK.Biometric.PaygilantScreenListener;
import com.paygilant.PG_FraudDetection_SDK.PaygilantManager;
import com.paygilant.myshop.OnLine.ByOnlineActivity;

import com.paygilant.pgdata.CheckPoint.Login;
import com.paygilant.pgdata.CheckPoint.Registration;
import com.paygilant.pgdata.CheckPoint.param.Address;
import com.paygilant.pgdata.CheckPoint.param.User;
import com.paygilant.pgdata.CheckPoint.param.VerificationType;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConnectActivity extends AppCompatActivity implements TextWatcher {
    Button buttonConnect, buttonLogin;

    EditText[] editText = new EditText[4];
    TextView titleText;
    private PaygilantScreenListener listener;
    private static final String TAG = ConnectActivity.class.getSimpleName();
    Boolean isStartApp = false;
    public static final int READ_PHONE_STATE_PERMISSION = 100;
    boolean isReg = false;
    FirebaseFirestore db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        forceLTRSupported(this);
        toolbar.inflateMenu(R.menu.menu2);
        db = FirebaseFirestore.getInstance();

        final String userID = Utils.getStringShareData(this,"USER_NAME","");

        if (!userID.equals("")) {
            loginProcess(userID);
        } else {

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                                Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CAMERA}
                        , READ_PHONE_STATE_PERMISSION);
            }
            long startTime = System.currentTimeMillis();
            long endTme = System.currentTimeMillis();
            Log.d("Instruction", "time to init paygilant manager :" + (endTme - startTime));
        }
        titleText = findViewById(R.id.title);
        buttonConnect = (Button) findViewById(R.id.buttonConnect);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        editText[0] = findViewById(R.id.editTextConnect);
        editText[1] = findViewById(R.id.editTextPassword);
        editText[2] = findViewById(R.id.editTextEmail);
        editText[3] = findViewById(R.id.editTextPhone);
        titleText.setText(R.string.login);
        editText[0].addTextChangedListener(this);

        for (int i = 1; i < 4; i++) {
            editText[i].setVisibility(View.INVISIBLE);
            editText[i].addTextChangedListener(this);

        }
        buttonLogin.setVisibility(View.INVISIBLE);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleText.setText(R.string.login);
                isReg = false;

                for (int i = 1; i < 4; i++) {
                    editText[i].setVisibility(View.INVISIBLE);
                }
                buttonLogin.setVisibility(View.INVISIBLE);
                textChange();

            }
        });

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReg) {
                    if ((editText[0].length() > 0) && (editText[1].length() > 0) && (editText[2].length() > 0) && (editText[3].length() > 0)) {

                        DocumentReference docRefGet = db.collection("users").document("dataUsers");
                        docRefGet.get()
                                .addOnCompleteListener(
                                        new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (!task.getResult().getMetadata().isFromCache()) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            if (checkKeyFromHashMap(editText[0].getText().toString(), document.getData())) {
                                                                Toast.makeText(ConnectActivity.this, getResources().getString(R.string.reg_message), Toast.LENGTH_LONG).show();
                                                            } else {
                                                                Map<String, Object> user = new HashMap<>();
                                                                user.put(editText[0].getText().toString(), "");
                                                                DocumentReference docRef = db.collection("users").document("dataUsers");
                                                                docRef.update(user)
                                                                        .addOnSuccessListener(
                                                                                new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {
                                                                                        regProcess();
                                                                                    }
                                                                                })
                                                                        .addOnFailureListener(getOnFailureListener());
                                                            }
                                                        } else {
                                                            Toast.makeText(ConnectActivity.this, getResources().getString(R.string.reg_message_error), Toast.LENGTH_LONG).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(ConnectActivity.this, getResources().getString(R.string.reg_message_error), Toast.LENGTH_LONG).show();
                                                    }
                                                }else{
                                                    Toast.makeText(ConnectActivity.this, getResources().getString(R.string.reg_message_error), Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        })
                                .addOnFailureListener(getOnFailureListener());
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
    private OnFailureListener getOnFailureListener() {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ConnectActivity.this,getResources().getString(R.string.reg_message_error),Toast.LENGTH_LONG).show();

            }
        };
    }
    private boolean checkKeyFromHashMap(String search, Map<String,Object> map){
        List<String> keys = new ArrayList<>(map.keySet());
        for (String key : map.keySet())
            if (key.equals(search)){
                return true;
            }
        return false;
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

        Utils.setStringShareData(this,"USER_NAME", userName);
        Utils.setStringShareData(this,"PASSWORD", password);
        Utils.setStringShareData(this,"EMAIL", email);
        Utils.setStringShareData(this,"PHONE_NUMBER", phoneNumber);


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
        final String email = Utils.getStringShareData(this,"EMAIL","");

        final String phoneNumber = Utils.getStringShareData(this,"PHONE_NUMBER", "");
        Utils.setStringShareData(this,"USER_NAME",userName);

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

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PaygilantManager.getInstance(this).onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume");

//        listener = PaygilantManager.getInstance(this).startNewScreenListener(ScreenListenerType.LOGIN_FORM,1,this);
    }
    @Override
    protected void onPause() {
        super.onPause();
//        if (listener!=null)
//            listener.pauseListenToSensors();
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

        textChange();

    }

    private void textChange() {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.register:
                isReg = true;
                buttonLogin.setVisibility(View.VISIBLE);

                for (int i = 1; i < 4; i++) {
//                    editText[i].addTextChangedListener(this);
                    editText[i].setVisibility(View.VISIBLE);
                }
                titleText.setText(R.string.register);
                textChange();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isReg = false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);
        return true;
    }
}



