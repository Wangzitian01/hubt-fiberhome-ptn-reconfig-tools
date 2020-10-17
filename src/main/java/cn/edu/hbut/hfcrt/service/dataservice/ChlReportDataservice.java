/**  

* <p>Title: ChlRingedNeRateReportDataservice.java</p>  

* <p>Description: </p>  

* @author Aurore 

* @date 2020年5月13日  

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

import cn.edu.hbut.hfcrt.constant.ChlAssessmentResult;
import cn.edu.hbut.hfcrt.constant.Nelevel;
import cn.edu.hbut.hfcrt.service.message.ChlRecord;
import cn.edu.hbut.hfcrt.service.message.Datasource;
import cn.edu.hbut.hfcrt.service.message.IMessage;

public class ChlReportDataservice implements IDataService  {

	public Datasource getDatasource() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IMessage> get() {
		// TODO Auto-generated method stub
		List<IMessage> chlRingedNeRateReport = new ArrayList<IMessage>();
		
		InputStream is;
		try {
			//根据评估报告Excel表记录问题类型
			is = new FileInputStream(Datasource.FIBERHOME_ASSESSMENT_REPORT_EXCEL_DEFAULT_PATH);
			HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(is));
			HSSFSheet chlRingedNeRateSheet = wb.getSheet("节点成环率");
			
			int activeRowNumber = 6;
			
			while (true) {
			
				HSSFRow row = chlRingedNeRateSheet.getRow(activeRowNumber);
				
				if (row == null) {
					break;
				}
				 HSSFCell nelevel = row.getCell(0);//获得网元层次 接入层-汇聚层
				 if (nelevel == null || nelevel.toString().trim().equals("")) {
					break;
				}
				
				 ChlRecord record = new ChlRecord();
				 
				 switch (nelevel.toString()) {
				 case "接入层":
					 record.setNelevel(Nelevel.JIERUCENG);//网元层次
					 break;
				 case "汇聚层":
					 break;
				}
				 
				record.setNelName(row.getCell(1).toString());
				record.setNelType(row.getCell(2).toString());
				record.setNelIp(row.getCell(3).toString());
				record.setUseClass(row.getCell(4).toString());
				
				switch (row.getCell(5).toString()) {
				case "成环":
					record.setAssessmentResult(ChlAssessmentResult.LOOPED);
					break;
				case "未成环":
					record.setAssessmentResult(ChlAssessmentResult.UNLOOPED);
					break;
					
				}
				
				chlRingedNeRateReport.add(record);
				activeRowNumber++;
						 
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return chlRingedNeRateReport;
	}

	public IMessage get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean set(IMessage message) {
		// TODO Auto-generated method stub
		return false;
	}

}
