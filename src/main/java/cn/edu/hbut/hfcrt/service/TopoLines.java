package cn.edu.hbut.hfcrt.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

import cn.edu.hbut.cs.hfcrt.databridge.dao.DataBridgeDAO;

public class TopoLines {
	
	
	static MongoCollection<Document> boardMapCol = new DataBridgeDAO().getCollection("boardMap");
	static MongoCollection<Document> nodePortListCol = new DataBridgeDAO().getCollection("nodePortList");
	static MongoCollection<Document> topologylinesCol = new DataBridgeDAO().getCollection("nodeTopology_lines");
	static MongoCollection<Document> topologynodesCol = new DataBridgeDAO().getCollection("nodeTopology_nodes");
	static MongoCollection<Document> neMapCol = new DataBridgeDAO().getCollection("neMap");
	static MongoCollection<Document> nodeTunnelMapCol= new DataBridgeDAO().getCollection("nodeTunnelMap");	
	
	
	//判断两个网元之间是否有光纤连接（测试成功） 
	public static Map<List<String>, Document> JudgeIsNotExistTopos(String NeName1,String NeName2){			
			Boolean flag=null;
			Map<List<String>, Document> availableTopoLIgyMap  = new HashMap<List<String>, Document>();//保存光纤数据和相应的网元信息
			List<String> neNameList = new ArrayList<String>();
			Document queryNe1 = new Document().append("neName", NeName1);
			Document ne1=neMapCol.find(queryNe1).first();//查找neName1符合的对象
			int neId1=(Integer) ne1.get("neId");
			
			Document queryNe2 = new Document().append("neName", NeName2);
			Document ne2=neMapCol.find(queryNe2).first();//查找neName1符合的对象
			int neId2=(Integer) ne2.get("neId");
			
			//在boardMap中通过neId找到所有的boardId
			Document queryBoard1 = new Document().append("neId", neId1);
			FindIterable<Document> findBoard1 = boardMapCol.find(queryBoard1);
			MongoCursor<Document> CursorBoard1 = findBoard1.iterator();
					
			/*所有的板卡board1进行逐一遍历*/
			while (CursorBoard1.hasNext()) {//所有的板卡board1进行逐一遍历
				Document board1=CursorBoard1.next();
				int boardId1 = (Integer) board1.get("boardId");//获得板卡1的BoardId1
				
				//在nodePortList中通过boartId1找到所有的portName1
				Document queryPort1 = new Document().append("boardId", Integer.valueOf(boardId1));
				FindIterable<Document> findPort1 = nodePortListCol.find(queryPort1);
				MongoCursor<Document> CursorPort1 = findPort1.iterator();
				while (CursorPort1.hasNext()) {//所有的端口Port1进行逐一遍历
					Document port1 = CursorPort1.next();
					if (port1.isEmpty()) {
						System.out.println();
						continue;
					}else {
						String portName1=  (String) port1.get("portName");//获取该网元上板卡的端口类型
						String portNo1=(String) port1.get("portNo");
						Document queryBoard2 = new Document().append("neId", neId2);
						FindIterable<Document> findBoart2 = boardMapCol.find(queryBoard2);
						MongoCursor<Document> CursorBoard2 = findBoart2.iterator();
							while (CursorBoard2.hasNext()) {
								Document board2 = CursorBoard2.next();
								int boardId2 = (Integer) board2.get("boardId");//获得板卡2的BoardId2
								//在nodePortList中通过boartId2找到所有的portName2
								Document queryPort2 = new Document().append("boardId", Integer.valueOf(boardId2));
								FindIterable<Document> findPort2 = nodePortListCol.find(queryPort2);
								MongoCursor<Document> CursorPort2 = findPort2.iterator();
								while (CursorPort2.hasNext()) {//所有的端口Port2进行逐一遍历
									Document port2 = CursorPort2.next();
									if (port2.isEmpty()) {
										continue;
									}else {
										String portName2 =(String) port2.get("portName");
										String portNo2=(String) port2.get("portNo");
											Document query1 = new Document().append("boardId1", boardId1)
																			.append("boardId2", boardId2)
																			.append("portName1", portName1)
																			.append("portName2", portName2);
											Document topology1 = (Document) topologylinesCol.find(query1).first();
											Document query2 = new Document().append("boardId1", boardId2)
																			.append("boardId2", boardId1)
																			.append("portName1", portName2)
																			.append("portName2", portName1);
											Document topology2 = (Document) topologylinesCol.find(query2).first();

											if (topology1 == null) {
												if (topology2 == null) {//1为空，2 为空
                                                    flag=false;
													continue;	
												}else {//1为空，2不为空
													System.out.println("两个网元之间有光纤连接");
													flag=true;

													neNameList.add(NeName1);
													neNameList.add(NeName2);
													availableTopoLIgyMap.put(neNameList, topology2);
												}
											}else {//1不为空
												flag=true;
												neNameList.add(NeName1);
												neNameList.add(NeName2);
												availableTopoLIgyMap.put(neNameList, topology1);
												if (topology2 == null) {
													flag=false;
													continue;
												} else {
													flag=true;
													System.out.println("两个网元之间有光纤连接");
													neNameList.add(NeName1);
													neNameList.add(NeName2);
													availableTopoLIgyMap.put(neNameList, topology2);
												}
											}																																																																
											

									}
								}
								
								
							}
					}
					
				}
			}
            System.out.println("两个网元之间是否有网元:"+flag);
			return availableTopoLIgyMap;
			}
	
