package com.nic.qrcodescanner.pojo;

import java.io.Serializable;
import java.util.List;

public class LoginPojo implements Serializable {
    private int temple_id;
    private int loginid;
    private String tokenid;
    private String temple_name;
    private String username;
    private String designation_desc;



    private String lastlogin_date;

//    private List<Userdata> userdata;
//
//    public class Userdata {
//
//    }
    public int getTemple_id() {
        return temple_id;
    }

    public void setTemple_id(int temple_id) {
        this.temple_id = temple_id;
    }

    public int getLoginid() {
        return loginid;
    }

    public void setLoginid(int loginid) {
        this.loginid = loginid;
    }

    public String getTokenid() {
        return tokenid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid;
    }

    public String getTemple_name() {
        return temple_name;
    }

    public void setTemple_name(String temple_name) {
        this.temple_name = temple_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDesignation_desc() {
        return designation_desc;
    }

    public void setDesignation_desc(String designation_desc) {
        this.designation_desc = designation_desc;
    }

    public String getTemple_logo() {
        return temple_logo;
    }

    public void setTemple_logo(String temple_logo) {
        this.temple_logo = temple_logo;
    }

    private String temple_logo;

    public String getLastlogin_date() {
        return lastlogin_date;
    }

    public void setLastlogin_date(String lastlogin_date) {
        this.lastlogin_date = lastlogin_date;
    }
}
