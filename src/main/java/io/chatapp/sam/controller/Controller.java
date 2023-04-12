package io.chatapp.sam.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public interface Controller {
    Logger logger = LoggerFactory.getLogger(Controller.class);
    Executor executor = Executors.newFixedThreadPool(100);
    Map<String, String> httpSession = new ConcurrentHashMap<>();
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
    default boolean isSessionValid(String userName, String session) {
        logger.info("validating session for user {} with session {}", userName, session);
        return userName != null && session != null && httpSession.containsKey(userName) && httpSession.get(userName).equals(session);
    }

}
