package cn.edu.hbut.hfcrt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import cn.edu.hbut.cs.hfcrt.databridge.dao.DataBridgeDAO;

/**
 * @author zitian
 *
 */
public class NeService {
	
	
	static MongoCollection<Document> neMapCol = new DataBridgeDAO().getCollection("neMap");
	static MongoCollection<Document> boardMapCol = new DataBridgeDAO().getCollection("boardMap");
	static MongoCollection<Document> nodePortListCol = new DataBridgeDAO().getCollection("nodePortList");
	
	
	
	//根据网元名查找该网元上的所有板卡
	public static List<Integer> findAllBoardByNeName(String neName){
		List<Integer> list =new ArrayList<Integer>();
		int neId = NeService.findNeId(neName);
		Document queryBoard = new Document().append("neId", neId);
		FindIterable<Document> findBoard = boardMapCol.find(queryBoard);
		MongoCursor<Document> CursorBoard = findBoard.iterator();
		while(CursorBoard.hasNext()){
			Document board = CursorBoard.next();
			int boardId = (int) board.get("boardId");
			list.add(boardId);
		}		
		return list;		
	}
	
	
	//根据网元ID查找不同于目标板卡的所有板卡
	public static List<Integer> findFilterFreeBoard(int neId,int boardId){
		List<Integer> list =new ArrayList<Integer>();
		Document queryBoard = new Document().append("neId", neId);
		FindIterable<Document> findBoard = boardMapCol.find(queryBoard);
		MongoCursor<Document> CursorBoard = findBoard.iterator();
		while(CursorBoard.hasNext()){
			Document board = CursorBoard.next();
			int boardId1 = (int) board.get("boardId");
			if(boardId1!=boardId){
				list.add(boardId1);
			}			 
		}		
		return list;
		
	}
	
	//根据板卡ID查找该板卡上的所有端口
	public static Map<Double,List<Double>> findAllBoardByBoardId(Double boardId){
		Map<Double,List<Double>> map =new HashMap<Double,List<Double>>();
		List<Double> list =new ArrayList<Double>();
		Document queryPort = new Document().append("boardId", Double.valueOf(boardId));
		FindIterable<Document> findPort = nodePortListCol.find(queryPort);
		MongoCursor<Document> CursorPort = findPort.iterator();
		while(CursorPort.hasNext()){
			Document port = CursorPort.next();
			Double portNo = (Double) port.get("portNo");
			list.add(portNo);
			map.put(boardId, list);		
		}
		return map;				
	}
	
	//根据板卡ID查找该板卡上的空闲端口
	public static void findFreePortByBoardId(Double BoardId){
		
	}
	
	//根据网元的neName，在neMap查找网元的neId
	public static int  findNeId(String neName) {
		Document query=new Document().append("neName", String.valueOf(neName));
		Document board=neMapCol.find(query).first();//查找neName符合的对象
		Integer neId = (Integer) board.get("neId");
		return neId;		
	}
	
	
	//根据网元的neId,在neMap查找网元的neId
	public static String  findNeName(int neId) {
		Document query=new Document().append("neId", neId);
		Document board=neMapCol.find(query).first();//查找neName符合的对象
		String neName = (String) board.get("neId");
		return neName;
		
	}
	
	//根据网元ID，找到该网元上板卡上的板卡ID和端口特定
	public static void findBoardAndportSpeed(int neId){
		Document query=new Document().append("neId", neId);
		Document board=boardMapCol.find().first();
		int boardId = (int) board.get("boardId");
		Document query1=new Document().append("boardId", boardId);
			
	}
	
	
	//根据两个有光纤连接的网元A、B，获的光纤上A的板卡和端口
	public List<Object> neInformation(String A,String B) {
		
		TopoService topo = new TopoService();
		BoardService board = new BoardService();
		Map<List<String>, Document> topoMap = topo.checkAdditionalTopos(A, B);
//		System.out.println("topoMap="+topoMap);
		List<String> list = new ArrayList<String>();
		list.add(A);
		list.add(B);
		Document topoDoc = topoMap.get(list);//根据网元组成的list获得光纤信息
//		System.out.println("topoDoc="+topoDoc);
		
		double boardId1 = Double.valueOf(topoDoc.get("boardId1").toString());
		String portName1 = topoDoc.get("portName1").toString();
		double boardId2 = Double.valueOf(topoDoc.get("boardId2").toString());
		String portName2 = topoDoc.get("portName2").toString();
		String linkSpeedName =  topoDoc.get("linkSpeedName").toString();
//		System.out.println("boardId1:"+(int)boardId1);
//		System.out.println("boardId2:"+(int)boardId2);
		
		boolean bl = board.isExist(A, (int)boardId1);//判断boardId1是否在A_neName上
//		System.out.println("bl="+bl);
		
		double boardId;
		String portName;
		if (bl) {
			boardId = boardId1;
			portName= portName1;
			}else {
			boardId= boardId2;
			portName= portName2;
			}
//		System.out.println("boardId:"+(int)boardId);
//		System.out.println("portName:"+portName);
//		System.out.println("linkSpeedName:"+linkSpeedName);
		
		List<Object> neList = new ArrayList<Object>();
		neList.add(A);
		neList.add(boardId);
		neList.add(portName);
		neList.add(linkSpeedName);
//		System.out.println("neList="+neList);
		return neList;
	}
	
 
	
	
	
}
