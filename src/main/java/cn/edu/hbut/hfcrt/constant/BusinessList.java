package cn.edu.hbut.hfcrt.constant;

public class BusinessList {
	public String tunnelId;
	public int MidNeId;
	public int ConvengerceNeId;
	public Double BoardId;
	public String NewPortName;
	public String NewPortNo;
	public String getTunnelId() {
		return tunnelId;
	}
	public void setTunnelId(String tunnelId) {
		this.tunnelId = tunnelId;
	}
	public int getMidNeId() {
		return MidNeId;
	}
	public void setMidNeId(int midNeId) {
		MidNeId = midNeId;
	}
	public int getConvengerceNeId() {
		return ConvengerceNeId;
	}
	public void setConvengerceNeId(int convengerceNeId) {
		ConvengerceNeId = convengerceNeId;
	}
	public Double getBoardId() {
		return BoardId;
	}
	public void setBoardId(Double boardId) {
		BoardId = boardId;
	}
	public String getNewPortName() {
		return NewPortName;
	}
	public void setNewPortName(String newPortName) {
		NewPortName = newPortName;
	}
	public String getNewPortNo() {
		return NewPortNo;
	}
	public void setNewPortNo(String newPortNo) {
		NewPortNo = newPortNo;
	}
	@Override
	public String toString() {
		return "BusinessList [tunnelId=" + tunnelId + ", MidNeId=" + MidNeId + ", ConvengerceNeId=" + ConvengerceNeId
				+ ", BoardId=" + BoardId + ", NewPortName=" + NewPortName + ", NewPortNo=" + NewPortNo + "]";
	}
	
 

	

}
