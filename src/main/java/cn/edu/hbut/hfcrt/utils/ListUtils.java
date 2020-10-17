package cn.edu.hbut.hfcrt.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListUtils {
	
	
	//获取map里面的key值
	public static List<Integer> findKeySetFromMap(Map<Integer,List<Integer>> map){
		  List<Integer> list=new ArrayList<Integer>();
		  //使用迭代器，获取key;
		  Iterator<Integer> iter = map.keySet().iterator();
		  while(iter.hasNext()){
		   Integer key=iter.next();
		   list.add(key);
		   //List<Integer> value = map.get(key);
//		   System.out.println(key+" "+value);
		  }
		return list;
	}
		
	public static List<String> listUtils(List<String> List){
		List<String> list =new ArrayList<String>();
		for(int i=0;i<List.size();i++){
			int j=i+1;
			if(j<List.size()){
				String str1 = List.get(i);
				String str2 = List.get(j);
				System.out.println("str1"+str1);
				System.out.println("str2"+str2);
				System.out.println("str1是否等于str2"+str1==str2);
				if(str1==str2){
					System.out.println("i的值:"+i);
					list.add(str1);
					i+=1;
				}else{
					System.out.println("ii的值"+i);
					list.add(str1);
				}
			}else{
				list.add(List.get(j-1));
			}			 
		}		
		return list;
	}
	
	
	
	//
	public static List<Integer> listUtils1(List<Integer> List){
		List<Integer> list =new ArrayList<Integer>();
		for(int i=0;i<List.size();i++){
			int j=i+1;
			if(j<List.size()){
				int str1 = List.get(i);
				int str2 = List.get(j);
				if(str1==str2){
					list.add(str1);
					i+=1;
				}else{
					list.add(str1);
				}
			}else{
				list.add(List.get(j-1));
			}			 
		}		
		return list;
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		 List<Integer> list=new ArrayList<Integer>();
		 list.add(1);
		 list.add(2);
		 list.add(3);
		 list.add(3);
		 list.add(4);
		 list.add(5);
		 list.add(6);
		 list.add(7);
		 list.add(8);
		 list.add(8);
		 list.add(8);
		 list.add(8);
		 list.add(9);
		 list.add(7);
		 list.add(7);
		 list.add(6);
		 System.out.println(list);
		 List<Integer> List = listUtils1(list);
		 System.out.println(List);
	}

}
