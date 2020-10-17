package cn.edu.hbut.hfcrt.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

import cn.edu.hbut.cs.hfcrt.databridge.dao.DataBridgeDAO;
import cn.edu.hbut.hfcrt.common.MongoDBHelper;
import cn.edu.hbut.hfcrt.constant.Constant;
import cn.edu.hbut.hfcrt.pojo.Board;
import cn.edu.hbut.hfcrt.pojo.BoardPort;

/**
 * @author ZTW
 *
 */

public class ChangeTunnelService {
	
	MongoDBHelper mongoDBHelper = new  MongoDBHelper();
	static MongoClient mongoClient = new MongoClient();
	static MongoDatabase mongoDatabase=MongoDBHelper.getMongoDatabase(mongoClient);
	static MongoCollection<Document> tunnelMapCol =  mongoDatabase.getCollection(Constant.tunnelMapCol);
	static MongoCollection<Document> neMapCol =  mongoDatabase.getCollection("neMap");
	static MongoCollection<Document> topologynodesCol =  mongoDatabase.getCollection(Constant.nodeTopologynodesCol);
	static MongoCollection<Document> topologylinesCol =  mongoDatabase.getCollection(Constant.nodeTopologyLinesCol);
	
	
	//遍历tunnelList中tunnel的所有tunnelId
	public static List<Integer> findTunnelId(List<Document> tunnelList){
		List<Integer> List=new ArrayList<Integer>();
		for(int i=0;i<tunnelList.size();i++){
			Document tunnel =tunnelList.get(i);
			int _id = (int)tunnel.get("_id");
			List.add(_id);
		}
		return List;
	}
	
	
	
	
	
	//筛选tunnel中包含汇聚网元的tunnel下标
	public static List<Integer> selectTunnelIndex(List<Document> tunnelList,String path,int convergence){
		Boolean flag =false;
		List<Integer> list =new ArrayList<Integer>();
		for(int i=0;i<tunnelList.size();i++){
			Document tunnel = tunnelList.get(i);
			flag = selectTunnelContainsConvergence(tunnel, convergence, path);
			if(flag==true){
				list.add(i);
			}
		}		
		return list;		
	}
		
	//判断当前的tunnel中是否包含汇聚网元
	public static Boolean selectTunnelContainsConvergence(Document tunnel ,int convergence,String path){
		Boolean flag =false;
		Document workPath = (Document) ((ArrayList) tunnel.get(path)).get(0);
		ArrayList pathsList = (ArrayList) workPath.get("pathsList");
		for(int i=0;i<pathsList.size();i++){
			int NeId = (int) ((Document)pathsList.get(i)).get("neId");
			if(NeId==convergence){
				flag = true;
			}
			 
		}	
		return flag;		
	}
	
	
	//寻找板卡端口以及加光纤总操作
		public static void TunnelChange(List<Document> tunnelList,int GNeId,int CNeId,String path,List<BoardPort> BList){
			int GnewBoardId = BList.get(0).getNewBoard();
			System.out.println("目标GnewBoardId "+GnewBoardId);
	        String GnewPortName = BList.get(0).getNewPortName();
	        System.out.println("目标端口GnewPortName "+GnewPortName);
	        int CnewBoardId = BList.get(1).getNewBoard();
	        String CnewPortName = BList.get(1).getNewPortName();
	        System.out.println("汇聚板卡id为："+CnewBoardId);
	        System.out.println("汇聚端口为:"+CnewPortName);
	        
			for(int i=0;i<tunnelList.size();i++){
				Document tunnel =tunnelList.get(i);
				int _id =(int) tunnel.get("_id");
//				System.out.println("第"+i+"条tunnel中的tunnelID为:"+_id);
				int GNIndex1 = ChangeTunnelService.findNeLocationFromTunnel(tunnel, GNeId, path);
				int CNIndex1 = ChangeTunnelService.findNeLocationFromTunnel(tunnel, CNeId, path);
				
				System.out.println("未删之前目标网元在tunnel中的位置GNIndex1:"+GNIndex1);
				System.out.println("未删之前汇聚网元在tunnel中的位置CNIndex1:"+CNIndex1);
				
		        ChangeTunnelService.DeleteNeFromgoalNeToConvengenceNeInOneTunnel(GNeId, CNeId, tunnel, path);
				
				Document tunnel1 = TunnelService.findDocument(_id, path);
				
				int GNIndex = ChangeTunnelService.findNeLocationFromTunnel(tunnel1, GNeId, path);
				System.out.println("删除网元之后目标网元在tunnel中的位置GNIndex"+GNIndex);
				int CNIndex = ChangeTunnelService.findNeLocationFromTunnel(tunnel1, CNeId, path);
				System.out.println("删除网元之后汇聚网元在tunnel中的位置CNIndex"+CNIndex);
				if(GNIndex > CNIndex){
					PtnService.updateNe(_id, GNeId, GnewBoardId, GnewPortName, GNIndex, path);
					PtnService.updateNe(_id, CNeId, CnewBoardId, CnewPortName, CNIndex+1, path);
				}else if(GNIndex < CNIndex){
								
					PtnService.updateNe(_id, GNeId, GnewBoardId, GnewPortName, GNIndex+1, path);
					PtnService.updateNe(_id, CNeId, CnewBoardId, CnewPortName, CNIndex, path);
				}
				
			}
						 
		}
		
	
 
