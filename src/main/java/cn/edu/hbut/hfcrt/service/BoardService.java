package cn.edu.hbut.hfcrt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import cn.edu.hbut.cs.hfcrt.databridge.dao.DataBridgeDAO;


public class BoardService {
	
	static MongoCollection<Document> boardMapCol = new DataBridgeDAO().getCollection("boardMap");
	static MongoCollection<Document> topologylinesCol = new DataBridgeDAO().getCollection("nodeTopology_lines");

	
	//查找特定速率下,aimBoardId以外的空闲板卡
	public static List<Document> checkAdditionalBoards1(int aimBoardId,String aimPortSpeed) {
			System.out.println("aimBoardId="+aimBoardId);
			System.out.println("aimPortSpeed="+aimPortSpeed);
		List<Document> availableBoardList = new ArrayList<Document>();//储存空闲板卡信息
		Document query=new Document().append("boardId", Double.valueOf(aimBoardId));
			System.out.println("query="+query);
		Document aimBoard=boardMapCol.find(query).first();//查找aimBoardId符合的对象,指定板卡的信息
//			System.out.println("aimBoard="+aimBoard);
		String aimBoardTypeName = aimBoard.get("boardTypeName").toString();
		int aimNeId = Integer.valueOf(aimBoard.get("neId").toString());
			System.out.println("aimBoardTypeName="+aimBoardTypeName);
			System.out.println("aimNeId="+aimNeId);
			System.out.println("aimBoardId="+aimBoardId);
		FindIterable<Document> find = boardMapCol.find();
		MongoCursor<Document> mongoCursor = find.iterator();
		while(mongoCursor.hasNext()){//遍历boardMap中的每一个板卡
			Document doc=mongoCursor.next();
				System.out.println("doc="+doc);
			int boardId=Integer.valueOf(doc.get("boardId").toString());
			int neId=Integer.valueOf(doc.get("neId").toString());
			String boardTypeName=doc.get("boardTypeName").toString();
//				System.out.println("boardId="+boardId);
//				System.out.println("neId="+neId);
//				System.out.println("boardTypeName="+boardTypeName);
				System.out.println(1);
			
			//查找同一个网元,同一板卡类型下的不同板卡的端口
			if(neId==aimNeId && boardTypeName.equals(aimBoardTypeName) && boardId!=aimBoardId ){
					System.out.println(1);
					System.out.println("boardId="+boardId);
					System.out.println("neId="+neId);
					System.out.println("boardTypeName="+boardTypeName);
				//此网元下空闲板卡的特定端口速率的一个空闲端口
				List<Document> additionalPorts = PortService.checkAdditionalPorts(boardId, aimPortSpeed);
//					System.out.println("additionalPorts="+additionalPorts);
				if(additionalPorts.size()>0){
					Document Doc = additionalPorts.get(0);
//						System.out.println("Doc="+Doc);
//					String portName = Doc.get("portName").toString();
//					String portNo =Doc.get("portNo").toString();
					Document document = doc.append("portName", Doc.get("portName")).append("portNo", Doc.get("portNo"));
//						System.out.println("document="+document);
					availableBoardList.add(document);
				}
				
			}
		}
//		System.out.println("availableBoardList="+availableBoardList);
		return availableBoardList;
	}

