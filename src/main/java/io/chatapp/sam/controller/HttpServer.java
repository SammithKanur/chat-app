package io.chatapp.sam.controller;

import io.chatapp.sam.controller.friend.FriendController;
import io.chatapp.sam.controller.group.GroupController;
import io.chatapp.sam.controller.user.UserController;
import io.chatapp.sam.dto.ServerDto;
import io.chatapp.sam.dto.UserDto;
import io.chatapp.sam.service.ChatService;
import io.chatapp.sam.service.SearchService;
import io.chatapp.sam.service.UserService;
import io.chatapp.sam.utils.Decoders;
import io.chatapp.sam.utils.EnvReader;
import io.chatapp.sam.utils.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;

@Controller
@RequestMapping(value = "/user")
public class HttpServer implements io.chatapp.sam.controller.Controller {
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private static ChatService chatService = new ChatService();
    private static SearchService searchService = new SearchService();
    private static UserService userService = new UserService();
    private final Map<String, Object> properties = EnvReader.getServerMetadata();
    @RequestMapping(value = "/request")
    public DeferredResult<ResponseEntity<?>> handleREquest(@RequestBody String message) {
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>(100L * 1000L);
        executor.execute(() -> {
            try {
                logger.info(message);
                ServerDto serverDto = Decoders.getServerDto(message);
                String validUserName = serverDto.getValidUserName();
                String session = serverDto.getSession();
                if(!isSessionValid(validUserName, session)) {
                    output.setResult(new ResponseEntity<>("unauthorized user", textHttpHeader, HttpStatus.BAD_REQUEST));
                } else {
                    String type= serverDto.getType();
                    String subtype = serverDto.getSubtype();
                    switch(type) {
                        case("user"):
                            output.setResult((ResponseEntity<?>)new UserController().request(subtype, message));
                            break;
                        case("friend"):
                            output.setResult(new FriendController().request(subtype, message));
                            break;
                        case("group"):
                            output.setResult(new GroupController().request(subtype, message));
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                output.setResult(new ResponseEntity<>("server error", textHttpHeader, HttpStatus.BAD_REQUEST));
            }
        });
        return output;
    }
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public DeferredResult<ResponseEntity<?>> login(@RequestBody  String message) {
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>(100L * 1000L);
        executor.execute(() -> {
            try {
                UserDto userDto = Decoders.getUserDto(message);
                if(userService.isUserValid(userDto.getUserName(), userDto.getPassword())) {
                    String session = getSession();
                    addHttpSession(userDto.getUserName(), session);
                    output.setResult(new ResponseEntity<>(session, textHttpHeader, HttpStatus.OK));
                } else {
                    output.setResult(new ResponseEntity<>("invalid username/password", textHttpHeader, HttpStatus.BAD_REQUEST));
                }
            } catch (Exception e) {
                e.printStackTrace();
                output.setResult(new ResponseEntity<>("server error", textHttpHeader, HttpStatus.BAD_REQUEST));
            }
        });
        return output;
    }
    @RequestMapping(value = "/create-account", method = RequestMethod.POST)
    public DeferredResult<ResponseEntity<?>> createAccount(@RequestBody String message) {
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>(100L * 1000L);
        executor.execute(() -> {
            try {
                UserDto userDto = Decoders.getUserDto(message);
                if(userDto.getUserName().length() > 0 && !userService.isUserPresent(userDto.getUserName())) {
                    searchService.insertUser(userDto.getUserName());
                    chatService.insertUser(userDto.getUserName());
                    userService.insertUser(Mapper.userDtoToUser(userDto));
                    output.setResult(new ResponseEntity<>("successfully created account", textHttpHeader, HttpStatus.OK));
                } else {
                    output.setResult(new ResponseEntity<>("account already exists/invalid user", textHttpHeader, HttpStatus.BAD_REQUEST));
                }
            } catch (Exception e) {
                e.printStackTrace();
                output.setResult(new ResponseEntity<>("server error", textHttpHeader, HttpStatus.BAD_REQUEST));
            }
        });
        return output;
    }
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String getHome(@RequestParam String userName, @RequestParam String session, ModelMap model) {
        logger.info("username is {} with session {}", userName, session);
        if(!isSessionValid(userName, session))
            return getLoginPage(model);
        model.addAllAttributes(Map.of("URL", properties.get("URL"), "WSURL", properties.get("WSHOMEURL"),
            "userName", userName, "session", session));
        return "home";
    }
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String getAccount(@RequestParam String userName, @RequestParam String session, ModelMap model) {
        if(!isSessionValid(userName, session)) {
            removeHttpSession(userName);
            return getLoginPage(model);
        }
        model.addAllAttributes(Map.of("URL", properties.get("URL"), "WSURL", properties.get("WSHOMEURL"),
                "userName", userName, "session", session));
        return "account";
    }
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String getProfile(@RequestParam String userName, @RequestParam String session, @RequestParam String friend, ModelMap model) {
        if(!isSessionValid(userName, session)) {
            removeHttpSession(userName);
            return getLoginPage(model);
        }
        model.addAllAttributes(Map.of("URL", properties.get("URL"), "WSURL", properties.get("WSHOMEURL"),
                "userName", userName, "session", session, "friend", friend));
        return "profile";
    }
    @RequestMapping(value = "/group-member", method = RequestMethod.GET)
    public String getGroupMember(@RequestParam String userName, @RequestParam String session,
                                 @RequestParam String groupName, ModelMap model) {
        if(!isSessionValid(userName, session)) {
            removeHttpSession(userName);
            return getLoginPage(model);
        }
        model.addAllAttributes(Map.of("URL", properties.get("URL"), "WSURL", properties.get("WSHOMEURL"),
                "userName", userName, "session", session, "groupName", groupName));
        return "group-member";
    }
    @RequestMapping(value = "/meeting", method = RequestMethod.GET)
    public String getMeetingPage(@RequestParam String userName, @RequestParam String session,
                                 @RequestParam String connection, @RequestParam String peerType,
                                 @RequestParam String sendRtcOffer, ModelMap model) {
        if(!isSessionValid(userName, session)) {
            removeHttpSession(userName);
            return getLoginPage(model);
        }
        model.addAllAttributes(Map.of("URL", properties.get("URL"), "WSURL", properties.get("WSMEETINGURL"),
                "userName", userName, "session", session, "connection", connection,
                "peerType", peerType, "sendRtcOffer", sendRtcOffer));
        return "meeting";
    }
    @RequestMapping(value = "/create-account-page", method = RequestMethod.GET)
    public String getCreateAccount(ModelMap model) {
        model.addAllAttributes(Map.of("URL", properties.get("URL"), "WSURL", properties.get("WSHOMEURL")));
        return "create-account";
    }
    @RequestMapping(value = "/login-page", method = RequestMethod.GET)
    public String getLoginPage(ModelMap model) {
        model.addAllAttributes(Map.of("URL", properties.get("URL"), "WSURL", properties.get("WSHOMEURL")));
        return "login";
    }
}