	//删除光纤从数据库中
	public static void deleteLines(double topoNodeId1,double topoNodeId2){
		Bson filters = Filters.eq("boardId1",topoNodeId1);
		filters= Filters.and(filters,Filters.eq("boardId2", topoNodeId2));
		topologylinesCol.deleteOne(filters);
	}
	
	
	//删除光纤从数据库中
	public static void deleteTopo(double topoNodeId1,double topoNodeId2){
		Bson filters = Filters.eq("topoNodeId1",topoNodeId1);
		filters= Filters.and(filters,Filters.eq("topoNodeId2", topoNodeId2));
		DeleteResult result = topologylinesCol.deleteOne(filters);
		if(result==null){
			filters = Filters.eq("topoNodeId1",topoNodeId2);
			filters= Filters.and(filters,Filters.eq("topoNodeId2", topoNodeId1));
			topologylinesCol.deleteOne(filters);
		}
	}
	
	//在网元A和网元F之间添加光纤，断开网元A和C的光纤，即在此光纤上C网元的信息改成F网元上的信息，A的信息不变
	public static void addTopo(int boardId1, String portName1,int boardId2, String portName2) {					
		Calendar cal=Calendar.getInstance();
		int year=cal.get(Calendar.YEAR);
		int month=cal.get(Calendar.MONTH);
		int date=cal.get(Calendar.DATE);
		int hour=cal.get(Calendar.HOUR_OF_DAY);
		int minute=cal.get(Calendar.MINUTE);
		int second=cal.get(Calendar.SECOND);
		String a=month+""+date+""+hour+""+minute+""+second+"";
		Double topoLinkId =Double.valueOf(a);
		
			// 需要boardId1,boardId2
			// boardId1中的端口依然使用之前的端口
			// boardId2找一个多余的端口
			
			String portKey1 = portName1;
			String portKey2 = portName2;
			String linkSpeedName;
			double topoNodeId1;
			double topoNodeId2;
			Double topoNodeId;
			
			Document query1 = new Document().append("boardId1", boardId1);
			Document doc1 = topologylinesCol.find(query1).first();
			if (doc1==null) {
				Document query2 = new Document().append("boardId2", boardId1);
				Document doc2 = topologylinesCol.find(query2).first();
				if(doc2==null){
					linkSpeedName =  "GE" ;//GE-10GE,原来是多少GE就是多少GE
					topoNodeId2 =  ToponodeService.findtopoNodeIdbyNeId(BoardService.findNeIdbyBoardId(boardId2)); 
					topoNodeId1 = ToponodeService.findtopoNodeIdbyNeId(BoardService.findNeIdbyBoardId(boardId1));
					topoNodeId = topoNodeId1;				
				}
				else{
					linkSpeedName = doc2.get("linkSpeedName").toString();  //GE-10GE,原来是多少GE就是多少GE
					topoNodeId2 = Double.valueOf(doc2.get("topoNodeId2").toString());
					topoNodeId1 = Double.valueOf(a);
					topoNodeId = topoNodeId1;				
				}
			}else {
				linkSpeedName = doc1.get("linkSpeedName").toString();//GE-10GE,原来是多少GE就是多少GE
				topoNodeId1 = Double.valueOf(doc1.get("topoNodeId1").toString());
				topoNodeId2 = Double.valueOf(a);
				topoNodeId = topoNodeId2;
			}			
			Document line = new Document().append("boardId1", boardId1)
											.append("boardId2", boardId2)
											.append("linkSpeedName", linkSpeedName)
											.append("portKey1", portKey1)
											.append("portKey2", portKey2)
											.append("portName1", portName1)
											.append("portName2", portName2)
											.append("topoLinkId", topoLinkId)
											.append("topoNodeId1", topoNodeId1)
											.append("topoNodeId2", topoNodeId2);

			topologylinesCol.insertOne(line);
//			System.out.println("插入光纤成功");
			
			ToponodeService.AddNodeTopo(boardId1, boardId2);

 
		}
		
	//获取断裂光纤的原始信息 ：光纤的速率GE-XGE、固定网元的板卡和端口
	public static List<Object> checkNeToAddTopo(String neName1,String neName2) {
		
		List<Object> list =  new ArrayList<Object>();
		Document find1= new Document().append("neName", neName1);
		Document document1 = neMapCol.find(find1).first();
		int neId1 = Integer.valueOf(document1.get("neId").toString());
			
		Document find2 = new Document().append("neName", neName2);
		Document document2 = neMapCol.find(find2).first();
		int neId2 = Integer.valueOf(document2.get("neId").toString());

		Document query1 = new Document().append("neId", neId1);
		Document doc1 = topologynodesCol.find(query1).first();
		double topoNodeId1 = Double.valueOf(doc1.get("topoNodeId").toString());
			
		Document query2 = new Document().append("neId", neId2);
		Document doc2 = topologynodesCol.find(query2).first();
		double topoNodeId2 = Double.valueOf(doc2.get("topoNodeId").toString());
		
		String neName = neName1 ;
		double boardId ;
		String portName;
		String linkSpeedName;
		Document query = new Document().append("topoNodeId1", (int)topoNodeId1).append("topoNodeId2", (int)topoNodeId2);
		Document doc = topologylinesCol.find(query).first();

		
		if (doc==null) {
			Document Query = new Document().append("topoNodeId1", (int)topoNodeId2).append("topoNodeId2", (int)topoNodeId1);
			Document Doc = topologylinesCol.find(Query).first();
			boardId =  Double.valueOf(Doc.get("boardId2").toString());
			portName = Doc.get("portName2").toString();
			linkSpeedName =Doc.get("linkSpeedName").toString();
			
		}else {
			boardId =  Double.valueOf(doc.get("boardId1").toString());
			portName = doc.get("portName1").toString();
			linkSpeedName =doc.get("linkSpeedName").toString();
		}
		list.add(neName);
		list.add(boardId);
		list.add(portName);
		list.add(linkSpeedName);
		return list;
	}
	
	

}
