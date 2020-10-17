package cn.edu.hbut.cs.hfcrt.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.junit.Test;

import cn.edu.hbut.hfcrt.controller.RingController;
import cn.edu.hbut.hfcrt.pojo.BoardPort;
import cn.edu.hbut.hfcrt.pojo.Condition;
import cn.edu.hbut.hfcrt.service.ChangeTunnelService;
import cn.edu.hbut.hfcrt.service.ConvergenceService;
import cn.edu.hbut.hfcrt.service.PtnService;
import cn.edu.hbut.hfcrt.service.TopoLines;
import cn.edu.hbut.hfcrt.service.TunnelService;
import cn.edu.hbut.hfcrt.utils.BigDecimalUtils;
import cn.edu.hbut.hfcrt.utils.ReadExcel;

public class Test01 {

	public static String ExcelPath="C:\\Users\\13409\\Desktop\\烽火\\环处理 - 副本(1).xls";
	public static int rowNum=85;
	
	static PtnService ptnService =new PtnService();
	
	public static int n=84;
	public static String path = "workPath";
	public static String path1 = "protectPath";
	
	
	 //方案二,放宽筛选tunnel的条件
	
	
	
	@Test
	public void Test001() throws IOException{
		RingRightTest(path);//==>
		RingLeftTest(path); //<==
	}
	
