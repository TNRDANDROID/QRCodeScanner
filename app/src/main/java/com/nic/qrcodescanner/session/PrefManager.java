package com.nic.qrcodescanner.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nic.qrcodescanner.constant.AppConstant;
import com.nic.qrcodescanner.pojo.LoginPojo;
import com.nic.qrcodescanner.pojo.QrcodePojo;

import org.json.JSONArray;

import java.util.ArrayList;


/**
 * Created by AchanthiSudan on 11/01/19.
 */
public class PrefManager {

    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private static final String APP_KEY = "AppKey";
    private static final String KEY_USER_AUTH_KEY = "auth_key";
    private static final String KEY_USER_PASS_KEY = "pass_key";
    private static final String KEY_ENCRYPT_PASS = "pass";
    private static final String KEY_USER_NAME = "UserName";
    private static final String KEY_USER_PASSWORD = "UserPassword";

    private static final String KEY_QR_CODE = "QRCode";
    private static final String KEY_USER_QR_CODE_JSON = "UserQrCodeJson";


    private static final String IMEI = "imei";


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(AppConstant.PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }



    public String getIMEI() {
        return pref.getString(IMEI,null);
    }

    public void setImei(String imei) {
        editor.putString(IMEI,imei);
        editor.commit();
    }

    public void setAppKey(String appKey) {
        editor.putString(APP_KEY, appKey);
        editor.commit();
    }

    public String getAppKey() {
        return pref.getString(APP_KEY, null);
    }


    public void clearSession() {
        editor.clear();
        editor.commit();
    }


    public void setUserAuthKey(String userAuthKey) {
        editor.putString(KEY_USER_AUTH_KEY, userAuthKey);
        editor.commit();
    }


    public String getUserAuthKey() {
        return pref.getString(KEY_USER_AUTH_KEY, null);
    }

    public void setUserPassKey(String userPassKey) {
        editor.putString(KEY_USER_PASS_KEY, userPassKey);
        editor.commit();
    }


    public String getUserPassKey() {
        return pref.getString(KEY_USER_PASS_KEY, null);
    }


    public void setUserName(String userName) {
        editor.putString(KEY_USER_NAME, userName);
        editor.commit();
    }

    public String getUserName() { return pref.getString(KEY_USER_NAME, null); }

    public void setUserPassword(String userPassword) {
        editor.putString(KEY_USER_PASSWORD, userPassword);
        editor.commit();
    }

    public String getUserPassword() { return pref.getString(KEY_USER_PASSWORD, null); }


    public void setEncryptPass(String pass) {
        editor.putString(KEY_ENCRYPT_PASS, pass);
        editor.commit();
    }

    public String getEncryptPass() {
        return pref.getString(KEY_ENCRYPT_PASS, null);
    }






    public void setQrCode(String userName) {
        editor.putString(KEY_QR_CODE, userName);
        editor.commit();
    }

    public String getQrCode() {
        return pref.getString(KEY_QR_CODE, null);
    }


    public void clearSharedPreferences(Context context) {
        pref = _context.getSharedPreferences(AppConstant.PREF_NAME, PRIVATE_MODE);
        editor.clear();
        editor.apply();
    }
//    public void setDistrictCodeJson(JSONArray jsonarray) {
//        editor.putString(KEY_DISTRICT_CODE_JSON, jsonarray.toString());
//        editor.commit();
//    }
//
//    private String getDistrictCodeJsonList() {
//        return pref.getString(KEY_DISTRICT_CODE_JSON, null);
//    }


    public void setUserQrCodeJson(JSONArray jsonarray) {
        editor.putString(KEY_USER_QR_CODE_JSON, jsonarray.toString());
        editor.commit();
    }

    private String getUserQrCodeJsonList() {
        return pref.getString(KEY_USER_QR_CODE_JSON, null);
    }

    public ArrayList<LoginPojo> getUserQrCodeJson() {
        ArrayList<LoginPojo> responseJson = null;
        String strJson = getUserQrCodeJsonList();//second parameter is necessary ie.,Value to return if this preference does not exist.
        try {
            if (strJson != null) {
                 responseJson = new Gson().fromJson(strJson.toString(), new TypeToken<ArrayList<LoginPojo>>() {
                }.getType());
            }
        } catch (Exception e) {

        }
        Log.d("prefBlockJson",""+responseJson);
        return responseJson;
    }



}
