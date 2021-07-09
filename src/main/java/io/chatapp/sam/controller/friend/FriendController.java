package io.chatapp.sam.controller.friend;

import io.chatapp.sam.controller.Controller;
import io.chatapp.sam.dto.FriendDto;
import io.chatapp.sam.service.ChatService;
import io.chatapp.sam.service.FriendsService;
import io.chatapp.sam.service.UserService;
import io.chatapp.sam.utils.Builder;
import io.chatapp.sam.utils.Decoders;
import io.chatapp.sam.utils.Encoders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.websocket.Session;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Controller
public class FriendController implements Controller {
    private static FriendsService friendsService = new FriendsService();
    private static UserService userService = new UserService();
    private static ChatService chatService = new ChatService();
    public ResponseEntity<?> request(String subtype, String message) throws Exception {
        FriendDto friendDto = Decoders.getFriendDto(message);
        ResponseEntity<?> result = new ResponseEntity<>("not processed", textHttpHeader, HttpStatus.BAD_REQUEST);
        switch(subtype) {
            case("view"):
                result = getFriend(friendDto);
                break;
            case("request-create"):
                result = new ResponseEntity<>(createFriendRequest(friendDto), textHttpHeader, HttpStatus.OK);
                break;
            case("request-update"):
                result = new ResponseEntity<>(updateFriend(friendDto), textHttpHeader, HttpStatus.OK);
                break;
            case("request-delete"):
                result = new ResponseEntity<>(deleteFriend(friendDto), textHttpHeader, HttpStatus.OK);
                break;
            case("list-friends"):
                result = new ResponseEntity<>(getFriends(friendDto), jsonHttpHeader, HttpStatus.OK);
                break;
            case("list-pending-invitation"):
                result = new ResponseEntity<>(getPendingInvitations(friendDto), jsonHttpHeader, HttpStatus.OK);
                break;
            case("list-connection-request"):
                result = new ResponseEntity<>(getConnectionRequests(friendDto), jsonHttpHeader, HttpStatus.OK);
                break;
            case("chat-read"):
                result = new ResponseEntity<>(getChat(friendDto), jsonHttpHeader, HttpStatus.OK);
                break;
            case("chat-write"):
                result = new ResponseEntity<>(writeChat(friendDto), textHttpHeader, HttpStatus.OK);
                break;
        }
        return result;
    }
    private ResponseEntity<?> getFriend(FriendDto friendDto) throws Exception {
        if(!friendDto.getUser().equals(friendDto.getConnection())) {
            return new ResponseEntity<>(Map.of("account", userService.getUser(friendDto.getConnection()), "status", friendsService.getStatus(friendDto.getUser(),
                    friendDto.getConnection())), jsonHttpHeader, HttpStatus.OK);
        }
        return new ResponseEntity<>("invalid query", textHttpHeader, HttpStatus.BAD_REQUEST);
    }
    private String createFriendRequest(FriendDto friendDto) throws Exception {
        friendsService.insertFriend(Builder.friendsBuilder(friendDto.getUser(), friendDto.getConnection(), 2));
        friendsService.insertFriend(Builder.friendsBuilder(friendDto.getConnection(), friendDto.getUser(), 1));
        return "success";
    }
    private String updateFriend(FriendDto friendDto) throws Exception {
        chatService.insertFriend(friendDto.getUser(), friendDto.getConnection());
        chatService.insertFriend(friendDto.getConnection(), friendDto.getUser());
        friendsService.updateStatus(Builder.friendsBuilder(friendDto.getUser(), friendDto.getConnection(), 3));
        friendsService.updateStatus(Builder.friendsBuilder(friendDto.getConnection(), friendDto.getUser(), 3));
        userService.updateFollowersByValue(friendDto.getUser(), 1);
        userService.updateFollowersByValue(friendDto.getConnection(), 1);
        return "success";
    }
    private String deleteFriend(FriendDto friendDto) throws Exception {
        chatService.deleteFriend(friendDto.getUser(), friendDto.getConnection());
        chatService.deleteFriend(friendDto.getConnection(), friendDto.getUser());
        friendsService.removeFriend(friendDto.getUser(), friendDto.getConnection());
        friendsService.removeFriend(friendDto.getConnection(), friendDto.getUser());
        userService.updateFollowersByValue(friendDto.getUser(), -1);
        userService.updateFollowersByValue(friendDto.getConnection(), -1);
        return "success";
    }
    private Map<String, Object> getFriends(FriendDto friendDto) throws Exception {
        return Map.of("friends", friendsService.getFriends(friendDto.getUser()));
    }
    private Map<String, Object> getPendingInvitations(FriendDto friendDto) throws Exception {
        return Map.of("pending-invitations", friendsService.getPendingInvitations(friendDto.getUser()));
    }
    private Map<String, Object> getConnectionRequests(FriendDto friendDto) throws Exception {
        return Map.of("connection-requests", friendsService.getConnectionRequests(friendDto.getUser()));
    }
    private Map<String, Object> getChat(FriendDto friendDto) throws Exception {
        return Map.of("chat", chatService.getFriendChat(friendDto.getUser(), friendDto.getConnection()));
    }
    private String writeChat(FriendDto friendDto) throws Exception {
        chatService.insertFriendMessage(friendDto.getUser(), friendDto.getConnection(), friendDto.getMessage(),
                friendDto.getUser());
        chatService.insertFriendMessage(friendDto.getConnection(), friendDto.getUser(), friendDto.getMessage(),
                friendDto.getUser());
        sendMessage(Arrays.asList(friendDto.getConnection(), friendDto.getUser()), "friend", friendDto.getUser(),
                friendDto.getMessage(), friendDto.getUser());
        return "success";
    }
}
