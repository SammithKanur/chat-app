package io.chatapp.sam.utils;

import io.chatapp.sam.dto.FriendDto;
import io.chatapp.sam.dto.GroupDto;
import io.chatapp.sam.dto.UserDto;
import io.chatapp.sam.entity.Friends;
import io.chatapp.sam.entity.Group;
import io.chatapp.sam.entity.User;

public class Mapper {
    public static User userDtoToUser(UserDto userDto) {
        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setFollowers(userDto.getFollowers());
        user.setGroups(userDto.getGroups());
        user.setPassword(userDto.getPassword());
        return user;
    }
    public static Friends friendDtoToFriends(FriendDto friendDto) {
        return Builder.friendsBuilder(friendDto.getUser(), friendDto.getConnection(), friendDto.getStatus());
    }
    public static Group groupDtoToGroup(GroupDto groupDto) {
        return Builder.groupBuilder(groupDto.getGroupName(), groupDto.getUserName(), groupDto.getStatus());
    }
}
