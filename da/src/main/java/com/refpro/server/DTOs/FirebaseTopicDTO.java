package com.refpro.server.DTOs;


import com.refpro.server.models.FirbaseTopics;

import java.util.LinkedHashSet;
import java.util.Set;

public class FirebaseTopicDTO {

    private String topicName;
    private Set<UserDTO> subscribedUsers = new LinkedHashSet<>();

    public FirebaseTopicDTO(){}
    public FirebaseTopicDTO(FirbaseTopics firebaseTopic) {
        this.topicName=firebaseTopic.getTopicName();
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Set<UserDTO> getSubscribedUsers() {
        return subscribedUsers;
    }

    public void setSubscribedUsers(Set<UserDTO> subscribedUsers) {
        this.subscribedUsers = subscribedUsers;
    }
}
