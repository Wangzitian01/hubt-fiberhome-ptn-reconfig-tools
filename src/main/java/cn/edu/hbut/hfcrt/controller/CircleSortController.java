package cn.edu.hbut.hfcrt.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.junit.Test;

import cn.edu.hbut.hfcrt.pojo.Condition;
import cn.edu.hbut.hfcrt.service.ChangeTunnelService;
import cn.edu.hbut.hfcrt.service.PortService;
import cn.edu.hbut.hfcrt.service.PtnService;
import cn.edu.hbut.hfcrt.service.TopoLines;
import cn.edu.hbut.hfcrt.utils.ReadExcel;
import cn.edu.hbut.hfcrt.utils.StringSplit;
public class CircleSortController {
	
	//黄石阳新五里湖;黄石阳新五里湖;黄石阳新饵料厂;黄石阳新麦口;黄石阳新麦口;
	//武九客专黄石阳新南湖沙场2;武九客专黄石阳新南湖沙场2;黄石阳新城南大桥;
	//黄石阳新城南大桥;黄石阳新粮食局;黄石阳新粮食局;
	//黄石阳新五里湖 黄石阳新饵料厂 黄石阳新麦口 武九客专黄石阳新南湖沙场2 黄石阳新城南大桥 黄石阳新粮食局
	
//	public static String path="C:\\Users\\13409\\Desktop\\烽火\\2020-08-19jhProblem.xls";

	@Test
	public void test() throws IOException{
		String path="C:\\Users\\13409\\Desktop\\烽火\\2020-08-19jhProblem.xls";
		List<Boolean> list =JudgeExcelCircleIsNotSort(path, 50);
		System.out.println(list);
	 						
	}


	public static List<Boolean> JudgeExcelCircleIsNotSort(String path,int rowNum) throws IOException{
		Boolean flag = null;
		List<Boolean> Bnlist=new ArrayList<Boolean>(); 
		List list = ReadExcel.readExcelToList1(path,rowNum);
		for(int i=0;i<list.size();i++){
//			System.out.println(list.get(i));
			String str = (String) list.get(i);
			List<String> list1 = StringSplit.SplitString(str);
//			System.out.println(list1);
//			System.out.println(list1.size());
			if(list1.size()<=4){
				flag=true;
				Bnlist.add(flag);
				continue;
			}
			int n=list1.size() / 2;
			int m=(list1.size() / 2)+2;
			String MidLeftNename=list1.get(n);
			String MidRightNeName=list1.get(m);
			int MidLeftNeId = PtnService.findNeId(MidLeftNename);
			int MidRightNeId = PtnService.findNeId(MidRightNeName);
			double topoNodeId1 = PtnService.findToponodeId(MidLeftNeId);
//			System.out.println("左边网元的topoNodeId是:"+topoNodeId1);
			double topoNodeId2 = PtnService.findToponodeId(MidRightNeId);
//			System.out.println("右边边网元的topoNodeId是:"+topoNodeId2);
			
			List<Condition> list2 = PtnService.findBoardIdAndPortNameByTopoId(topoNodeId1, topoNodeId2);
//			System.out.println("输出板卡和端口的信息"+list2);
			
//			System.out.println("该list集合的长度"+list2.size());
			double boardId = list2.get(0).getBoardId1();
			String portName= list2.get(0).getPortName1();
			double neId = list2.get(0).getNeId1();
//			System.out.println("中间左边网元id"+neId);
				
			List<Document> tunnelList = PtnService.selectTunnels1(boardId, portName, neId, "workPath");
//			System.out.println("tunnel的结果:"+tunnelList);
//			System.out.println("tunnel的条数"+tunnelList.size());
			
			
			List<Set<Integer>> list3 = PtnService.findNeIdByTunnelList(tunnelList);
//			System.out.println("满足条件的所有tunnel中所经过的网元id为"+list3);
			
			
			List<Integer> list4 =ChangeTunnelService.JudgeNeLocationInTunnelList((int)neId, tunnelList,path);
//			System.out.println("目标网元在所经过的tunnel中网元的位置"+list4);
			 
//			PortService.checkAdditionalPorts(boardId, aimPortSpeed);
			 			 
			List<Boolean> blist = ChangeTunnelService.JudgeNeNextIsNotMidRightNe(MidRightNeId, list4, tunnelList);
//			System.out.println(blist);
			flag= ChangeTunnelService.JudgeNeNextIsNotMidRightNe1(blist);
			Bnlist.add(flag);	
		}
		return Bnlist;
	 			
	}
 
	
 
	
	
	

}
