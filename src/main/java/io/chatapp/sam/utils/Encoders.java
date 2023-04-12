package io.chatapp.sam.utils;

import com.google.gson.Gson;

import java.util.Map;

public class Encoders {
    private static final Gson gson = new Gson();
    public static String getObjectEncoded(Object result) {
        return gson.toJson(result);
    }
}
