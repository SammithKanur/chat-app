package io.chatapp.sam.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.util.Map;
import java.util.TreeMap;

public class EnvReader {
    private static final String envPath = "/home/sammith/projects/spring/chat-app/src/main/resources/ChatApp.yml";
    public static Map<String, Object> readYamlFile() {
        FileInputStream fls = null;
        Yaml yaml = new Yaml();
        Map<String, Object> conf = new TreeMap<>();
        try {
            fls = new FileInputStream(envPath);
            conf = yaml.load(fls);
            conf = (Map<String, Object>)conf.get(System.getenv("chatAppEnv"));
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fls.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return conf;
    }
    public static Map<String, Object> getMysqlMetadata() {
        return (Map<String, Object>)readYamlFile().get("mysql");
    }
    public static Map<String, Object> getMongoMetadata() {
        return (Map<String, Object>)readYamlFile().get("mongo");
    }
    public static Map<String, Object> getElasticSearchMetadata() {
        return (Map<String, Object>)readYamlFile().get("elasticsearch");
    }
    public static Map<String, Object> getServerMetadata() {
        return (Map<String, Object>)readYamlFile().get("server");
    }
}
