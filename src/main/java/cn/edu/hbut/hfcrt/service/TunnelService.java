/**  

* <p>Title: TunnelService.java</p>  

* <p>Description: </p>  

* @author Aurore 

* @date 2020年4月14日  

* @version 1.0  

*/  
package cn.edu.hbut.hfcrt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.print.Doc;
import javax.swing.InputMap;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import cn.edu.hbut.cs.hfcrt.databridge.dao.DataBridgeDAO;
import cn.edu.hbut.hfcrt.common.MongoDBHelper;
import cn.edu.hbut.hfcrt.constant.Constant;
import cn.edu.hbut.hfcrt.pojo.Board;
import cn.edu.hbut.hfcrt.pojo.BoardPort;
import cn.edu.hbut.hfcrt.service.message.LongChain;
import cn.edu.hbut.hfcrt.service.message.neMap.ne;

/**
 * @author sha
 *
 */
public class TunnelService {
	
//	static MongoCollection<Document> tunnelMapCol = new DataBridgeDAO().getCollection("tunnelMap");
//	static MongoCollection<Document> neMapCol = new DataBridgeDAO().getCollection("neMap");
	
	MongoDBHelper mongoDBHelper = new  MongoDBHelper();
	static MongoClient mongoClient = new MongoClient();
	static MongoDatabase mongoDatabase=MongoDBHelper.getMongoDatabase(mongoClient);
	static MongoCollection<Document> tunnelMapCol =  mongoDatabase.getCollection(Constant.tunnelMapCol);
	static MongoCollection<Document> neMapCol =  mongoDatabase.getCollection(Constant.neMapCol);
	
	
	//根据一个tunnel的id查找这个tunnel下的pathslist下有多少个网元
	public static int findNeIdLengthByPathsList(int _id,String path){
		
		Document query=new Document().append("_id", _id);
		Document tunnel =  tunnelMapCol.find(query).first();
		Document Path = (Document) ((ArrayList) tunnel.get(path)).get(0);//获取该Tunnel中的workpath
		ArrayList pathsList = (ArrayList) Path.get("pathsList");
		return pathsList.size();		
	}
	
	
	//根据tunnel的id找tunnel
	public static Document findDocument(int _id,String path){
		Document query=new Document().append("_id", _id);
		Document tunnel =  tunnelMapCol.find(query).first();	
		return tunnel;		
	}
	
	
	public static Map<Integer,List<Integer>> findTunnelAndNeId(List<Document> List,String path){
		List<Integer> list=new ArrayList<Integer>();
		Map<Integer,List<Integer>> map=new HashMap<Integer,List<Integer>>();
		for(int i=0;i<List.size();i++){
			Document tunnel=List.get(i);
			int _id = Integer.valueOf( tunnel.get("_id").toString());
			Document workPath = (Document) ((ArrayList) tunnel.get(path)).get(0);
			ArrayList pathsList = (ArrayList) workPath.get("pathsList");
			for(int j=0;j<pathsList.size();j++){
				int firstNeId = Integer.valueOf( ((Document)pathsList.get(j)).get("neId").toString());
				list.add(firstNeId);
			}
		    List newList = list.stream().distinct().collect(Collectors.toList());
			map.put(_id, newList);
		}
		return map;
		
	}
	
	
	
	
	
	
	//path插入一个网元ne:tunnel的_id ,在lastNe和nextNe网元之间插入aimNe,插入的位置X
	public static void insertOneNe(int id,String aimNe,String lastNe,String nextNe,int X,String path) {
		
		NeService neService = new NeService();
		int aimNeId = neService.findNeId(aimNe);
		System.out.println("id="+id);
		Document query = new Document().append("_id", id);
		Document tunnel =  tunnelMapCol.find(query).first();
		System.out.println("tunnel="+tunnel);
		int label = Integer.valueOf(tunnel.get("label").toString());
//		System.out.println("tunnel="+tunnel);

//		Document workPath = (Document) ((ArrayList) tunnel.get("workPath")).get(0);//获取该Tunnel中的workpath
//		ArrayList w_pathsList = (ArrayList) workPath.get("pathsList");
//		Document protectPath = (Document) ((ArrayList) tunnel.get("protectPath")).get(0);//获取该Tunnel中的protectPath
//		ArrayList<Document> p_pathsList = (ArrayList) protectPath.get("pathsList");
		
		Document Path = (Document) ((ArrayList) tunnel.get(path)).get(0);//获取该Tunnel中的workpath
		ArrayList wp_pathsList = (ArrayList) Path.get("pathsList");
		
		ArrayList pathsList=new ArrayList<Object>();

		for(int i=0;i<wp_pathsList.size();i++){//要把原来的pathsList里面的每一个网元都添加进去
		   pathsList.add(wp_pathsList.get(i));
		} 
		
		TopoService topo = new TopoService();
		BoardService board = new BoardService();
		//获得与上一个网元有光纤连接的板卡和端口
		//网元的接入端 =
		Map<List<String>, Document> lastMap = topo.checkAdditionalTopos(lastNe, aimNe);
//		System.out.println("lastMap="+lastMap);
		List<String> list1 = new ArrayList<String>();
		list1.add(lastNe);
		list1.add(aimNe);
//			System.out.println("list1="+list1);
		Document topoDoc1 = lastMap.get(list1);//根据网元组成的list获得光纤信息
//			System.out.println("topoDoc1="+topoDoc1);
		double boardID1 = Double.valueOf(topoDoc1.get("boardId1").toString());
		String portName1 = topoDoc1.get("portName1").toString();
		double boardID2 = Double.valueOf(topoDoc1.get("boardId2").toString());
		String portName2 = topoDoc1.get("portName2").toString();
//			System.out.println("boardID1="+(int)boardID1);
//			System.out.println("boardID2="+(int)boardID2);
//			System.out.println("portName1="+portName1);
//			System.out.println("portName2="+portName2);
		
//		Iterator  lastIt = lastMap.keySet().iterator();
//		Document topo1 = null;
//		while (lastIt.hasNext()) {
//	        	System.out.println("lastIt.next()="+lastIt.next());
//	        	topo1 = lastMap.get(lastIt.next());
//	        }
//		double boardID1=topo1.getDouble("boardId1");
//		double boardID2=topo1.getDouble("boardId2");
//		String portName1=topo1.getString("portName1");
//		String portName2=topo1.getString("portName2");
		boolean yes1 = board.isExist(aimNe, (int)boardID1);
		if (yes1) {
			//根据实际更改
			String[] strArray = portName1.split("_");
			int portNo1 =Integer .valueOf(strArray[strArray.length-1].toString());
			Document doc1 = new Document().append("boardId", boardID1)
										 .append("boardNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
										 .append("boardNameRef.id", boardID1)//与boardID1相同
										 .append("label", label+2)
										 .append("neId", aimNeId)
										 .append("neNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
										 .append("neNameRef.id",aimNeId)//与aimNeId相同
										 .append("portName", portName1)
										 .append("portNo", portNo1);
			pathsList.add(X,doc1);
		}else {
			//根据实际更改
			//网元的接入端
			String[] strArray = portName2.split("_");
			int portNo2 =Integer .valueOf(strArray[strArray.length-1].toString());
			Document doc1 = new Document().append("boardId", boardID2)
										 .append("boardNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
										 .append("boardNameRef.id", boardID2)//与boardID2相同
										 .append("label", label+2)
										 .append("neId", aimNeId)
										 .append("neNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
										 .append("neNameRef.id",aimNeId)//与aimNeId相同
										 .append("portName", portName2)
										 .append("portNo", portNo2);
			pathsList.add(X,doc1);
		}
		
		//网元的输出端
		//获得与下一个网元有光纤连接的板卡和端口
				Map<List<String>, Document> nextMap = topo.checkAdditionalTopos(nextNe, aimNe);
//				System.out.println("nextMap="+nextMap);
				List<String> list2 = new ArrayList<String>();
				list2.add(nextNe);
				list2.add(aimNe);
//					System.out.println("list2="+list2);
				Document topoDoc2 = nextMap.get(list2);//根据网元组成的list获得光纤信息
					System.out.println("topoDoc2="+topoDoc2);
				double boardID_1 = Double.valueOf(topoDoc2.get("boardId1").toString());
				String portName_1 = topoDoc2.get("portName1").toString();
				double boardID_2 = Double.valueOf(topoDoc2.get("boardId2").toString());
				String portName_2 = topoDoc2.get("portName2").toString();
//					System.out.println("boardID_1="+(int)boardID_1);
//					System.out.println("boardID_2="+(int)boardID_2);
//					System.out.println("portName_1="+portName_1);
//					System.out.println("portName_2="+portName_2);
				
//				Iterator  nextIt = nextMap.keySet().iterator();
//				Document topo2 = null;
//				while (nextIt.hasNext()) {
//			        	System.out.println(nextIt.next());
//			        	topo2 = nextMap.get(nextIt.next());
//			        }
//				double boardID_1=topo2.getDouble("boardId1");
//				double boardID_2=topo2.getDouble("boardId2");
//				String portName_1=topo2.getString("portName1");
//				String portName_2=topo2.getString("portName2");
				boolean yes2 = board.isExist(aimNe, (int)boardID_1);
				if (yes2) {
					//根据实际更改
					String[] strArray = portName_1.split("_");
					int portNo_1 =Integer .valueOf(strArray[strArray.length-1].toString());
					Document doc2 = new Document().append("boardId", boardID_1)
												 .append("boardNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
												 .append("boardNameRef.id", boardID_1)//与boardID1相同
												 .append("label", label+2)
												 .append("neId", aimNeId)
												 .append("neNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
												 .append("neNameRef.id",aimNeId)//与aimNeId相同
												 .append("portName", portName_1)
												 .append("portNo", portNo_1);
					pathsList.add(X+1,doc2);
				}else {
					//根据实际更改
					String[] strArray = portName_2.split("_");
					int portNo_2 =Integer .valueOf(strArray[strArray.length-1].toString());
					Document doc2 = new Document().append("boardId", boardID_2)
												 .append("boardNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
												 .append("boardNameRef.id", boardID_2)//与boardID2相同
												 .append("label", label+2)
												 .append("neId", aimNeId)
												 .append("neNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
												 .append("neNameRef.id",aimNeId)//与aimNeId相同
												 .append("portName", portName_2)
												 .append("portNo", portNo_2);
					pathsList.add(X+1,doc2);
				} 
		Document document3 = new Document().append(path+".0.pathsList", pathsList);
//		System.out.println("document3="+document3);
		tunnelMapCol.updateOne(tunnel, new Document("$set", document3));
		System.out.println("插入网元"+aimNe+"成功");
		
	}

	//path插入一个只连接输出端的网元,长链最底下的网元
	public static void insertNe1(int id,String aimNe,String lastNe,int X,String path) {
		
		Document query = new Document().append("_id", id);	
		Document tunnel =  tunnelMapCol.find(query).first();
		int label = Integer.valueOf(tunnel.get("label").toString());
		System.out.println("tunnel="+tunnel);

//		Document workPath = (Document) ((ArrayList) tunnel.get("workPath")).get(0);//获取该Tunnel中的workpath
//		ArrayList w_pathsList = (ArrayList) workPath.get("pathsList");
//		Document protectPath = (Document) ((ArrayList) tunnel.get("protectPath")).get(0);//获取该Tunnel中的protectPath
//		ArrayList<Document> p_pathsList = (ArrayList) protectPath.get("pathsList");
		
		Document Path = (Document) ((ArrayList) tunnel.get(path)).get(0);//获取该Tunnel中的workpath
		ArrayList wp_pathsList = (ArrayList) Path.get("pathsList");
		
		ArrayList pathsList=new ArrayList<Object>();

		for(int i=0;i<wp_pathsList.size();i++){//要把原来的pathsList里面的每一个网元都添加进去
		   pathsList.add(wp_pathsList.get(i));
		} 
		
		TopoService topo = new TopoService();
		BoardService board = new BoardService();
		
		//获得与下一个网元有光纤连接的板卡和端口
				Map<List<String>, Document> lastMap = topo.checkAdditionalTopos(lastNe, aimNe);
				Iterator  lastIt = lastMap.keySet().iterator();
				Document topo2 = null;
				while (lastIt.hasNext()) {
			        	System.out.println(lastIt.next());
			        	topo2 = lastMap.get(lastIt.next());
			        }
				double boardID_1=topo2.getDouble("boardId1");
				double boardID_2=topo2.getDouble("boardId2");
				String portName_1=topo2.getString("portName1");
				String portName_2=topo2.getString("portName2");
				String str = portName_1.substring(0);//portName_1和portName_2的PortSpeed相同
				String aimPortSpeed = null;
				if (str.equals("X")) {
					aimPortSpeed = "XGE";
				}else {
					aimPortSpeed = "GE";
				}
				
				boolean yes2 = board.isExist(aimNe, (int)boardID_1);
				if (yes2) {
					//网元的接入端
					List<Document> availableBoardList = BoardService.checkAdditionalBoards((int)boardID_1, aimPortSpeed);
					Document doc = availableBoardList.get(0);
					Double boardID1=Double.valueOf(doc.get("boardId").toString());
					String portName1= doc.get("portName").toString();
					Document doc1 = new Document().append("boardId", boardID1)
							 .append("boardNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
							 .append("boardNameRef.id", boardID1)//与boardID1相同
							 .append("label", label+2)
							 .append("neId", aimNe)
							 .append("neNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
							 .append("neNameRef.id",aimNe)//与aimNe相同
							 .append("portName", portName1)
							 .append("portNo", TopoGainAdjustRange.getIntFromString(portName1));
					pathsList.add(X,doc1);
					//网元的输出端
					Document doc2 = new Document().append("boardId", boardID_1)
												 .append("boardNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
												 .append("boardNameRef.id", boardID_1)//与boardID1相同
												 .append("label", label+2)
												 .append("neId", aimNe)
												 .append("neNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
												 .append("neNameRef.id",aimNe)//与aimNe相同
												 .append("portName", portName_1)
												 .append("portNo", TopoGainAdjustRange.getIntFromString(portName_1));
					pathsList.add(X+1,doc2);
				}else {
					//根据实际更改
					//网元的接入端
					List<Document> availableBoardList = BoardService.checkAdditionalBoards((int )boardID_2, aimPortSpeed);
					Document doc = availableBoardList.get(0);
					Double boardID1=Double.valueOf(doc.get("boardId").toString());
					String portName1= doc.get("portName").toString();
					Document doc1 = new Document().append("boardId", boardID1)
							 .append("boardNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
							 .append("boardNameRef.id", boardID1)//与boardID1相同
							 .append("label", label+2)
							 .append("neId", aimNe)
							 .append("neNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
							 .append("neNameRef.id",aimNe)//与aimNe相同
							 .append("portName", portName1)
							 .append("portNo", TopoGainAdjustRange.getIntFromString(portName1));
					pathsList.add(X,doc1);
					//网元的输出端
					Document doc2 = new Document().append("boardId", boardID_2)
												 .append("boardNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
												 .append("boardNameRef.id", boardID_2)//与boardID2相同
												 .append("label", label+2)
												 .append("neId", aimNe)
												 .append("neNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
												 .append("neNameRef.id",aimNe)//与aimNe相同
												 .append("portName", portName_2)
												 .append("portNo", TopoGainAdjustRange.getIntFromString(portName_2));
					pathsList.add(X+1,doc2);
				} 
		Document document3 = new Document().append(path+".0.pathsList", pathsList);
		System.out.println("document3="+document3);
		tunnelMapCol.updateOne(tunnel, new Document("$set", document3));
		
	}
	
	//path插入一个只连接接入端的网元,长链最底下的网元
	public static void insertNe2(int id,String aimNe,String nextNe,int X,String path) {
			
			Document query = new Document().append("_id", id);	
			Document tunnel =  tunnelMapCol.find(query).first();
			int label = Integer.valueOf(tunnel.get("label").toString());
			System.out.println("tunnel="+tunnel);

//			Document workPath = (Document) ((ArrayList) tunnel.get("workPath")).get(0);//获取该Tunnel中的workpath
//			ArrayList w_pathsList = (ArrayList) workPath.get("pathsList");
//			Document protectPath = (Document) ((ArrayList) tunnel.get("protectPath")).get(0);//获取该Tunnel中的protectPath
//			ArrayList<Document> p_pathsList = (ArrayList) protectPath.get("pathsList");
			
			Document Path = (Document) ((ArrayList) tunnel.get(path)).get(0);//获取该Tunnel中的workpath
			ArrayList wp_pathsList = (ArrayList) Path.get("pathsList");
			
			ArrayList pathsList=new ArrayList<Object>();

			for(int i=0;i<wp_pathsList.size();i++){//要把原来的pathsList里面的每一个网元都添加进去
			   pathsList.add(wp_pathsList.get(i));
			} 
			
			TopoService topo = new TopoService();
			BoardService board = new BoardService();
			
			//获得与下一个网元有光纤连接的板卡和端口
					Map<List<String>, Document> nextMap = topo.checkAdditionalTopos(nextNe, aimNe);
					Iterator  nextIt = nextMap.keySet().iterator();
					Document topo2 = null;
					while (nextIt.hasNext()) {
				        	System.out.println(nextIt.next());
				        	topo2 = nextMap.get(nextIt.next());
				        }
					double boardID_1=topo2.getDouble("boardId1");
					double boardID_2=topo2.getDouble("boardId2");
					String portName_1=topo2.getString("portName1");
					String portName_2=topo2.getString("portName2");
					String str = portName_1.substring(0);//portName_1和portName_2的PortSpeed相同
					String aimPortSpeed = null;
					if (str.equals("X")) {
						aimPortSpeed = "XGE";
					}else {
						aimPortSpeed = "GE";
					}
					
					boolean yes2 = board.isExist(aimNe, (int)boardID_1);
					if (yes2) {
						//网元的输出端
						List<Document> availableBoardList = BoardService.checkAdditionalBoards((int)boardID_1, aimPortSpeed);
						Document doc = availableBoardList.get(0);
						Double boardID1=Double.valueOf(doc.get("boardId").toString());
						String portName1= doc.get("portName").toString();
						Document doc1 = new Document().append("boardId", boardID1)
								 .append("boardNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
								 .append("boardNameRef.id", boardID1)//与boardID1相同
								 .append("label", label+2)
								 .append("neId", aimNe)
								 .append("neNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
								 .append("neNameRef.id",aimNe)//与aimNe相同
								 .append("portName", portName1)
								 .append("portNo", TopoGainAdjustRange.getIntFromString(portName1));
						pathsList.add(X+1,doc1);
						//网元的接入端
						Document doc2 = new Document().append("boardId", boardID_1)
													 .append("boardNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
													 .append("boardNameRef.id", boardID_1)//与boardID1相同
													 .append("label", label+2)
													 .append("neId", aimNe)
													 .append("neNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
													 .append("neNameRef.id",aimNe)//与aimNe相同
													 .append("portName", portName_1)
													 .append("portNo", TopoGainAdjustRange.getIntFromString(portName_1));
						pathsList.add(X,doc2);
					}else {
						//根据实际更改
						//网元的输出端
						List<Document> availableBoardList = BoardService.checkAdditionalBoards((int)boardID_2, aimPortSpeed);
						Document doc = availableBoardList.get(0);
						Double boardID1=Double.valueOf(doc.get("boardId").toString());
						String portName1= doc.get("portName").toString();
						Document doc1 = new Document().append("boardId", boardID1)
								 .append("boardNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
								 .append("boardNameRef.id", boardID1)//与boardID1相同
								 .append("label", label+2)
								 .append("neId", aimNe)
								 .append("neNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
								 .append("neNameRef.id",aimNe)//与aimNe相同
								 .append("portName", portName1)
								 .append("portNo", TopoGainAdjustRange.getIntFromString(portName1));
						pathsList.add(X+1,doc1);
						//网元的端接入
						Document doc2 = new Document().append("boardId", boardID_2)
													 .append("boardNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
													 .append("boardNameRef.id", boardID_2)//与boardID2相同
													 .append("label", label+2)
													 .append("neId", aimNe)
													 .append("neNameRef.collectionName", "Biz_Board_IdName")//数据建立时间
													 .append("neNameRef.id",aimNe)//与aimNe相同
													 .append("portName", portName_2)
													 .append("portNo", TopoGainAdjustRange.getIntFromString(portName_2));
						pathsList.add(X,doc2);
					} 
			Document document3 = new Document().append(path+".0.pathsList", pathsList);
			System.out.println("document3="+document3);
			tunnelMapCol.updateOne(tunnel, new Document("$set", document3));
			
		}
		
	
	//path删除一个网元ne:tunnel的_id,
	public static void deleteOneNe(int id,int aimNeId,String path) {
		Document query = new Document().append("_id", id);
		Document tunnel = tunnelMapCol.find(query).first();
//		Document workPath = (Document) ((ArrayList) tunnel.get("workPath")).get(0);//获取该Tunnel中的workpath
//		ArrayList w_pathsList = (ArrayList) workPath.get("pathsList");
//		Document protectPath = (Document) ((ArrayList) tunnel.get("protectPath")).get(0);//获取该Tunnel中的protectPath
//		ArrayList<Document> p_pathsList = (ArrayList) protectPath.get("pathsList");
		
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
		  System.out.println(tunnel);
		  System.out.println("删除网元成功");
	}
	
	
	//根据一条tunnel中的网元获取新板卡新端口
	public static List<BoardPort> selectNewBoardAndNewPortNameFromOneTunnel(Document tunnel,int GNeId,int CNeId,String path){
		List<BoardPort> list=new ArrayList<BoardPort>();		 
		int GNIndex = ChangeTunnelService.findNeLocationFromTunnel(tunnel, GNeId, path);
		int CNIndex = ChangeTunnelService.findNeLocationFromTunnel(tunnel, CNeId, path);
		if(GNIndex > CNIndex){
			System.out.println("GNIndex 大于 CNIndex");
			System.out.println("GNIndex:"+GNIndex);
			System.out.println("CNIndex:"+CNIndex);
			//目标网元在下,汇聚网元在上  那么就是目标入,汇聚出
			List<Board> GBoard = ChangeTunnelService.GetOldBoardIdAndPortNameplus(GNeId, tunnel, path);
			List<Board> CBoard = ChangeTunnelService.GetOldBoardIdAndPortNameplus(CNeId, tunnel, path);	
			
			//得到入口目标网元新的板卡和新的端口  
			int GNeEntranceBoard = GBoard.get(0).getBoardId(); //旧板卡
			List<Integer> newBList = NeService.findFilterFreeBoard(GNeId, GNeEntranceBoard); //得到新板卡的集合
 
			List<Integer> Blist = BoardService.SelectpossessPortNameBoardId(newBList);  //拥有端口的板卡集合
		    System.out.println("有端口新板卡目标板卡集合:"+Blist);
		    int OtherNewGBoard=0;
		    String GnewPortName=null;
		    if(Blist.size()==0){
		    	//没有新板卡,就用旧板卡 	    	
		    	OtherNewGBoard=GNeEntranceBoard;
		    	Map<Double,List<String>> Gmap = PortService.checkAdditionalPorts1(OtherNewGBoard);//发现新的板卡中新的端口
		    	List<String> slist = Gmap.get((double)OtherNewGBoard);
		    	System.out.println("旧板卡的端口为:"+slist);
		    	if(slist.size()==0){
		    		//如果旧板卡空闲端口,那就用旧端口
		    		GnewPortName=GBoard.get(0).getPortName();
					BoardPort Gboardport =new BoardPort();
					Gboardport.setNewBoard(OtherNewGBoard);
					Gboardport.setNewPortName(GnewPortName);
					Gboardport.setNeIndex(GNIndex);
					list.add(Gboardport);
		    	}
		    	else{
			        GnewPortName = Gmap.get((double)OtherNewGBoard).get(0); //获取旧板卡的第一个空闲端口
			    	System.out.println("旧板卡的第一个空闲端口名字为:GnewPortName:"+GnewPortName);
					BoardPort Gboardport =new BoardPort();
					Gboardport.setNewBoard(OtherNewGBoard);
					Gboardport.setNewPortName(GnewPortName);
					Gboardport.setNeIndex(GNIndex);
					list.add(Gboardport);
		    	}
 	    	
		    }else{
				OtherNewGBoard = Blist.get(0);//获取第一个板卡  固定
				System.out.println("第一个新板卡为:"+OtherNewGBoard);		
				Map<Double,List<String>> Gmap = PortService.checkAdditionalPorts1(OtherNewGBoard);//发现新的板卡中新的端口
				List<String> slist = Gmap.get((double)OtherNewGBoard);
				System.out.println("第一个新板卡的空闲端口集合:"+slist);				
				GnewPortName = Gmap.get((double)OtherNewGBoard).get(0); //获取第一个板卡的第一个端口
				System.out.println("第一个新板卡的第一个端口名字为:GnewPortName:"+GnewPortName);								
				BoardPort Gboardport =new BoardPort();
				Gboardport.setNewBoard(OtherNewGBoard);
				Gboardport.setNewPortName(GnewPortName);
				Gboardport.setNeIndex(GNIndex);
				list.add(Gboardport);	    	
		    }
 
//			System.out.println("------------------------------分割线------------------------------------------");
								
			//得到汇聚网元出口新的板卡和新的端口
			int CNeExportBoard = CBoard.get(1).getBoardId();
			List<Integer> newCList = NeService.findFilterFreeBoard(CNeId, CNeExportBoard);
			List<Integer> Clist = BoardService.SelectpossessPortNameBoardId(newCList);  //拥有端口的板卡集合
	        System.out.println("汇聚新板卡板卡集合"+Clist);
			int OtherNewCBoard =0;   //选中第一个板卡
			String CnewPortName = null;
			if(Clist.size()==0){
				//没有新板卡就用旧板卡
				OtherNewCBoard = CNeExportBoard;
				Map<Double,List<String>> Cmap = PortService.checkAdditionalPorts1(OtherNewCBoard);
				List<String> clist = Cmap.get((double)OtherNewCBoard);
				System.out.println("汇聚旧板卡的端口集合:"+clist);
				CnewPortName = Cmap.get((double)OtherNewCBoard).get(0);  // 选中第一个板卡中的第一个的端口
				System.out.println("汇聚旧板卡的空闲端口名字为:CnewPortName:"+CnewPortName);
				BoardPort Cboardport =new BoardPort();
				Cboardport.setNewBoard(OtherNewCBoard);
				Cboardport.setNewPortName(CnewPortName);
				Cboardport.setNeIndex(CNIndex);
				list.add(Cboardport);
				
			}else{
				OtherNewCBoard = Clist.get(0);
				System.out.println("汇聚新板卡为:"+OtherNewCBoard);
				Map<Double,List<String>> Cmap = PortService.checkAdditionalPorts1(OtherNewCBoard);
				List<String> clist = Cmap.get((double)OtherNewCBoard);
	            System.out.println("汇聚第一个新板卡的端口集合:"+clist);
				CnewPortName = Cmap.get((double)OtherNewCBoard).get(0);  // 选中第一个板卡中的第一个的端口
				System.out.println("汇聚第一个板卡的第一个端口名字为:CnewPortName:"+CnewPortName);				
				BoardPort Cboardport =new BoardPort();
				Cboardport.setNewBoard(OtherNewCBoard);
				Cboardport.setNewPortName(CnewPortName);
				Cboardport.setNeIndex(CNIndex);
				list.add(Cboardport);							
			}
			 
			TopoLines.addTopo(OtherNewGBoard, GnewPortName, OtherNewCBoard, CnewPortName); // 在中间网元与汇聚网元之间加上光纤  汇聚出 目标进
			return list;
		}
			
		else if(GNIndex < CNIndex){
			
			System.out.println("GNIndex 小于 CNIndex");
			System.out.println("GNIndex:"+GNIndex);
			System.out.println("CNIndex:"+CNIndex);
			//目标网元在上,汇聚网元在下  那么就是目标出,汇聚入
			List<Board> GBoard = ChangeTunnelService.GetOldBoardIdAndPortNameplus(GNeId, tunnel, path);
			List<Board> CBoard = ChangeTunnelService.GetOldBoardIdAndPortNameplus(CNeId, tunnel, path);	
			
			//得到目标网元出口新的板卡和新的端口
			int GNeEntranceBoard = GBoard.get(1).getBoardId();//获取旧板卡
			List<Integer> newBList = NeService.findFilterFreeBoard(GNeId, GNeEntranceBoard); //得到新板卡的集合
			List<Integer> Blist = BoardService.SelectpossessPortNameBoardId(newBList);  //拥有端口的板卡集合
		    int OtherNewGBoard=0;
		    String GnewPortName=null;
			if(Blist.size()==0){
				//没有新板卡,就用旧板卡 	    	
		      OtherNewGBoard=GNeEntranceBoard;
		      Map<Double,List<String>> Gmap = PortService.checkAdditionalPorts1(OtherNewGBoard);//发现新的板卡中新的端口
		      List<String> slist = Gmap.get((double)OtherNewGBoard);
		      if(slist.size()==0){
		    	  //旧板卡没有新端口就用就端口
		    	GnewPortName = GBoard.get(1).getPortName();
				BoardPort Gboardport =new BoardPort();
				Gboardport.setNewBoard(OtherNewGBoard);
				Gboardport.setNewPortName(GnewPortName);
				Gboardport.setNeIndex(GNIndex);
				list.add(Gboardport);
		    	  
		      }else{
		    	  //就用旧板卡中的新的空闲端口
				  GnewPortName = Gmap.get((double)OtherNewGBoard).get(0);//获取旧板卡的第一个端口
				  System.out.println("旧板卡的第一个空闲端口名字为:GnewPortName:"+GnewPortName);			
				  BoardPort Gboardport =new BoardPort();
				  Gboardport.setNewBoard(OtherNewGBoard);
				  Gboardport.setNewPortName(GnewPortName);
				  Gboardport.setNeIndex(GNIndex);
				  list.add(Gboardport);	
		      }		      		
			}else{
				//有新板卡时
				OtherNewGBoard = Blist.get(0);//获取第一个板卡
				Map<Double,List<String>> Gmap = PortService.checkAdditionalPorts1(OtherNewGBoard);//发现新的板卡中新的端口
				List<String> slist = Gmap.get((double)OtherNewGBoard);
				System.out.println("第一个新板卡的可用端口集合:"+slist);
				GnewPortName = Gmap.get((double)OtherNewGBoard).get(0);//获取第一个板卡的第一个端口
				System.out.println("第一个新板卡的第一个端口名字为:GnewPortName:"+GnewPortName);				
				BoardPort Gboardport =new BoardPort();
				Gboardport.setNewBoard(OtherNewGBoard);
				Gboardport.setNewPortName(GnewPortName);
				Gboardport.setNeIndex(GNIndex);
				list.add(Gboardport);
			}		
//			System.out.println("------------------------------分割线------------------------------------------");
								
			//得到汇聚网元入口新的板卡和新的端口
			int CNeExportBoard = CBoard.get(0).getBoardId();
			List<Integer> newCList = NeService.findFilterFreeBoard(CNeId, CNeExportBoard);
			List<Integer> Clist = BoardService.SelectpossessPortNameBoardId(newCList);  //拥有端口的板卡集合
	        System.out.println("汇聚层可用板卡的集合"+Clist);
		    int OtherNewCBoard=0;
		    String CnewPortName=null;
	        if(Clist.size()==0){
				//没有新板卡,就用旧板卡 	    	
	        	  OtherNewCBoard=CNeExportBoard;
			      Map<Double,List<String>> Cmap = PortService.checkAdditionalPorts1(OtherNewCBoard);//发现旧的板卡中空闲的端口
			      List<String> slist = Cmap.get((double)OtherNewCBoard);
				  System.out.println("汇聚旧板卡的可用端口集合:"+slist);
				  if(slist.size()==0){
					  //旧板卡没有新的空闲端口就用旧的端口
					  CnewPortName = CBoard.get(0).getPortName();
					  System.out.println("汇聚旧板卡的旧端口名字为:GnewPortName:"+CnewPortName);			
					  BoardPort Cboardport =new BoardPort();
					  Cboardport.setNewBoard(OtherNewGBoard);
					  Cboardport.setNewPortName(CnewPortName);
					  Cboardport.setNeIndex(CNIndex);
					  list.add(Cboardport);						  					  
				  }else{
					  CnewPortName = Cmap.get((double)OtherNewCBoard).get(0); //旧板卡上有空闲端口
					  BoardPort Cboardport =new BoardPort();
					  Cboardport.setNewBoard(OtherNewCBoard);
					  Cboardport.setNewPortName(CnewPortName);
					  Cboardport.setNeIndex(CNIndex);
					  list.add(Cboardport);
					  				  
				  }
	        }else{
				OtherNewCBoard = Clist.get(0);   //获取第一个板卡
				Map<Double,List<String>> Cmap = PortService.checkAdditionalPorts1(OtherNewCBoard);
				List<String> clist = Cmap.get((double)OtherNewCBoard);
	            System.out.println("汇聚新板卡板卡的可用端口集合:"+clist);
	            CnewPortName = Cmap.get((double)OtherNewCBoard).get(0); //	获取第一个板卡的第一个端口
				System.out.println("汇聚新板卡的第一个端口名字为:CnewPortName"+CnewPortName);					
				BoardPort Cboardport =new BoardPort();
				Cboardport.setNewBoard(OtherNewCBoard);
				Cboardport.setNewPortName(CnewPortName);
				Cboardport.setNeIndex(CNIndex);
				list.add(Cboardport);
	        	
	        }
	        			
			TopoLines.addTopo(OtherNewGBoard, GnewPortName, OtherNewCBoard, CnewPortName); // 在中间网元与汇聚网元之间加上光纤  汇聚出 目标进
			return list;				
		}
		return list;
	
	}
	
	
	
	
	
	
	
	
//
	//更新一个网元的boardID和portName
	public static void updateNe(int id,int aimNeId,int newBoardId,String NewPortName,int X,String path) {
		System.out.println("chuanjinglaid X为："+X);
		System.out.println("传进来的板卡ID为"+newBoardId);
		System.out.println("传进来的端口名字为"+NewPortName);  //GE_3
		
//		//chuanjinglaid X为：8
//		传进来的板卡ID为100674743
//		传进来的端口名字为GE_3
		Document query = new Document().append("_id", id);
		Document tunnel = tunnelMapCol.find(query).first();		
		Document Path = (Document) ((ArrayList) tunnel.get(path)).get(0);//获取该Tunnel中的workpath
		ArrayList wp_pathsList = (ArrayList) Path.get("pathsList");
		
		Document ne = (Document) wp_pathsList.get(X);
		double oldBoardId = Double.valueOf(ne.get("boardId").toString());
		System.out.println("旧的板卡id:   "+oldBoardId);
		
		String oldPortName = ne.get("portName").toString();
		System.out.println("旧的端口名字：   "+oldPortName);
		int oldPortNo = (Integer) ne.get("portNo");
		System.out.println("旧的端口号：   "+oldPortNo);
		
		String[] strArray = NewPortName.split("_");  //		传进来的端口名字为GE_3
	 
		int NewPortNo = Integer.valueOf(strArray[strArray.length-1].toString());
		System.out.println("NewPortNo:"+NewPortNo);
		
		Document NewboardID = new Document("$set",new Document(path+".0.pathsList."+X+".boardId", Double.valueOf(newBoardId)));		
		Bson OldboardID= Filters.eq(path+".0.pathsList."+X+".boardId", Double.valueOf(oldBoardId));		
		tunnelMapCol.updateOne(OldboardID, NewboardID);//更新板卡boardId
		
		Document NewportName= new Document("$set",new Document(path+".0.pathsList."+X+".portName", NewPortName));
		Bson OldportName= Filters.eq(path+".0.pathsList."+X+".portName",oldPortName);		
		tunnelMapCol.updateOne(OldportName, NewportName);//更新端口名	
		
		Document NewportNo= new Document("$set",new Document(path+".0.pathsList."+X+".portNo", Integer.valueOf(NewPortNo)));
		Bson OldportNo= Filters.eq(path+".0.pathsList."+X+".portNo", Integer.valueOf(oldPortNo));
		tunnelMapCol.updateOne(OldportNo, NewportNo);//更新端口号		
	}
	
	/**
	//查询和修改Tunnel
	public static boolean selecTunnels1(Double findBoardID,String portName1 ) {
		
		List<Object> tunnelList = new ArrayList<Object>();
		Bson filters = Filters.eq("protectPath.pathsList.boardId",Double.valueOf(findBoardID));
		filters= Filters.and(filters,Filters.eq("protectPath.pathsList.portNo", portName1));
		FindIterable<Document> findIterable = tunnelMapCol.find(filters);
		MongoCursor<Document> cursor = findIterable.iterator();
		
		//环为：汇聚1——A——B——C——D——汇聚2，链为C——E——F，环与链的连接网元为C，在AF之间加光纤
//		TopoService topoService = new TopoService();
//		TopoService.addTopo1( 222222222, "GE_1", 333333333, "GE_3");
		NeService nel=new NeService();
		int A_neid = nel.ringFindNeId().get(0);//网元a的neid
		int B_neid = nel.ringFindNeId().get(1);//网元B的neid
//		int C_neid = nel.ringFindNeId(neMapCol).get(2);//网元C的neid
		int C_neid = nel.longChainFindNeId().get(0);//网元C的neid,网元C为环与链的连接网元，处于长链表中的第一个
		int E_neid = nel.longChainFindNeId().get(1);//网元E的neid,链上的网元
		int F_neid = nel.longChainFindNeId().get(2);//网元F的neid，链上的网元
		
		while (cursor.hasNext()) {
			Document tunnel= cursor.next();
			System.out.println(tunnel);
			int neid = (Integer) tunnel.get("protectPath.pathsList.0.neid");//获取tunnel第一个网元的neid
			//判断网元B是不是tunnel的起始网元,是起始网元则删除此tunnel,退出此次循环
			//可优化
			if (neid==B_neid) {
				tunnelMapCol.deleteOne(tunnel);
				continue;
			}
			//判断网元E是不是tunnel的起始网元，是则将E-(C2,C3)-B-A修改为E-F(16-3,16-2)-A
			if (neid==E_neid) {
				//删除B、C 网元，插入F网元
				//可优化
				//deleteOneNe(B_neid);
				//deleteOneNe(C_neid);
				//insertOneNe(F_neid);
				continue;
			}
			//判断网元F是不是tunnel的起始网元，是则将F(17-2,16-3)-E-C-B-A修改为F(17-2,16-2)-A
			if (neid==F_neid) {
				//删除E、C、B，修改F的输出端的boardId和portNo
				//可优化
				//deleteOneNe(E_neid);
				//deleteOneNe(B_neid);
				//deleteOneNe(C_neid);
				//updateNe( F_neid,333333333,3);
				continue;
			}
			//如果B、E、F都不是起始网元，则tunnel必过C，将D-(C1,C3)-B-A(17-2,16-1)改为D-(C1,C2)-E-F(16-3,16-2)-A(17-2,16-1)
			if (neid==F_neid) {
				//删除B网元，修改C网元输出端的boardId和portNo，插入E、F网元
				//可优化
				//deleteOneNe(B_neid);
				//updateNe( C_neid,444444444,4);
				//insertOneNe(E_neid);
				//insertOneNe(F_neid);
				continue;
			}
			
//			tunnelList.add(tunnel);
		}
		return true;
		}

	//查询和修改Tunnel,A和F之间有光纤
	public static boolean selecTunnels2(String A_neName,String F_neName,Double A_BoardID,String A_portName,
			List<String> longchain,List<String> ring,String connectionNeName ) {
			
		//在protectPath上查找
			List<Object> tunnelList = new ArrayList<Object>();
			Bson filters = Filters.eq("protectPath.pathsList.boardId",Double.valueOf(A_BoardID));
			filters= Filters.and(filters,Filters.eq("protectPath.pathsList.portName", A_portName));
			FindIterable<Document> findIterable = tunnelMapCol.find(filters);
			MongoCursor<Document> cursor = findIterable.iterator();
			
			int A_index = ring.indexOf(A_neName);//A网元在环上的位置
			int connection_index = ring.indexOf(connectionNeName);//交叉网元在环上的位置
			int F_index = longchain.indexOf(F_neName);//F网元在链上的位置
			NeService nel = new NeService();
			while (cursor.hasNext()) {
				Document tunnel = cursor.next();
				int neid = (Integer) tunnel.get("protectPath.pathsList.0.neid");//获取tunnel第一个网元的neid
				if (A_index < connection_index) {//A网元位于交叉网元的左侧
					for (int i = 0; i < A_index; i++) {//环上A网元之前的网元
						if (neid == nel.findNeId(ring.get(i))) {//删除A与交叉网元之间的所有网元，插入F以及F与交叉网元之间的所有网元
							
						}
					}
					for (int i = A_index+1; i < connection_index; i++) {//环上A网元和交叉网元之间的网元
						
					}
					for (int i = connection_index+1; i < ring.size(); i++) {//环上交叉网元之后的网元
						
					}
					for (int i = 0; i < F_index; i++) {//链上F网元之前的网元
						
					}
					for (int i = F_index+1; i < longchain.size(); i++) {//链上F网元之后的网元
						
					}
					
				}
				if (A_index > connection_index) {//A网元位于交叉网元的右侧
					for (int i = A_index; i < ring.size(); i++) {//环上A网元之后的网元
						
					}
					for (int i = A_index-1; i >connection_index; i--) {//环上A网元和交叉网元之间的网元
						
					}
					for (int i = connection_index-1; i >=0; i--) {//环上交叉网元之前的网元
						
					}
					for (int i = 0; i < F_index; i++) {//链上F网元之前的网元
						
					}
					for (int i = F_index+1; i < longchain.size(); i++) {//链上F网元之后的网元
						
					}
				}
				
			}
			
		return true;	
	}	*/		

	//查找到Path所有符合条件的tunnel,path为protectPath和workPath
	public static List<Object> selecTunnels(Double findBoardID,String portName1,String path) {
		
		List<Object> tunnelList = new ArrayList<Object>();
		Bson filters = Filters.eq(path+".pathsList.boardId",Double.valueOf(findBoardID));
		filters= Filters.and(filters,Filters.eq(path+".pathsList.portName", portName1));
		FindIterable<Document> findIterable = tunnelMapCol.find(filters);
		MongoCursor<Document> cursor = findIterable.iterator();
		while (cursor.hasNext()) {
			Document tunnel= cursor.next();
//			System.out.println("tunnel="+tunnel);
//			Document ne = tunnel.append(path+".pathsList.neId",134217731);
//			System.out.println(tunnel);
//			System.out.println("ne="+ne);
			tunnelList.add(tunnel);
		}
		System.out.println("tunnelList="+tunnelList);
		return tunnelList;
	}
	
	//切换业务
	public static boolean changeTunnel(String A_neName,String F_neName,List<String> longchain,List<String> ring,String connectionNeName) {
		
		int A_index = ring.indexOf(A_neName);//A网元在环上的位置
		int connection_index = ring.indexOf(connectionNeName);//交叉网元在环上的位置
		int F_index = longchain.indexOf(F_neName);//F网元在链上的位置
		TopoService topoService = new TopoService();
		BoardService boardService = new BoardService();
		if (A_index < connection_index) {//网元A位于交叉网元的左侧
			Map<List<String>, Document> topoResult = topoService.checkAdditionalTopos(A_neName, ring.get(A_index+1));
			Document topo = null ;
			Iterator  it = topoResult.keySet().iterator();
	        while (it.hasNext()) {
	        	System.out.println(it.next());
	        	topo = topoResult.get(it.next());//获得neName1和neName2之间的光纤信息
	        }
		    double boardID1=topo.getDouble("boardId1");
		    double boardID2=topo.getDouble("boardId2");
			String portName1=topo.getString("portName1");
			String portName2=topo.getString("portName2");
			boolean yes = boardService.isExist(A_neName, (int)boardID1);
			if (yes) {//boardID1在A_neName
				//在workPath查找
				List<Object> tunnelList1 = TunnelService.selecTunnels(boardID1, portName1, "workPath");//工作路径上的tunnel
				for (int i = 0; i < tunnelList1.size(); i++) {
					Document tunnel = (Document) tunnelList1.get(i);
					//int _id = (int) tunnel.get("_id");
					//int firstnNeId = (int) tunnel.get("workPath.pathsList.0.neId");
					int _id = Integer.valueOf( tunnel.get("_id").toString());
					int firstnNeId = Integer.valueOf(tunnel.get("workPath.pathsList.0.neId").toString());
					int count = 0;
					for (int j = 0; j < A_index+1; j++) {//网元A及网元A之前的网元
						int neId = NeService.findNeId(ring.get(i));
						if (firstnNeId == neId) {//网元A之前的为tunnel的起点
							//删除A和交叉网元之间的网元，更新A和交叉网元的板卡和端口，插入F和交叉网元之间的网元
							for (int k = A_index+1; k < connection_index; k++) {
								//删除
								TunnelService.deleteOneNe(_id, NeService.findNeId(ring.get(k)), "workPath");
							}
							Map<List<String>, Document> topoMap =topoService.checkAdditionalTopos(connectionNeName, longchain.get(longchain.size()-1));
							Document topo1 = null ;
							Iterator  it1 = topoResult.keySet().iterator();
					        while (it1.hasNext()) {
					        	System.out.println(it1.next());
					        	topo1 = topoResult.get(it1.next());//获得neName1和neName2之间的光纤信息
					        }
						    double boardID_1=topo.getDouble("boardId1");
						    double boardID_2=topo.getDouble("boardId2");
							String portName_1=topo.getString("portName1");
							String portName_2=topo.getString("portName2");
							boolean yes1 = boardService.isExist(connectionNeName, (int)boardID_1);
							if (yes1) {
								//更新
								TunnelService.updateNe(_id, NeService.findNeId(connectionNeName),(int)boardID_1, portName_1, 2*count, "workPath");
							}else {
								//更新
								TunnelService.updateNe(_id, NeService.findNeId(connectionNeName),(int)boardID_2, portName_2, 2*count,"workPath");
							}
							count = A_index+1;
							TunnelService.insertOneNe(_id, F_neName, A_neName, longchain.get(F_index+1), count*2, "workPath");
							for (int k = F_index+1; k < longchain.size(); k++) {
								//插入
								count = count+1;
								TunnelService.insertOneNe(_id, longchain.get(k), longchain.get(k-1), longchain.get(k+1), count*2, "workPath");
								
							}							
						}					
					}
					for (int j = A_index+1; j < connection_index; j++) {//网元A与交叉网元之间的网元为tunnel的起点
						int neId = NeService.findNeId(ring.get(i));
						if (firstnNeId == neId) {//网元A与交叉网元之间的网元为tunnel的起点
						//删除此网元与A网元之间的网元
						//修改此网元的输出端板卡和端口
						//插入此网元与交叉网元之间的网元
						//插入检查网元
						//插入交叉网元与F网元之间的网元
						//插入F网元
						//修改A网元的接入端
							for (int k = A_index+1; k < j; k++) {
								//删除
								TunnelService.deleteOneNe(_id, NeService.findNeId(ring.get(k)), "workPath");
							}
							Map<List<String>, Document> topoMap = topoService.checkAdditionalTopos(ring.get(i), ring.get(i+1));
							Document topo1 = null ;
							Iterator  it1 = topoResult.keySet().iterator();
					        while (it1.hasNext()) {
					        	System.out.println(it1.next());
					        	topo1 = topoResult.get(it1.next());//获得neName1和neName2之间的光纤信息
					        }
						    double boardID_1=topo.getDouble("boardId1");
						    double boardID_2=topo.getDouble("boardId2");
							String portName_1=topo.getString("portName1");
							String portName_2=topo.getString("portName2");
							boolean yes1 = boardService.isExist(connectionNeName, (int)boardID_1);
							if (yes1) {
								//更新
								TunnelService.updateNe(_id, neId, (int)boardID_1, portName_1,2*count, "workPath");
							}else {
								//更新
								TunnelService.updateNe(_id, neId, (int)boardID_1, portName_1, 2*count,"workPath");
							}
							count = 1;
							//插入
							for (int k = i+1; k < connection_index; k++) {
								count++;
								TunnelService.insertOneNe(_id, ring.get(k), ring.get(k-1), ring.get(k+1), count*2, "workPath");
							}
							//插入
							count++;
							TunnelService.insertOneNe(_id, connectionNeName, ring.get(connection_index-1), longchain.get(longchain.size()-1), count*2, "workPath");
							count++;
							TunnelService.insertOneNe(_id, longchain.get(longchain.size()-1), connectionNeName, longchain.get(longchain.size()-2), count*2, "workPath");
							for (int k = longchain.size()-2; k < F_index; k--) {
								count++;
								TunnelService.insertOneNe(_id, longchain.get(k), longchain.get(k+1), longchain.get(k-1), count*2, "workPath");
							}
							count++;
							TunnelService.insertOneNe(_id, F_neName, longchain.get(F_index+1), A_neName, count*2, "workPath");
							Map<List<String>, Document> TopoMap = topoService.checkAdditionalTopos(F_neName, A_neName);
							Document topo2= null ;
							Iterator  it2 = topoResult.keySet().iterator();
					        while (it2.hasNext()) {
					        	System.out.println(it2.next());
					        	topo1 = topoResult.get(it2.next());//获得neName1和neName2之间的光纤信息
					        }
						    double boardID1_1=topo.getDouble("boardId1");
						    double boardId2_2=topo.getDouble("boardId2");
							String portName1_1=topo.getString("portName1");
							String portName2_2=topo.getString("portName2");
							boolean yes2 = boardService.isExist(connectionNeName, (int)boardID_1);
						}
					}
					for (int j = connection_index; j < ring.size(); j++) {
						
					}
					for (int j = 0; j <=  F_index; j++) {
						
					}
					for (int j = F_index+1; j < longchain.size(); j++) {
						
					}
				}
				//在protectPath查找
				List<Object> tunnelList2 = TunnelService.selecTunnels(boardID1, portName1, "protectPath");//保护路径 上的tunnel
			}else {//boardID2在A_neName
				
			}
		}
		if (A_index > connection_index) {//网元A位于交叉网元的右侧
			
		}
		
		return false;
	}
	
	
	//切换业务(一条信息加光纤的业务切换),A网元和F网元加光纤的板卡和端口
	public static boolean oneLongChainChangeTunnel(String A_neName,int A_boardId,String A_portName,String F_neName,int F_boardId,String F_portName,List<String> longchain,List<String> ring,String connectionNeName) {
		
		System.out.println("A_neName="+A_neName);
		int A_index = ring.indexOf(A_neName);//A网元在环上的位置
		int connection_index = ring.indexOf(connectionNeName);//交叉网元在环上的位置
//		System.out.println("A_index="+A_index);
//		System.out.println("connection_index="+connection_index);
		//int F_index = longchain.indexOf(F_neName);//F网元在链上的位置
		
		//获得A_neName与connectionNeName连接的板卡和端口，才能查找tunnel
		TopoService topo = new TopoService();
		BoardService board = new BoardService();
		Map<List<String>, Document> topoMap = topo.checkAdditionalTopos(A_neName, connectionNeName);
//			System.out.println("topoMap="+topoMap);
		List<String> list = new ArrayList<String>();
		list.add(A_neName);
		list.add(connectionNeName);
		Document topoDoc = topoMap.get(list);//根据网元组成的list获得光纤信息
			System.out.println("topoDoc="+topoDoc);
		double boardId1 = Double.valueOf(topoDoc.get("boardId1").toString());
		String portName1 = topoDoc.get("portName1").toString();
		double boardId2 = Double.valueOf(topoDoc.get("boardId2").toString());
		String portName2 = topoDoc.get("portName2").toString();
		System.out.println("boardId1:"+(int)boardId1);
		System.out.println("boardId2:"+(int)boardId2);
		boolean bl = board.isExist(A_neName, (int)boardId1);//判断boardId1是否在A_neName上
		System.out.println("bl="+bl);
		double boardId;
		String portName;
		if (bl) {
			boardId = boardId1;
			portName= portName1;
			}else {
			boardId= boardId2;
			portName= portName2;
			}
		System.out.println("boardId:"+(int)boardId);
		System.out.println("portName:"+portName);
		System.out.println();
		
		Map<List<String>, Document> topoMap1 = topo.checkAdditionalTopos(longchain.get(longchain.size()-1), connectionNeName);
		System.out.println("topoMap1="+topoMap1);
		List<String> list1 = new ArrayList<String>();
		list1.add(longchain.get(longchain.size()-1));
		list1.add(connectionNeName);
		Document topoDoc1= topoMap1.get(list1);//根据网元组成的list1获得光纤信息
		System.out.println("topoDoc1="+topoDoc1);
		double boardId1_c = Double.valueOf(topoDoc1.get("boardId1").toString());
		String portName1_c = topoDoc1.get("portName1").toString();
		double boardId2_c = Double.valueOf(topoDoc1.get("boardId2").toString());
		String portName2_c = topoDoc1.get("portName2").toString();
		System.out.println("boardId1_c:"+(int)boardId1_c);
		System.out.println("boardId2_c:"+(int)boardId2_c);
		boolean bl_c = board.isExist(connectionNeName, (int)boardId1);//判断boardId1_c是否在connectionNeName上
		System.out.println("bl_c="+bl_c);
		double boardId_c;
		String portName_c;
		if (bl_c) {
			boardId_c = boardId1_c;
			portName_c= portName1_c;
			}else {
			boardId_c= boardId2_c;
			portName_c= portName2_c;
			}
		System.out.println("boardId_c:"+(int)boardId_c);
		System.out.println("portName_c="+portName_c);
		
//		List<Object> tunnelList1 = TunnelService.selecTunnels(boardId, portName, "workPath");//查找workPath上的tunnel
//		List<Object> tunnelList2 = TunnelService.selecTunnels(boardId, portName, "protectPath");//查找protectPath上的tunnel
		//判断A_neName是在connectionNeName左侧还是右侧
		System.out.println("进入修改中");
		System.out.println();
		if (A_index < connection_index) {//A_neName是在connectionNeName左侧
			
			
			System.out.println(1);
			System.out.println(2);
			System.out.println(3);
			System.out.println(4);
			System.out.println(5);
			System.out.println("workPath");
			//查找workPath上的tunnel
			List<Object> tunnelList1 = TunnelService.selecTunnels(boardId, portName, "workPath");
			System.out.println("tunnelList1="+tunnelList1);
			for (int i = 0; i < tunnelList1.size(); i++) {//遍历所有的tunnel
				Document tunnel = (Document) tunnelList1.get(i);
//				System.out.println("tunnel="+tunnel);
				int _id = Integer.valueOf( tunnel.get("_id").toString());
				System.out.println("_id:"+_id);
				
				Document workPath = (Document) ((ArrayList) tunnel.get("workPath")).get(0);//获取该Tunnel中的workpath
				ArrayList w_pathsList = (ArrayList) workPath.get("pathsList");
				int firstnNeId = Integer.valueOf( ((Document)w_pathsList.get(0)).get("neId").toString());
//				System.out.println("firstnNeId="+firstnNeId);
				for (int j = 0; j < ring.size(); j++) {
					int neId = new NeService().findNeId(ring.get(j));
					if (firstnNeId == neId) {
						System.out.println("firstnNeId="+firstnNeId);
						System.out.println("neId:"+neId);
						System.out.println("j="+j);
						System.out.println("YES");
						//判断neId位于A_neNamede的左侧还是右侧，从而确定tunnel的走向
						if (j <= A_index) {//tunel的起点位于A的左侧
							System.out.println("j <= A_index");
							//修改A网元的输出端
							//插入长链上的网元
							//修改交叉网元的输入端
							int count =A_index-j+1;//
							TunnelService.updateNe(_id, NeService.findNeId(A_neName), A_boardId, A_portName,2*(count-1)+1, "workPath");
							count++;
							for (int k = 0; k < longchain.size(); k++) {
								if (k==0) {//插入F网元
									TunnelService.insertOneNe(_id, F_neName, A_neName,longchain.get(1), 2*(count-1), "workPath");
									count++;
								}
								if (k != 0 && k != longchain.size()-1) {
									TunnelService.insertOneNe(_id, longchain.get(k), longchain.get(k-1), longchain.get(k+1), 2*(count-1), "workPath");
									count++;
								}
								if (k == longchain.size()-1) {//插入与长链上与交叉网元连接的最后一个网元
									TunnelService.insertOneNe(_id, longchain.get(k), longchain.get(k-1), connectionNeName, 2*(count-1), "workPath");
									count++;
								}
							}
							TunnelService.updateNe(_id, NeService.findNeId(connectionNeName), (int)boardId_c, portName_c, 2*(count-1),"workPath");
							
						}
						if (j > A_index) {//tunel的起点位于A的右侧
							System.out.println("j > A_index");
							//修改交叉网元的输出端
							//插入长链上的网元
							//修改A网元的接入端
							int count = j-connection_index+1;
//							System.out.println("count="+count);
						
//							System.out.println("_id:"+_id);
//							System.out.println("NeService.findNeId(connectionNeName):"+NeService.findNeId(connectionNeName));
//							System.out.println("boardId_c:"+(int)boardId_c);
//							System.out.println("portName_c:"+portName_c);
//							System.out.println("2*(count-1)+1:"+(2*(count-1)+1));
							TunnelService.updateNe(_id, NeService.findNeId(connectionNeName), (int)boardId_c, portName_c, 2*(count-1)+1, "workPath");
							count++;
//							System.out.println("count:"+count);
							for (int k = longchain.size()-1; k >=0; k--) {
								if (k==longchain.size()-1) {
									System.out.println("longchain.size()-1:"+(longchain.size()-1));
									System.out.println("k="+k);
									
//									System.out.println("_id:"+_id);
//									System.out.println("longchain.get(k):"+longchain.get(k));
//									System.out.println("connectionNeName:"+connectionNeName);
//									System.out.println("longchain.get(k-1):"+longchain.get(k-1));
//									System.out.println("2*(count-1):"+2*(count-1));
//									System.out.println();
									TunnelService.insertOneNe(_id, longchain.get(k), connectionNeName, longchain.get(k-1), 2*(count-1), "workPath");
									count++;
								}
								if (k!=longchain.size()-1 && k!=0 ) {
									System.out.println("k="+k);
									TunnelService.insertOneNe(_id, longchain.get(k), longchain.get(k+1), longchain.get(k-1),  2*(count-1), "workPath");
									count++;
								}
								if (k==0) {
									System.out.println("k="+k);
									TunnelService.insertOneNe(_id, longchain.get(k), longchain.get(k+1), A_neName,2*(count-1),"workPath");
									count++;
								}
							}
							TunnelService.updateNe(_id, NeService.findNeId(A_neName), (int)boardId, portName,2*(count-1),"workPath");
						}
						break;
					}
				}
			}
			System.out.println(1);
			System.out.println(2);
			System.out.println(3);
			System.out.println(4);
			System.out.println(5);
			System.out.println("protectPath");
			//查找protectPath上的tunnel
			List<Object> tunnelList2 = TunnelService.selecTunnels(boardId, portName, "protectPath");
			for (int i = 0; i < tunnelList2.size(); i++) {//遍历所有的tunnel
				Document tunnel = (Document) tunnelList2.get(i);
				int _id = Integer.valueOf( tunnel.get("_id").toString());
				Document protectPath = (Document) ((ArrayList) tunnel.get("protectPath")).get(0);//获取该Tunnel中的workpath
				ArrayList p_pathsList = (ArrayList) protectPath.get("pathsList");
				int firstnNeId = Integer.valueOf( ((Document)p_pathsList.get(0)).get("neId").toString());
				for (int j = 0; j < ring.size(); j++) {
					int neId = new NeService().findNeId(ring.get(j));
					if (firstnNeId == neId) {
						//判断neId位于A_neName的左侧还是右侧，从而确定tunnel的走向
						if (j <= A_index) {//tunel的起点位于A的左侧
							//修改A网元的输出端
							//插入长链上的网元
							//修改交叉网元的输入端
							int count =A_index-j+1;//
							TunnelService.updateNe(_id, NeService.findNeId(A_neName), A_boardId, A_portName,2*(count-1)+1, "protectPath");
							count++;
							for (int k =0; k < longchain.size(); k++) {
								if (k==0) {//插入F网元
//									_id, F_neName, A_neName,longchain.get(1), 2*(count-1), "protectPath"
//									System.out.println("");
//									System.out.println();
//									System.out.println();
//									System.out.println();
									TunnelService.insertOneNe(_id, F_neName, A_neName,longchain.get(1), 2*(count-1), "protectPath");
									count++;
								}
								if (k != 0 && k != longchain.size()-1) {
									TunnelService.insertOneNe(_id, longchain.get(k), longchain.get(k-1), longchain.get(k+1), 2*(count-1), "protectPath");
									count++;
								}
								if (k == longchain.size()-1) {//插入与长链上与交叉网元连接的最后一个网元
									TunnelService.insertOneNe(_id, longchain.get(k), longchain.get(k-1), connectionNeName, 2*(count-1), "protectPath");
									count++;
								}
							}
							TunnelService.updateNe(_id, NeService.findNeId(connectionNeName), (int)boardId_c, portName_c, 2*(count-1),"protectPath");
							
						}
						if (j > A_index) {//tunel的起点位于A的右侧
							//修改交叉网元的输出端
							//插入长链上的网元
							//修改A网元的接入端
							int count = j-connection_index+1;
							TunnelService.updateNe(_id, NeService.findNeId(connectionNeName), (int)boardId_c, portName_c, 2*(count-1)+1, "protectPath");
							count++;
							for (int k = longchain.size()-1; k >=0; k--) {
								if (k==longchain.size()-1) {
									TunnelService.insertOneNe(_id, longchain.get(k), connectionNeName, longchain.get(k-1), 2*(count-1), "protectPath");
									count++;
								}
								if (k!=longchain.size()-1 && k!=0 ) {
									TunnelService.insertOneNe(_id, longchain.get(k), longchain.get(k+1), longchain.get(k-1),  2*(count-1), "protectPath");
									count++;
								}
								if (k==0) {
									TunnelService.insertOneNe(_id, longchain.get(k), longchain.get(k+1), A_neName,  2*(count-1), "protectPath");
									count++;
								}
							}
							TunnelService.updateNe(_id, NeService.findNeId(A_neName), (int)boardId, portName,  2*(count-1), "protectPath");
						}
						break;
					}
				}
			}
			return true;
		}
		else if (A_index > connection_index) {//A_neName是在connectionNeName右侧
			
			//查找workPath上的tunnel
			List<Object> tunnelList1 = TunnelService.selecTunnels(boardId, portName, "workPath");
			for (int i = 0; i < tunnelList1.size(); i++) {//遍历所有的tunnel
				Document tunnel = (Document) tunnelList1.get(i);
				int _id = Integer.valueOf( tunnel.get("_id").toString());
				int firstnNeId = Integer.valueOf(tunnel.get("workPath.pathsList.0.neId").toString());
				for (int j = 0; j < ring.size(); j++) {
					int neId = new NeService().findNeId(ring.get(j));
					if (firstnNeId == neId) {
						//判断neId位于A_neName的左侧还是右侧，从而确定tunnel的走向
						if (j < A_index) {//tunel的起点位于A的左侧
							//修改交叉网元的输出端
							//插入长链上的网元
							//修改A_neName的输入端
							int count = connection_index-j+1;//2(2/3)
							TunnelService.updateNe(_id, NeService.findNeId(connectionNeName), (int)boardId_c, portName_c, 2*(count-1)+1, "workPath");
							count++;//3(4/5)
							for (int k = longchain.size()-1; k >=0; k--) {
								if (k == longchain.size()-1) {
									TunnelService.insertOneNe(_id, longchain.get(k), connectionNeName, longchain.get(k-1), 2*(count-1), "workPath");
									count++;
								}
								if (k!=longchain.size()-1&&k!=0) {
									TunnelService.insertOneNe(_id, longchain.get(k), longchain.get(k+1), longchain.get(k-1), 2*(count-1), "workPath");
									count++;
								}
								if (k==0) {
									TunnelService.insertOneNe(_id, longchain.get(k), longchain.get(k+1), A_neName, 2*(count-1), "workPath");
									count++;
								}
							}
							TunnelService.updateNe(_id, NeService.findNeId(A_neName), (int)boardId, portName, 2*(count-1), "workPath");
						}
						if (j >= A_index) {//tunel的起点位于A的有侧
							//修改A_neName的输出端
							//插入长链上的网元
							//修改交叉望远的输入端
							int count = j-A_index+1;//3(4/5)
							TunnelService.updateNe(_id, NeService.findNeId(A_neName), (int)boardId, portName,2*(count-1)+1, "workPath");
							count++;//4(6/7)
							for (int k = 0; k < longchain.size(); k++) {
								if (k==0) {
									TunnelService.insertOneNe(_id, longchain.get(k), A_neName, longchain.get(k+1), 2*(count-1)+1, "workPath");
									count++;
								}
								if (k!=0&&k!=longchain.size()-1) {
									TunnelService.insertOneNe(_id, longchain.get(k), longchain.get(k-1), longchain.get(k+1),  2*(count-1)+1, "workPath");
									count++;
								}
								if (k==longchain.size()-1) {
									TunnelService.insertOneNe(_id, longchain.get(k), longchain.get(k-1), connectionNeName, 2*(count-1)+1, "workPath");
									count++;
								}
							}
							TunnelService.updateNe(_id, NeService.findNeId(connectionNeName), (int)boardId_c, portName_c,  2*(count-1)+1, "workPath");
						}
						break;
					}
					
				}
			}
			
			//查找protectPath上的tunnel
			List<Object> tunnelList2 = TunnelService.selecTunnels(boardId, portName, "protectPath");
			for (int i = 0; i < tunnelList2.size(); i++) {//遍历所有的tunnel
				Document tunnel = (Document) tunnelList2.get(i);
				int _id = Integer.valueOf( tunnel.get("_id").toString());
				int firstnNeId = Integer.valueOf(tunnel.get("protectPath.pathsList.0.neId").toString());
				for (int j = 0; j < ring.size(); j++) {
					int neId = new NeService().findNeId(ring.get(j));
					if (firstnNeId == neId) {
						//判断neId位于A_neName的左侧还是右侧，从而确定tunnel的走向
						if (j < A_index) {//tunel的起点位于A的左侧
							//修改交叉网元的输出端
							//插入长链上的网元
							//修改A_neName的输入端
							int count = connection_index-j+1;//2(2/3)
							TunnelService.updateNe(_id, NeService.findNeId(connectionNeName), (int)boardId_c, portName_c, 2*(count-1)+1, "protectPath");
							count++;//3(4/5)
							for (int k = longchain.size()-1; k >0; k--) {
								if (k == longchain.size()-1) {
									TunnelService.insertOneNe(_id, longchain.get(k), connectionNeName, longchain.get(k-1), 2*(count-1), "protectPath");
									count++;
								}
								if (k!=longchain.size()-1&&k!=0) {
									TunnelService.insertOneNe(_id, longchain.get(k), longchain.get(k+1), longchain.get(k-1), 2*(count-1), "protectPath");
									count++;
								}
								if (k==0) {
									TunnelService.insertOneNe(_id, longchain.get(k), longchain.get(k+1), A_neName, 2*(count-1), "protectPath");
									count++;
								}
							}
							TunnelService.updateNe(_id, NeService.findNeId(A_neName), (int)boardId, portName, 2*(count-1), "protectPath");
						}
						if (j >= A_index) {//tunel的起点位于A的有侧
							//修改A_neName的输出端
							//插入长链上的网元
							//修改交叉望远的输入端
							int count = j-A_index+1;//3(4/5)
							TunnelService.updateNe(_id, NeService.findNeId(A_neName), (int)boardId, portName,2*(count-1)+1, "protectPath");
							count++;//4(6/7)
							for (int k = 0; k < longchain.size(); k++) {
								if (k==0) {
									TunnelService.insertOneNe(_id, longchain.get(k), A_neName, longchain.get(k+1), 2*(count-1)+1, "protectPath");
									count++;
								}
								if (k!=0&&k!=longchain.size()-1) {
									TunnelService.insertOneNe(_id, longchain.get(k), longchain.get(k-1), longchain.get(k+1),  2*(count-1)+1, "protectPath");
									count++;
								}
								if (k==longchain.size()-1) {
									TunnelService.insertOneNe(_id, longchain.get(k), longchain.get(k-1), connectionNeName, 2*(count-1)+1, "protectPath");
									count++;
								}
							}
							TunnelService.updateNe(_id, NeService.findNeId(connectionNeName), (int)boardId_c, portName_c,  2*(count-1)+1, "protectPath");
						}
						break;
					}
					
				}
			}
			return true;
		}else {
			return false;
			
		}
	}
	
}
