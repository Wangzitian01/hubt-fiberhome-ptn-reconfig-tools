package cn.edu.hbut.hfcrt.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.commons.collections4.iterators.FilterIterator;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import cn.edu.hbut.cs.hfcrt.databridge.dao.DataBridgeDAO;

public class TopoService {		
	static MongoCollection<Document> boardMapCol = new DataBridgeDAO().getCollection("boardMap");
	static MongoCollection<Document> nodePortListCol = new DataBridgeDAO().getCollection("nodePortList");
	static MongoCollection<Document> topologylinesCol = new DataBridgeDAO().getCollection("nodeTopology_lines");
	static MongoCollection<Document> topologynodesCol = new DataBridgeDAO().getCollection("nodeTopology_nodes");
	static MongoCollection<Document> neMapCol = new DataBridgeDAO().getCollection("neMap");
	static MongoCollection<Document> nodeTunnelMapCol= new DataBridgeDAO().getCollection("nodeTunnelMap");	
	public static void addTopo1(int boardId, String portName, int aimBoardId, String aimportName) {

		// 需要boardId1,boardId2
		// boardId2中的端口依然使用之前的端口吗？
		// 还是和boardId1一样找一个多余的端口？
		int boardId1 = boardId;
		int boardId2 = Integer.valueOf(aimBoardId);
		String linkSpeedName = "";
		String portKey1 = portName;
		String portKey2 = aimportName;
		String portName1 = portName;
		String portName2 = aimportName;

		//[10000000,99999999)
		Calendar cal=Calendar.getInstance();
		int year=cal.get(Calendar.YEAR);
		int month=cal.get(Calendar.MONTH);
		int date=cal.get(Calendar.DATE);
		int hour=cal.get(Calendar.HOUR_OF_DAY);
		int minute=cal.get(Calendar.MINUTE);
		int second=cal.get(Calendar.SECOND);
		String a=month+""+date+""+hour+""+minute+""+second+"";
		int topoLinkId =Integer.valueOf(a);
		int topoNodeId1 = Integer.valueOf(a);
		int topoNodeId2 = Integer.valueOf(a);
		
		Document line = new Document().append("boardId1", boardId1).append("boardId2", boardId2)
				.append("linkSpeedName", linkSpeedName).append("portKey1", portKey1).append("portKey2", portKey2)
				.append("portName1", portName1).append("portName2", portName2).append("topoLinkId", topoLinkId)
				.append("topoNodeId1", topoNodeId1).append("topoNodeId2", topoNodeId2);
		topologylinesCol.insertOne(line);

		Document query = new Document().append("boardId", boardId);
		Document doc=boardMapCol.find(query).first();
		
		int neId = Integer.valueOf(doc.get("neId").toString());
		int topoNodeId = topoNodeId1;
		Document node = new Document().append("neId", neId).append("topoNodeId", topoNodeId);
		topologynodesCol.insertOne(node);

	}
	
	//距离,有问题
	public static boolean Distance(String NeName1,String NeName2){		
		
		Bson filters = Filters.eq("workPath.pathsList.portName",NeName1);
		filters= Filters.and(filters,Filters.eq("protectPath.pathsList.portNo",NeName2));
		FindIterable<Document> findIterable = nodeTunnelMapCol.find(filters);
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		
		List<Document> availableNeName = new ArrayList<Document>();
	 	
		while (mongoCursor.hasNext()) {
			Document doc = mongoCursor.next();
			availableNeName.add(doc);
		}
		
		if(availableNeName.size() > 0){
			for(int i=0;i<availableNeName.size();i++){
				Document workPath=(Document) availableNeName.get(i).get("workPath");
				Document pathLists=(Document) workPath.get("pathLists");
				String NeName=pathLists.get("portName").toString();
				if(NeName=="GE"){
					int M= (int) (Math.random()*50);
					if(M<=40){
						//TODO
						//此处假的光纤的信息,需要获取这个网元之间的空闲板卡和空闲端口
						//addTopo(boardId, portName, aimBoardId, aimportName);
						return true;
					}
				}		
				if(NeName=="XGE"){
					int M=(int) (Math.random()*100);
					if(M<=80){
						//TODO
						//此处假的光纤的信息,需要获取这个网元之间的空闲板卡和空闲端口
						//addTopo(boardId, portName, aimBoardId, aimportName);
						return true;
					}				
			}
		}														
	 }
		return false;								
  }	
	
	
	
