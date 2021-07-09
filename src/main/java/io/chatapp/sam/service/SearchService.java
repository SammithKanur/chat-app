package io.chatapp.sam.service;

import io.chatapp.sam.dao.ElasticSearchDao;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
    private static ElasticSearchDao dao = new ElasticSearchDao();
    public void insertUser(String userName) throws Exception {
        dao.insert(userName);
    }
    public void deleteUser(String userName) throws Exception {
        dao.delete(userName);
    }
    public String getUsers(String prefix) throws Exception {
        return dao.getNames(prefix);
    }
}
