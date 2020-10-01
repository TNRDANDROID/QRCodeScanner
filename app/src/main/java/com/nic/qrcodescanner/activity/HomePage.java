package com.nic.qrcodescanner.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nic.qrcodescanner.Api.Api;
import com.nic.qrcodescanner.Api.ApiService;
import com.nic.qrcodescanner.Api.ServerResponse;
import com.nic.qrcodescanner.R;
import com.nic.qrcodescanner.constant.AppConstant;
import com.nic.qrcodescanner.databinding.HomePageBinding;
import com.nic.qrcodescanner.dialog.MyDialog;
import com.nic.qrcodescanner.pojo.LoginPojo;
import com.nic.qrcodescanner.pojo.QrcodePojo;
import com.nic.qrcodescanner.session.PrefManager;
import com.nic.qrcodescanner.utils.UrlGenerator;
import com.nic.qrcodescanner.utils.Utils;
import com.nic.qrcodescanner.windowpreferences.WindowPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePage extends AppCompatActivity implements MyDialog.myOnClickListener, Api.ServerResponseListener {
    private IntentIntegrator qrScan;
    private HomePageBinding homePageBinding;
    private PrefManager prefManager;
    ArrayList<LoginPojo> loginPojos;
//    ArrayList<LoginPojo> loginResponse = getIntent().getStringArrayListExtra("loginPojo");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());

        homePageBinding = DataBindingUtil.setContentView(this, R.layout.home_page);
        homePageBinding.setActivity(this);
        prefManager = new PrefManager(this);
        qrScan = new IntentIntegrator(this);
        loginPojos = (ArrayList<LoginPojo>) getIntent().getSerializableExtra("login_Pojo");

        if(getIntent().getStringExtra("Ticket_screen").equalsIgnoreCase("Ticket_screen")){
            loginPojos = prefManager.getUserQrCodeJson();
        }

        if (loginPojos != null && loginPojos.size() > 0) {

            homePageBinding.userName.setText(loginPojos.get(0).getUsername());
            homePageBinding.designation.setText(loginPojos.get(0).getDesignation_desc());
            homePageBinding.templeName.setText(loginPojos.get(0).getTemple_name());
            homePageBinding.lastVisitedDate.setText(loginPojos.get(0).getLastlogin_date());
            if (loginPojos.get(0).getTemple_logo() != null) {
                homePageBinding.defaultLogo.setVisibility(View.GONE);
                Glide.with(this).load(loginPojos.get(0).getTemple_logo()).into(homePageBinding.templeLogo);
            } else {
                homePageBinding.defaultLogo.setVisibility(View.VISIBLE);
            }


        }

        Log.d("LoginPojos", "" + loginPojos);
//        homePageBinding.titleTv.setText(loginResponse.getUserName());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it

            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {

                Map<String, String> qrCodeParams = new HashMap<>();
                qrCodeParams.put(AppConstant.KEY_ACK_NO, result.getContents());
                prefManager.setQrCode(result.getContents());
                qrCodeParams.put(AppConstant.KEY_SERVICE_ID, AppConstant.JSON_DATA);
                qrCodeParams.put(AppConstant.KEY_TEMPLE_ID, String.valueOf(loginPojos.get(0).getTemple_id()));
                qrCodeParams.put(AppConstant.KEY_TOKEN_ID, String.valueOf(loginPojos.get(0).getTokenid()));
                Log.d("temple",""+loginPojos.get(0).getTokenid());
                Log.d("token",""+loginPojos.get(0).getTokenid());
                Log.d("QrCodeparams",""+qrCodeParams);

                new ApiService(this).makeRequest("QrDataJson", Api.Method.POST, UrlGenerator.getCommonUrl(), qrCodeParams, "not cache", this);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void scanQrCode() {
        if (Utils.isOnline()) {
            qrScan.initiateScan();
        } else {
            Utils.showAlert(this, getResources().getString(R.string.no_internet));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }


    public void closeApplication() {
        new MyDialog(this).exitDialog(this, "Are you sure you want to Logout?", "Logout");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                new MyDialog(this).exitDialog(this, "Are you sure you want to exit ?", "Exit");
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onButtonClick(AlertDialog alertDialog, String type) {
        alertDialog.dismiss();
        if ("Exit".equalsIgnoreCase(type)) {
            onBackPressed();
        } else {

            Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("EXIT", false);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject qrdataResponse = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            String status;
            String message;

            if ("QrDataJson".equals(urlType)) {
                status = qrdataResponse.getString(AppConstant.KEY_STATUS);
                message = qrdataResponse.getString(AppConstant.KEY_MESSAGE);
                if (status.equalsIgnoreCase("OK")) {
                    if (message.equalsIgnoreCase("Valid Ticket")) {
//                        String key = loginResponse.getString(AppConstant.KEY_USER);
                        JSONArray res_data = qrdataResponse.getJSONArray(AppConstant.RES_DATA);
//                        String decryptedKey = Utils.decrypt(prefManager.getEncryptPass(), key);
//                        String userDataDecrypt = Utils.decrypt(prefManager.getEncryptPass(), user_data);
//                        Log.d("userdatadecry", "" + userDataDecrypt);
//                        jsonObject = new JSONObject(res_data);
                        final ArrayList<QrcodePojo> responseJson = new Gson().fromJson(res_data.toString(), new TypeToken<ArrayList<QrcodePojo>>() {
                        }.getType());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showHomeScreen(responseJson);
                            }
                        }, 1000);

                    }
                } else {
                    if (status.equals("FAILED")) {
                        Utils.showAlert(this, message);
                    }
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showHomeScreen(ArrayList<QrcodePojo> qrcodePojos) {
        Intent intent = new Intent(HomePage.this, TicketData.class);
        intent.putExtra("username", loginPojos.get(0).getUsername());
        intent.putExtra("temple_id", String.valueOf(loginPojos.get(0).getTemple_id()));
        intent.putExtra("tokenid", loginPojos.get(0).getTokenid());
        intent.putExtra("qrcode_Pojo", qrcodePojos);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }

    @Override
    public void OnError(VolleyError volleyError) {

    }
}