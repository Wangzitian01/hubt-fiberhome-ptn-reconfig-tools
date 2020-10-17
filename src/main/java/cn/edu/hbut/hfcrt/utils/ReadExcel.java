package cn.edu.hbut.hfcrt.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import cn.edu.hbut.hfcrt.constant.BusinessList;
import cn.edu.hbut.hfcrt.pojo.Convergence;
import cn.edu.hbut.hfcrt.service.message.ChlProblem;

/**
 * @author ZTW
 *
 */

public class ReadExcel {
	
	//获取环路信息  ==》该巨环有哪些网元
	public static List<String> readCircleFromExcel(String ExcelPath,int rowNum) throws IOException{
		List<String> list=new ArrayList<String>();
		InputStream is = new FileInputStream(ExcelPath);//
		POIFSFileSystem fs = new POIFSFileSystem(is);//
		HSSFWorkbook workbook = new HSSFWorkbook(fs);//
		HSSFSheet hssfSheet = workbook.getSheetAt(4);//读取表的index
		if (hssfSheet == null) {
			return null;
		}
		for (int i = 1; i < rowNum; i++) {
			HSSFRow hssfRow = hssfSheet.getRow(i);
			HSSFCell a1 = hssfRow.getCell(0);
			String aa1 = a1.toString();			
			list.add(aa1);
			 
		}	
		return list;
 
	}
	
	//将结果写入Excel表中去
	public static void writeListToExcel(Map<Integer,List<Integer>> map,String excelPath) throws IOException{ 
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("tunnel变化1情况表");
		List<Integer> keyList = ListUtils.findKeySetFromMap(map);
		for (int i = 0; i < map.size(); i++) {
		    int id = keyList.get(i);
		    List list=map.get(id);
			HSSFRow row = sheet.createRow(i);
			row.createCell(0).setCellValue(id);
			row.createCell(1).setCellValue(list.toString());
		 
		}
		FileOutputStream fileout = new FileOutputStream(excelPath);
		workbook.write(fileout);
		fileout.close();
	}
	
	
	
	//将列表清单结果写入Excel表中去
	public static void writeListToExcel1(List<BusinessList>BSL,String excelPath) throws IOException{ 
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("业务列表清单情况表");	 
		for (int i = 1; i <= BSL.size(); i++) {
			HSSFRow row = sheet.createRow(i);
//			row.createCell(5).setCellValue(BSL.get(i-1).getTunnelId());
//			row.createCell(6).setCellValue(BSL.get(i-1).getMidNeId());
//			row.createCell(7).setCellValue(BSL.get(i-1).getConvengerceNeId());
			row.createCell(8).setCellValue(BSL.get(i-1).getBoardId());
			row.createCell(9).setCellValue(BSL.get(i-1).getNewPortName());	 
		}
		FileOutputStream fileout = new FileOutputStream(excelPath);
		workbook.write(fileout);
		fileout.close();
	}
	
	
	
	public static List<String> readCircleFromExcel1(String ExcelPath,int rowNum) throws IOException{
		List<String> list=new ArrayList<String>();
		InputStream is = new FileInputStream(ExcelPath);//
		POIFSFileSystem fs = new POIFSFileSystem(is);//
		HSSFWorkbook workbook = new HSSFWorkbook(fs);//
		HSSFSheet hssfSheet = workbook.getSheetAt(4);//读取表的index
		if (hssfSheet == null) {
			return null;
		}
		for (int i = 1; i < rowNum; i++) {
			HSSFRow hssfRow = hssfSheet.getRow(i);
			HSSFCell a1 = hssfRow.getCell(0);
			String aa1 = a1.toString();			
			list.add(aa1);	 
		}
		return list;
	}
	
	
	
	
	
	
	//获取A端汇聚网元
	public static List<String> readAConvergenceFromExcel(String ExcelPath,int rowNum) throws IOException{		
		List<String> list =new ArrayList<String>();
		InputStream is = new FileInputStream(ExcelPath);//
		POIFSFileSystem fs = new POIFSFileSystem(is);//
		HSSFWorkbook workbook = new HSSFWorkbook(fs);//
		HSSFSheet hssfSheet = workbook.getSheetAt(4);//读取表的index
		if (hssfSheet == null) {
			return null;
		}
		for (int i = 1; i < rowNum; i++) {
			HSSFRow hssfRow = hssfSheet.getRow(i);

			HSSFCell a1 = hssfRow.getCell(2);

			String aa1 = a1.toString();
			
			if(aa1=="tunnel不经过汇聚"){
				aa1="123456";
			}		 						
			list.add(aa1);
			 
		}
		return list;
		
	}
	
