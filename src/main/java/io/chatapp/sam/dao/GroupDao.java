package io.chatapp.sam.dao;

import io.chatapp.sam.entity.Group;
import io.chatapp.sam.utils.EnvReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public class GroupDao {
    private static final Logger logger = LoggerFactory.getLogger(GroupDao.class);
    private static final Map<String, Object> properties = EnvReader.getMysqlMetadata();
    private static final String url = (String)properties.get("url");
    private static final String dbUserName = (String)properties.get("userName");
    private static final String dbPassword = (String)properties.get("password");
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public void insert(Group group) throws Exception {
        String query = String.format("INSERT INTO `groups` VALUES('%s', '%s', '%d')", group.getGroupName(),
                group.getUserName(), group.getStatus());
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
    public void deleteOne(String groupName, String userName) throws Exception {
        String query = String.format("DELETE FROM `groups` WHERE groupName='%s' AND userName='%s'", groupName,
                userName);
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
    public void deleteUser(String userName) throws Exception {
        String query = String.format("DELETE FROM `groups` WHERE userName='%s'", userName);
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
    public void updateStatus(String groupName, String userName, Integer status) throws Exception {
        String query = String.format("UPDATE `groups` SET status='%d' WHERE groupName='%s' AND userName='%s'", status,
                groupName, userName);
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
    public Integer findStatus(String groupName, String userName) throws Exception {
        String query = String.format("SELECT status FROM `groups` WHERE groupName='%s' AND userName='%s'", groupName,
                userName);
        logger.info(query);
        Connection conn = null;
        Statement stmt = null;
        int status = 0;
        try {
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
        logger.info("{}", status);
        return status;
    }
    public List<String> findUserByGroup(String groupName, Integer status) throws Exception {
        String query = String.format("SELECT userName FROM `groups` WHERE groupName='%s' AND status='%d'", groupName,
                status);
        logger.info(query);
        Connection conn = null;
        Statement stmt = null;
        List<String> list = new LinkedList<>();
        try {
            conn = DriverManager.getConnection(url, dbUserName, dbPassword);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                list.add(rs.getString("userName"));
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
    public List<String> findGroupByUser(String userName, Integer status) throws Exception {
        String query = String.format("SELECT groupName FROM `groups` WHERE userName='%s' AND status='%d'", userName, status);
        logger.info(query);
        Connection conn = null;
        Statement stmt = null;
        List<String> list = new LinkedList<>();
        try {
            conn = DriverManager.getConnection(url, dbUserName, dbPassword);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                list.add(rs.getString("groupName"));
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
}
