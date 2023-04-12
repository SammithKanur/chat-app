package io.chatapp.sam.dto;

public class UserDto {
    private String userName;
    private String password;
    private Integer groups;
    private Integer followers;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGroups(Integer groups) {
        this.groups = groups;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public String getPassword() {
        return password;
    }

    public Integer getGroups() {
        return groups;
    }

    public Integer getFollowers() {
        return followers;
    }
}
