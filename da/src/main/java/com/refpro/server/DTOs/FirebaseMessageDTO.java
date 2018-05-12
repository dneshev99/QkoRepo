package com.refpro.server.DTOs;

import java.util.HashMap;
import java.util.Map;

public class FirebaseMessageDTO {
    private String to;
    private Map<String, String> data = new HashMap<>();

    public FirebaseMessageDTO(String to, Map<String, String> data) {
        this.to = to;
        this.data = data;
    }

    public FirebaseMessageDTO() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public void addToData(String key, String value) {
        data.put(key, value);
    }
}
