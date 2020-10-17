package cn.edu.hbut.hfcrt.service;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import cn.edu.hbut.cs.hfcrt.databridge.dao.DataBridgeDAO;
import cn.edu.hbut.hfcrt.common.MongoDBHelper;
import cn.edu.hbut.hfcrt.constant.Constant;

public class ToponodeService {
	
	MongoDBHelper mongoDBHelper = new  MongoDBHelper();
	static MongoClient mongoClient = new MongoClient();
	static MongoDatabase mongoDatabase=MongoDBHelper.getMongoDatabase(mongoClient);
	static MongoCollection<Document> boardMapCol = new DataBridgeDAO().getCollection("boardMap");
	static MongoCollection<Document> tunnelMapCol =  mongoDatabase.getCollection(Constant.tunnelMapCol);
	static MongoCollection<Document> neMapCol =  mongoDatabase.getCollection("neMap");
	static MongoCollection<Document> topologynodesCol =  mongoDatabase.getCollection(Constant.nodeTopologynodesCol);
	static MongoCollection<Document> topologylinesCol =  mongoDatabase.getCollection(Constant.nodeTopologyLinesCol);
	
	//加一条光纤，需要加两条nodetopology_node
	public static void AddNodeTopo(int board1,int board2){
		
		Document query = new Document().append("boardId", board1);
		Document doc=boardMapCol.find(query).first();
		Double neId = Double.valueOf(doc.get("neId").toString());

		
		Document query1=new Document().append("neId", neId);
		Document doc1=topologynodesCol.find(query1).first();
		Double topoNodeId=Double.valueOf(doc1.get("topoNodeId").toString());				
		Document node = new Document().append("neId", neId).append("topoNodeId", topoNodeId);	
				
		Document query2 = new Document().append("boardId", board2);
		Document doc2=boardMapCol.find(query2).first();
		Double neId1 = Double.valueOf(doc2.get("neId").toString());

		Document query3=new Document().append("neId", neId1);
		Document doc3=topologynodesCol.find(query3).first();
		Double topoNodeId1=Double.valueOf(doc3.get("topoNodeId").toString());				
		Document node1 = new Document().append("neId", neId1).append("topoNodeId", topoNodeId1);
		
		topologynodesCol.insertOne(node);
		topologynodesCol.insertOne(node1);	
		
	}
	
	
	public static double findtopoNodeIdbyNeId(double neId){
		Document query=new Document().append("neId", neId);
		Document topo=topologynodesCol.find(query).first();//查找neName符合的对象
		double topoNodeId = (double) topo.get("topoNodeId");	
		return topoNodeId;
		
	}
	

}
