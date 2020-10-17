package cn.edu.hbut.hfcrt.pojo;

public class BoardPort {
	public int newBoard;
	public String newPortName;
	public int NeIndex;
	public int getNewBoard() {
		return newBoard;
	}
	public void setNewBoard(int newBoard) {
		this.newBoard = newBoard;
	}
	public String getNewPortName() {
		return newPortName;
	}
	public void setNewPortName(String newPortName) {
		this.newPortName = newPortName;
	}
	public int getNeIndex() {
		return NeIndex;
	}
	public void setNeIndex(int neIndex) {
		NeIndex = neIndex;
	}
	@Override
	public String toString() {
		return "BoardPort [newBoard=" + newBoard + ", newPortName=" + newPortName + ", NeIndex=" + NeIndex + "]";
	}
	
	
	

}
