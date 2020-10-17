package cn.edu.hbut.hfcrt.pojo;

public class Condition {
	double boardId1;
	double boardId2;
	String portName1;
	String portName2;
	double neId1;
	double neId2;
	public double getBoardId1() {
		return boardId1;
	}
	public void setBoardId1(double boardId1) {
		this.boardId1 = boardId1;
	}
	public double getBoardId2() {
		return boardId2;
	}
	public void setBoardId2(double boardId2) {
		this.boardId2 = boardId2;
	}
	public String getPortName1() {
		return portName1;
	}
	public void setPortName1(String portName1) {
		this.portName1 = portName1;
	}
	public String getPortName2() {
		return portName2;
	}
	public void setPortName2(String portName2) {
		this.portName2 = portName2;
	}
	public double getNeId1() {
		return neId1;
	}
	public void setNeId1(double neId1) {
		this.neId1 = neId1;
	}
	public double getNeId2() {
		return neId2;
	}
	public void setNeId2(double neId2) {
		this.neId2 = neId2;
	}
	@Override
	public String toString() {
		return "Condition [boardId1=" + boardId1 + ", boardId2=" + boardId2 + ", portName1=" + portName1
				+ ", portName2=" + portName2 + ", neId1=" + neId1 + ", neId2=" + neId2 + "]";
	}
	
 
	

}
