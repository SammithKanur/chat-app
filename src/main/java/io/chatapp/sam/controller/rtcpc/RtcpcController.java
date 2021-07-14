package io.chatapp.sam.controller.rtcpc;

import io.chatapp.sam.controller.Controller;
import io.chatapp.sam.dto.RtcpcDto;
import io.chatapp.sam.utils.Decoders;
import io.chatapp.sam.utils.Encoders;

import javax.websocket.Session;

@org.springframework.stereotype.Controller
public class RtcpcController implements Controller {
    public void request(String message) throws Exception {
        RtcpcDto rtcpcDto = Decoders.getRtcpcDto(message);
        switch(rtcpcDto.getSubtype()) {
            case("offer"):
                sendMessage(rtcpcDto, message);
                break;
            case("answer"):
                sendMessage(rtcpcDto, message);
                break;
            case("icecandidate"):
                sendMessage(rtcpcDto, message);
                break;
        }
    }
    private void sendMessage(RtcpcDto rtcpcDto, String message) throws Exception {
        if(hasWsSession(rtcpcDto.getPeer())) {
            Session session = getWsSession(rtcpcDto.getSender());
            session.getBasicRemote().sendText(Encoders.getObjectEncoded(new RtcpcDto("rtcpc",
                    "error", rtcpcDto.getPeer() + " is offline", "", "")));
        } else {
            Session session = getWsSession(rtcpcDto.getPeer());
            session.getBasicRemote().sendText(message);
        }
    }
}
