package cn.edu.hbut.hfcrt.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.edu.hbut.hfcrt.utils.ListUtils;
import cn.edu.hbut.hfcrt.utils.ReadExcel;
import cn.edu.hbut.hfcrt.utils.StringSplit;
/**
 * @author zitian
 *
 */
public class RingService {
	
	public static String ExcelPath="C:\\Users\\13409\\Desktop\\烽火\\环处理 - 副本(1).xls";
	public static int rowNum=87;
	
	
	
	//获取环路信息,去重且保留远处相同
	public static List<List<String>> findRingInfoAndControl(String ExcelPath,int rowNum) throws IOException{
		List<List<String>> List=new ArrayList<List<String>>();
		List<String> list = ReadExcel.readCircleFromExcel1(ExcelPath, rowNum);
		for(int i=0;i<list.size();i++){
			List<String> list1 = StringSplit.SplitString(list.get(i));
			List<String> list2 = ListUtils.listUtils(list1);
			List.add(list2); 
		}		
		return List;	
	}
		
	///获取环路信息,去重且保留远处相同
	public static List<List<Integer>> findRingNeIdInfoAndControl(List<List<Integer>> List){
		List<List<Integer>> List1=new ArrayList<List<Integer>>();
		for(int i=0;i<List.size();i++){
			List<Integer> list = List.get(i);
			List<Integer> list1 = ListUtils.listUtils1(list);
			List1.add(list1);
		}		
		return List1;
		
	}
	//获取重复的所有网元环路ID
	public static List<List<Integer>> findRingNeIdByAllRing(String ExcelPath,int rowNum)throws IOException{
		List<List<Integer>> List =new ArrayList<List<Integer>>();
		List<String> list = ReadExcel.readCircleFromExcel1(ExcelPath, rowNum);
		for(int i=0;i<list.size();i++){
			List<String> list1 = StringSplit.SplitString(list.get(i));
			List<Integer> list2=new ArrayList<Integer>();
			for(int j=0;j<list1.size();j++){
				String neName = list1.get(j);
				int neId = NeService.findNeId(neName);
				list2.add(neId);				
			}
			List.add(list2);
		}		
		return List;	
	}
	
	
	//获取中间左网元的集合
	public static List<Integer> findMidLeftNeIdForEachRing(List<List<Integer>> List){
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<List.size();i++){
			int j = List.get(i).size();
			if(j%2==0){
				int NeId = List.get(i).get((j/2)-1);
				list.add(NeId);
			}
			else{
				int NeId = List.get(i).get((j/2));
				list.add(NeId);
			}
		}
		return list;
		
	}
	
	
	//获取中间右网元的集合
	public static List<Integer> findRightNeIdForEachRing(List<List<Integer>> List){
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<List.size();i++){
			int j = List.get(i).size();
			if(j%2==0){
				int NeId = List.get(i).get((j/2));
				list.add(NeId);
			}
			else{
				int NeId = List.get(i).get((j/2)+1);
				list.add(NeId);
			}
		}
		return list;	
	}
	
	
	
	//获取环路信息
	public static List<List<String>> findRingInfo(String ExcelPath,int rowNum) throws IOException{
		List<List<String>> List=new ArrayList<List<String>>();
		List<String> list = ReadExcel.readCircleFromExcel1(ExcelPath, rowNum);
		for(int i=0;i<list.size();i++){
			List<String> list1 = StringSplit.SplitString(list.get(i));
			Set<String> set =SetProcess(list1);
			List<String>list2 = SetConvertToList1(set);
			List.add(list2);
		}
		return List;	
	}
	
	
	//去掉重复的网元
	public static Set<String> SetProcess(List<String> list){
		Set<String> set=new LinkedHashSet<String>();
		for(int i=0;i<list.size();i++){
			set.add(list.get(i));
		}
		return set;
	}
	
	
	
	public static List<String> SetConvertToList1(Set<String> Set){
		List<String> list=new ArrayList<String>();
		for(String str:Set){
			list.add(str);
		}
		return list;	
	}
	
	//将环路的网元名转变为网元ID
	public static List<List<Integer>> findNeNameToNeId(List<Set<String>> SetList){
		List<List<Integer>> List=new ArrayList<List<Integer>>();	 
		for(int i=0;i<SetList.size();i++){
			List<Integer> list1=new ArrayList<Integer>();
			List<String> list = SetConvertToList1(SetList.get(i));
			for(int j=0;j<list.size();j++){
				String neName = list.get(j);
				int neId = NeService.findNeId(neName);
				list1.add(neId);
			}	 
			List.add(list1);		 
		}
		return List;	
	}
 
		
 
	//获取具体某条环路的网元ID 
	public static List<Integer> findSpecialNeId(List<List<Integer>> List,int i){
		List<Integer> list = List.get(i);
		return list;
	}
	
	//获取具体某条环路的网元ID,并查找目标网元的下标索引
	public static int findLocationfromRing(List<Integer> list,int goalNeId){
		int k = list.indexOf(goalNeId);
		return  k;		
	}
	
	//获取某条环路的目标网元到汇聚网元之间的网元id   ==>表格
	public static List<Integer> findNeIdfromGoalNeToConvergenceNe(List<Integer> list,int i,int j){
		List<Integer> List=new ArrayList<Integer>();
		for(int k=i+1;k<=j;k++){
			System.out.println("k的值为:"+k);
			int NeId = list.get(k);
			List.add(NeId);
		} 	
		return List;	
	}
	
	//获取某条环路的目标网元到汇聚网元之间的网元id   ==>表格  方向向右
	public static List<Integer> findNeIdfromGoalNeToLastNe(List<Integer> list,int i,int j){
		List<Integer> List=new ArrayList<Integer>();
		for(int k=i+1;k<=j;k++){
			int NeId = list.get(k);
			List.add(NeId);
		} 	
		return List;	
	}
	
	//获取某条环路的目标网元到汇聚网元之间的网元id   ==>表格  方向向左
	public static List<Integer> findNeIdfromGoalNeToFirstNe(List<Integer> list,int i,int j){
		List<Integer> List=new ArrayList<Integer>();
		for(int k=i-1;k>=j;k--){
			int NeId = list.get(k);
			List.add(NeId);
		} 	
		return List;	
	}
	
	
	//表中每个巨环环路的中间左网元
	public static List<String> findMidLeftNeName() throws IOException{
		List<String> List=new ArrayList<String>();
		List<List<String>> list =RingService.findRingInfo(ExcelPath, rowNum);
		for(int i=0;i<list.size();i++){
			int j = list.get(i).size();
			if(j%2==0){
				String str = list.get(i).get((j/2)-1);
				List.add(str);
			}
			else{
				String str = list.get(i).get((j/2));
				List.add(str);
			}
		}
		return List;
	}
		
	//表中每个巨环环路的中间右网元
	public static List<String> findMidRightNeName() throws IOException{
		List<String> List=new ArrayList<String>();
		List<List<String>> list =RingService.findRingInfo(ExcelPath, rowNum);
		for(int i=0;i<list.size();i++){
			int j = list.get(i).size();
			if(j%2==0){
				String str = list.get(i).get(j/2);
				List.add(str);
			}
			else{
				String str = list.get(i).get(j/2+1);
				List.add(str);
			}
		}
		return List;
	}
	
	

}
