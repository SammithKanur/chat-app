package io.chatapp.sam.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Group {
    @Id
    private String groupName;
    @Id
    private String userName;
    private Integer status;

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
