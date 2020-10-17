package cn.edu.hbut.hfcrt.pojo;

public class Board {
	
	int BoardId;
	String PortName;
	int PortNo;
	public int getBoardId() {
		return BoardId;
	}
	public void setBoardId(int boardId) {
		BoardId = boardId;
	}
	public String getPortName() {
		return PortName;
	}
	public void setPortName(String portName) {
		PortName = portName;
	}
	public int getPortNo() {
		return PortNo;
	}
	public void setPortNo(int portNo) {
		PortNo = portNo;
	}
	@Override
	public String toString() {
		return "Board [BoardId=" + BoardId + ", PortName=" + PortName + ", PortNo=" + PortNo + "]";
	}
	
 

}
