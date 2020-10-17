package cn.edu.hbut.hfcrt.utils;

import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import cn.edu.hbut.hfcrt.utils.MongoDBHelper;



public class Tjvhuan {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MongoDBHelper mongoHelper = new MongoDBHelper();
		MongoClient mongoClient = mongoHelper.getMongoClient();
		MongoDatabase mongoDatabase = mongoHelper.getMongoDatabase(mongoClient);
//		MongoDatabase mongoDataBase = mongoHelper.getMongoDataBase(mongoClient);
		MongoCollection<Document> tunnelMapCol = mongoDatabase.getCollection("nodeTunnelMap");
		MongoCollection<Document> neMapCol = mongoDatabase.getCollection("neMap");

		FindIterable<Document> manyTunnel = tunnelMapCol.find();// 获取tunnel
		MongoCursor<Document> iter = manyTunnel.iterator();
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		// 黄石大冶(三);黄石大冶640-6;黄石大冶640;黄石市大冶一中桥头650;黄石大冶新冶矿业;黄石大冶炕头;黄石大冶工商所640;黄石大冶六中本部;黄石大冶金都640
//		list.add("黄石大冶金都640");
//		list.add("黄石大冶六中本部");
//		list.add("黄石大冶(三)");
//		list.add("黄石大冶640");
		list.add("黄石大冶新冶矿业");
		list.add("黄石大冶炕头");
//		list.add("黄石大冶工商所640");
//		list.add("黄石大冶六中本部");
//		list.add("黄石大冶金都640");

		list2.add("黄石大冶640");
		list2.add("黄石大冶新冶矿业");
		list2.add("黄石大冶炕头");
		list2.add("黄石大冶640-6");
		list2.add("黄石大冶工商所640");
		list2.add("黄石市大冶一中桥头650");
		if (list2.containsAll(list)) {
			System.out.println("yes");
		}
		System.out.println(list);
		int n = 0;
		//遍历所有得tunnel
		while (iter.hasNext()) {
			Document tunnel = iter.next();
			ArrayList work = (ArrayList) tunnel.get("workPath");//得到workPath
			ArrayList protect = (ArrayList) tunnel.get("protectPath");//得到protectPath
			int circuitId = tunnel.getInteger("circuitId");
			n++;
			Document workPath = new Document();
			Document protectPath = new Document();
			workPath = (Document) work.get(0);
			if (protect.size() != 0) {
				protectPath = (Document) protect.get(0);
			} else {
				continue;
			}

			ArrayList<Document> wPathsList = (ArrayList) workPath.get("pathsList");
			ArrayList<Document> pPathsList = (ArrayList) protectPath.get("pathsList");

			ArrayList<String> newworkpathlist = new ArrayList<String>();
			ArrayList<String> newprotectlist = new ArrayList<String>();
			for (int i = 0; i < wPathsList.size(); i++) {
				if (i % 2 == 0) {
					int neIdi = wPathsList.get(i).getInteger("neId");
					Document queryNe1 = new Document().append("neId", neIdi);
					Document ne1 = neMapCol.find(queryNe1).first();
					String neName1 = ne1.getString("neName");
					newworkpathlist.add(neName1);
				}

			}
			for (int i = 0; i < pPathsList.size(); i++) {
				if (i % 2 == 0) {
					int neIdi = pPathsList.get(i).getInteger("neId");
					Document queryNe1 = new Document().append("neId", neIdi);
					Document ne1 = neMapCol.find(queryNe1).first();
					String neName1 = ne1.getString("neName");
					newprotectlist.add(neName1);
				}
			}
//			System.out.println("work: " + newworkpathlist);
//			System.out.println("protect: " + newprotectlist);
			if (newworkpathlist.containsAll(list)) {
				System.out.println("work: " + circuitId + "  " + newworkpathlist);
			}
			if (newprotectlist.containsAll(list)) {
				System.out.println("protect: " + circuitId + "  " + newprotectlist);
			}
		}
	}

}
