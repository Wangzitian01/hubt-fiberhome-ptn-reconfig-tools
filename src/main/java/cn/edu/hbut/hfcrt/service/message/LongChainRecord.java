/**  

* <p>Title: LongChainRecord.java</p>  

* <p>Description: </p>  

* @author Aurore 

* @date 2020年5月13日  

* @version 1.0  

*/  
package cn.edu.hbut.hfcrt.service.message;
/**
*/

import java.util.List;

/**
 * @author sha
 *
 */
public class LongChainRecord extends Message implements IMessage{
	
	private int lie;
	private String nelName;
	private int nelNum;
	private String regular;
	
	private List<String> longChain;//顺序输出的长链
	private List<String> ring;//顺序输出的环
	private String connectionNeName;//环与链交叉的网元名称
	private String neNameA;//汇聚网元A名称
	private String neNameB;//汇聚网元B名称
	
	
	public int getLie() {
		return lie;
	}
	public void setLie(int lie) {
		this.lie = lie;
	}
	public String getNelName() {
		return nelName;
	}
	public void setNelName(String nelName) {
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
	
	public List<String> getLongChain() {
		return longChain;
	}
	public void setLongChain(List<String> longChain) {
		this.longChain = longChain;
	}
	public List<String> getRing() {
		return ring;
	}
	public void setRing(List<String> ring) {
		this.ring = ring;
	}
	public String getConnectionNeName() {
		return connectionNeName;
	}
	public void setConnectionNeName(String connectionNeName) {
		this.connectionNeName = connectionNeName;
	}
	public String getNeNameA() {
		return neNameA;
	}
	public void setNeNameA(String neNameA) {
		this.neNameA = neNameA;
	}
	public String getNeNameB() {
		return neNameB;
	}
	public void setNeNameB(String neNameB) {
		this.neNameB = neNameB;
	}
	

}
