package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Integer, Message> messageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private HashMap<String, User> userMap;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.messageMap = new HashMap<Integer, Message>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<String>();
        this.userMap = new HashMap<String, User>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name, String mobile) throws Exception{

        if(userMobile.contains(mobile)){
            throw new Exception("User already exists");
        }

        userMobile.add(mobile);
        userMap.put(mobile, new User(name, mobile));

        return "Success";
    }

    public Group createGroup(List<User> users){
        Group newGroup;
        if(users.size() == 2){
            newGroup = new Group(users.get(1).getName(), users.size());
        }else{
            newGroup = new Group("Group"+ ++customGroupCount, users.size());
        }
        adminMap.put(newGroup, users.get(0));
        groupUserMap.put(newGroup, users);
        return newGroup;
    }

    public int createMessage(String content){

        messageId++;
        Message message = new Message();
        message.setId(messageId);
        message.setContent(content);
        messageMap.put(messageId, message);

        return messageId;
    }

    public int  sendMessage(Message message, User sender, Group group) throws Exception {

        if(group == null){
            throw new Exception("Group does not exist");
        }
        boolean isUserInGroup = false;

        for(User user:groupUserMap.get(group)){
            if(sender.getMobile().equals(user.getMobile())){
                isUserInGroup = true;
                break;
            }
        }
        if(!isUserInGroup){
            throw new Exception("You are not allowed to send message");
        }

        senderMap.put(message, sender);
        List<Message> mssgList = groupMessageMap.get(group);
        mssgList.add(message);
        groupMessageMap.put(group, mssgList);
        return mssgList.size();
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {

        if(!adminMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }

        if(!adminMap.get(group).equals(approver)){
            throw new Exception("Approver does not have rights");
        }

        boolean isUserInGroup = false;
        for(User userCheck: groupUserMap.get(group)){
            if(userCheck.equals((user))){
               isUserInGroup = true;
               break;
            }
        }

        if(!isUserInGroup){
            throw new Exception("User is not a participant");
        }

        adminMap.put(group, user);
        return  "Success";
    }
}