	//判断两个网元之间是否有光纤连接（测试成功）
	public static Map<List<String>, Document> checkAdditionalTopos(String NeName1,String NeName2){
			
			int count=0;
			Map<List<String>, Document> availableTopoLIgyMap  = new HashMap<List<String>, Document>();//保存光纤数据和相应的网元信息
			List<String> neNameList = new ArrayList<String>();
//			List<Document> availableTopoLIgyList = new ArrayList<Document>();
			//在neMap中通过neName找到neId
			Document queryNe1 = new Document().append("neName", NeName1);
			Document ne1=neMapCol.find(queryNe1).first();//查找neName1符合的对象
			int neId1=(Integer) ne1.get("neId");
			Document queryNe2 = new Document().append("neName", NeName2);
			Document ne2=neMapCol.find(queryNe2).first();//查找neName1符合的对象
			int neId2=(Integer) ne2.get("neId");
//				System.out.println("neId1="+neId1);
//				System.out.println("neId2="+neId2);
			
			//在boardMap中通过neId找到所有的boardId
			Document queryBoard1 = new Document().append("neId", neId1);
			FindIterable<Document> findBoard1 = boardMapCol.find(queryBoard1);
			MongoCursor<Document> CursorBoard1 = findBoard1.iterator();
			
//			Document queryBoard2 = new Document().append("neId", neId2);
//			FindIterable<Document> findBoart2 = boardMapCol.find(queryBoard2);
//			MongoCursor<Document> CursorBoard2 = findBoart2.iterator();
			
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
						String portName1=  (String) port1.get("portName");//获得端口1的portName1
//						System.out.println();	
//						System.out.println("板卡1："+"boardId1="+boardId1+"portName1="+portName1);
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
//										System.out.println("boardId2="+boardId2+"portName2="+portName2);
//											System.out.println();
//											System.out.println("boardId1:"+boardId1);
//											System.out.println("boardId2:"+boardId2);
//											System.out.println("portName1:"+portName1);
//											System.out.println("portName2:"+portName2);
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
//											System.out.println("topology1="+topology1);
//											System.out.println("topology2="+topology2);
											if (topology1 == null) {
												if (topology2 == null) {//1为空，2 为空
//													System.out.println("两个网元之间没有光纤连接");
													//System.out.println();
													continue;	
												}else {//1为空，2不为空
//													System.out.println("两个网元之间有光纤连接");
													count++;
//													System.out.println("boardId1="+boardId2);
//													System.out.println("boardId2="+boardId1);
//													System.out.println("portName1="+portName2);
//													System.out.println("portName2="+portName1);
//													System.out.println("topology2="+topology2);
//													System.out.println("TopoService.java");
//													System.out.println();
//													availableTopoLIgyList.add(topology);
													neNameList.add(NeName1);
													neNameList.add(NeName2);
													availableTopoLIgyMap.put(neNameList, topology2);
												}
											}else {//1不为空
//												System.out.println("两个网元之间有光纤连接");
//												System.out.println("boardId1="+boardId1);
//												System.out.println("boardId2="+boardId2);
//												System.out.println("portName1="+portName1);
//												System.out.println("portName2="+portName2);
//												System.out.println("topology1="+topology1);
//												System.out.println("TopoService.java");
//												System.out.println();
//												availableTopoLIgyList.add(topology);
												neNameList.add(NeName1);
												neNameList.add(NeName2);
												availableTopoLIgyMap.put(neNameList, topology1);
												if (topology2 == null) {
//													System.out.println("两个网元之间没有光纤连接");
													//System.out.println();
													continue;
												} else {
//													System.out.println("两个网元之间有光纤连接");
//													System.out.println("boardId1="+boardId2);
//													System.out.println("boardId2="+boardId1);
//													System.out.println("portName1="+portName2);
//													System.out.println("portName2="+portName1);
//													System.out.println("topology2="+topology2);
//													System.out.println("TopoService.java");
//													System.out.println();
//													availableTopoLIgyList.add(topology);
													neNameList.add(NeName1);
													neNameList.add(NeName2);
													availableTopoLIgyMap.put(neNameList, topology2);
												}
											}																																																																
											
//										}
									}
								}
								
								
							}
//						}
					}
					
				}
			}
//			System.out.println("count="+count);
//			System.out.println("neNameList="+neNameList);
//			System.out.println("availableTopoLIgyMap="+availableTopoLIgyMap);
			return availableTopoLIgyMap;
			}

	//加光纤，我重新写的
	//在网元A和网元F之间添加光纤，断开网元A和C的光纤，即件此光纤上C网元的信息改成F网元上的信息，A的信息不变
	public static void addTopo(int boardId1, String portName1,int boardId2, String portName2) {

//		System.out.println("boardId1="+boardId1);
//		System.out.println("portName1="+portName1);
//		System.out.println("boardId2="+boardId2);
//		System.out.println("portName2="+portName2);
		
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
					
				}
				linkSpeedName = doc2.get("linkSpeedName").toString();//GE-10GE,原来是多少GE就是多少GE
				topoNodeId2 = Double.valueOf(doc2.get("topoNodeId2").toString());
				topoNodeId1 = Double.valueOf(a);
				topoNodeId = topoNodeId1;
