package io.chatapp.sam.service;

import io.chatapp.sam.dao.ChatDao;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private static ChatDao dao = new ChatDao();
    private static final String friendsCollection = "friends";
    private static final String groupCollection = "groups";
    public static void main(String[] args) {
        try {
            ChatService service = new ChatService();
            service.insertFriend("a", "b");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insertUser(String userName) {
        Document document = new Document("_id", userName);
        dao.insert(friendsCollection, document);
    }
    public void insertFriend(String userName, String friendName) throws Exception {
        if(userName.equals(friendName))
            throw new Exception();
        Document selectQuery = new Document("_id", userName);
        Document document= new Document("$set", new Document(friendName, new LinkedList<>()));
        dao.update(friendsCollection, selectQuery, document);
    }
    public void deleteUser(String userName) {
        Document document = new Document("_id", userName);
        dao.delete(friendsCollection, document);
    }
    public void insertFriendMessage(String userName, String friendName, String message, String sender) {
        Document updateQuery = new Document("_id", userName);
        Document data = new Document("$push", new Document(friendName, Arrays.asList(message, sender)));
        dao.update(friendsCollection, updateQuery, data);
    }
    public List<List<String>> getFriendChat(String userName, String friendName) {
        Document match = new Document("$match", new Document("_id", userName));
        Document project = new Document("$project", new Document(Map.of("_id", 0, friendName, 1)));
        List<List<String>> chat = (List<List<String>>)dao.find(friendsCollection, Arrays.asList(match, project)).get(friendName);
        return chat;
    }
    public void deleteFriend(String userName, String friendName) {
        Document updateQuery = new Document("_id", userName);
        Document data = new Document("$unset", new Document(friendName, ""));
        dao.update(friendsCollection, updateQuery, data);
    }
    public void insertGroup(String groupName) {
        Document document = new Document(Map.of("_id", groupName, "message", new LinkedList<>()));
        dao.insert(groupCollection, document);
    }
    public void deleteGroup(String groupName) {
        Document document = new Document("_id", groupName);
        dao.delete(groupCollection, document);
    }
    public void insertGroupMessage(String groupName, String message, String sender) {
        Document updateQuery = new Document("_id", groupName);
        Document data = new Document("$push", new Document("message", Arrays.asList(message, sender)));
        dao.update(groupCollection, updateQuery, data);
    }
    public List<List<String>> getGroupChat(String groupName) {
        Document match = new Document("$match", new Document("_id", groupName));
        Document project = new Document("$project", new Document(Map.of("_id", 0, "message", 1)));
        List<List<String>> chat = (List<List<String>>)dao.find(groupCollection, Arrays.asList(match, project)).get("message");
        return chat;
    }
}
