package cn.edu.hbut.hfcrt.service.message.nodeTunnelMap;


public class PathsList {
	private String neId;
	private String portNo;
	private String boardId;
	private String portName;
	private NeNameRef neNameRef;
	private BoardNameRef boardNameRef;
    private String label;

    @Override
    public String toString() {
        return "{"
                + "\"neId\":\""
                + neId + '\"'
                + ",\"portNo\":\""
                + portNo + '\"'
                + ",\"boardId\":\""
                + boardId + '\"'
                + ",\"portName\":\""
                + portName + '\"'
                + ",\"neNameRef\":"
                + neNameRef
                + ",\"boardNameRef\":"
                + boardNameRef
                + ",\"label\":\""
                + label + '\"'
                + "}";

    }

    public void clear(){
    }
    public String getNeId() {
        return neId;
    }

    public void setNeId(String neId) {
        this.neId = neId;
    }

    public String getPortNo() {
        return portNo;
    }

    public void setPortNo(String portNo) {
        this.portNo = portNo;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public NeNameRef getNeNameRef() {
        return neNameRef;
    }

    public void setNeNameRef(NeNameRef neNameRef) {
        this.neNameRef = neNameRef;
    }

    public BoardNameRef getBoardNameRef() {
        return boardNameRef;
    }

    public void setBoardNameRef(BoardNameRef boardNameRef) {
        this.boardNameRef = boardNameRef;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