//				System.out.println("topoNodeId1="+topoNodeId1);
//				System.out.println("topoNodeId2="+topoNodeId2);
				System.out.println("yes");
			}else {
				linkSpeedName = doc1.get("linkSpeedName").toString();//GE-10GE,原来是多少GE就是多少GE
				topoNodeId1 = Double.valueOf(doc1.get("topoNodeId1").toString());
				topoNodeId2 = Double.valueOf(a);
				topoNodeId = topoNodeId2;
//				System.out.println("topoNodeId1="+topoNodeId1);
//				System.out.println("topoNodeId2="+topoNodeId2);
				System.out.println("no");
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
//			System.out.println("line="+line);
			topologylinesCol.insertOne(line);
			System.out.println("插入line成功");

			//将光纤连接的F网元的信息存入数据库
			Document Aquery1 = new Document().append("boardId", boardId2);
			Document doc=boardMapCol.find(Aquery1).first();
			Double AneId2 = Double.valueOf(doc.get("neId").toString());
			Document node = new Document().append("neId", AneId2).append("topoNodeId", topoNodeId);
			topologynodesCol.insertOne(node);
			System.out.println("插入node成功");
			
			
			//将光纤连接的F网元的信息存入数据库
			Document query = new Document().append("boardId", boardId2);
			Document doc2=boardMapCol.find(query).first();
			Double neId2 = Double.valueOf(doc2.get("neId").toString());
//			Double topoNodeId = topoNodeId2;
			Document node1 = new Document().append("neId", neId2).append("topoNodeId", topoNodeId);
			topologynodesCol.insertOne(node1);
			System.out.println("插入node成功");
		}
	
	//加光纤距离判断
	public static boolean distance(String linkSpeedName) {
		if (linkSpeedName == "10GE") {
			int count = 0;
			for (int i = 0; i < 1000; i++) {
				int M= (int) (Math.random()*50);
				if (M <= 80) {
					count++;
				}
			}
			if (count>=800) {
				return true;//80%的随机数小于80则可以加光纤
			}
		}
		if (linkSpeedName == "GE") {
			int count = 0;
			for (int i = 0; i < 1000; i++) {
				int M= (int) (Math.random()*50);
				if (M <= 40) {
					count++;
				}
			}
			if (count>=800) {
				return true;//80%的随机数小于40则可以加光纤
			}
		}
		return false;
	}
	
	//

	//获取断裂光纤的原始信息 ：光纤的速率GE-XGE、固定网元的板卡和端口
	public static Map<String,List<Object>> checkNeToAddTopo(String neName1,String neName2) {
		
		Map<String,List<Object>> map =new HashMap<String,List<Object>>();
		List<Object> list1 =  new ArrayList<Object>();
		List<Object> list2 =  new ArrayList<Object>();
		
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
//		
		String NeName1 = neName1 ;
		String NeName2 = neName2;
		double boardId1,boardId2 ;
		String portName1,portName2;
		String linkSpeedName1,linkSpeedName2;
		Document query = new Document().append("topoNodeId1", (int)topoNodeId1).append("topoNodeId2", (int)topoNodeId2);
		Document doc = topologylinesCol.find(query).first();
		
		if (doc==null) {
			Document Query = new Document().append("topoNodeId1", (int)topoNodeId2).append("topoNodeId2", (int)topoNodeId1);
			Document Doc = topologylinesCol.find(Query).first();
			boardId1 =  Double.valueOf(Doc.get("boardId2").toString());
			portName1 = Doc.get("portName2").toString();
			linkSpeedName1 =Doc.get("linkSpeedName").toString();
			list1.add(0, boardId1);
			list1.add(1, portName1);
			list1.add(2, linkSpeedName1);
			
			boardId2 =  Double.valueOf(Doc.get("boardId1").toString());
			portName2 = Doc.get("portName1").toString();
			linkSpeedName2 =Doc.get("linkSpeedName").toString();
			list2.add(0, boardId2);
			list2.add(1, portName2);
			list2.add(2, linkSpeedName2);
			
			map.put(neName1, list1);
			map.put(neName2, list2);
					
		}else {
			boardId1 =  Double.valueOf(doc.get("boardId1").toString());
			portName1 = doc.get("portName1").toString();
			linkSpeedName1 =doc.get("linkSpeedName").toString();
			list1.add(0, boardId1);
			list1.add(1, portName1);
			list1.add(2, linkSpeedName1);
			
			boardId2 =  Double.valueOf(doc.get("boardId2").toString());
			portName2 = doc.get("portName2").toString();
			linkSpeedName2 =doc.get("linkSpeedName").toString();
			list2.add(0, boardId2);
			list2.add(1, portName2);
			list2.add(2, linkSpeedName2);
			
			map.put(neName1, list1);
			map.put(neName2, list2);

		}
 
		return map;
	}
	
}
