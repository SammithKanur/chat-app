package io.chatapp.sam.service;

import io.chatapp.sam.dao.GroupDao;
import io.chatapp.sam.entity.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);
    private static GroupDao groupDao = new GroupDao();

    public void insertGroup(Group group) throws Exception {
        groupDao.insert(group);
    }
    public void deleteMember(String groupName, String userName) throws Exception {
        groupDao.deleteOne(groupName, userName);
    }
    public void deleteUser(String userName) throws Exception {
        groupDao.deleteUser(userName);
    }
    public void setStatus(String groupName, String userName, Integer status) throws Exception {
        groupDao.updateStatus(groupName, userName, status);
    }
    public String getStatus(String groupName, String userName) throws Exception {
        return trackStatus(groupDao.findStatus(groupName, userName));
    }
    public List<String> getGroupRequests(String userName) throws Exception {
        return groupDao.findGroupByUser(userName, 1);
    }
    public List<String> getGroups(String userName) throws Exception {
        return groupDao.findGroupByUser(userName, 2);
    }
    public List<String> getMembers(String groupName) throws Exception {
        return groupDao.findUserByGroup(groupName, 2);
    }
    public List<String> getRequests(String groupName) throws Exception {
        return groupDao.findUserByGroup(groupName, 1);
    }
    public String trackStatus(Integer status) {
        if(status == 1)
            return "Pending";
        else if(status == 2)
            return "Member";
        return "not member";
    }
    public boolean isGroupPresent(String groupName) throws Exception {
        return getMembers(groupName).size() > 0;
    }
    public boolean isMember(String groupName, String userName) throws Exception {
        return !getStatus(groupName, userName).equals("not member");
    }
}
