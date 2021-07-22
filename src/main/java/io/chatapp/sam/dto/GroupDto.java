package io.chatapp.sam.dto;
public class GroupDto {
    private String groupName;
    private String userName;
    private String message;
    private Integer status;
    private Integer inMeeting;

    public GroupDto(String groupName, String userName, String message, Integer status, Integer inMeeting) {
        this.groupName = groupName;
        this.userName = userName;
        this.message = message;
        this.status = status;
        this.inMeeting = inMeeting;
    }
    public Integer getInMeeting() {
        return inMeeting;
    }

    public void setInMeeting(Integer inMeeting) {
        this.inMeeting = inMeeting;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
