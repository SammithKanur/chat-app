package io.chatapp.sam.controller.user;

import io.chatapp.sam.controller.Controller;
import io.chatapp.sam.dto.UserDto;
import io.chatapp.sam.entity.User;
import io.chatapp.sam.service.*;
import io.chatapp.sam.utils.Decoders;
import io.chatapp.sam.utils.Encoders;
import io.chatapp.sam.utils.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.websocket.Session;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Controller
public class UserController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static FriendsService friendsService = new FriendsService();
    private static GroupService groupService = new GroupService();
    private static UserService userService = new UserService();
    private static ChatService chatService = new ChatService();
    private static SearchService searchService = new SearchService();
    public ResponseEntity<?> request(String subtype, String message) throws Exception {
        UserDto userDto = Decoders.getUserDto(message);
        ResponseEntity<?> result = new ResponseEntity<>("not processed", textHttpHeader, HttpStatus.BAD_REQUEST);
        switch(subtype) {
            case("home"):
                result = new ResponseEntity<>(getHome(userDto), jsonHttpHeader, HttpStatus.OK);
                break;
            case("account-view"):
                result = new ResponseEntity<>(getAccountView(userDto), jsonHttpHeader, HttpStatus.OK);
                break;
            case("account-update"):
                result = new ResponseEntity<>(getAccountUpdate(userDto), textHttpHeader, HttpStatus.OK);
                break;
            case("account-remove"):
                result = new ResponseEntity<>(getAccountRemove(userDto), textHttpHeader, HttpStatus.OK);
                break;
            case("logout"):
                result = new ResponseEntity<>(userLogout(userDto), textHttpHeader, HttpStatus.OK);
                break;
        }
        return result;
    }
    private Map<String, Object> getHome(UserDto userDto) throws Exception {
        return Map.of("friends", friendsService.getFriends(userDto.getUserName()), "groups",
                groupService.getGroups(userDto.getUserName()));
    }
    private User getAccountView(UserDto userDto) throws Exception {
        return userService.getUser(userDto.getUserName());
    }
    private String getAccountUpdate(UserDto userDto) throws Exception {
        userService.updateUser(Mapper.userDtoToUser(userDto));
        return "success";
    }
    private String getAccountRemove(UserDto userDto) throws Exception {
        List<String> friends = friendsService.getFriends(userDto.getUserName());
        for(String friend : friends) {
            chatService.deleteFriend(friend, userDto.getUserName());
        }
        userService.deleteUser(userDto.getUserName());
        friendsService.removeUser(userDto.getUserName());
        groupService.deleteUser(userDto.getUserName());
        chatService.deleteUser(userDto.getUserName());
        searchService.deleteUser(userDto.getUserName());
        return "success";
    }
    public String userLogout(UserDto userDto) throws Exception {
        removeHttpSession(userDto.getUserName());
        return "success";
    }
}
