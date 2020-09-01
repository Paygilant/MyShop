package com.paygilant.myshop.OnLine;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.test.uiautomator.By;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;


import com.kaopiz.kprogresshud.KProgressHUD;
import com.paygilant.PG_FraudDetection_SDK.Biometric.PaygilantScreenListener;
import com.paygilant.PG_FraudDetection_SDK.Communication.PaygilantCommunication;
import com.paygilant.PG_FraudDetection_SDK.PaygilantManager;
import com.paygilant.myshop.ConnectActivity;
import com.paygilant.myshop.MainActivity;
import com.paygilant.myshop.R;
import com.paygilant.myshop.ResultActivityAmount;
import com.paygilant.myshop.Singlton;
import com.paygilant.myshop.Utils;
import com.paygilant.pgdata.CheckPoint.CheckPointStatus;
import com.paygilant.pgdata.CheckPoint.CheckPointType;
import com.paygilant.pgdata.CheckPoint.CurrencyCode;
import com.paygilant.pgdata.CheckPoint.ScreenListenerType;
import com.paygilant.pgdata.CheckPoint.Transaction;
import com.paygilant.pgdata.CheckPoint.TransactionType;
import com.paygilant.pgdata.CheckPoint.param.Address;
import com.paygilant.pgdata.CheckPoint.param.AuthorizationResponse;
import com.paygilant.pgdata.CheckPoint.param.CreditCardDetail;
import com.paygilant.pgdata.CheckPoint.param.Payment;
import com.paygilant.pgdata.CheckPoint.param.PaymentMethodType;
import com.paygilant.pgdata.CheckPoint.param.User;
import com.paygilant.pgdata.CheckPoint.param.VerificationType;

//import com.paygilant.deviceidetification.fingerprintdialog.FingerprintAuthenticationDialogFragment;

import java.util.ArrayList;
import java.util.UUID;

import javax.crypto.Cipher;


public class ByOnlineActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter adapter;
    SharedPreferences preferences;
    private ArrayList<ImageItem> imageItems;
    SearchView searchView;
    public  boolean isPress =false;
    private  Boolean canAccess = true;
    private String userID;

    public boolean isScanFirst = false;
    public boolean isScanFirst() {
        return isScanFirst;
    }
    private PaygilantScreenListener listener;

    public void setScanFirst(boolean scanFirst) {
        isScanFirst = scanFirst;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by_online);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        forceLTRSupported(this);
        toolbar.inflateMenu(R.menu.menu);
//        forceLTRSupported(this);
//        bitmap = getImage(Environment.getExternalStorageDirectory()+File.separator+ "test.png");

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.gridView1);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        imageItems = new ArrayList<>();
        imageItems = MainActivity.getData(getResources());


        View v = findViewById(R.id.activity_by_online);
//        purchaseButton = findViewById(R.id.send);

        adapter = new MyRecyclerViewAdapter(this, imageItems);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setClickListener(ByOnlineActivity.this);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userID = preferences.getString("USER_NAME", "");



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

// do something on text submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                newText=newText.toLowerCase();
                ArrayList<ImageItem> newList=new ArrayList<ImageItem>();
                for(ImageItem imageItem:imageItems){
                    String title =imageItem.getTitle().toLowerCase();
                    if (title.startsWith(newText)){
                        newList.add(imageItem);
                    }
                }
                adapter.setFilter(newList);
// do something when text changes
                return true;
            }
        });
