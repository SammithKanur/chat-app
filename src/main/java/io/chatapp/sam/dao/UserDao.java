package io.chatapp.sam.dao;

import io.chatapp.sam.entity.User;
import io.chatapp.sam.utils.EnvReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

@Repository
public class UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);
    private static final Map<String, Object> properties = EnvReader.getMysqlMetadata();
    private static final String url = (String)properties.get("url");
    private static final String dbUserName = (String)properties.get("userName");
    private static final String dbPassword = (String)properties.get("password");
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public void insert(User user) throws Exception {
        String query = String.format("INSERT INTO user VALUES('%s', '%s', '%d', '%d')", user.getUserName(),
                user.getPassword(), user.getGroups(), user.getFollowers());
        logger.info("executing query {}", query);
        Connection conn = null;
        Statement stmt = null;
        try {
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
    public void update(User user) throws Exception {
        String query = String.format("UPDATE user SET password=ISNULL('%s', password), " +
                "groups=ISNULL('%d',groups), followers=ISNULL('%d',followers) WHERE userName='%s'", user.getPassword(),
                user.getGroups(), user.getFollowers(), user.getUserName());
        logger.info(query);
        Connection conn = null;
        Statement stmt = null;
        try {
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
    public void delete(String userName) throws Exception {
        String query = String.format("DELETE FROM user WHERE userName='%s'", userName);
        Connection conn = null;
        Statement stmt = null;
        try {
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
    public User find(String userName) throws Exception {
        String query = String.format("SELECT * FROM user WHERE userName='%s'", userName);
        logger.info(query);
        Connection conn = null;
        Statement stmt = null;
        User user = new User();
        try {
            conn = DriverManager.getConnection(url, dbUserName, dbPassword);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                user.setUserName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                user.setGroups(rs.getInt("groups"));
                user.setFollowers(rs.getInt("followers"));
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
        return user;
    }
    public void updateGroupsByValue(String userName, Integer value) throws Exception {
        String query = String.format("UPDATE user SET `groups` = `groups` + %d WHERE userName = '%s'", value, userName);
        logger.info(query);
        Connection conn = null;
        Statement stmt = null;
        try {
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
    public void updateFollowersByValue(String userName, Integer value) throws Exception {
        String query = String.format("UPDATE user SET followers = followers + %d WHERE userName = '%s'", value, userName);
        logger.info(query);
        Connection conn = null;
        Statement stmt = null;
        try {
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
