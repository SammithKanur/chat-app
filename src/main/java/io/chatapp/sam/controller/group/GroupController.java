package io.chatapp.sam.controller.group;

import io.chatapp.sam.controller.Controller;
import io.chatapp.sam.dto.FriendDto;
import io.chatapp.sam.dto.GroupDto;
import io.chatapp.sam.dto.ServerDto;
import io.chatapp.sam.service.ChatService;
import io.chatapp.sam.service.FriendsService;
import io.chatapp.sam.service.GroupService;
import io.chatapp.sam.service.UserService;
import io.chatapp.sam.utils.Decoders;
import io.chatapp.sam.utils.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@org.springframework.stereotype.Controller
public class GroupController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);
    private static GroupService groupService = new GroupService();
    private static ChatService chatService = new ChatService();
    private static UserService userService = new UserService();
    private static FriendsService friendsService = new FriendsService();
    public ResponseEntity<?> request(String subtype, String message) throws Exception {
        GroupDto groupDto = Decoders.getGroupDto(message);
        ResponseEntity<?> result = new ResponseEntity<>("not processed", textHttpHeader, HttpStatus.BAD_REQUEST);
        switch(subtype) {
            case("create"):
                result = createGroup(groupDto);
                break;
            case("member-add"):
                result = memberAdd(groupDto, Decoders.getServerDto(message));
                break;
            case("member-update-status"):
                result = new ResponseEntity<>(memberUpdateStatus(groupDto), textHttpHeader, HttpStatus.OK);
                break;
            case("member-delete"):
                result = new ResponseEntity<>(memberDelete(groupDto), textHttpHeader, HttpStatus.OK);
                break;
            case("chat-read"):
                result = new ResponseEntity<>(getChat(groupDto), jsonHttpHeader, HttpStatus.OK);
                break;
            case("chat-write"):
                result = new ResponseEntity<>(writeChat(groupDto), textHttpHeader, HttpStatus.OK);
                break;
            case("group-members"):
                result = new ResponseEntity<>(getGroupMembers(groupDto), jsonHttpHeader, HttpStatus.OK);
                break;
            case("group-pending-invitations"):
                result = new ResponseEntity<>(getGroupPendingInvitations(groupDto), jsonHttpHeader, HttpStatus.OK);
                break;
            case("user-group-requests"):
                result = new ResponseEntity<>(getuserGroupRequests(groupDto), jsonHttpHeader, HttpStatus.OK);
                break;
            case("user-group-list"):
                result = new ResponseEntity<>(getUserGroups(groupDto), jsonHttpHeader, HttpStatus.OK);
                break;
        }
        return result;
    }

    private ResponseEntity<?> createGroup(GroupDto groupDto) throws Exception {
        if(!groupDto.getGroupName().isEmpty() && !groupService.isGroupPresent(groupDto.getGroupName())) {
            chatService.insertGroup(groupDto.getGroupName());
            groupService.insertGroup(Mapper.groupDtoToGroup(groupDto));
            userService.updateGroupsByValue(groupDto.getUserName(), 1);
            return new ResponseEntity<>("successfully created group", textHttpHeader, HttpStatus.OK);
        }
        return new ResponseEntity<>("invalid groupname/group already exists", textHttpHeader, HttpStatus.BAD_REQUEST);
    }
    private ResponseEntity<?> memberAdd(GroupDto groupDto, ServerDto serverDto) throws Exception {
        if(userService.isUserPresent(groupDto.getUserName()) && !groupService.isMember(groupDto.getGroupName(),
                groupDto.getUserName()) && friendsService.isFriend(serverDto.getValidUserName(), groupDto.getUserName())) {
            groupService.insertGroup(Mapper.groupDtoToGroup(groupDto));
            return new ResponseEntity<>("success", textHttpHeader, HttpStatus.OK);
        }
        return new ResponseEntity<>("invalid member", textHttpHeader, HttpStatus.BAD_REQUEST);
    }
    private String memberUpdateStatus(GroupDto groupDto) throws Exception {
        groupService.setStatus(groupDto.getGroupName(), groupDto.getUserName(), groupDto.getStatus());
        if(groupDto.getStatus() == 2) {
            userService.updateGroupsByValue(groupDto.getUserName(), 1);
        }
        return "success";
    }
    private String memberDelete(GroupDto groupDto) throws Exception {
        groupService.deleteMember(groupDto.getGroupName(), groupDto.getUserName());
        userService.updateGroupsByValue(groupDto.getUserName(), -1);
        if(!groupService.isGroupPresent(groupDto.getGroupName()))
            chatService.deleteGroup(groupDto.getGroupName());
        return "success";
    }
    private Map<String, Object> getChat(GroupDto groupDto) throws Exception {
        return Map.of("chat", chatService.getGroupChat(groupDto.getGroupName()));
    }
    private String writeChat(GroupDto groupDto) throws Exception {
        chatService.insertGroupMessage(groupDto.getGroupName(), groupDto.getMessage(),
                groupDto.getUserName());
        sendMessage(groupService.getMembers(groupDto.getGroupName()), "group", groupDto.getGroupName(),
                groupDto.getMessage(), groupDto.getUserName());
        return "success";
    }
    private Map<String, Object> getGroupMembers(GroupDto groupDto) throws Exception {
        return Map.of("group-members", groupService.getMembers(groupDto.getGroupName()));
    }
    private Map<String, Object> getGroupPendingInvitations(GroupDto groupDto) throws Exception {
        return Map.of("group-pending-invitations", groupService.getRequests(groupDto.getGroupName()));
    }
    private Map<String, Object> getuserGroupRequests(GroupDto groupDto) throws Exception {
        return Map.of("user-group-requests", groupService.getGroupRequests(groupDto.getUserName()));
    }
    private Map<String, Object> getUserGroups(GroupDto groupDto) throws Exception {
        return Map.of("user-groups", groupService.getGroups(groupDto.getUserName()));
    }
}
