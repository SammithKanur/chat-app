package io.chatapp.sam.dao;

import io.chatapp.sam.utils.EnvReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class ElasticSearchDao {
    private static final Map<String, Object> properties = EnvReader.getElasticSearchMetadata();
    private static final String host = (String)properties.get("url");
    private static final String index = (String)properties.get("index");
    private HttpURLConnection getConn(String httpUrl, String method, String jsonRequest) throws Exception {
        URL url = new URL(httpUrl);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(jsonRequest.getBytes());
        return conn;
    }
    public void insert(String userName) throws Exception {
        String httpUrl = host + index + "/_doc";
        String jsonRequest = String.format("{\"name\":\"%s\"}",userName);
        HttpURLConnection conn = getConn(httpUrl, "POST", jsonRequest);
        if(conn.getResponseCode() > 299) {
            throw new Exception("error with elasticsearch insert operation");
        }
    }
    public void delete(String userName) throws Exception {
        String httpUrl = host + index + "/_delete_by_query";
        String jsonRequest = String.format("{\"query\":{\"match\":{\"name\":\"%s\"}}}",userName);
        HttpURLConnection conn = getConn(httpUrl, "POST", jsonRequest);
        if(conn.getResponseCode() > 299) {
            throw new Exception("error with elasticsearch delete operation");
        }
    }
    public String getNames(String prefix) throws Exception {
        String httpUrl = host + index + "/_search";
        String jsonRequest = String.format("{\"query\":{\"prefix\":{\"name\":\"%s\"}}}",prefix);
        HttpURLConnection conn = getConn(httpUrl, "POST", jsonRequest);
        String result = "";
        if(conn.getResponseCode() > 299) {
            throw new Exception("error with elasticsearch getNames operation");
        } else {
            StringBuilder response = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String resp = "";
            while((resp = br.readLine()) != null) {
                response.append(resp);
            }
            result = response.toString();
        }
        return result;
    }
}
