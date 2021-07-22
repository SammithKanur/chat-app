package io.chatapp.sam.dao;

import io.chatapp.sam.utils.EnvReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import io.chatapp.sam.entity.Friends;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public class FriendsDao {
    private static final Logger logger = LoggerFactory.getLogger(FriendsDao.class);
    private static final Map<String, Object> properties = EnvReader.getMysqlMetadata();
    private static final String url = (String)properties.get("url");
    private static final String dbUserName = (String)properties.get("userName");
    private static final String dbPassword = (String)properties.get("password");
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public void insert(Friends friends) throws Exception {
        String query = String.format("INSERT INTO friends VALUES('%s','%s', '%d', '%d')",friends.getUser(), friends.getConnection(),
                friends.getStatus(), friends.getCalling());
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(url, dbUserName, dbPassword);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
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
    public void deleteOne(String user, String connection) throws Exception {
        String query = String.format("DELETE FROM friends WHERE user='%s' AND connection='%s'", user, connection);
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(url, dbUserName, dbPassword);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
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
    public void deleteUser(String user) throws Exception {
        String query = String.format("DELETE FROM friends WHERE user='%s' OR connection='%s'", user, user);
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(url, dbUserName, dbPassword);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
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
    public void updateStatus(String user, String connection, Integer status) throws Exception {
        String query = String.format("UPDATE friends SET status='%d' WHERE user='%s' AND connection='%s'", status,
                user, connection);
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(url, dbUserName, dbPassword);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
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
    public Integer findStatus(String userName, String connection) throws Exception {
        String query = String.format("SELECT status FROM friends WHERE user='%s' AND connection='%s'", userName,
                connection);
        logger.info(query);
        Connection conn = null;
        Statement stmt = null;
        int status = 0;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(url, dbUserName, dbPassword);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                status = rs.getInt("status");
            }
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
        return status;
    }
    public List<Friends> findByStatus(String userName, int status) throws Exception {
        String query = String.format("SELECT connection, calling FROM friends WHERE user='%s' AND status='%d'", userName,
                status);
        logger.info(query);
        Connection conn = null;
        Statement stmt = null;
        List<Friends> list = new LinkedList<>();
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(url, dbUserName, dbPassword);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                Friends friends = new Friends();
                friends.setCalling(rs.getInt("calling"));;
                friends.setConnection(rs.getString("connection"));
                list.add(friends);
            }
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
        return list;
    }
    public void setCalling(String user, String connection, Integer calling) throws Exception {
        String query = String.format("UPDATE friends SET calling='%d' WHERE user='%s' AND connection='%s'", calling,
                user, connection);
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(url, dbUserName, dbPassword);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
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
