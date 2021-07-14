package io.chatapp.sam.controller;

import io.chatapp.sam.controller.rtcpc.RtcpcController;
import io.chatapp.sam.dto.ServerDto;
import io.chatapp.sam.utils.Decoders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@org.springframework.stereotype.Controller
@ServerEndpoint(value = "/websocket-server/{userName}/{userSession}")
public class WebSocketServer implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    private Session session;
    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName, @PathParam("userSession") String userSession) throws Exception{
        this.session = session;
        logger.info("in open websocket connection with user {} and session {}", userName, session);
        if(isSessionValid(userName, userSession)) {
            addWebSocketSession(userName, session);
            session.getBasicRemote().sendText("server aok");
        } else {
            session.close();
        }
    }
    @OnMessage
    public void onMessage(Session session, String message) {
        logger.info("Websocketserver message {}", message);
        try {
            ServerDto serverDto = Decoders.getServerDto(message);
            switch(serverDto.getType()) {
                case("rtcpc"):
                    new RtcpcController().request(message);
                    break;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @OnClose
    public void onClose(Session session) {
        logger.info("in close removing session {}", session.getId());
        removeWebSocketSession(session.getId());
    }
    @OnError
    public void onError(Throwable th) {
        th.printStackTrace();
    }
}
