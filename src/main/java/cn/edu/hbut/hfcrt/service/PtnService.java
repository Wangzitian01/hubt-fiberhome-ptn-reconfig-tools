package cn.edu.hbut.hfcrt.service;

/**
 * @author ZTW
 *
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import cn.edu.hbut.hfcrt.common.MongoDBHelper;
import cn.edu.hbut.hfcrt.constant.Constant;
import cn.edu.hbut.hfcrt.pojo.Condition;

public class PtnService {
	
	MongoDBHelper mongoDBHelper = new  MongoDBHelper();
	static MongoClient mongoClient = new MongoClient();
	static MongoDatabase mongoDatabase=MongoDBHelper.getMongoDatabase(mongoClient);
	static MongoCollection<Document> tunnelMapCol =  mongoDatabase.getCollection(Constant.tunnelMapCol);
	static MongoCollection<Document> neMapCol =  mongoDatabase.getCollection("neMap");
	static MongoCollection<Document> topologynodesCol =  mongoDatabase.getCollection(Constant.nodeTopologynodesCol);
	static MongoCollection<Document> topologylinesCol =  mongoDatabase.getCollection(Constant.nodeTopologyLinesCol);
	
	//根据网元名找 neid
	public static int  findNeId(String neName) {
		Document query=new Document().append("neName", String.valueOf(neName));
		Document board=neMapCol.find(query).first();//查找neName符合的对象
		Integer neId = (Integer) board.get("neId");
		return neId;	
	}

	//根据网元ID在toponodeId
	public static double findToponodeId(int neId){
		Document query=new Document().append("neId", neId);
		Document node=topologynodesCol.find(query).first();
		double topoNodeId =  (double) node.get("topoNodeId");		
		return  topoNodeId;	
	}
	
	//根据两个网元的toponodeId找两个网元之间的光纤连接信息,获得boardId1,boardId2,portName1,portName2,
	public static Document findTopoLinesInfo(Double topoNodeId1,Double topoNodeId2){
	
		Document query1=new Document().append("topoNodeId1", topoNodeId1).append("topoNodeId2", topoNodeId2);
		Document doc = topologylinesCol.find(query1).first();
		if(doc==null){  //toponodeId1可能是右边网元的，toponodeId2可能是左边网元的，交换一下位置再查询
			Document query2=new Document().append("topoNodeId1", topoNodeId2).append("topoNodeId2", topoNodeId1);
			Document doc2 = topologylinesCol.find(query2).first();
			doc=doc2;
		}
		return doc;	
	}
	
	//根据toponodeId查找neID
	public static double findNeIdByTopoID(double topoNodeId1){
		Document query=new Document().append("topoNodeId", topoNodeId1);
		Document node=topologynodesCol.find(query).first();
		double neId =  (double) node.get("neId");			
		return neId;	
	}
	
	//根据两个网元的toponodeId找两个网元之间的光纤连接信息,获得boardId1,boardId2,portName1,portName2,
	public static List<Condition> findBoardIdAndPortNameByTopoId(Double topoNodeId1,Double topoNodeId2){
		Boolean flag = false;
		List<Condition> list =new ArrayList<Condition>();
		Condition con = new Condition();
		double boardId1;
		double boardId2;
		String portName1;
		String portName2;
		Document query1=new Document().append("topoNodeId1", topoNodeId1).append("topoNodeId2", topoNodeId2);
		Document doc = topologylinesCol.find(query1).first();
		if(doc==null){  //toponodeId1可能是右边网元的，toponodeId2可能是左边网元的，交换一下位置再查询
			Document query2=new Document().append("topoNodeId1", topoNodeId2).append("topoNodeId2", topoNodeId1);
			Document doc2 = topologylinesCol.find(query2).first();
			if (doc2==null){
				flag = true;
				double BoardId1=0;
				boardId1 = BoardId1;
				double BoardId2=0;
				boardId2 = BoardId2;
				String PortName1=null;
				portName1 = PortName1;
				String PortName2=null;
				portName2 = PortName2;
				double neId1 = PtnService.findNeIdByTopoID(topoNodeId1);
				double neId2 = PtnService.findNeIdByTopoID(topoNodeId2);
				con.setBoardId1(BoardId2);
				con.setBoardId2(BoardId1);
				con.setPortName1(PortName1);
				con.setPortName2(PortName2);
				con.setNeId1(neId1);
				con.setNeId2(neId2);
				list.add(con);
			}else{
				 
				double BoardId1 =  (double) doc2.get("boardId1");
				System.out.println("BoardId1"+BoardId1);
				boardId1 = BoardId1;
				double BoardId2 =  (double) doc2.get("boardId2");
				boardId2 = BoardId2;
				String PortName1 = (String) doc2.get("portName1");
				portName1 = PortName1;
				String PortName2 = (String) doc2.get("portName2");
				portName2 = PortName2;
				double neId1 = PtnService.findNeIdByTopoID(topoNodeId1);
				double neId2 = PtnService.findNeIdByTopoID(topoNodeId2);
				con.setBoardId1(BoardId2);
				con.setBoardId2(BoardId1);
				con.setPortName1(PortName1);
				con.setPortName2(PortName2);
				con.setNeId1(neId1);
				con.setNeId2(neId2);
				list.add(con);	
				
			}
 		
		}
		else{
			double BoardId1 =  (double) doc.get("boardId1");
			boardId1 = BoardId1;
			double BoardId2 =  (double) doc.get("boardId2");
			boardId2 = BoardId2;
			String PortName1 = (String) doc.get("portName1");
			portName1 = PortName1;
			String PortName2 = (String) doc.get("portName2");
			portName2 = PortName2;
			double neId1= PtnService.findNeIdByTopoID(topoNodeId1);		
			double neId2 = PtnService.findNeIdByTopoID(topoNodeId2);	
			con.setBoardId1(BoardId1);
			con.setBoardId2(BoardId2);
			con.setPortName1(PortName1);
			con.setPortName2(PortName2);
			con.setNeId1(neId1);
			con.setNeId2(neId2);
			list.add(con);	
						
		}
		return list;	
	}
	
	//根据两个网元名字，去nodetopology_lines找boardId1 boardId2 portName1 portName2
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
			
		String neName = neName1;
		double boardId ;
		String portName;
		String linkSpeedName;	
		//根据两个网元的topoNodeId在nodetopolines找到光纤对应信息
		Document query = new Document().append("topoNodeId1", (int)topoNodeId1).append("topoNodeId2", (int)topoNodeId2);
		Document doc = topologylinesCol.find(query).first();
		
		System.out.println("要断网元之间的光纤信息"+doc);		
		if (doc==null) {
			Document Query = new Document().append("topoNodeId1", (int)topoNodeId2).append("topoNodeId2", (int)topoNodeId1);
			Document Doc = topologylinesCol.find(Query).first();
			System.out.println("找到满足条件的boardId,portName"+Doc);	
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
	
	//查找满足条件的tunnel,多加一个限制条件
	public static List<Document> selectTunnels1(Double boardId,String portName, double neId,String path){
		List<Document> tunnelList = new ArrayList<Document>();
		Bson filters = Filters.eq(path+".pathsList.boardId", Double.valueOf(boardId));
		filters = Filters.and(filters,Filters.eq(path+".pathsList.portName", portName));
		filters = Filters.and(filters,Filters.eq(path+".pathsList.neId", neId));
		FindIterable<Document> findIterable = tunnelMapCol.find(filters);
		MongoCursor<Document> cursor =findIterable.iterator();
		while(cursor.hasNext()){
			Document tunnel = cursor.next();
			tunnelList.add(tunnel);
		}	
		return tunnelList;
		
	}
	
	//直接根据网元id查tunnel
	public static List<Document> selectTunnels2(double neId,String path){
		List<Document> tunnelList = new ArrayList<Document>();
		Bson filters = Filters.eq(path+".0.pathsList.neId",neId);		 
		FindIterable<Document> findIterable = tunnelMapCol.find(filters);
		MongoCursor<Document> cursor =findIterable.iterator();
		while(cursor.hasNext()){
			Document tunnel = cursor.next();
			tunnelList.add(tunnel);
		}	
		return tunnelList;
		
	}
	
	//查找到Path所有符合条件的tunnel,path为protectPath和workPath
	public static List<Object> selecTunnels(Double findBoardID,String portName1,String path) {
		
		List<Object> tunnelList = new ArrayList<Object>();
		Bson filters = Filters.eq(path+".pathsList.boardId",Double.valueOf(findBoardID));
		filters= Filters.and(filters,Filters.eq(path+".pathsList.portName", portName1));
		FindIterable<Document> findIterable = tunnelMapCol.find(filters);
		MongoCursor<Document> cursor = findIterable.iterator();
		while (cursor.hasNext()) {
			Document tunnel= cursor.next();
			tunnelList.add(tunnel);
		}
		return tunnelList;
	}
	
	//查找满足条件一条tunnel中所经过的所有网元ID
		public static Set<Integer>  findNeIdByTunnelList1(Document tunnel){
			
			Set<Integer> set=new LinkedHashSet<Integer>();
			List<Integer> list2=new ArrayList<Integer>();
			
			Document workPath = (Document) ((ArrayList) tunnel.get("workPath")).get(0);
			ArrayList pathsList = (ArrayList) workPath.get("pathsList");
//			System.out.println("当前pathList中网元的个数为:"+pathsList.size()); //需要除以2
			for(int j=0;j<pathsList.size();j++){
				int TunnelNeId = Integer.valueOf(((Document)pathsList.get(j)).get("neId").toString());
				set.add(TunnelNeId);
//				System.out.println("经过该tunnel的每一个网元neId"+TunnelNeId);				
			}
			return set;
		}
		
		//查找满足条件所有tunnel中所经过的所有网元ID
		public static List<Set<Integer>> findNeIdByTunnelList(List<Document> list){
			List<Set<Integer>> list1 =new ArrayList<Set<Integer>>();
			for(int i=0;i<list.size();i++){
				Set<Integer> set = findNeIdByTunnelList1(list.get(i));
				list1.add(set);
			}
			return list1;
		}
		
		
		//根据满足条件的tunnel中经过网元ID查找网元名
		public static  Map<Integer,String> findneNameByNeID(Set<Integer> set){
			Map<Integer,String> map =new HashMap<Integer,String>();
			List<Integer> list=new ArrayList<Integer>();
			list.addAll(set);
			for(int i=0;i<list.size();i++){	
					int NeId = list.get(i);
					Document query=new Document().append("neId", Integer.valueOf(NeId));
					Document board=neMapCol.find(query).first();//查找neName符合的对象
					String neName = (String) board.get("neName");
					map.put(NeId, neName);
			}
			return  map;
		}
		
		
		
	

//			for(int i=0;i<list.size();i++){
//				set.clear();
//				list2.clear();
//				System.out.println("清空后的数据为:"+set);
//				Document tunnel = list.get(i);
//				Document workPath = (Document) ((ArrayList) tunnel.get("workPath")).get(0);
//				ArrayList pathsList = (ArrayList) workPath.get("pathsList");
//				System.out.println("正在查找第"+i+"tunnel中的网元");
//				System.out.println("当前pathList中网元的个数为:"+pathsList.size()); //需要除以2
//				for(int j=0;j<pathsList.size();j++){
//					int TunnelNeId = Integer.valueOf(((Document)pathsList.get(j)).get("neId").toString());
//					set.add(TunnelNeId);
//					System.out.println("经过该tunnel的每一个网元neId"+TunnelNeId);				
//				}
//				System.out.println("当前tunnel中所经过的网元id名为:"+set);
//	           for(Integer ii:set){
//	            	list2.add(ii);
//	            }
//				System.out.println("list2"+list2);
//				list3.add(list2);
//				System.out.println("list3"+list3);

//				System.out.println("当前set集合中的数据为:"+set);
//				list2.addAll(set);
//				System.out.println("当前list集合中数据为:"+list2);
//				list3.add(list2);
//				System.out.println("list3"+list3);
//				list2.clear();
//				System.out.println("当前tunnel中网元set集合中的数据:"+set);
//				map.put(i,set);
//				System.out.println("当前map中的第i条数据:"+map.get(i));
//				System.out.println("map总数据为:"+map);

//			}
//
//			return map;		
//		}
//	
	
//	//查找满足条件tunnel中所经过的所有网元ID
//	public static Map<Integer,Set<Integer>>  findNeIdByTunnelList(List<Document> list){
//		Map<Integer,Set<Integer>> map=new HashMap<Integer,Set<Integer>>();
//		Set<Integer> set=new LinkedHashSet<Integer>();
//		List<Integer> list2=new ArrayList<Integer>();
//		List<Set<Integer>> list1=new ArrayList<Set<Integer>>();
//		List<List<Integer>> list3=new ArrayList<List<Integer>>();
//		System.out.println("tunnel的长度"+list.size());
//        Integer neId[][]=new Integer[list.size()][1000];
//		for(int i=0;i<list.size();i++){
//			set.clear();
//			list2.clear();
//			System.out.println("清空后的数据为:"+set);
//			Document tunnel = list.get(i);
//			Document workPath = (Document) ((ArrayList) tunnel.get("workPath")).get(0);
//			ArrayList pathsList = (ArrayList) workPath.get("pathsList");
//			System.out.println("正在查找第"+i+"tunnel中的网元");
//			System.out.println("当前pathList中网元的个数为:"+pathsList.size()); //需要除以2
//			for(int j=0;j<pathsList.size();j++){
//				int TunnelNeId = Integer.valueOf(((Document)pathsList.get(j)).get("neId").toString());
//				set.add(TunnelNeId);
//				System.out.println("经过该tunnel的每一个网元neId"+TunnelNeId);				
//			}
//			System.out.println("当前tunnel中所经过的网元id名为:"+set);
////           for(Integer ii:set){
////            	list2.add(ii);
////            }
////			System.out.println("list2"+list2);
////			list3.add(list2);
////			System.out.println("list3"+list3);
//
////			System.out.println("当前set集合中的数据为:"+set);
////			list2.addAll(set);
////			System.out.println("当前list集合中数据为:"+list2);
////			list3.add(list2);
////			System.out.println("list3"+list3);
////			list2.clear();
////			System.out.println("当前tunnel中网元set集合中的数据:"+set);
////			map.put(i,set);
////			System.out.println("当前map中的第i条数据:"+map.get(i));
////			System.out.println("map总数据为:"+map);
//
//		}
//
//		return map;		
//	}
//	
	public static int[] iteratorInSet(Set<Integer> set){
		int a[] = null;
		Iterator it=set.iterator();
		int i=0;	
		while(it.hasNext()){
			Integer s = (Integer) it.next();
			a[i]=s;
			i++;
		}
		return a;
	}
	
	
	
 
	
	
	
//	
//	//根据满足条件的tunnel中经过网元ID查找网元名
//	public static List<List<String>> findneNameByNeID(List<List<Integer>> list){
//		List<String> list2=new ArrayList<String>();
//		List<List<String>> list1=new ArrayList<List<String>>();
//		for(int i=0;i<list.size();i++){
//			System.out.println("当前经过第"+i+"条tunnel");
//			System.out.println(list.size());
//			for(int j=0;j<list.get(i).size();j++){
//				System.out.println("当前tunnel中的网元个数为:"+list.get(i).size());
//				int NeId = list.get(i).get(j);
//				Document query=new Document().append("neId", Integer.valueOf(NeId));
//				Document board=neMapCol.find(query).first();//查找neName符合的对象
//				String neName = (String) board.get("neName");
//				System.out.println("此时经过的tunnel中的网元名是:"+neName);
//				list2.add(neName);
//			}
//			list1.add(list2);
//		}
//		return list1;
//	}
//	
	
	
	
	
	@SuppressWarnings("rawtypes")
	public static Boolean JudgeTunnelIsExistNe(List<Object> tunnelList,int neId,String path){
		Boolean flag = false;	
		for(int i=0;i<tunnelList.size();i++){
			Document tunnel = (Document) tunnelList.get(i);
			Document Path = (Document) ((ArrayList) tunnel.get(path)).get(0);
			ArrayList pathsList = (ArrayList) Path.get("pathsList");
			for(int j=0;j<=pathsList.size();j++){
				int NeId = Integer.valueOf(((Document) pathsList.get(i)).get("neId").toString());
				if(neId == NeId){
					 flag = true;
				}			
			}			 							
		}
		return flag;		
	}
		
	//path删除网元ne:tunnel的_id,
	public static void deleteOneNe(int id,int aimNeId,String path) {
		Document query = new Document().append("_id", id);
		Document tunnel = tunnelMapCol.find(query).first();		
		Document Path = (Document) ((ArrayList) tunnel.get(path)).get(0);//获取该Tunnel中的workpath
		ArrayList wp_pathsList = (ArrayList) Path.get("pathsList");		
		ArrayList pathsList=new ArrayList<Object>();		
		  for(int i=0;i<wp_pathsList.size();i++){//要把原来的pathsList里面的每一个网元都添加进去
			//  int neId= (Integer) wp_pathsList.get(i).get("neId");
			  int neId =(int) ((Document)wp_pathsList.get(i)).get("neId");
			  if (neId != aimNeId) {//neId不是需要删除的网元aimNeId，则将其加入
				  pathsList.add(wp_pathsList.get(i));
			}
		  }
		  Document document3 = new Document().append(path+".0.pathsList", pathsList);
		  tunnelMapCol.updateOne(tunnel, new Document("$set", document3));
		  System.out.println("切换业务后的tunnel"+tunnel);
		  System.out.println("网元6,7,8,9删除成功");
	}
	
	//更新网元5的boardID和portName
	public static void updateNe(int id,int aimNeId,int newBoardId,String NewPortName,int X,String path) {
		//504409321, 134218261, 100667480, "GE_2", 1, "workPath"
		System.out.println("传入进来的板卡ID为:"+newBoardId);
		System.out.println("传入进来的端口名字为为:"+NewPortName);
		System.out.println("传入进来位置X为:"+X);
		System.out.println("传入进来的网元ID为:"+aimNeId);
		
		Document query = new Document().append("_id", id);
		Document tunnel = tunnelMapCol.find(query).first();		
		Document Path = (Document) ((ArrayList) tunnel.get(path)).get(0);//获取该Tunnel中的workpath
		ArrayList wp_pathsList = (ArrayList) Path.get("pathsList");
		
		Document ne = (Document) wp_pathsList.get(X);
		
//		double oldBoardId = Double.valueOf(ne.get("boardId").toString());
//		String oldPortName = ne.get("portName").toString();
//		int oldPortNo = (Integer) ne.get("portNo");	
		
		String[] strArray = NewPortName.split("_");
		int NewPortNo =Integer.valueOf(strArray[strArray.length-1].toString());	
		 
		Document NewboardID= new Document("$set",new Document(path+".0.pathsList."+X+".boardId", Double.valueOf(newBoardId)));
//		Bson OldboardID= Filters.eq(path+".0.pathsList."+X+".boardId", Double.valueOf(oldBoardId));		
//		tunnelMapCol.updateOne(OldboardID, NewboardID);//更新板卡boardId	
		
		Document NewportName= new Document("$set",new Document(path+".0.pathsList."+X+".portName", NewPortName));
//		Bson OldportName= Filters.eq(path+".0.pathsList."+X+".portName",oldPortName);
//		tunnelMapCol.updateOne(OldportName, NewportName);//更新端口名
		
		Document NewportNo= new Document("$set",new Document(path+".0.pathsList."+X+".portNo", Integer.valueOf(NewPortNo)));
//		Bson OldportNo= Filters.eq(path+".0.pathsList."+X+".portNo", Integer.valueOf(oldPortNo));
//		tunnelMapCol.updateOne(OldportNo, NewportNo);//更新端口号
		
		tunnelMapCol.updateOne(tunnel, NewboardID);
		
		Document tunnel1=tunnelMapCol.find(query).first();		
		tunnelMapCol.updateOne(tunnel1,NewportName);
		
		Document tunnel2=tunnelMapCol.find(query).first();
		tunnelMapCol.updateOne(tunnel2,NewportNo);
		
		
		
		
		
		
		
		

	}
		
}
