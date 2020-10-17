package cn.edu.hbut.cs.hfcrt.databridge.dao;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import cn.edu.hbut.hfcrt.common.MongoDBHelper;


public class DataBridgeDAO {

	MongoDBHelper mongoHelper = new MongoDBHelper();
	MongoClient mongoClient = mongoHelper.getMongoClient();
	MongoDatabase mongoDataBase = mongoHelper.getMongoDatabase(mongoClient);

	public boolean insert(String table, Document document) {
		MongoCollection<Document> collection = mongoDataBase.getCollection(table);
		collection.insertOne(document);
		long count = collection.count(document);
		if (count == 1) {
			// System.out.println("document insert sucessful!");
			return true;
		} else {
			// System.out.println("document insert fail!");
			return false;
		}

	}

	public MongoCollection<Document> getCollection(String colName) {
		return mongoDataBase.getCollection(colName);
	}
	
}
