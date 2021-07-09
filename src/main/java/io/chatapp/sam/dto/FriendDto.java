package io.chatapp.sam.dto;
public class FriendDto {
    private String user;
    private String connection;
    private String message;
    private Integer status;

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
