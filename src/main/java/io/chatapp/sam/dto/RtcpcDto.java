package io.chatapp.sam.dto;

public class RtcpcDto {
    private String type;
    private String subtype;
    private Object payload;
    private String peer;
    private String sender;

    public RtcpcDto(String type, String subtype, Object payload, String peer, String sender) {
        this.type = type;
        this.subtype = subtype;
        this.payload = payload;
        this.peer = peer;
        this.sender = sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getType() {
        return type;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPeer() {
        return peer;
    }

    public void setPeer(String peer) {
        this.peer = peer;
    }
}