	public static void RingRightTest(String path) throws IOException{
		
		for(int i=0;i<n;i++){
			System.out.println("当前行数是第==========================================="+i+"行=======================================");
            List<Integer> lList = RingController.findMidLeftNeIdFromAllRingInfo(ExcelPath, rowNum);
            List<Integer> rList = RingController.findMidRightNeIdFromAllRingInfo(ExcelPath, rowNum);
            			
			System.out.println("第"+i+"条的中间左网元id为:"+lList.get(i));
			System.out.println("第"+i+"条的中间右网元id为:"+rList.get(i));			
			//1.拿到每条数据的左右网元ID
			int NeId1 = lList.get(i);
			int NeId2 = rList.get(i);
			
			
			//3.拿到topoid		
			double topoNodeId1 = PtnService.findToponodeId(NeId1);
			System.out.println("左边网元的topoNodeId是:"+topoNodeId1);
			double topoNodeId2 = PtnService.findToponodeId(NeId2);
			System.out.println("右边边网元的topoNodeId是:"+topoNodeId2);
			
			
			//4.根据两个网元的toponodeId找两个网元之间的光纤连接信息,获得boardId1,portName1,以及网元id 
			List<Condition> list = PtnService.findBoardIdAndPortNameByTopoId(topoNodeId1, topoNodeId2);
			double boardId1 = list.get(0).getBoardId1();
			String portName1= list.get(0).getPortName1();
			double neId1 = list.get(0).getNeId1(); //左边网元id
			if(boardId1==0){
				continue;
			}
			System.out.println(boardId1);
			System.out.println(portName1);
			System.out.println("中间左网元ID"+neId1);
											
			//5.根据boardId,portName,neId,查找经过该网元的所有tunnel
			List<Document> tunnelList = PtnService.selectTunnels1(boardId1, portName1, neId1, path);
			System.out.println("-----------------初始tunnel的条数---------------------"+tunnelList.size());
			if(tunnelList.size()==0){
				continue;
			}
			System.out.println("初始tunnel中第一条情况"+tunnelList.get(0));
						
			//6.判断目标网元在所查找的tunnel中的位置
			List<Integer> GList =ChangeTunnelService.JudgeNeLocationInTunnelList((int)neId1, tunnelList,path);
			System.out.println("目标网元在所经过的tunnel中网元的位置"+GList);
			System.out.println("-----------GList.size()的大小为------------------------"+GList.size());
			
			
			//7.查找汇聚网元在tunnel的位置
			
			List<String> list2=ConvergenceService.findFConvergenceNeId(ExcelPath, rowNum);
			
			String FConvergenceNeId = list2.get(i);
			
			System.out.println("FConvergenceNeId"+FConvergenceNeId);
			String FConvergenceNeId1 = BigDecimalUtils.BigDecimalToIntNum(FConvergenceNeId);
			
			int FConvergenceNeId2 = Integer.parseInt(FConvergenceNeId1);
					
			System.out.println("右汇聚FConvergenceNeId:"+FConvergenceNeId2);
	 	
			List<Integer> CList = ChangeTunnelService.JudgeNeLocationInTunnelList(FConvergenceNeId2, tunnelList,path);
			
			System.out.println("汇聚网元在所经过的tunnel中网元的位置"+CList);
			System.out.println("-----------------------------CList.size()的大小----------------------"+CList.size());
			
			int count=0;
			for(int a=0;a<CList.size();a++){
				int n = CList.get(a);
				if(n==0){
					count++;
				}
			}
			if(CList.size()==count){
				System.out.println("tunnel中不经过汇聚");
				continue;
			}
			//初步筛选tunnel
			for(int i1=CList.size()-1;i1>=0;i1--){			
				if(CList.get(i1)==0){
					CList.remove(i1);
					tunnelList.remove(i1);
					GList.remove(i1); 
				}
			}
			System.out.println("初步筛选的tunnel中第一条情况"+tunnelList.get(0));
			System.out.println("初步筛选的tunnel条数"+tunnelList.size());
			System.out.println("初步筛选后目标网元集合情况"+GList);
			System.out.println("初步筛选后目标网元集合大小"+GList.size());
			System.out.println("初步筛选后汇聚网元集合情况"+CList);
			System.out.println("初步筛选后汇聚网元集合大小"+CList.size());
			
			
			//方案二执行 查看经过目标网元的tunnel是否包含汇聚网元,包含则选出来(放宽条件)
			
			List<Integer> list4 = ChangeTunnelService.selectTunnelIndex(tunnelList, path, FConvergenceNeId2);

			//8.查找目标网元到汇聚网元之间网元是否与环路上的网元对应，从而筛选符合条件的tunnel
			
			//查找表格中某一行中 当前目标网元到汇聚网元之间的网元id集合
//			List<Integer> list4 = RingController.findSpecialNeIdList1(i, (int)neId1);
//			System.out.println("第"+i+"行表格中目标网元到汇聚网元之间的网元id集合"+list4);
//
//			//查找经过目标网元的所有tunnel中目标网元到汇聚网元之间的网元(不包含目标,以及汇聚本身网元本身)
//			List<List<Integer>> TunnelList1 = ChangeTunnelService.findgoalNeToConvengenceNeFromMongo(CList, GList, tunnelList,path);
			
			//筛选满足条件的tunnel下标索引
//			List<Integer> list5 = ChangeTunnelService.GetFilterTunnelIndex(TunnelList1, list4);
//
//			System.out.println("第二次筛选的tunnel的条数:"+list5.size());
//			System.out.println("第二次筛选满足的tunnel的下标索引"+list5);
//			
//			//二次筛选,目标集合以及汇聚集合
//			
//			List<Integer> CList1 = ChangeTunnelService.selectFilterConditionCListAndGListIndex(list5, CList);
//			List<Integer> GList1 = ChangeTunnelService.selectFilterConditionCListAndGListIndex(list5, GList);		
//			System.out.println("二次筛选后目标网元集合情况"+GList1);
//			System.out.println("二次筛选后汇聚网元集合情况"+CList1);
				
			//筛选满足条件的tunnel
			List<Document> tunnelList1= ChangeTunnelService.findFilterConditionTunnel(tunnelList,list4);
			System.out.println("最终筛选的tunnel条数"+tunnelList1.size());
 
			if(tunnelList1.size()==0){
				continue;
			}
			
					
			System.out.println("第一条tunnel数据"+tunnelList1.get(0));
			Document Atunnel = tunnelList1.get(0);
			Document APath = (Document) ((ArrayList) Atunnel.get(path)).get(0);//获取该Tunnel中的workpath
			List<Object> ApathsList = (ArrayList) APath.get("pathsList");
			System.out.println("未进行业务操作之前的tunnel中第一条tunnel为"+tunnelList1.get(0));
			System.out.println("未进行业务操作之前的tunnel中第一条tunnel中pathlist中的长度为"+ApathsList.size());
		
  
			//9.从删选出来的tunnel中,删除目标网元到汇聚网元之间的网元,进行业务操作
			
			//根据旧网元查找可用板卡和端口,以及加光纤
			
			List<BoardPort> BList = TunnelService.selectNewBoardAndNewPortNameFromOneTunnel(Atunnel, (int)neId1, FConvergenceNeId2, path);
	        
			//删网元以及更新板卡业务操作
			ChangeTunnelService.TunnelChange(tunnelList1, (int)neId1, FConvergenceNeId2, path, BList);
					
			//删除中间网元到目标网元之间的网元==业务切换
 			
			System.out.println("业务操作之后tunnel情况"+tunnelList1.get(0));
			System.out.println("业务操作之后tunnel条数"+tunnelList1.size());
			
//			Map<Integer,List<Integer>> map1 = TunnelService.findTunnelAndNeId(tunnelList1, path);
//			ReadExcel.writeListToExcel(map1,"C:\\Users\\13409\\Desktop\\烽火\\tunnel变化后结果.xls");
			
			int _id = (int) tunnelList1.get(0).get("_id");
			int length = TunnelService.findNeIdLengthByPathsList(_id, "workPath");
			System.out.println("进行业务操作之后的tunnel中第一条tunnel中pathlist中的长度为"+length);
			 
		}
		
	}
	public static void RingLeftTest(String path)throws IOException{
		for(int i=0;i<n;i++){
			System.out.println("当前行数是第==========================================="+i+"行=======================================");
			List<Integer> lList = RingController.findMidLeftNeIdFromAllRingInfo(ExcelPath, rowNum);
            List<Integer> rList = RingController.findMidRightNeIdFromAllRingInfo(ExcelPath, rowNum);
			System.out.println("第"+i+"条的中间左网元id为:"+lList.get(i));
			System.out.println("第"+i+"条的中间右网元id为:"+rList.get(i));
			//1.拿到每条数据的左右网元ID
			int NeId1 = lList.get(i);
			int NeId2 = rList.get(i);
			//2.拿到topoid		
			double topoNodeId1 = PtnService.findToponodeId(NeId1);
			System.out.println("左边网元的topoNodeId是:"+topoNodeId1);
			double topoNodeId2 = PtnService.findToponodeId(NeId2);
			System.out.println("右边边网元的topoNodeId是:"+topoNodeId2);
			//3.根据两个网元的toponodeId找两个网元之间的光纤连接信息,获得boardId1,portName1,以及网元id 
			List<Condition> list = PtnService.findBoardIdAndPortNameByTopoId(topoNodeId1, topoNodeId2);
			double boardId1 = list.get(0).getBoardId2();
			String portName1= list.get(0).getPortName2();
			double neId1 = list.get(0).getNeId2(); //右边网元id
			if(boardId1==0){
				continue;
			}
			System.out.println(boardId1);
			System.out.println(portName1);
			System.out.println("中间右网元ID"+neId1);
			
			//新增的一个功能删除两个网元之间的光纤
			TopoLines.deleteTopo(topoNodeId1, topoNodeId2);
			
			//4.根据boardId,portName,neId,查找经过该网元的所有tunnel
			List<Document> tunnelList = PtnService.selectTunnels1(boardId1, portName1, neId1, path);
			System.out.println("-----------------初始tunnel的条数---------------------"+tunnelList.size());
			if(tunnelList.size()==0){
				continue;
			}
			System.out.println("初始tunnel中第一条情况"+tunnelList.get(0));
			
			//5.判断目标网元在所查找的tunnel中的位置
			List<Integer> GList =ChangeTunnelService.JudgeNeLocationInTunnelList((int)neId1, tunnelList,path);
			System.out.println("目标网元在所经过的tunnel中网元的位置"+GList);
			System.out.println("-----------GList.size()的大小为------------------------"+GList.size());
			
			//6.查找汇聚网元在tunnel的位置
			
			List<String> list2=ConvergenceService.findAConvergenceNeId(ExcelPath, rowNum);
			
			String AConvergenceNeId = list2.get(i);
			
			System.out.println("AConvergenceNeId"+AConvergenceNeId);
			String AConvergenceNeId1 = BigDecimalUtils.BigDecimalToIntNum(AConvergenceNeId);
			
			int AConvergenceNeId2 = Integer.parseInt(AConvergenceNeId1);
					
			System.out.println("左汇聚FConvergenceNeId:"+AConvergenceNeId2);
	 	
			List<Integer> CList = ChangeTunnelService.JudgeNeLocationInTunnelList(AConvergenceNeId2, tunnelList,path);
			
			System.out.println("汇聚网元在所经过的tunnel中网元的位置"+CList);
			System.out.println("-----------------------------CList.size()的大小----------------------"+CList.size());
			
			int count=0;
			for(int a=0;a<CList.size();a++){
				int n = CList.get(a);
				if(n==0){
					count++;
				}
			}
			if(CList.size()==count){
				System.out.println("tunnel中不经过汇聚");
				continue;
			}
			
			//初步筛选tunnel
			for(int ii=CList.size()-1;ii>=0;ii--){			
				if(CList.get(ii)==0){
					CList.remove(ii);
					tunnelList.remove(ii);
					GList.remove(ii); 
				}
			}
			
			System.out.println("初步筛选的tunnel中第一条情况"+tunnelList.get(0));
			System.out.println("初步筛选的tunnel条数"+tunnelList.size());
			System.out.println("初步筛选后目标网元集合情况"+GList);
			System.out.println("初步筛选后目标网元集合大小"+GList.size());
			System.out.println("初步筛选后汇聚网元集合情况"+CList);
			System.out.println("初步筛选后汇聚网元集合大小"+CList.size());
			
			
			//8.查找目标网元到汇聚网元之间网元是否与环路上的网元想对应，从而筛选符合条件的tunnel
			
			//方案二执行 查看经过目标网元的tunnel是否包含汇聚网元,包含则选出来(放宽条件)
			
			List<Integer> list4 = ChangeTunnelService.selectTunnelIndex(tunnelList, path, AConvergenceNeId2);
			
//			//查找表格中某一行中 当前目标网元到汇聚网元之间的网元id集合
//			List<Integer> list4 = RingController.findSpecialNeIdList(i, (int)neId1);
//			System.out.println("第"+i+"行表格中目标网元到汇聚网元之间的网元id集合"+list4);
//
//			//查找经过目标网元的所有tunnel中目标网元到汇聚网元之间的网元(不包含目标,以及汇聚本身网元本身)
//			List<List<Integer>> TunnelList1 = ChangeTunnelService.findgoalNeToConvengenceNeFromMongo(CList, GList, tunnelList,path);
//			
//			//筛选满足条件的tunnel下标索引
//			List<Integer> list5 = ChangeTunnelService.GetFilterTunnelIndex(TunnelList1, list4);
//
//			System.out.println("第二次筛选的tunnel的条数:"+list5.size());
//			System.out.println("第二次筛选满足的tunnel的下标索引"+list5);
//			
//			//二次筛选,目标集合以及汇聚集合
//			
//			List<Integer> CList1 = ChangeTunnelService.selectFilterConditionCListAndGListIndex(list5, CList);
//			List<Integer> GList1 = ChangeTunnelService.selectFilterConditionCListAndGListIndex(list5, GList);		
//			System.out.println("二次筛选后目标网元集合情况"+GList1);
//			System.out.println("二次筛选后汇聚网元集合情况"+CList1);
				
			//筛选满足条件的tunnel
			List<Document> tunnelList1= ChangeTunnelService.findFilterConditionTunnel(tunnelList,list4);
			System.out.println("最终筛选的tunnel条数"+tunnelList1.size());
 
			if(tunnelList1.size()==0){
				continue;
			}
			
			System.out.println("第一条tunnel数据"+tunnelList1.get(0));
			Document Atunnel = tunnelList1.get(0);
			Document APath = (Document) ((ArrayList) Atunnel.get(path)).get(0);//获取该Tunnel中的workpath
			List<Object> ApathsList = (ArrayList) APath.get("pathsList");
			System.out.println("未进行业务操作之前的tunnel中第一条tunnel为"+tunnelList1.get(0));
			System.out.println("未进行业务操作之前的tunnel中第一条tunnel中pathlist中的长度为"+ApathsList.size());
			
			  
			//9.从删选出来的tunnel中,删除目标网元到汇聚网元之间的网元,进行业务操作
			
			//根据旧网元查找可用板卡和端口,以及加光纤
			
			List<BoardPort> BList = TunnelService.selectNewBoardAndNewPortNameFromOneTunnel(Atunnel, (int)neId1, AConvergenceNeId2, path);
	        
			//删网元以及更新板卡业务操作
			ChangeTunnelService.TunnelChange(tunnelList1, (int)neId1, AConvergenceNeId2, path, BList);
					
			//删除中间网元到目标网元之间的网元==业务切换
 			
			System.out.println("业务操作之后tunnel情况"+tunnelList1.get(0));
			System.out.println("业务操作之后tunnel条数"+tunnelList1.size());
			
			int _id = (int) tunnelList1.get(0).get("_id");
			int length = TunnelService.findNeIdLengthByPathsList(_id, "workPath");
			System.out.println("进行业务操作之后的tunnel中第一条tunnel中pathlist中的长度为"+length);
				
		}
	}
	
	
	

}