	//查找特定速率下,aimBoardId的空闲端口或者其他板卡上的空闲端口
	public static List<Document> checkAdditionalBoards(int aimBoardId,String aimPortSpeed) {
//				System.out.println("aimBoardId="+aimBoardId);
//				System.out.println("aimPortSpeed="+aimPortSpeed);
			List<Document> availableBoardList = new ArrayList<Document>();//储存空闲板卡信息
			Document query=new Document().append("boardId", Integer.valueOf(aimBoardId));
//				System.out.println("query="+query);
			Document aimBoard=boardMapCol.find(query).first();//查找aimBoardId符合的对象,指定板卡的信息
//				System.out.println("aimBoard="+aimBoard);
			String aimBoardTypeName = aimBoard.get("boardTypeName").toString();
			int aimNeId = Integer.valueOf(aimBoard.get("neId").toString());
//				System.out.println("aimBoardTypeName="+aimBoardTypeName);
//				System.out.println("aimNeId="+aimNeId);
//				System.out.println("aimBoardId="+aimBoardId);
			FindIterable<Document> find = boardMapCol.find();
			MongoCursor<Document> mongoCursor = find.iterator();
			while(mongoCursor.hasNext()){//遍历boardMap中的每一个板卡
				Document doc=mongoCursor.next();
//					System.out.println("doc="+doc);
				int boardId=Integer.valueOf(doc.get("boardId").toString());
				int neId=Integer.valueOf(doc.get("neId").toString());
				String boardTypeName=doc.get("boardTypeName").toString();
//					System.out.println("boardId="+boardId);
//					System.out.println("neId="+neId);
//					System.out.println("boardTypeName="+boardTypeName);
//					System.out.println(1);
				
				//查找同一个网元
				if(neId==aimNeId){
						if ( boardTypeName.equals(aimBoardTypeName)){//查找同一板卡类型下
//							System.out.println("boardId="+boardId);
							if (boardId != aimBoardId) {//不同板卡的空闲端口
//								System.out.println(1);
//								System.out.println("boardId="+boardId);
//								System.out.println("neId="+neId);
//								System.out.println("boardTypeName="+boardTypeName);
							//此网元下空闲板卡的特定端口速率的一个空闲端口
							List<Document> additionalPorts = PortService.checkAdditionalPorts(boardId, aimPortSpeed);
//								System.out.println("additionalPorts="+additionalPorts);
							if(additionalPorts.size()>0){
								Document Doc = additionalPorts.get(0);
//									System.out.println("Doc="+Doc);
//								String portName = Doc.get("portName").toString();
//								String portNo =Doc.get("portNo").toString();
								Document document = doc.append("portName", Doc.get("portName")).append("portNo", Doc.get("portNo"));
									System.out.println("document="+document);
								availableBoardList.add(document);
							}
							} else {//相同板卡的空闲端口
								List<Document> additionalPorts = PortService.checkAdditionalPorts(boardId, aimPortSpeed);
//									System.out.println("additionalPorts="+additionalPorts);
								if(additionalPorts.size()>0){
									Document Doc = additionalPorts.get(0);
//										System.out.println("Doc="+Doc);
//									String portName = Doc.get("portName").toString();
//									String portNo =Doc.get("portNo").toString();
									Document document = doc.append("portName", Doc.get("portName")).append("portNo", Doc.get("portNo"));
//										System.out.println("document="+document);
									availableBoardList.add(document);
								}
							}
						}else {
							continue;
						}
					
				}else {
					continue;
				}
			}
//			System.out.println("availableBoardList="+availableBoardList);
//			System.out.println("BoardService.java");
			return availableBoardList;
		}
	
	//判断boardId是否属于neName上
	public static boolean isExist(String aimNeName,int boardId1) {
		NeService nel = new NeService();
//			System.out.println("aimNeName="+aimNeName);
//			System.out.println("boardId1="+boardId1);
		int aimNeId = nel.findNeId(aimNeName);
		Document query = new Document().append("neId", Integer.valueOf(aimNeId));
		FindIterable<Document> find = boardMapCol.find(query);
		MongoCursor<Document> cursor = find.iterator();
		while (cursor.hasNext()) {
			Document board = cursor.next();
			int boardId =  (int) board.get("boardId");
//			System.out.println("boardId="+boardId);
			if (boardId == boardId1) {
				return true;
			}
		}
		return false;
	}
	
	//根据网元的boardTd,在neMap查找网元的neId
	public static int  findNeIdbyBoardId(int boardTd) {
		Document query=new Document().append("boardId", boardTd);
		Document board=boardMapCol.find(query).first();//查找neName符合的对象
		Integer neId = (Integer) board.get("neId");
		return neId;		
	}
	
	
	//获取拥有空闲板卡的板卡ID集合
	public static List<Integer> SelectpossessPortNameBoardId(List<Integer> BList){
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<BList.size();i++){
			int Newboard = BList.get(i);
			Map<Double,List<String>> map = PortService.checkAdditionalPorts1(Newboard);
			if(map.get((double)Newboard).size()==0){
				continue;
			}
			else{
				list.add(Newboard);
			}
		}
		return list;
	}
	
	
	
	
}
