package io.chatapp.sam.utils;

import io.chatapp.sam.dao.FriendsDao;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Map;

public class InitSqlDb {
    private static final Logger logger = LoggerFactory.getLogger(InitSqlDb.class);
    private static final Map<String, Object> properties = EnvReader.getMysqlMetadata();
    private static final String url = (String)properties.get("script-url");
    private static final String dbUserName = (String)properties.get("userName");
    private static final String dbPassword = (String)properties.get("password");
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static void main(String[] args) {
        try {
            logger.info("user is {}, url is {}, password is{}", dbUserName, url, dbPassword);
            initChatApp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void initChatApp() throws Exception {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(url, dbUserName, dbPassword);
            ScriptRunner sr = new ScriptRunner(conn);
            Reader reader = new BufferedReader(new FileReader("src/main/resources/schema.sql"));
            sr.runScript(reader);
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if(conn != null) conn.close();
                if(stmt != null) stmt.close();
            } catch(Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }
}
