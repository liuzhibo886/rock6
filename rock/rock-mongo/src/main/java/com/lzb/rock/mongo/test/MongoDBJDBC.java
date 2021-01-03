package com.lzb.rock.mongo.test;


import java.util.ArrayList;  
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;  
import com.mongodb.MongoCredential;  
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;  
  
public class MongoDBJDBC {  
	@Autowired
	private MongoTemplate mongoTemplate;
	
    public static void main(String[] args){
        // 连接到 mongodb 服务
        MongoClient mongoClient = new MongoClient( "192.168.8.173" , 27017 );
        // 连接到数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase("mycol");  
        
        mongoDatabase.createCollection("test");
        System.out.println("集合创建成功");
        
        
        MongoCollection<Document> collection = mongoDatabase.getCollection("test");
        
        System.out.println("集合 test 选择成功");
        
        Document document = new Document("title", "MongoDB").  
        append("description", "database").  
        append("likes", 100).  
        append("by", "Fly");  
        
        List<Document> documents = new ArrayList<Document>();  
        documents.add(document);  
        collection.insertMany(documents);  
        
        
        
        
        
        mongoClient.close();
    }  
} 
