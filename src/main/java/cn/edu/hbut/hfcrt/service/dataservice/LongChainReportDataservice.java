/**  

* <p>Title: LongChainReportDataservice.java</p>  

* <p>Description: </p>  

* @author Aurore 

* @date 2020年6月23日  

* @version 1.0  

*/  
package cn.edu.hbut.hfcrt.service.dataservice;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import cn.edu.hbut.hfcrt.service.message.Datasource;
import cn.edu.hbut.hfcrt.service.message.IMessage;
import cn.edu.hbut.hfcrt.service.message.LongChainRecord;

public class LongChainReportDataservice implements IDataService {

	@Override
	public Datasource getDatasource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IMessage> get() {
		// TODO Auto-generated method stub
		List<IMessage> longChainReport = new ArrayList<IMessage>();
		
		InputStream is;
		try {
			//根据评估报告Excel表记录问题类型
			is = new FileInputStream(Datasource.FIBERHOME_ASSESSMENT_REPORT_EXCEL_DEFAULT_PATH);
			HSSFWorkbook wb =  new HSSFWorkbook(new POIFSFileSystem(is));
			HSSFSheet longChainSheet = wb.getSheet("长链节点比");
			
			int activeRowNumber = 3;
			
			while(true) {
				
				HSSFRow row = longChainSheet.getRow(activeRowNumber);
				if (row ==null) {
					break;
				}
				HSSFCell lie = row.getCell(0);//获得链路ID
				if (lie == null || lie.toString().trim().equals("")) {
					break;
				}
				HSSFCell longChain = row.getCell(4);
				if (longChain == null || longChain.toString().trim().equals("")) {
					break;
				}
				HSSFCell ring = row.getCell(5);
				if (ring == null || ring.toString().trim().equals("")) {
					break;
				}
				HSSFCell connectionNeName = row.getCell(6);
				if (connectionNeName == null || connectionNeName.toString().trim().equals("")) {
					break;
				}
				
				LongChainRecord record = new LongChainRecord();
				record.setLie(Integer.valueOf(row.getCell(0).toString()));
				record.setNelName(row.getCell(1).toString());
				record.setNelNum(Integer.valueOf(row.getCell(2).toString()));
				record.setRegular(row.getCell(3).toString());
				
				LongChainReportDataservice Dataservice = new LongChainReportDataservice();
				record.setLongChain(Dataservice.toList(row.getCell(4).toString()));
				record.setRing(Dataservice.toList(row.getCell(5).toString()));
				record.setConnectionNeName(row.getCell(6).toString());
				record.setNeNameA(row.getCell(7).toString());
				record.setNeNameB(row.getCell(8).toString());
				
				longChainReport.add(record);
				activeRowNumber++;
				
				
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return longChainReport;
	}

	public List<String> toList(String str) {
		 
		List<String> list = new ArrayList<String>();
		if (str != null||!str.toString().trim().equals("")) {
			String[] aArr = str.split(";");
			for (String string : aArr) {
				list.add(string);
//				System.out.println(string);
			}
		}
//		System.out.println(list);
		return list;
		
	}
	
	@Override
	public IMessage get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean set(IMessage message) {
		// TODO Auto-generated method stub
		return false;
	}

}
