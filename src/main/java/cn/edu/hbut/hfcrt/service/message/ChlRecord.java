/**  

* <p>Title: ChlRingedNeRateRecord.java</p>  

* <p>Description: </p>  

* @author Aurore 

* @date 2020年5月12日  

* @version 1.0  

*/  
package cn.edu.hbut.hfcrt.service.message;

import org.bson.Document;

import cn.edu.hbut.hfcrt.constant.ChlAssessmentResult;
import cn.edu.hbut.hfcrt.constant.Nelevel;

/**
*/

/**
 * @author sha
 * 结果的类型
 *
 */
public class ChlRecord extends Message implements IMessage {
	
	private Nelevel nelevel;//网元层次
	private String nelName;//网元名称
	private String nelType;//网元类型
	private String nelIp;//网元IP
	private String useClass;//业务使用分类
	private ChlAssessmentResult assessmentResult;//评估结果
	
	public Nelevel getNelevel() {
		return nelevel;
	}
	public void setNelevel(Nelevel nelevel) {
		this.nelevel = nelevel;
	}
	public String getNelName() {
		return nelName;
	}
	public void setNelName(String nelName) {
		this.nelName = nelName;
	}
	public String getNelType() {
		return nelType;
	}
	public void setNelType(String nelType) {
		this.nelType = nelType;
	}
	public String getNelIp() {
		return nelIp;
	}
	public void setNelIp(String nelIp) {
		this.nelIp = nelIp;
	}
	public String getUseClass() {
		return useClass;
	}
	public void setUseClass(String useClass) {
		this.useClass = useClass;
	}
	public ChlAssessmentResult getAssessmentResult() {
		return assessmentResult;
	}
	public void setAssessmentResult(ChlAssessmentResult assessmentResult) {
		this.assessmentResult = assessmentResult;
	}
	
	
		
	

}
