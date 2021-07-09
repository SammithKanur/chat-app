package io.chatapp.sam.dto;

public class ServerDto {
    private String validUserName;
    private String session;
    private String type;
    private String subtype;

    public String getSession() {
        return session;
    }

    public String getValidUserName() {
        return validUserName;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setValidUserName(String validUserName) {
        this.validUserName = validUserName;
    }

    public String getType() {
        return type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public void setType(String type) {
        this.type = type;
    }
}
