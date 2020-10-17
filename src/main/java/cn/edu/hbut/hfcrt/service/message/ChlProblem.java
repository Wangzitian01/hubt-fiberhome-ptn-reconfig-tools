/**  

* <p>Title: ChlProblem.java</p>  

* <p>Description: </p>  

* @author Aurore 

* @date 2020年4月14日  

* @version 1.0  

*/  
package cn.edu.hbut.hfcrt.service.message;
/**  

* <p>Title: ChlProblem.java</p>  

* <p>Description: </p>  

* @author Aurore 

* @date 2020年3月5日  

* @version 1.0  

*/  


import java.util.Date;

/**
 * @author sha
 *
 */
public class ChlProblem {

	private String level;//网元层次
	private String nelName;//网元名称
	private String nelType;//网元类型
	private String nelIp;//网元IP
	private String useClass;//业务使用分类
	private String regular;//评估结果
	
	private String totalNum;//网元数量
	private String circleNum;//成环数量
	private String rate;//成环比例
	private String result;//评估结果
	
	public String toString() {
		return "节点成环率：level="+level+",totalNum="+totalNum+",circleNum="+circleNum+",rate="+rate+",result="+result+
				"ChlProblem[level="+level+",nelName="+nelName+",nelType="+nelType+",nelIp="+nelIp+",useClass="+useClass+",regular="+regular+"]";
	}
	
	public ChlProblem() {
		super();
	}
	
	public ChlProblem(String level, String nelName, String nelType, String nelIp, String useClass, String regular) {
		// TODO Auto-generated constructor stub
		super();
		this.level=level;
		this.nelName=nelName;
		this.nelType=nelType;
		this.nelIp=nelIp;
		this.useClass=useClass;
		this.regular=regular;
	}


	public ChlProblem(String level, String totalNum, String circleNum, String rate, String result) {
		// TODO Auto-generated constructor stub
		super();
		this.level=level;
		this.circleNum=circleNum;
		this.rate=rate;
		this.result=result;
	}


	
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
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



	public String getRegular() {
		return regular;
	}
	public void setRegular(String regular) {
		this.regular = regular;
	}


	public String getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}



	public String getCircleNum() {
		return circleNum;
	}
	public void setCircleNum(String circleNum) {
		this.circleNum = circleNum;
	}

	
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}


	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}

	

}

