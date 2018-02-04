package com.elsys.refpro.refpromobile.http.dto;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class NotificationDTO {
    @SerializedName("to")
    private String recipient;
    private Map<String,String> data;

    public NotificationDTO(String recipient, Map<String, String> data) {
        this.recipient = recipient;
        this.data = data;
    }

    public NotificationDTO() {
        this.data = new HashMap<>();
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public void addData(String key,String value){
        data.put(key,value);
    }
}
