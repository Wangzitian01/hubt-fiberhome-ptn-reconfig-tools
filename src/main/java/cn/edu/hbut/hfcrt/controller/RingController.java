package cn.edu.hbut.hfcrt.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import cn.edu.hbut.hfcrt.service.RingService;

/**
 * @author zitian
 *
 */

public class RingController {
	
	public static String ExcelPath="C:\\Users\\13409\\Desktop\\烽火\\环处理 - 副本(1).xls";
	public static int rowNum=87;
	
	
	@Test
	public void test() throws IOException{
		List<List<Integer>> list = RingService.findRingNeIdByAllRing(ExcelPath, 7);
		System.out.println(list.get(5));
		List<List<Integer>> list1 =RingService.findRingNeIdInfoAndControl(list);
		System.out.println(list1.get(5));
		
	}
	
	//获取左网元集合
	public static List<Integer> findMidLeftNeIdFromAllRingInfo(String Excel,int rowNum) throws IOException{
		
		List<List<Integer>> List =findRingNeIdByAllRing(Excel, rowNum);
		List<Integer> list = RingService.findMidLeftNeIdForEachRing(List);
		return list;
		
	}
	
	//获取左网元集合
	public static List<Integer> findMidRightNeIdFromAllRingInfo(String Excel,int rowNum) throws IOException{
		
		List<List<Integer>> List =findRingNeIdByAllRing(Excel, rowNum);
		List<Integer> list = RingService.findRightNeIdForEachRing(List);
		return list;
		
	}
	
	
	//获取表格中所有环路的网元ID,已经相邻去重,远处相同保留
	public static List<List<Integer>> findRingNeIdByAllRing(String ExcelPath,int rowNum) throws IOException{
		List<List<Integer>> list = RingService.findRingNeIdByAllRing(ExcelPath, rowNum);
		List<List<Integer>> list1 =RingService.findRingNeIdInfoAndControl(list);
		return list1;		
	}
	
	
	//在Excel表格中,获取某个环路中目标网元到最右边网元之间网元ID,(左开右闭)  方向向右 --->
	public static List<Integer> findSpecialNeIdList1(int i,int goalNeId) throws IOException{
		 List<List<Integer>> list = RingService.findRingNeIdByAllRing(ExcelPath, rowNum);
		 List<List<Integer>> list1 =RingService.findRingNeIdInfoAndControl(list);		 
		 List<Integer>list2 = RingService.findSpecialNeId(list1, i);//获取特定的一个环中的网元集合(集合中都是网元ID)
		 int lastNeIdIndex  = list2.size()-1; 
	     System.out.println("第"+i+"行表格中的网元集合为:"+list2);
		 int goalIndex = RingService.findLocationfromRing(list2, goalNeId); //获取目标网元在集合中的下标索引	
		 System.out.println("目标网元在集合中的下标索引"+goalIndex);		
		 List<Integer> list3 = RingService.findNeIdfromGoalNeToLastNe(list2, goalIndex, lastNeIdIndex);//获取目标网元到最后一个网元之间的所有网元ID
		 return list3;		 
	}
	
	
	//在Excel表格中,获取某个环路中目标网元到最右边网元之间网元ID,(左闭右开)  方向向左 --->
	public static List<Integer> findSpecialNeIdList(int i,int goalNeId) throws IOException{
		 List<List<Integer>> list = RingService.findRingNeIdByAllRing(ExcelPath, rowNum);
		 List<List<Integer>> list1 =RingService.findRingNeIdInfoAndControl(list); 
		 List<Integer>list2 = RingService.findSpecialNeId(list1, i);//获取特定的一个环中的网元集合(集合中都是网元ID)
	     System.out.println("第"+i+"行表格中的网元集合为:"+list2);
		 int goalIndex = RingService.findLocationfromRing(list2, goalNeId); //获取目标网元在集合中的下标索引	
		 System.out.println("目标网元在集合中的下标索引"+goalIndex);		
		 List<Integer> list3 = RingService.findNeIdfromGoalNeToFirstNe(list2, goalIndex, 0);//获取目标网元到最后一个网元之间的所有网元ID
		 return list3;		 
	}
	
	
	//去除重复的,保证有序的集合
	public static Set<String> Setprocess(List<String> list){
		Set<String> set=new LinkedHashSet<String>();
		for(int i=0;i<list.size();i++){
			String str = list.get(i);
			set.add(str);
		}
		return set;		
	}
	
	//List===>Set
	public static List<Set<String>> ListConvertToSet(List<List<String>> List){
		List<Set<String>> list =new ArrayList<Set<String>>();
		for(int i=0;i<List.size();i++){
			Set<String> set = Setprocess(List.get(i));
			list.add(set);
		}
		return list;
	}
	

	

}
