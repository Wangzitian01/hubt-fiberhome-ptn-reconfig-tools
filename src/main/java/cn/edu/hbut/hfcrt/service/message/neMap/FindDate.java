package cn.edu.hbut.hfcrt.service.message.neMap;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import cn.edu.hbut.hfcrt.constant.ChlAssessmentResult;
import cn.edu.hbut.hfcrt.service.dataservice.ChlReportDataservice;
import cn.edu.hbut.hfcrt.service.dataservice.LongChainReportDataservice;
import cn.edu.hbut.hfcrt.service.message.IMessage;
import cn.edu.hbut.hfcrt.service.message.LongChainRecord;

public class FindDate {
	public  boolean ifInclude(List<String> list,String str){
        for(int i=0;i<list.size();i++){
               if (list.get(i).indexOf(str)!=-1) {
                   return true;
               }
           }
           return false;
       }
   
	public LongChainRecord getDate1(String neName) {//查找未成环网元所在的链在Excel表中的位置
		LongChainRecord record = new LongChainRecord();
		InputStream is;
		try {
			//在排序长链表Excel中查找长链位置
			is = new FileInputStream(Datasource.FIBERHOME_ASSESSMENT_REPORT_EXCEL_DEFAULT_PATH);
			HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(is));
			HSSFSheet longChainSheet = wb.getSheet("长链节点比");
//			System.out.println("长链节点比的总行数="+longChainSheet.getLastRowNum());
			
			for (int rownum =3 ; rownum <= longChainSheet.getLastRowNum(); rownum++) {
				HSSFRow row = longChainSheet.getRow(rownum);
//				System.out.println("rownum="+rownum);
				if (row == null) {
					continue;
				}
				Cell longChain = row .getCell(4);//第5列为顺序输出的长链
				Cell ring = row .getCell(5);//第6列为顺序输出的环
				
				if (longChain == null || longChain.toString().trim().equals("")) {
					continue;
				}
				if (ring == null || ring.toString().trim().equals("")) {
					continue;
				}
//				System.out.println("longChain.getCellType()="+longChain.getCellType());
				if (longChain.getCellType() ==CellType.STRING) {
//					System.out.println(longChain.getRichStringCellValue().getString().trim());
					int count = 0;
					for (String tmp = longChain.getRichStringCellValue().toString(); tmp !=null && tmp .length()>=neName.length();) {
						if (tmp.indexOf(neName )== 0) {
							count ++;
						}
						tmp = tmp.substring(1);
//						System.out.println("tmp="+tmp);
					}
					if (count!=0) {
						record.setLie(Integer.valueOf(row.getCell(0).toString()));
						record.setNelName(row.getCell(1).toString());
						record.setNelNum(Integer.valueOf(row.getCell(2).toString()));
						record.setRegular(row.getCell(3).toString());
						record.setLongChain(Arrays.asList(row.getCell(4).toString()));
						record.setRing(Arrays.asList(row.getCell(5).toString()));
						record.setConnectionNeName(row.getCell(6).toString());
						record.setNeNameA(row.getCell(7).toString());
						record.setNeNameB(row.getCell(8).toString());
//						System.out.println("longChain="+longChain);
//						return row.getRowNum();
//						System.out.println("row.getRowNum()="+row.getRowNum());
					}else {
						record = null;
					}
					
				}
				
			}
		
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
//		System.out.println("record="+record);
		return record;
		
		
	}

	public LongChainRecord getDate(String neName)  {//查找未成环网元所在的链在Excel表中的位置
		
		LongChainReportDataservice service = new LongChainReportDataservice();
		List<IMessage> allRecord = service.get();
		
		LongChainRecord abnormalRecord =new LongChainRecord();
		int count =0;
		if (allRecord.size() > 0) {
			for (IMessage  message : allRecord) {
				LongChainRecord record = (LongChainRecord) message;
				count++;
				if (record.getLongChain()!=null) {
					List<String> longChain = record.getLongChain();
					for (int i = 0; i < longChain.size(); i++) {
//						System.out.println(longChain.get(i));
						if (longChain.get(i).equals(neName)) {
							System.out.println();
							System.out.println("["+neName+"]"+"  in   "+longChain);
							System.out.println("FindDate.java");
							System.out.println();
							abnormalRecord = record;
							break;
						}
					}
				}
			}
		}
		return abnormalRecord;
		
		
	}
}