//        actionManager.setTouchToAllChildren(v);
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

            case R.id.register:

                Singlton.getInstance().setReg(true);
                Utils.logOut(this);

                return true;

            case R.id.logout:

                Utils.logOut(this);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume(){
        Log.d("Amount", "onResume");

//        actionManager.resumeListen();

        super.onResume();
        listener = PaygilantManager.getInstance(this).startNewScreenListener(ScreenListenerType.PRODUCT_SCREEN,2,this);

    }
    @Override
    public void onStart(){
        Log.d("Amount", "onStart");
        super.onStart();
    }
    @Override
    public void onPause(){
        Log.d("Amount", "onPause");
//        actionManager.pauseListenToSensors();

        super.onPause();
        if (listener != null) {
            listener.pauseListenToSensors();
        }
    }

    @Override
    public void onStop(){
        Log.d("Amount", "onStop");

        super.onStop();
    }

    @Override
    public void onDestroy(){
        Log.d("Amount", "onDestory");
//        actionManager.finishScreenListener();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getApplicationContext().getResources().getString(R.string.app_name));
        alertDialogBuilder.setMessage(getResources().getString(R.string.exit_condition));
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }
    private void getRisk(final ImageItem item) {
        final KProgressHUD hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        hud.show();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userID = preferences.getString("USER_NAME", "");
        final String email = preferences.getString("EMAIL", "");

        final String phoneNumber = preferences.getString("PHONE_NUMBER", "");




        Address user_address = new Address("Alexander", "Delarge", "314 Wall street",
                "",  "New York",  "NY", "US", "10001", "+12885550153");
        User user = new User(userID,"tylerd@gmail.com", VerificationType.VERIFIED,"+12885550153",VerificationType.UNKNOWN,user_address);


        CreditCardDetail credit = new CreditCardDetail( "EGHV234AUD54367",  "4q5w8e0r1t0y",  "458010",  "7854",  19,  06);
        Payment payment = new Payment( PaymentMethodType.CREDIT_CARD,  "stripe",  "Tyler Durden","",  credit,null );


        Address billingAddress = new Address( "Tyler",  "Durden",  "537 Paper street",  "",  "Bradford",  "DE",  "US",  "19808",  "+12885550153");
        Address shippingAddress = new Address( "Jane",  "Doe",  "241 S Moreno Drive",  "",  "Beverly Hills",  "CA",  "US",  "90210",  "+14185551234");

        AuthorizationResponse authResponse = new AuthorizationResponse( "success",  "Y",  "N7",  "Decline for CVV2 failure (VISA)",  "00",  "Transaction approved",  "Y",  "Y",  "5");

        Transaction transaction = new Transaction(System.currentTimeMillis(), TransactionType.PURCHASE,
                CurrencyCode.EUR, item.getTitle(),Double.valueOf(item.getPrice()),
                user,billingAddress,shippingAddress,payment,authResponse);
        PaygilantManager.getInstance(this).getRiskForCheckPoint(transaction, new PaygilantCommunication() {
            @Override
            public void receiveRisk(int i, String s, String s1) {
                PaygilantManager.getInstance(ByOnlineActivity.this).updateCheckPointStatus(CheckPointType.TRANSACTION, s1, CheckPointStatus.APPROVED, UUID.randomUUID().toString());

                if (i == -1 ){
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ByOnlineActivity.this);
                    alertDialogBuilder.setTitle(getApplicationContext().getResources().getString(R.string.app_name));
                    alertDialogBuilder.setMessage(getResources().getString(R.string.internet_connection));
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.retry),new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.dismiss();
                                    getRisk(item);
                                }
                            });


                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else {
                    isScanFirst = false;
                    Intent intent = new Intent(ByOnlineActivity.this, ResultActivityAmount.class);
                    intent.putExtra("RISK_RESULT", i);
                    hud.dismiss();
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    @Override
    public void onItemClick(View view, int position) {

//        ImageItem item = (ImageItem) adapter.getItem(position);

//            if (item.getTitle().equals(imageItems.get(numRan).getTitle())) {
        if (!isPress) {
            isPress = true;

            ImageItem item = adapter.getItem(position);
            getRisk(item);

        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public class PurchaseButtonClickListener implements View.OnClickListener {

        Cipher mCipher;
        String mKeyName;
        int riskLevel;

        PurchaseButtonClickListener(Cipher cipher, String keyName) {
            mCipher = cipher;
            mKeyName = keyName;
        }

        @Override
        public void onClick(View view) {

        }
    }


}
