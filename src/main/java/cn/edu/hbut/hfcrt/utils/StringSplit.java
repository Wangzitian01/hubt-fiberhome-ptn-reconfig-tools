package cn.edu.hbut.hfcrt.utils;

import java.util.ArrayList;
import java.util.List;
 

 
 

public class StringSplit {
	
	
	public static List<String> SplitString(String string){
		List<String> list=new ArrayList<String>();
		String[] split = string.split(";");
		for(int i=0;i<split.length;i++){
			list.add(split[i]);
		}
		return list;     
	}
	
	 public static List<String> RemoveString(List<String> list){
		 List<String> List=new ArrayList<String>();
		 for(int i=0;i<list.size();i++){
			 String str = list.get(i);
			 String removeStr = ";";
			 String newstr = str.replace(removeStr, " ");
			 List.add(newstr);		 
		 }	 
		return List;	 
	 }

}
