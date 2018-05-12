package com.refpro.server.DBhandlers;


import com.refpro.server.DTOs.FirebaseTopicDTO;
import com.refpro.server.DTOs.UserDTO;
import com.refpro.server.models.FirbaseTopics;
import com.refpro.server.models.User;
import com.refpro.server.repositories.FirebaseTopicRepository;
import com.refpro.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

@Component
public class FirebaseTopicsHandler {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHandler userHandler;

    @Autowired
    private FirebaseTopicRepository firebaseTopicRepository;

    public List<FirebaseTopicDTO> getTopicsForUsername(String userName) throws Exception {
        User user = userRepository.getUserByUsername(userName);
        if(user==null)
            throw new Exception("User with not found");

        Query query = new Query();
        Criteria criteria = Criteria.where("subscribedUsers").is(user);
        query.addCriteria(criteria);
        List<FirbaseTopics> topicsForUser = mongoTemplate.find(query, FirbaseTopics.class);
        List<FirebaseTopicDTO> topicsForUserDto = new LinkedList<>();
        for(FirbaseTopics firebaseTopic :topicsForUser){
            topicsForUserDto.add(new FirebaseTopicDTO(firebaseTopic));
        }
        return topicsForUserDto;

    }

    public List<String> createTopics(List<FirebaseTopicDTO> firebaseTopicDTOs) throws Exception {
        List<String> result = new LinkedList<>();
        if(firebaseTopicDTOs==null || firebaseTopicDTOs.isEmpty())
            return null;

        Set<String> userNames = new LinkedHashSet<String>();
        for(FirebaseTopicDTO firebaseTopicDTO:firebaseTopicDTOs){
            if(firebaseTopicDTO.getSubscribedUsers()!=null) {
                for (UserDTO userDto : firebaseTopicDTO.getSubscribedUsers()) {
                    if(!StringUtils.isEmpty(userDto.getUsername())){
                        userNames.add(userDto.getUsername());
                    }

                }
            }
        }

        List<User> foundUsers = userHandler.findAllUsersByUsernames(userNames);
        Map<String,User> userNameToUserMap = new HashMap<>();
        if(foundUsers==null || foundUsers.isEmpty())
            throw new Exception("No users were found to be attached to the topics");
        for(User user:foundUsers){
            userNameToUserMap.put(user.getUsername(),user);
        }

        for(FirebaseTopicDTO firebaseTopicDTO:firebaseTopicDTOs){
            FirbaseTopics newFireBaseTopic = new FirbaseTopics();
            newFireBaseTopic.setTopicName(firebaseTopicDTO.getTopicName());
            for(UserDTO userDTO:firebaseTopicDTO.getSubscribedUsers()){
                if(userNameToUserMap.containsKey(userDTO.getUsername())){
                    newFireBaseTopic.getSubscribedUsers().add(userNameToUserMap.get(userDTO.getUsername()));
                }
            }
            firebaseTopicRepository.save(newFireBaseTopic);
            result.add(newFireBaseTopic.getId());
        }
        return result;
    }
}
