package io.chatapp.sam.utils;

import io.chatapp.sam.entity.Friends;
import io.chatapp.sam.entity.Group;

public class Builder {
    public static Friends friendsBuilder(String user, String connection, int status) {
        Friends friends = new Friends();
        friends.setUser(user);
        friends.setConnection(connection);
        friends.setStatus(status);
        return friends;
    }
    public static Group groupBuilder(String groupName, String userName, int status) {
        Group group = new Group();
        group.setGroupName(groupName);
        group.setStatus(status);
        group.setUserName(userName);
        return group;
    }
}
