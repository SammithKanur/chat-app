package io.chatapp.sam.service;

import io.chatapp.sam.dao.UserDao;
import io.chatapp.sam.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserDao dao = new UserDao();
    public void insertUser(User user) throws Exception {
        dao.insert(user);
    }
    public void updateUser(User user) throws Exception {
        dao.update(user);
    }
    public void deleteUser(String userName) throws Exception {
        dao.delete(userName);
    }
    public User getUser(String userName) throws Exception {
        return dao.find(userName);
    }
    public boolean isUserValid(String userName, String password) throws Exception {
        User user = getUser(userName);
        if(user.getUserName() != null && user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }
    public boolean isUserPresent(String userName) throws Exception {
        User user = getUser(userName);
        if(user.getUserName() != null)
            return true;
        return false;
    }
    public void updateGroupsByValue(String userName, Integer value) throws Exception{
        dao.updateGroupsByValue(userName, value);
    }
    public void updateFollowersByValue(String userName, Integer value) throws Exception {
        dao.updateFollowersByValue(userName, value);
    }
}
