package io.chatapp.sam.controller.websocketserver;

import io.chatapp.sam.controller.Controller;
import io.chatapp.sam.controller.user.UserController;
import io.chatapp.sam.dto.ServerDto;
import io.chatapp.sam.utils.Decoders;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@org.springframework.stereotype.Controller
@ServerEndpoint(value = "/websocket-home-server/{userName}/{userSession}")
public class WebSocketHomeServer implements Controller{
    private static final Logger logger = LoggerFactory.getLogger(WebSocketHomeServer.class);
    private static final Map<String, Session> wsUser = new ConcurrentHashMap<>();
    private static final Map<String, String> wsSession = new ConcurrentHashMap<>();
    private Session session;
    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName, @PathParam("userSession") String userSession) throws Exception{
        this.session = session;
        logger.info("in open websocket connection with user {} and session {}", userName, session);
        if(isSessionValid(userName, userSession)) {
            addWsSession(userName, session);
        } else {
            session.close();
        }
    }
    @OnMessage
    public void onMessage(Session session, String message) {
        logger.info("Websocketserver message {}", message);
        ServerDto serverDto = Decoders.getServerDto(message);
        try {
            switch(serverDto.getType()) {
                case("user"):
                    String result = (String)new UserController().request(serverDto.getSubtype(), message);
                    sendMessage(Arrays.asList(serverDto.getValidUserName()), String.format("{\"type\":\"user\", " +
                            "\"subtype\":\"prediction-list\", \"payload\":%s}", result), "");
                    break;
            }
        } catch(Exception e) {
            e.printStackTrace();
            try {
                sendMessage(Arrays.asList(serverDto.getValidUserName()), String.format("{\"type\":\"user\", " +
                        "\"subtype\":\"error\", \"payload\":\"%s\"}","error-message"), "");
            } catch(Exception e0) {
                e0.printStackTrace();
            }
        }
    }
    @OnClose
    public void onClose(Session session) throws Exception{
        logger.info("in close removing session {}", session.getId());
        removeWsSession(session.getId());
    }
    @OnError
    public void onError(Throwable th) {
        th.printStackTrace();
    }
    public static boolean hasWsSession(String userName) {
        return wsUser.containsKey(userName);
    }
    public static void removeWsSession(String session) {
        wsUser.remove(wsSession.get(session));
        wsSession.remove(session);
    }
    public static void addWsSession(String userName, Session session) {
        wsSession.put(session.getId(), userName);
        wsUser.put(userName, session);
    }
    public static Session getWsSession(String userName) {
        return wsUser.get(userName);
    }
    public static void sendMessage(List<String> members, String payLoad, String sender) throws Exception {
        for(String member : members) {
            if(hasWsSession(member)) {
                if(member.equals(sender))
                    continue;
                Session session = getWsSession(member);
                session.getBasicRemote().sendText(payLoad);
            }
        }
    }
}

