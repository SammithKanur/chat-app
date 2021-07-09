package io.chatapp.sam.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    private String userName;
    private String password;
    private Integer groups;
    private Integer followers;

    public Integer getFollowers() {
        return followers;
    }

    public Integer getGroups() {
        return groups;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public void setGroups(Integer groups) {
        this.groups = groups;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
