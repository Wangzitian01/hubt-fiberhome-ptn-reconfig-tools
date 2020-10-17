/**  

* <p>Title: LongChainNERate.java</p>  

* <p>Description: </p>  

* @author Aurore 

* @date 2020年4月14日  

* @version 1.0  

*/  
package cn.edu.hbut.hfcrt.service.message;

import java.util.ArrayList;

/**  

* <p>Title: LongChainNERate.java</p>  

* <p>Description: </p>  

* @author Aurore 

* @date 2020年3月17日  

* @version 1.0  

*/  


public class LongChain {
	private int lie;
	private ArrayList<String> nelName;
	private int nelNum;
	private String regular;
	public int getLie() {
		return lie;
	}
	
	public String toString() {
		return "LongChainNERate[lie="+lie+",nelName="+nelName+",nelNum="+nelNum+",regular="+regular+"]";
	}
	
	public LongChain() {
		super();
	}
	public LongChain(int lie,ArrayList<String> nelName,int nelNum,String regular) {
		super();
		this.lie=lie;
		this.nelName=nelName;
		this.nelNum=nelNum;
		this.regular=regular;
	}
	
	public void setLie(int lie) {
		this.lie = lie;
	}
	public ArrayList<String> getNelName() {
		return nelName;
	}
	public void setNelName(ArrayList<String> nelName) {
		this.nelName = nelName;
	}
	public int getNelNum() {
		return nelNum;
	}
	public void setNelNum(int nelNum) {
		this.nelNum = nelNum;
	}
	public String getRegular() {
		return regular;
	}
	public void setRegular(String regular) {
		this.regular = regular;
	}
	
		
	
}

