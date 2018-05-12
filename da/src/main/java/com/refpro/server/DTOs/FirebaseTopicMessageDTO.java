package com.refpro.server.DTOs;

import java.util.HashMap;
import java.util.Map;

public class FirebaseTopicMessageDTO {
    private String condition;
    private Map<String, String> data = new HashMap<>();

    public FirebaseTopicMessageDTO(String condition, Map<String, String> data) {
        this.condition = condition;
        this.data = data;
    }

    public FirebaseTopicMessageDTO() {
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
