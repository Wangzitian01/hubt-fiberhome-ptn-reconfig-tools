/**  

* <p>Title: ne.java</p>  

* <p>Description: </p>  

* @author Aurore 

* @date 2020年4月14日  

* @version 1.0  

*/  
package cn.edu.hbut.hfcrt.service.message.neMap;

public class ne {
	private String basicDomainid;
	private boolean convergence;
	private int neId;
	private String neIp;
	private String neName;
	private String neTypeName;
	
	public String tosString() {
		return "{"+"basicDomainid="+this.basicDomainid+"convergence="+this.basicDomainid
				+"neId="+this.neId+"neIp="+this.neIp+"neName="+this.neName+"neTypeName="+this.neTypeName;
	}
	
	public String getBasicDomainid() {
		return basicDomainid;
	}
	public void setBasicDomainid(String basicDomainid) {
		this.basicDomainid = basicDomainid;
	}

	public boolean isConvergence() {
		return convergence;
	}

	public void setConvergence(boolean convergence) {
		this.convergence = convergence;
	}

	public int getNeId() {
		return neId;
	}

	public void setNeId(int neId) {
		this.neId = neId;
	}

	public String getNeIp() {
		return neIp;
	}

	public void setNeIp(String neIp) {
		this.neIp = neIp;
	}

	public String getNeName() {
		return neName;
	}

	public void setNeName(String neName) {
		this.neName = neName;
	}

	public String getNeTypeName() {
		return neTypeName;
	}

	public void setNeTypeName(String neTypeName) {
		this.neTypeName = neTypeName;
	}
	
}
