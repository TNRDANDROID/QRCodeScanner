package com.nic.qrcodescanner.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class QrcodePojo implements Serializable {
    private String transaction_id;
    private String ack_id;


    private String ackid;
    private String usagestatus;
    private String ticket_updatedon;
    private String darshan_datetime;
    private String service_amount;
    private String tokenid;



    private String booked_date;
    private Integer id;

    public Integer getId() {
        return id;
    }

    public ArrayList<userdata> userdata;

    public class userdata implements Serializable {
        private String age;
        private String name;
        private String gender;
        private int order_no;



        private String proof_desc;
        private String proof_no;
        private String service_amount;

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public int getOrder_no() {
            return order_no;
        }

        public void setOrder_no(int order_no) {
            this.order_no = order_no;
        }

        public String getProof_desc() {
            return proof_desc;
        }

        public void setProof_desc(String proof_desc) {
            this.proof_desc = proof_desc;
        }

        public String getProof_no() {
            return proof_no;
        }

        public void setProof_no(String proof_no) {
            this.proof_no = proof_no;
        }

        public String getService_amount() {
            return service_amount;
        }

        public void setService_amount(String service_amount) {
            this.service_amount = service_amount;
        }
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getAcknowledgement_no() {
        return ack_id;
    }

    public void setAcknowledgement_no(String acknowledgement_no) {
        this.ack_id = acknowledgement_no;
    }

    public String getUsagestatus() {
        return usagestatus;
    }

    public void setUsagestatus(String usagestatus) {
        this.usagestatus = usagestatus;
    }

    public String getTicket_updatedon() {
        return ticket_updatedon;
    }

    public void setTicket_updatedon(String ticket_updatedon) {
        this.ticket_updatedon = ticket_updatedon;
    }

    public String getDarshan_datetime() {
        return darshan_datetime;
    }

    public void setDarshan_datetime(String darshan_datetime) {
        this.darshan_datetime = darshan_datetime;
    }

    public String getService_amount() {
        return service_amount;
    }

    public void setService_amount(String service_amount) {
        this.service_amount = service_amount;
    }

    public String getTokenid() {
        return tokenid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid;
    }

    public String getBooked_date() {
        return booked_date;
    }

    public void setBooked_date(String booked_date) {
        this.booked_date = booked_date;
    }

    public String getAckid() {
        return ackid;
    }

    public void setAckid(String ackid) {
        this.ackid = ackid;
    }

}
