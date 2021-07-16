package io.chatapp.sam.controller;

import io.chatapp.sam.utils.Encoders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.yaml.snakeyaml.Yaml;

import javax.websocket.Session;

import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public interface Controller {
    Logger logger = LoggerFactory.getLogger(Controller.class);
    Executor executor = Executors.newFixedThreadPool(100);
    Map<String, Session> webSocketSession = new ConcurrentHashMap();
    Map<String, String> httpSession = new ConcurrentHashMap<>();
    Map<String, String> webSocketSessionUser = new ConcurrentHashMap<>();
    HttpHeaders textHttpHeader = new HttpHeaders(new LinkedMultiValueMap<>(Map.of(
            "Accept", new LinkedList<>(Arrays.asList("text/plain")),
            "Content-type", new LinkedList<>(Arrays.asList("text/plain")))
    ));
    HttpHeaders jsonHttpHeader = new HttpHeaders(new LinkedMultiValueMap<>(Map.of(
            "Accept", new LinkedList<>(Arrays.asList("application/json")),
            "Content-type", new LinkedList<>(Arrays.asList("application/json"))
    )
    ));
    default String getSession() {
        Integer session = new Random().nextInt(100000000);
        return session.toString();
    }
    default void addHttpSession(String userName, String session) {
        logger.info("adding http session for user {}", userName);
        httpSession.put(userName, session);
    }
    default void removeHttpSession(String userName) {
        logger.info("removing http session for user {}", userName);
        httpSession.remove(userName);
    }
    default void addWebSocketSession(String userName, Session session) {
        logger.info("adding websocket connection for user {}", userName);
        webSocketSessionUser.put(session.getId(), userName);
        webSocketSession.put(userName, session);
    }
    default void removeWebSocketSession(String sessionId) {
        if(webSocketSessionUser.containsKey(sessionId)) {
            logger.info("removing websocket connection for user {}", webSocketSessionUser.get(sessionId));
            webSocketSession.remove(webSocketSessionUser.get(sessionId));
            webSocketSessionUser.remove(sessionId);
        }
    }
    default Session getWsSession(String userName) {
        return webSocketSession.get(userName);
    }
    default boolean hasWsSession(String userName) {
        return webSocketSession.containsKey(userName);
    }
    default boolean isSessionValid(String userName, String session) {
        logger.info("validating session for user {} with session {}", userName, session);
        return userName != null && session != null && httpSession.containsKey(userName) && httpSession.get(userName).equals(session);
    }
    default void sendMessage(List<String> members, String connectionType, String connection, String message, String sender) throws Exception {
        for(String member : members) {
            if(webSocketSession.containsKey(member)) {
                if(member.equals(sender))
                    continue;
                Session session = webSocketSession.get(member);
                session.getBasicRemote().sendText(Encoders.getObjectEncoded(Map.of("type", "chat-message",
                        "connectionType", connectionType, "connection", connection,
                        "message", message, "sender", sender)));
            }
        }
    }
}
