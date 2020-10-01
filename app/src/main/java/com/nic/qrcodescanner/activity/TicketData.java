package com.nic.qrcodescanner.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.VolleyError;
import com.nic.qrcodescanner.Api.Api;
import com.nic.qrcodescanner.Api.ApiService;
import com.nic.qrcodescanner.Api.ServerResponse;
import com.nic.qrcodescanner.R;
import com.nic.qrcodescanner.adapter.QrCodeUserDataAdapter;
import com.nic.qrcodescanner.constant.AppConstant;
import com.nic.qrcodescanner.databinding.ActivityTicketDataBinding;
import com.nic.qrcodescanner.pojo.QrcodePojo;
import com.nic.qrcodescanner.utils.UrlGenerator;
import com.nic.qrcodescanner.utils.Utils;
import com.nic.qrcodescanner.windowpreferences.WindowPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TicketData extends AppCompatActivity implements Api.ServerResponseListener {
    private ActivityTicketDataBinding activityTicketDataBinding;
    ArrayList<QrcodePojo> qrcodePojos;

    private QrCodeUserDataAdapter qrCodeUserDataAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTicketDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_ticket_data);
        activityTicketDataBinding.setActivity(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        qrcodePojos = (ArrayList<QrcodePojo>) getIntent().getSerializableExtra("qrcode_Pojo");
        activityTicketDataBinding.titleTv.setText(getIntent().getStringExtra("username"));
        if (qrcodePojos != null && qrcodePojos.size() > 0) {
            if (qrcodePojos.get(0).userdata.size() > 0) {
                initRecyclerView();
            } else {
                activityTicketDataBinding.notFoundTv.setVisibility(View.VISIBLE);
            }
//            activityTicketDataBinding.transactionId.setText(qrcodePojos.get(0).getTransaction_id());
            activityTicketDataBinding.acknowledgementNo.setText(qrcodePojos.get(0).getAcknowledgement_no());
            activityTicketDataBinding.usagestatus.setText(qrcodePojos.get(0).getUsagestatus());
            activityTicketDataBinding.bookedDate.setText(qrcodePojos.get(0).getBooked_date());
            activityTicketDataBinding.darshanDatetime.setText(qrcodePojos.get(0).getDarshan_datetime());
//            activityTicketDataBinding.serviceAmount.setText(qrcodePojos.get(0).getService_amount());
        }

    }

    private void initRecyclerView() {
        activityTicketDataBinding.userList.setVisibility(View.VISIBLE);
        qrCodeUserDataAdapter = new QrCodeUserDataAdapter(this, qrcodePojos);
        activityTicketDataBinding.userList.setAdapter(qrCodeUserDataAdapter);
        activityTicketDataBinding.userList.setLayoutManager(new LinearLayoutManager(this));
        activityTicketDataBinding.userList.setItemAnimator(new DefaultItemAnimator());
        activityTicketDataBinding.userList.setNestedScrollingEnabled(false);
    }

    public void verifyTheTicket() {
        if (Utils.isOnline()) {
            Map<String, String> verifyTicketParams = new HashMap<>();
            verifyTicketParams.put(AppConstant.KEY_ACK_NO, qrcodePojos.get(0).getAckid());
            verifyTicketParams.put(AppConstant.KEY_SERVICE_ID, AppConstant.TICKET_UPDATE);
            verifyTicketParams.put(AppConstant.KEY_TEMPLE_ID, String.valueOf(getIntent().getStringExtra("temple_id")));
            verifyTicketParams.put(AppConstant.KEY_TOKEN_ID, getIntent().getStringExtra("tokenid"));

            Log.d("VerifyParam",""+verifyTicketParams);

            new ApiService(this).makeRequest("VerifyTicket", Api.Method.POST, UrlGenerator.getCommonUrl(), verifyTicketParams, "not cache", this);
        } else {
            Utils.showAlert(this, getResources().getString(R.string.no_internet));
        }
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject qrdataResponse = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            String status;
            String message;

            if ("VerifyTicket".equals(urlType)) {
                status = qrdataResponse.getString(AppConstant.KEY_STATUS);
                message = qrdataResponse.getString(AppConstant.KEY_MESSAGE);
                if (status.equalsIgnoreCase("OK")) {
                    if (message.equalsIgnoreCase("Ticket Details Updated Successfully")) {
                        Utils.showAlert(this, message);
                    }
                }else{
                    if(status.equalsIgnoreCase("FAILED")){
                        Utils.showAlert(this,message);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    public void onBack() {
        Intent intent = new Intent(this, HomePage.class);
        intent.putExtra("Ticket_screen", "Ticket_screen");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
