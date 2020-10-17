package cn.edu.hbut.hfcrt.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

 
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import cn.edu.hbut.hfcrt.service.message.ChlProblem;

public class ReadWrite {
	
	/**
	* @Title: readExcelToList
	* @Description: (描述这个方法的作用)
	* @param excelPath 需要读取的Excel路径
	* @param rowNum 需要读取的Excel行数
	* @return 返回对应list
	* @throws IOException
	* @return List<ChlProblem>
	*/
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
	/**
	* @Title: writeListToExcel
	* @Description: 将list写入Excel
	* @param list
	* @param excelPath
	* @throws IOException
	* @return void
	*/
	public static void writeListToExcel(List<ChlProblem> list,String excelPath) throws IOException{
		int count  = 0;
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("CHLProblem");
		for (int i = 0; i < list.size(); i++) {
//			HSSFRow row = sheet.createRow(i);
//			row.createCell(0).setCellValue(list.get(i).getLevel());
//			row.createCell(1).setCellValue(list.get(i).getTotalNum());
//			row.createCell(2).setCellValue(list.get(i).getCircleNum());
//			row.createCell(3).setCellValue(list.get(i).getRate()+"%");
//			row.createCell(4).setCellValue(list.get(i).getResult());
			count++;
		}
		for (int i = count+1; i < list.size(); i++) {
			HSSFRow row = sheet.createRow(i);
//			row.createCell(0).setCellValue(list.get(i).getLevel());
//			row.createCell(1).setCellValue(list.get(i).getNelName());
//			row.createCell(2).setCellValue(list.get(i).getNelType());
//			row.createCell(3).setCellValue(list.get(i).getNelIp());
//			row.createCell(4).setCellValue(list.get(i).getUseClass());
//			row.createCell(5).setCellValue(list.get(i).getRegular());
		}
		
		FileOutputStream fileout = new FileOutputStream(excelPath);
		workbook.write(fileout);
		fileout.close();
	}	
	/**
	* @Title: chlExcelCompare
	* @Description: 比较CHL成环问题的解决效果
	* @param list1 有问题的成环率excel
	* @param list2 修改后的成环率excel
	* @return void
	*/
	public void chlExcelCompare(List<ChlProblem> list1,List<ChlProblem> list2) {
//		int count = 0;
//		for(int i=0;i<list1.size();i++) {
//			 if(list2.get(i).getResult().equals("正常"))
//				 count++;
//		}
//		int remain = list1.size();
//		remain -= count;
		DecimalFormat df = new DecimalFormat("0.00");
		double rate = ((double) (list1.size()-list2.size()))/((double) list1.size()) * 100;
		System.out.println("CHL问题已解决："+ (list1.size()-list2.size()) +" 条，"+"未解决："+ list2.size() +" 条，解决率: "+ df.format(rate) +"% 。");
		
	}
	
	
	
}
