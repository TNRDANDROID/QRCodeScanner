package com.nic.qrcodescanner.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nic.qrcodescanner.Api.Api;
import com.nic.qrcodescanner.Api.ApiService;
import com.nic.qrcodescanner.Api.ServerResponse;
import com.nic.qrcodescanner.R;
import com.nic.qrcodescanner.Support.ProgressHUD;
import com.nic.qrcodescanner.constant.AppConstant;
import com.nic.qrcodescanner.databinding.LoginScreenBinding;
import com.nic.qrcodescanner.pojo.LoginPojo;
import com.nic.qrcodescanner.session.PrefManager;
import com.nic.qrcodescanner.utils.UrlGenerator;
import com.nic.qrcodescanner.utils.Utils;
import com.nic.qrcodescanner.windowpreferences.WindowPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by AchanthiSundar on 28-12-2018.
 */

public class LoginScreen extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {

    private String randString;


    public static SQLiteDatabase db;
    JSONObject jsonObject;

    private PrefManager prefManager;
    private ProgressHUD progressHUD;
    private int setPType;

    public LoginScreenBinding loginScreenBinding;

    Animation stb2;
    String imei;
    String sb;

    //To get Mobile details
    TelephonyManager telephonyManager;
    private Runnable runnable;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    private Handler myHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        loginScreenBinding = DataBindingUtil.setContentView(this, R.layout.login_screen);
        loginScreenBinding.setActivity(this);

        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
//        loginScreenBinding.scrollView.setVerticalScrollBarEnabled(true);
//        loginScreenBinding.scrollView.isSmoothScrollingEnabled();