	//查找满足条件一条tunnel中所经过的所有网元ID
	public static List<Integer>  findNeIdByTunnelList1(Document tunnel,String path){
		
//		Set<Integer> set=new LinkedHashSet<Integer>();
		List<Integer> list=new ArrayList<Integer>();
		
		Document workPath = (Document) ((ArrayList) tunnel.get(path)).get(0);
		ArrayList pathsList = (ArrayList) workPath.get("pathsList");
//		System.out.println("当前pathList中网元的个数为:"+pathsList.size()); //需要除以2
		for(int j=0;j<pathsList.size();j++){
			int TunnelNeId = Integer.valueOf(((Document)pathsList.get(j)).get("neId").toString());
			list.add(TunnelNeId);
//			System.out.println("经过该tunnel的每一个网元neId"+TunnelNeId);				
		}
		return list;
	}
	
	//查找满足条件所有tunnel中所经过的所有网元ID
	public static List<List<Integer>> findNeIdByTunnelList(List<Document> list,String path){
		List<List<Integer>> list1 =new ArrayList<List<Integer>>();
		for(int i=0;i<list.size();i++){
			List<Integer> list2 = findNeIdByTunnelList1(list.get(i),path);
			list1.add(list2);
		}
		return list1;
	}
	
	
	//查找要断开网元在tunnel中属于第几个位置
	public static List<Integer>  JudgeNeLocationInTunnelList(int neId,List<Document> list,String path){
		List<Integer> lists=new ArrayList<Integer>();
		List<List<Integer>> list1 = findNeIdByTunnelList(list,path);
		for(int i=0;i<list1.size();i++){
			int location = 0;
			int index=list1.get(i).indexOf(neId)+1;
			if(index!=0){
				  location = (index / 2)+1;
			}
			else{
				  location =  0;//表示这条tunnel不经过没有汇聚网元
			}
		 
//			System.out.println("目标网元在第"+i+"条tunnel中的位置是第:"+location+"个网元");
			lists.add(location);
		}
		return lists;
	}
	
