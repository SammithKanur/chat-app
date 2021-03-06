package io.chatapp.sam.service;

import io.chatapp.sam.controller.Controller;
import io.chatapp.sam.dao.FriendsDao;
import io.chatapp.sam.dto.RtcpcDto;
import io.chatapp.sam.entity.Friends;
import io.chatapp.sam.utils.Encoders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.List;

@Service
public class FriendsService {
    private static final Logger logger = LoggerFactory.getLogger(FriendsService.class);
    private static FriendsDao friendsDao = new FriendsDao();
    public void insertFriend(Friends friends) throws Exception {
        if(friends.getConnection().equals(friends.getUser()))
            throw new Exception();
        friendsDao.insert(friends);
    }
    public void removeFriend(String user, String connection) throws Exception {
        friendsDao.deleteOne(user, connection);
        friendsDao.deleteOne(connection, user);
    }
    public void removeUser(String user) throws Exception {
        friendsDao.deleteUser(user);
    }
    public void updateStatus(Friends friends) throws Exception {
        friendsDao.updateStatus(friends.getUser(), friends.getConnection(), friends.getStatus());
    }
    public List<Friends> getFriends(String user) throws Exception {
        return friendsDao.findByStatus(user, 3);
    }
    public List<Friends> getPendingInvitations(String user) throws Exception {
        return friendsDao.findByStatus(user, 2);
    }
    public List<Friends> getConnectionRequests(String user) throws Exception {
        return friendsDao.findByStatus(user, 1);
    }
    public String getStatus(String user, String connection) throws Exception {
        return trackStatus(friendsDao.findStatus(user, connection));
    }
    public String trackStatus(Integer status) {
        if(status == 1) {
            return "Request";
        } else if(status == 2) {
            return "Pending";
        } else if(status == 3) {
            return "Friends";
        }
        return "not connected";
    }
    public boolean isFriend(String user, String connection) throws Exception {
        return !getStatus(user, connection).equals("not connected");
    }
    public void updateCalling(String user, String connection, Integer calling) throws Exception {
        friendsDao.setCalling(user, connection, calling);
    }
}
