package io.chatapp.sam.dao;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.diagnostics.logging.Logger;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ChatDao {
    private static String connectionUrl = "localhost:8085";
    private static String dbName = "chatApp";

    private MongoCollection<Document> getCollection(String collectionName) {
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.writeConcern(WriteConcern.JOURNAL_SAFE);
        builder.readConcern(ReadConcern.DEFAULT);
        MongoClient client = new MongoClient(new ServerAddress(connectionUrl), builder.build());
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(collectionName);
        return collection;
    }
    public void insert(String collectionName, Document document) {
        MongoCollection<Document> collection = getCollection(collectionName);
        collection.insertOne(document);
    }
    public void update(String collectionName, Document updateQuery, Document obj) {
        MongoCollection<Document> collection = getCollection(collectionName);
        collection.updateOne(updateQuery, obj);
    }
    public Document find(String collectionName, List<Document> aggregateQuery) {
        MongoCollection<Document> collection = getCollection(collectionName);
        return collection.aggregate(aggregateQuery).into(new LinkedList<>()).getFirst();
    }
    public void delete(String collectionName, Document document) {
        MongoCollection<Document> collection = getCollection(collectionName);
        collection.deleteOne(document);
    }
}
