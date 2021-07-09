package io.chatapp.sam.dao;

import io.chatapp.sam.entity.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

@Repository
public class GroupDao {
    private static final Logger logger = LoggerFactory.getLogger(GroupDao.class);
    private static final String url = "jdbc:h2:~/tmp/h2dbs/chatApp";
    private static final String dbUserName = "sam";
    private static final String dbPassword = "sam";
    private static final String JDBC_DRIVER = "org.h2.Driver";
    public void insert(Group group) throws Exception {
        String query = String.format("INSERT INTO GROUPS VALUES('%s', '%s', '%d')", group.getGroupName(),
                group.getUserName(), group.getStatus());
        logger.info(query);
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
    public void deleteOne(String groupName, String userName) throws Exception {
        String query = String.format("DELETE FROM GROUPS WHERE groupName='%s' AND userName='%s'", groupName,
                userName);
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
    public void deleteUser(String userName) throws Exception {
        String query = String.format("DELETE FROM GROUPS WHERE userName='%s'", userName);
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
    public void updateStatus(String groupName, String userName, Integer status) throws Exception {
        String query = String.format("UPDATE GROUPS SET status='%d' WHERE groupName='%s' AND userName='%s'", status,
                groupName, userName);
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
    public Integer findStatus(String groupName, String userName) throws Exception {
        String query = String.format("SELECT status FROM GROUPS WHERE groupName='%s' AND userName='%s'", groupName,
                userName);
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
        logger.info("{}", status);
        return status;
    }
    public List<String> findUserByGroup(String groupName, Integer status) throws Exception {
        String query = String.format("SELECT userName FROM GROUPS WHERE groupName='%s' AND status='%d'", groupName,
                status);
        logger.info(query);
        Connection conn = null;
        Statement stmt = null;
        List<String> list = new LinkedList<>();
        try {
            Class.forName(JDBC_DRIVER);
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
        String query = String.format("SELECT groupName FROM GROUpS WHERE userName='%s' AND status='%d'", userName, status);
        logger.info(query);
        Connection conn = null;
        Statement stmt = null;
        List<String> list = new LinkedList<>();
        try {
            Class.forName(JDBC_DRIVER);
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