        intializeUI();


    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        stb2 = AnimationUtils.loadAnimation(this, R.anim.stb2);
        loginScreenBinding.password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        loginScreenBinding.password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    checkLoginScreen();
                }
                return false;
            }
        });

        loginScreenBinding.password.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Poppins-Regular.ttf"));
        loginScreenBinding.rd.setTranslationY(400);
        loginScreenBinding.and.setTranslationY(400);
        loginScreenBinding.dpt.setTranslationY(400);
        loginScreenBinding.tvVersionNumber.setTranslationY(400);
        loginScreenBinding.nicName.setTranslationY(400);

        loginScreenBinding.btnBuy.setTranslationY(400);

        loginScreenBinding.ivItemOne.setTranslationX(800);
        loginScreenBinding.ivItemTwo.setTranslationX(800);


        loginScreenBinding.btnBuy.setAlpha(0);

        loginScreenBinding.rd.setAlpha(0);
        loginScreenBinding.and.setAlpha(0);
        loginScreenBinding.dpt.setAlpha(0);
        loginScreenBinding.tvVersionNumber.setAlpha(0);
        loginScreenBinding.nicName.setAlpha(0);

        loginScreenBinding.ivItemOne.setAlpha(0);
        loginScreenBinding.ivItemTwo.setAlpha(0);


        loginScreenBinding.btnBuy.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();

        loginScreenBinding.rd.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        loginScreenBinding.and.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        loginScreenBinding.dpt.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        loginScreenBinding.tvVersionNumber.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        loginScreenBinding.nicName.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(100).start();

        loginScreenBinding.ivItemOne.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(200).start();
        loginScreenBinding.ivItemTwo.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(400).start();

        loginScreenBinding.ivIlls.startAnimation(stb2);


        try {
            String versionName = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;
            loginScreenBinding.tvVersionNumber.setText("Version" + " " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        checkIMEI();

    }

    public void checkIMEI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                        PERMISSIONS_REQUEST_READ_PHONE_STATE);

            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            imei = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.d("10", "" + imei);
        } else {
            getMobileDetails();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    public boolean validate() {
        boolean valid = true;
        String username = loginScreenBinding.username.getText().toString().trim();
        prefManager.setUserName(username);
        String password = loginScreenBinding.password.getText().toString().trim();


        if (username.isEmpty()) {
            valid = false;
            Utils.showAlert(this, "Please enter the username");
        } else if (password.isEmpty()) {
            valid = false;
            Utils.showAlert(this, "Please enter the password");
        }
        return valid;
    }

    public void checkLoginScreen() {
        final String username = loginScreenBinding.username.getText().toString().trim();
        final String password = loginScreenBinding.password.getText().toString().trim();
        prefManager.setUserPassword(password);

        if (Utils.isOnline()) {
            if (!validate())
                return;
            else if (prefManager.getUserName().length() > 0 && password.length() > 0) {
                new ApiService(this).makeRequest("LoginScreen", Api.Method.POST, UrlGenerator.getCommonUrl(), loginParams(), "not cache", this);
            } else {
                Utils.showAlert(this, "Please enter your username and password!");
            }
        } else {
            Utils.showAlert(this, getResources().getString(R.string.no_internet));
        }
    }


    private void getMobileDetails() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (telephonyManager.getDeviceId() != null) {
            imei = telephonyManager.getDeviceId();
        } else {
            imei = Settings.Secure.getString(
                    getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
//        imei = telephonyManager.getDeviceId();
        Log.d("IMEI", "" + imei);

    }

    public Map<String, String> loginParams() {

        Map<String, String> params = new HashMap<>();
        params.put(AppConstant.KEY_SERVICE_ID, "login");

//
//        String random = Utils.randomChar();
//
//        params.put(AppConstant.USER_LOGIN_KEY, random);
//        Log.d("randchar", "" + random);

        params.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        Log.d("user", "" + loginScreenBinding.username.getText().toString().trim());

//        String encryptUserPass = Utils.md5(loginScreenBinding.password.getText().toString().trim());
//        prefManager.setEncryptPass(encryptUserPass);
//        Log.d("md5", "" + encryptUserPass);

//        String userPass = encryptUserPass.concat(random);
//        Log.d("userpass", "" + userPass);
        String sha256 = Utils.getSHA(loginScreenBinding.username.getText().toString().trim().concat(loginScreenBinding.password.getText().toString().trim()));
        Log.d("sha", "" + sha256);

        params.put(AppConstant.KEY_USER_PASSWORD, sha256);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            imei = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        else {
            getMobileDetails();
        }
        params.put(AppConstant.KEY_USER_IMEI_NO, "1cd4709abfe8aa90");
//        params.put(AppConstant.KEY_USER_IMEI_NO, imei);

        Log.d("params", "" + params);

        return params;
    }

    //The method for opening the registration page and another processes or checks for registering


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject loginResponse = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            String status;
            String message;

            if ("LoginScreen".equals(urlType)) {
                status = loginResponse.getString(AppConstant.KEY_STATUS);
                message = loginResponse.getString(AppConstant.KEY_MESSAGE);
                if (status.equalsIgnoreCase("OK")) {
                    if (message.equalsIgnoreCase("LOGIN SUCCESS")) {
//                        String key = loginResponse.getString(AppConstant.KEY_USER);
                        final JSONArray res_data = loginResponse.getJSONArray(AppConstant.RES_DATA);
//                        String decryptedKey = Utils.decrypt(prefManager.getEncryptPass(), key);
//                        String userDataDecrypt = Utils.decrypt(prefManager.getEncryptPass(), user_data);
//                        Log.d("userdatadecry", "" + userDataDecrypt);
//                        jsonObject = new JSONObject(res_data);
                        prefManager.setUserQrCodeJson(res_data);
                        final ArrayList<LoginPojo> responseJson = new Gson().fromJson(res_data.toString(), new TypeToken<ArrayList<LoginPojo>>() {
                        }.getType());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showHomeScreen(responseJson);
//                                prefManager.setUserName(res_data.toString());
                            }
                        }, 1000);



                    } else {
                        if (message.equals("LOGIN_FAILED")) {
                            Utils.showAlert(this, "Invalid UserName Or Password");
                        }
                    }
                } else {
                    if (status.equals("FAILED")) {
                        Utils.showAlert(this, message);
                    }
                }

            }
            if ("VillageList".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {

                }
                Log.d("VillageList", "" + responseDecryptedBlockKey);
            }
            if ("HabitationList".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {

                }
                Log.d("HabitationList", "" + responseDecryptedBlockKey);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void OnError(VolleyError volleyError) {
        Utils.showAlert(this, "Login Again");
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        showHomeScreen();
//    }

    public void showHomeScreen(ArrayList<LoginPojo> loginPojo) {
//        Gson gson = new Gson();
//        String convertedJson = gson.toJson(loginPojo);
        Intent intent = new Intent(LoginScreen.this, HomePage.class);
        intent.putExtra("Ticket_screen", "Login");
        intent.putExtra("login_Pojo", loginPojo);

        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
