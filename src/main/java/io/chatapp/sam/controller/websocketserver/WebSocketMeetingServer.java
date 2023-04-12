package io.chatapp.sam.controller.websocketserver;

import io.chatapp.sam.controller.Controller;
import io.chatapp.sam.controller.friend.FriendController;
import io.chatapp.sam.controller.group.GroupController;
import io.chatapp.sam.controller.rtcpc.RtcpcController;
import io.chatapp.sam.dto.FriendDto;
import io.chatapp.sam.dto.GroupDto;
import io.chatapp.sam.dto.ServerDto;
import io.chatapp.sam.utils.Decoders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@org.springframework.stereotype.Controller
@ServerEndpoint(value = "/websocket-meeting-server/{userName}/{userSession}/{peer}/{peerType}")
public class WebSocketMeetingServer implements Controller{
    private static final Logger logger = LoggerFactory.getLogger(WebSocketMeetingServer.class);
    private static final Map<String, Map<String, Object>> wsUser = new ConcurrentHashMap<>();
    private static final Map<String, String> wsSession = new ConcurrentHashMap<>();
    private Session session;
    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName, @PathParam("userSession")
            String userSession, @PathParam("peer") String peer, @PathParam("peerType") String peerType) throws Exception {
        this.session = session;
        logger.info("in open websocket connection with user {} and session {}", userName, session);
        if(isSessionValid(userName, userSession)) {
            addWsSession(userName, session, peer, peerType);
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
    public void onClose(Session session) throws Exception{
        logger.info("in close removing session {}", session.getId());
        String userName = wsSession.get(session.getId());
        String peerType = getWsPeerType(userName);
        String peer = getWsPeer(userName);
        if(peerType.equals("friend")) {
            new FriendController().updateCalling(new FriendDto(peer, userName, "", 0, 0));
        } else {
            new GroupController().updateInMeeting(new GroupDto(peer, userName, "", 0, 0));
        }
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
    public static void addWsSession(String userName, Session session, String peer, String peerType) {
        wsSession.put(session.getId(), userName);
        wsUser.put(userName, Map.of("session", session, "peer", peer, "peerType", peerType));
    }
    public static Session getWsSession(String userName) {
        return (Session)wsUser.get(userName).get("session");
    }
    public static String getWsPeer(String userName) {
        return (String)wsUser.get(userName).get("peer");
    }
    public static String getWsPeerType(String userName) {
        return (String)wsUser.get(userName).get("peerType");
    }
    public static void sendMessage(String peer, String sender, String message) throws Exception {
        if(hasWsSession(peer)) {
            getWsSession(peer).getBasicRemote().sendText(message);
        }
    }
}