	// 获取F端汇聚网元
	public static List<String> readFConvergenceFromExcel(String ExcelPath,int rowNum) throws IOException{		
		List<String> list =new ArrayList<String>();
		InputStream is = new FileInputStream(ExcelPath);//
		POIFSFileSystem fs = new POIFSFileSystem(is);//
		HSSFWorkbook workbook = new HSSFWorkbook(fs);//
		HSSFSheet hssfSheet = workbook.getSheetAt(4);//读取表的index
		if (hssfSheet == null) {
			return null;
		}
		for (int i = 1; i < rowNum; i++) {
			HSSFRow hssfRow = hssfSheet.getRow(i);

			HSSFCell a1 = hssfRow.getCell(4);

			String aa1 = a1.toString();	 						
			list.add(aa1);			 
		}
		return list;		
	}
	
	
	public static List<Convergence> readExcelToList2(String ExcelPath,int rowNum) throws IOException{		
		List<Convergence> list =new ArrayList<Convergence>();
		InputStream is = new FileInputStream(ExcelPath);//
		POIFSFileSystem fs = new POIFSFileSystem(is);//
		HSSFWorkbook workbook = new HSSFWorkbook(fs);//
		HSSFSheet hssfSheet = workbook.getSheetAt(3);//读取表的index
		if (hssfSheet == null) {
			return null;
		}
		for (int i = 4; i < rowNum; i++) {
			HSSFRow hssfRow = hssfSheet.getRow(i);

			HSSFCell a1 = hssfRow.getCell(6);
			HSSFCell a2 = hssfRow.getCell(8);
			String aa1 = a1.toString();
			String aa2 = a2.toString();
			Convergence convergence=new Convergence();
			convergence.setAconvergence(Integer.valueOf(aa1));
			convergence.setFconvergence(Integer.valueOf(aa2));
			list.add(convergence);			 
		}	
		return list;		
	}

	
	public static List readExcelToList1(String ExcelPath,int rowNum) throws IOException{
		List<List<String>> lists=new ArrayList<List<String>>();
		List<String> list =new ArrayList<String>();
		InputStream is = new FileInputStream(ExcelPath);//
		POIFSFileSystem fs = new POIFSFileSystem(is);//
		HSSFWorkbook workbook = new HSSFWorkbook(fs);//
		HSSFSheet hssfSheet = workbook.getSheetAt(0);//读取表的index
		if (hssfSheet == null) {
			return null;
		}
		for (int i = 4; i < rowNum; i++) {
			HSSFRow hssfRow = hssfSheet.getRow(i);

			HSSFCell a2 = hssfRow.getCell(2);

			String aa2 = a2.toString();

			list.add(aa2);
			 
		}	
		return list;		
	}
	
	
	public static <type> List<type> readExeclToList(String ExcelPath,int rowNum) throws IOException{
		List<ChlProblem> list = new ArrayList<ChlProblem>();//
		InputStream is = new FileInputStream(ExcelPath);//
		POIFSFileSystem fs = new POIFSFileSystem(is);//
		HSSFWorkbook workbook = new HSSFWorkbook(fs);//
		HSSFSheet hssfSheet = workbook.getSheetAt(0);//读取表的index
		if (hssfSheet == null) {
			return null;
		}
		for (int i = 0; i < 2; i++) {
			HSSFRow hssfRow = hssfSheet.getRow(i);
			HSSFCell a1 = hssfRow.getCell(0);
			HSSFCell a2 = hssfRow.getCell(1);
			HSSFCell a3 = hssfRow.getCell(2);
			HSSFCell a4 = hssfRow.getCell(3);
			HSSFCell a5 = hssfRow.getCell(4);
			String aa1 = a1.toString();
			String aa2 = a2.toString();
			String aa3 = a3.toString();
			String aa4 = a4.toString();
			String aa5 = a5.toString();
			if (aa5.equals("gaoqing")) {
				continue;
			}
			ChlProblem chlP = new ChlProblem(aa1,aa2,aa3,aa4,aa5);
			list.add(chlP);	
		}
		for (int i = 5; i < rowNum; i++) {
			HSSFRow hssfRow = hssfSheet.getRow(i);
			HSSFCell a1 = hssfRow.getCell(0);
			HSSFCell a2 = hssfRow.getCell(1);
			HSSFCell a3 = hssfRow.getCell(2);
			HSSFCell a4 = hssfRow.getCell(3);
			HSSFCell a5 = hssfRow.getCell(4);
			HSSFCell a6 = hssfRow.getCell(5);
			String aa1 = a1.toString();
			String aa2 = a2.toString();
			String aa3 = a3.toString();
			String aa4 = a4.toString();
			String aa5 = a5.toString();
			String aa6 = a6.toString();
			if (aa6.equals("chenghuan")) {
				continue;
			}
			ChlProblem chlP = new ChlProblem(aa1,aa2,aa3,aa4,aa5,aa6);
			list.add(chlP);	
		}
		return (List<type>) list;
	}

}
