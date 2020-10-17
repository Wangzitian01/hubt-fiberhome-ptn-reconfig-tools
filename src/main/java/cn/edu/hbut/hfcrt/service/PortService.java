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


public class PortService {
	
	static MongoCollection<Document> boardMapCol = new DataBridgeDAO().getCollection("boardMap");
	static MongoCollection<Document> nodePortListCol = new DataBridgeDAO().getCollection("nodePortList");
	static MongoCollection<Document> topologylinesCol = new DataBridgeDAO().getCollection("nodeTopology_lines");
	
	
	
	//通过板卡ID,查找空闲新的端口
	public static List<String> findNewPortNameAndAddTopo(int AboardId,String AboardPortName,int FboardId,String FboardPortName){
		List<String> list=new ArrayList<String>();
		Map<Double,List<String>> Amap =PortService.checkAdditionalPorts1(AboardId);
		Map<Double,List<String>> Fmap =PortService.checkAdditionalPorts1(FboardId);
		
		List<Double> AKlist=new ArrayList<Double>();
		List<Double> FKlist=new ArrayList<Double>();
		
		AKlist.addAll(Amap.keySet());
		FKlist.addAll(Fmap.keySet());
		
		List<String> Alist = Amap.get(AKlist.get(0));
		List<String> Flist = Fmap.get(FKlist.get(0)); 
		
		System.out.println("AboardId"+AboardId);
		System.out.println("FboardId"+FboardId);
		if(Alist.size()==0 || Flist.size()==0){
			TopoLines.deleteLines(AboardId,FboardId);
			System.out.println("有可能是该板卡没有空闲端口");				
			String AnewportName =  null;
			String FnewportName =  null;

			list.add(0, AnewportName);
			list.add(1, FnewportName);
			return list;
		}
		int AnewportNameIndex = (int)(Math.random()*(Alist.size()));
		int FnewportNameIndex = (int)(Math.random()*(Flist.size()));
		
		String AnewportName = Amap.get(AKlist.get(0)).get(AnewportNameIndex);
		String FnewportName = Fmap.get(FKlist.get(0)).get(FnewportNameIndex);

		list.add(0, AnewportName);
		list.add(1, FnewportName);
		return list;
		
	}
	
	public static void JudgePortIsExist(int boardId){
		Map<Double,List<String>> map =PortService.checkAdditionalPorts1(boardId);
		System.out.println("map信息"+map);
		List<Double> Klist=new ArrayList<Double>();
		Klist.addAll(map.keySet());
		System.out.println("板卡ID为"+Klist.get(0));
		List<String> slist = map.get(Klist.get(0));
		int valuelen = slist.size();
		System.out.println("value的大小为"+slist.size());
		if(valuelen==0){
			
		}
		
	}
	
	
	
	
	

	//查找特定速率下,boardId下的空闲端口 
	public static List<Document> checkAdditionalPorts(int boardId,String aimPortSpeed) {
		List<Document> availablePortList = new ArrayList<Document>();//储存空闲端口信息
		Document query = new Document().append("boardId", Double.valueOf(boardId));
		FindIterable<Document> find = nodePortListCol.find(query);
		MongoCursor<Document> mongoCursor = find.iterator();	
		while (mongoCursor.hasNext()) {
			Document doc = mongoCursor.next();//nodePortList中的boardId上的每一个端口
			Double portNo = (Double) doc.get("portNo");
			String portSpeed = doc.get("portSpeed").toString();
			String portName = doc.get("portName").toString();			
			Document query1 = new Document().append("boardId1", Double.valueOf(boardId)).append("portName1",portName);
			Document find1 = topologylinesCol.find(query1).first();//光纤中查找此板卡和端口
			Document query2 = new Document().append("boardId2", Double.valueOf(boardId)).append("portName2",portName);
			Document find2 = topologylinesCol.find(query2).first();//光纤中查找此板卡和端口			
			if (find1==null && find2==null) {//该端口未连接光纤	
				if(portSpeed==aimPortSpeed){
					availablePortList.add(doc);
				}
			}else//该端口连接光纤
				continue;
		}
		 
		return availablePortList;
	}
	
	//boardId下的空闲端口
	public static Map<Double,List<String>> checkAdditionalPorts1(int boardId) {
		Map<Double,List<String>> map=new HashMap<Double,List<String>>();
		List<String> list=new ArrayList<String>();
		List<Document> availablePortList = new ArrayList<Document>();//储存空闲端口信息
		Document query = new Document().append("boardId", Double.valueOf(boardId));
		FindIterable<Document> find = nodePortListCol.find(query);
		MongoCursor<Document> mongoCursor = find.iterator();	
		while (mongoCursor.hasNext()) {
			Document doc = mongoCursor.next();//nodePortList中的boardId上的每一个端口
			Double portNo = (Double) doc.get("portNo");
			String portSpeed = doc.get("portSpeed").toString();
			String portName = doc.get("portName").toString();			
			Document query1 = new Document().append("boardId1", Double.valueOf(boardId)).append("portName1",portName);
			Document find1 = topologylinesCol.find(query1).first();//光纤中查找此板卡和端口
			Document query2 = new Document().append("boardId2", Double.valueOf(boardId)).append("portName2",portName);
			Document find2 = topologylinesCol.find(query2).first();//光纤中查找此板卡和端口			
			if (find1==null && find2==null) {//该端口未连接光纤	
				list.add(portName);	
				 
			}else//该端口连接光纤
				continue;
		}
		map.put((double) boardId, list);
		return map;
	}
}