	//判断目标网元的下一条是否为MidRightNe   ===>判断环是否是有序的
	public static List<Boolean> JudgeNeNextIsNotMidRightNe(int MidRightNe,List<Integer> list,List<Document> doc){
		Boolean flag=false;
		List<Boolean> blist=new ArrayList<Boolean>();
		for(int i=0;i<doc.size();i++){
			int j = list.get(i);
			Document tunnel = doc.get(i);
			Document workPath = (Document) ((ArrayList) tunnel.get("workPath")).get(0);
			ArrayList pathsList = (ArrayList) workPath.get("pathsList");
			int NextNeid = Integer.valueOf(((Document)pathsList.get(j+2)).get("neId").toString());
			if(MidRightNe==NextNeid){
				flag=true;
			}
			blist.add(flag);
		}
		return blist;
		
	}
	public static Boolean JudgeNeNextIsNotMidRightNe1(List<Boolean> list){
		int falseNum = 0;
		int trueNum = 0;
		for(int i=0;i<list.size();i++){
			Boolean flag = list.get(i);
			if(flag==true){
				trueNum++;
			}else{
				falseNum++;
			}
		}
		if(trueNum>=falseNum){
			return true;
		}else{
			return false;
		}
	}
	
	//path删除网元ne:tunnel的_id,DeleteNeInTunnelList
	@SuppressWarnings("unchecked")
	public static List DeleteNeInTunnelList(int MidLeftNeId,List<Document> tunnelList,String path) {
		int flag = 0;
		List pathsList=new ArrayList<>();
		for(int i=0;i<tunnelList.size();i++){
			Document tunnel = tunnelList.get(i);
			Document Path = (Document) ((ArrayList) tunnel.get(path)).get(0);//获取该Tunnel中的workpath
			ArrayList pathsLists = (ArrayList) Path.get("pathsList");
			ArrayList pathsList1=new ArrayList<Object>();
			ArrayList pathsList2=new ArrayList<Object>();		
			for(int j=0;j<pathsLists.size();j++){
				int neId = (int) ((Document)pathsLists.get(j)).get("neId");
				if(MidLeftNeId==neId){
					flag=j;
				}
			}
			for(int k=0;k<=flag;k++){
				pathsList1.add(pathsLists.get(k));
			}
			for(int z=flag+1;z<pathsLists.size();z++){
				pathsList2.add(pathsLists.get(z));
			}
			System.out.println("pathsList"+pathsList1);
			System.out.println(pathsList1.size());
			Document document3 = new Document().append(path+".0.pathsList", pathsList1);
//			tunnelMapCol.updateOne(tunnel, new Document("$set", document3));
			System.out.println("删掉网元后的的tunnel"+tunnel);
			pathsList=pathsList1;
		}
		return pathsList;	
	}
	//查找汇聚网元在所查找出来的tunnel中的位置
	public static List<Integer> findLastNelocation(int neId,List<Document> tunnelList,String path){
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<tunnelList.size();i++){
			Document tunnel = tunnelList.get(i);
			Document Path = (Document) ((ArrayList) tunnel.get(path)).get(0);
			ArrayList pathsList = (ArrayList) Path.get("pathsList");
			for(int j = 0;j<pathsList.size();j++){
				int NeId = (int) ((Document)pathsList.get(j)).get("neId");
				if(NeId==neId){
					list.add(j);				 
				}
			}				
		}
		return list;		
	}
	
	//查找经过目标网元的所有tunnel中目标网元到汇聚网元之间的网元 ====>   在芒果数据库中
	public static List<List<Integer>> findgoalNeToConvengenceNeFromMongo(List<Integer> CList,List<Integer> GList,List<Document> tunnelList,String path){
		List<List<Integer>> TunnelList =new ArrayList<List<Integer>>();
		for(int i=0;i<tunnelList.size();i++){
			Document tunnel = tunnelList.get(i);
			Document workPath = (Document) ((ArrayList) tunnel.get(path)).get(0);
			ArrayList pathsList = (ArrayList) workPath.get("pathsList");
			List<Integer> List=new ArrayList<Integer>();
			if(GList.get(i) > CList.get(i)){   //目标网元在汇聚的下面    9>4
				for(int j=((GList.get(i)-1)*2)-1;j>=(CList.get(i)-1)*2+2;j-=2){				 
					int NeId = (int) ((Document)pathsList.get(j)).get("neId");
					List.add(NeId);
				}		
			}else if(GList.get(i) < CList.get(i)){  //汇聚网元在目标的下面
				for(int j=((GList.get(i)-1)*2)+2;j<=((CList.get(i)-1)*2)-1;j+=2){
					int NeId = (int) ((Document)pathsList.get(j)).get("neId");
					List.add(NeId);
				}				
			}			 
			TunnelList.add(List);
		}
		return TunnelList;
	}
	
	//比较芒果数据库与表格之间的网元是否一一对应
	public static Boolean Compare(List<Integer> list1,List<Integer> list2){	
		Boolean flag=false;
		int count = 0;
		for(int i=0;i<list2.size();i++){
			int listId1= list1.get(i);
			int listId2 =list2.get(i);
			if(listId1==listId2){
				count++;
				if(count==list2.size()){
					flag=true;
				}
			}		
		}				
		return flag;
		
	}
		
	//筛选满足条件的tunnel下标索引
	public static List<Integer> GetFilterTunnelIndex(List<List<Integer>> TunnelList,List<Integer> list){
		Boolean flag=false;
		List<Integer> List =new ArrayList<Integer>(); //保存满足条件的tunnel的下标索引
		for(int i=0;i<TunnelList.size();i++){
			int tunenlSize = TunnelList.get(i).size();
			int listSize = list.size();
			if(tunenlSize!=listSize){
				System.out.println("excel表格中与芒果数据库中目标网元到汇聚网元之间的网元数不一致,可排除,不是满足要求的tunnel");
				continue;
			}else{					
					 flag =ChangeTunnelService.Compare(TunnelList.get(i), list);					
					 if(flag == true){
						 List.add(i);
					 }
			}
		}
		return List;		
	}

	//筛选满足条件的tunnel
	public static List<Document> findFilterConditionTunnel(List<Document> List,List<Integer> list){
		List<Document> tunnelList =new ArrayList<Document>();
		for(int i=0;i<list.size();i++){
			Document tunnel  = List.get(list.get(i));
			tunnelList.add(tunnel);
		}			
		return tunnelList;		
	}
	
	
	//筛选满足条件的目标网元集合和汇聚网元集合
	public static List<Integer>selectFilterConditionCListAndGListIndex(List<Integer> list1,List<Integer> list2){
		List<Integer> List=new ArrayList<Integer>();
		for(int i=0;i<list1.size();i++){
			int index=list2.get(list1.get(i));
			List.add(index);
		}
				
		return List;
		
	}
	//删除 满足条件的tunnel,目标到汇聚之间的网元
	public static void DeleteNeFromgoalNeToConvengenceNe(int MidLeftNeId,int ConvengenceNeId ,List<Document> tunnelList,String path) {
		int Gflag = 0;  //目标网元在tunnel中的下标索引
		int Cflag = 0;  //汇聚网元在tunenl中的下表索引
		List pathsList=new ArrayList<>();
		for(int i=0;i<tunnelList.size();i++){
			Document tunnel = tunnelList.get(i);
			Document Path = (Document) ((ArrayList) tunnel.get(path)).get(0);//获取该Tunnel中的workpath
			List<Object> pathsLists = (ArrayList) Path.get("pathsList");
			List<Object> PathsList = new ArrayList<Object>();
			List<Object> pathsLists1=new ArrayList<Object>();
			List<Object> pathsLists2=new ArrayList<Object>();		
			for(int j=0;j<pathsLists.size();j++){ //找目标网元的下表索引
				int neId = (int) ((Document)pathsLists.get(j)).get("neId");
				if(MidLeftNeId==neId){
					Gflag=j;
					break;
				}
				
			}
			for(int j=0;j<pathsLists.size();j++){  //找汇聚网元的下表索引
				int neId = (int) ((Document)pathsLists.get(j)).get("neId");
				if(ConvengenceNeId==neId){
					Cflag=j;
					break;
				}
				
			}
			if(Gflag>Cflag){
				for(int k=Gflag;k<=pathsLists.size()-1;k++){  //目标网元左边的网元
					pathsLists2.add(pathsLists.get(k));
				}
				for(int z=0;z<=Cflag+1;z++){  //汇聚网元的右边
					pathsLists1.add(pathsLists.get(z));
				}		
			}
			else if(Gflag<Cflag){
				for(int k=0;k<=Gflag+1;k++){  //目标网元左边的网元
					pathsLists1.add(pathsLists.get(k));
				}
				for(int z=Cflag;z<=pathsLists.size()-1;z++){  //汇聚网元的右边
					pathsLists2.add(pathsLists.get(z));
				}
			} 
			pathsLists1.addAll(pathsLists2);
			Document document3 = new Document().append(path+".0.pathsList", pathsLists1);
			tunnelMapCol.updateOne(tunnel, new Document("$set", document3));
		}	 
	}
	
	
	//删除一条tunnel中目标到汇聚之间的网元
	public static void DeleteNeFromgoalNeToConvengenceNeInOneTunnel(int GNeId,int CNeId ,Document tunnel,String path){
        Document Tunnel = null;
		List pathsList=new ArrayList<>();
		Document Path = (Document) ((ArrayList) tunnel.get(path)).get(0);//获取该Tunnel中的workpath
		List<Object> pathsLists = (ArrayList) Path.get("pathsList");
		List<Object> PathsList = new ArrayList<Object>();
		List<Object> pathsLists1=new ArrayList<Object>();
		List<Object> pathsLists2=new ArrayList<Object>();
		int Gflag = ChangeTunnelService.findNeLocationFromTunnel(tunnel, GNeId, path);
		int Cflag = ChangeTunnelService.findNeLocationFromTunnel(tunnel, CNeId, path);
 
		if(Gflag>Cflag){
			for(int k=Gflag;k<=pathsLists.size()-1;k++){  //目标网元左边的网元
				pathsLists2.add(pathsLists.get(k));
			}
			for(int z=0;z<=Cflag+1;z++){  //汇聚网元的右边
				pathsLists1.add(pathsLists.get(z));
			}		
		}
		else if(Gflag<Cflag){
			for(int k=0;k<=Gflag+1;k++){  //目标网元左边的网元
				pathsLists1.add(pathsLists.get(k));
			}
			for(int z=Cflag;z<=pathsLists.size()-1;z++){  //汇聚网元的右边
				pathsLists2.add(pathsLists.get(z));
			}
		} 
		pathsLists1.addAll(pathsLists2);
		Document document3 = new Document().append(path+".0.pathsList", pathsLists1);
        tunnelMapCol.updateOne(tunnel, new Document("$set", document3)); 	 
	}	 
	
	
	
	
	
	
	
	//查找某个网元在tunnel中的位置
	public static int findNeLocationFromTunnel(Document tunnel,int GoalNeId,String path){
		int flag=0;
		Document Path = (Document) ((ArrayList) tunnel.get(path)).get(0);//获取该Tunnel中的workpath
		List<Object> pathsLists = (ArrayList) Path.get("pathsList");
		for(int i=0;i<pathsLists.size();i++){
			int neId = (int) ((Document)pathsLists.get(i)).get("neId"); 
			if(GoalNeId == neId){
				flag=i;
				break;
			}			
		}	
		return flag;
		
	}
	
	//获取要断开网元的boardId和PortName,portNo    
	public static List<Board> GetOldBoardIdAndPortName(int GoalNeId,List<Document> tunnelList,String path){		     
		    List<Board> list =new ArrayList<Board>();
		    for(int i=0;i<tunnelList.size();i++){
		    	Document tunnel = tunnelList.get(i);
		    	Document Path = (Document) ((ArrayList) tunnel.get(path)).get(0);//获取该Tunnel中的workpath
				List<Object> pathsLists = (ArrayList) Path.get("pathsList");
				int Gflag = ChangeTunnelService.findNeLocationFromTunnel(tunnel, GoalNeId, path);//出口板卡
				int boardId = (int) ((Document)pathsLists.get(Gflag+1)).get("boardId");
				String portName = (String) ((Document)pathsLists.get(Gflag+1)).get("portName");
				int portNo = (int) ((Document)pathsLists.get(Gflag+1)).get("portNo");	
				Board board=new Board();
				board.setBoardId(boardId);
				board.setPortName(portName);
				board.setPortNo(portNo);
				list.add(board);
		    }	
		return list;	
	}
	
	//获取要断开网元的boardId和PortName,portNo    
	public static List<Board> GetOldBoardIdAndPortNameplus(int GoalNeId, Document tunnel,String path){		     
		    List<Board> list =new ArrayList<Board>();
		    Document Path = (Document) ((ArrayList) tunnel.get(path)).get(0);//获取该Tunnel中的workpath
			List<Object> pathsLists = (ArrayList) Path.get("pathsList");
			
			//入口
			int Gflag = ChangeTunnelService.findNeLocationFromTunnel(tunnel, GoalNeId, path);//入口板卡
			int boardId =  ((Number)((Document)pathsLists.get(Gflag)).get("boardId")).intValue();
			String portName = (String) ((Document)pathsLists.get(Gflag)).get("portName");
			int portNo = (int) ((Document)pathsLists.get(Gflag)).get("portNo");	
			Board board=new Board();
			board.setBoardId(boardId);
			board.setPortName(portName);
			board.setPortNo(portNo);
			list.add(board);
			
			//出口
			
			int boardId1 = ((Number) ((Document)pathsLists.get(Gflag+1)).get("boardId")).intValue();  //出口板卡
			String portName1 = (String)((Document)pathsLists.get(Gflag+1)).get("portName");
			int portNo1 = (int) ((Document)pathsLists.get(Gflag+1)).get("portNo");	
			Board board1=new Board();
			board1.setBoardId(boardId1);
			board1.setPortName(portName1);
			board1.setPortNo(portNo1);
			list.add(board1);		   	
		return list;	
	}
	
	//查找筛选后的tunnel中,目标网元中板卡上的空闲端口
	public static List<List<String>> GetFreePort(List<Board> Blist){
		List<List<String>> List=new ArrayList<List<String>>();
		for(int i=0;i<Blist.size();i++){
			int boardId = Blist.get(i).getBoardId();
			Map<Double,List<String>> map =PortService.checkAdditionalPorts1(boardId);
			List<String> list =map.get(boardId);
			List.add(list);
		}
		return List;	 		
	}
	
	//切换业务操作
	public static void ChangeTunnel(List<Document> tunnelList,int GoalNeId,int ConvengenceNeId,String path){
		List<Board> GBlist =ChangeTunnelService.GetOldBoardIdAndPortName(GoalNeId, tunnelList, path);
		List<Board> CBlist =ChangeTunnelService.GetOldBoardIdAndPortName(ConvengenceNeId, tunnelList, path);
		for(int i=0;i<tunnelList.size();i++){
			int GboardId =GBlist.get(i).getBoardId();
			String GportName = GBlist.get(i).getPortName();
			int CboardId= CBlist.get(i).getBoardId();
			String CportName = CBlist.get(i).getPortName();
			TopoLines.addTopo(GboardId, GportName, CboardId, CportName);
			ChangeTunnelService.DeleteNeFromgoalNeToConvengenceNe(GoalNeId, ConvengenceNeId, tunnelList, path);
		}
		
		
	}
	
	
	
	
	
	
	
	
	
}



