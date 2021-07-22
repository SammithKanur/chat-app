package io.chatapp.sam.dto;
public class FriendDto {
    private String user;
    private String connection;
    private String message;
    private Integer status;
    private Integer calling;

    public FriendDto(String user, String connection, String message, Integer status, Integer calling) {
        this.user = user;
        this.connection = connection;
        this.message = message;
        this.status = status;
        this.calling = calling;
    }
    public Integer getCalling() {
        return calling;
    }

    public void setCalling(Integer calling) {
        this.calling = calling;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getUser() {
        return user;
    }

    public String getConnection() {
        return connection;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
