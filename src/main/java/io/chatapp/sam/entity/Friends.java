package io.chatapp.sam.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Friends {
    @Id
    private String user;
    @Id
    private String connection;
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getConnection() {
        return connection;
    }

    public String getUser() {
        return user;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
