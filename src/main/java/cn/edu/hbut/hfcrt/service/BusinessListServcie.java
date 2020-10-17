package cn.edu.hbut.hfcrt.service;

import java.util.List;

import cn.edu.hbut.hfcrt.constant.BusinessList;

public class BusinessListServcie {
	
	public static BusinessList AddClass(List<Integer> List,int MidNeId,int ConvengerceNeId,double boardId,String PortName){
		BusinessList BL=new BusinessList();
		BL.setTunnelId(List.toString());
		BL.setMidNeId(MidNeId);
		BL.setConvengerceNeId(ConvengerceNeId);
		BL.setBoardId(boardId);
		BL.setNewPortName(PortName);
		return BL;
		
		
	}

}
