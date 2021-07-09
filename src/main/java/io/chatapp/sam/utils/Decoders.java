package io.chatapp.sam.utils;

import com.google.gson.Gson;
import io.chatapp.sam.dto.FriendDto;
import io.chatapp.sam.dto.GroupDto;
import io.chatapp.sam.dto.ServerDto;
import io.chatapp.sam.dto.UserDto;

public class Decoders {
    private static final Gson gson = new Gson();
    public static UserDto getUserDto(String message) {
        return gson.fromJson(message, UserDto.class);
    }
    public static FriendDto getFriendDto(String message) {
        return gson.fromJson(message, FriendDto.class);
    }
    public static GroupDto getGroupDto(String message) {
        return gson.fromJson(message, GroupDto.class);
    }
    public static ServerDto getServerDto(String message) {
        return gson.fromJson(message, ServerDto.class);
    }
}
