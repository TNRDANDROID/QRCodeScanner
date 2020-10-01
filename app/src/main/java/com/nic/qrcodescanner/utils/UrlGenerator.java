package com.nic.qrcodescanner.utils;


import com.nic.qrcodescanner.Application.NICApplication;
import com.nic.qrcodescanner.R;

/**
 * Created by Achanthi Sundar  on 21/03/16.
 */
public class UrlGenerator {


    public static String getCommonUrl() {
        return NICApplication.getAppString(R.string.COMMON_URL);
    }

    public static String getTnhrceHostName() {
        return NICApplication.getAppString(R.string.TNHRCE_HOST_NAME);
    }


}
