package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private final HashMap<Group, List<User>> groupUserMap;
    private final HashMap<Group, List<Message>> groupMessageMap;
    private final HashMap<Message, User> senderMap;
    private final HashMap<Group, User> adminMap;
    private final HashSet<String> userMobile;
    private final HashMap<String, User> userMap;
    private final HashMap<Integer, Message> messageMap;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.userMap = new HashMap<>();
        this.messageMap =  new HashMap<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name, String mobile) throws Exception{
        if(userMobile.contains(mobile)){
            throw new Exception("User already exists");
        }
        User user = new User(name, mobile);
        userMap.put(mobile, user);
        userMobile.add(mobile);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users){
        Group newGroup;
        if(users.size()==2){
            newGroup = new Group(users.get(1).getName(), users.size());
        }else{
            newGroup = new Group("Group "+ ++customGroupCount, users.size());
        }
        groupUserMap.put(newGroup, users);
        adminMap.put(newGroup, users.get(0));
        groupMessageMap.put(newGroup, new ArrayList<Message>());
        return  newGroup;
    }

    public int createMessage(String content){
        messageId++;
        Message newMessage = new Message(messageId, content);
        messageMap.put(messageId, newMessage);
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        if(!groupUserMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }
        boolean flag = false;
        for(User user: groupUserMap.get(group)){
            if(user.getMobile().equals(sender.getMobile())){
                flag = true;
                break;
            }
        }
        if(!flag){
            throw new Exception("You are not allowed to send message");
        }

        senderMap.put(message, sender);
        List<Message> list = groupMessageMap.get(group);
        list.add(message);
        groupMessageMap.put(group, list);
        return list.size();
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception{
        if(!groupUserMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }
        if(adminMap.get(group).getMobile() != approver.getMobile()){
            throw new Exception("Approver does not have rights");
        }
        boolean flag = false;
        for(User u: groupUserMap.get(group)){
            if(u.getMobile().equals(user.getMobile())){
                flag = true;
                break;
            }
        }
        if(!flag)
            throw new Exception("User is not a participant");
        adminMap.put(group, user);
        return "SUCCESS";
    }

}