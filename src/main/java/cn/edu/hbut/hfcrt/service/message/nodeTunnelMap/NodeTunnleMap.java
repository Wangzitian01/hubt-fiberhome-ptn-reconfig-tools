package cn.edu.hbut.hfcrt.service.message.nodeTunnelMap;

public class NodeTunnleMap {
	
	private String circuitId;
	private String dependenciesL2vpnRef;
	private String dependenciesL3vpnRef;
	private String dependenciesPwRef;
	private String label;
	private String mongoDataCreateTime;
	private String name;
	private ProtectPath protectPath;
	private String protectType;
	private WorkPath workPath;
	@Override
	public String toString() {
		return "nodeTunnleMap [circuitId=" + circuitId + ", dependenciesL2vpnRef=" + dependenciesL2vpnRef
				+ ", dependenciesL3vpnRef=" + dependenciesL3vpnRef + ", dependenciesPwRef=" + dependenciesPwRef
				+ ", label=" + label + ", mongoDataCreateTime=" + mongoDataCreateTime + ", name=" + name
				+ ", protectPath=" + protectPath + ", protectType=" + protectType + ", workPath=" + workPath + "]";
	}
	
//	public NodeTunnleMap(String circuitId, String dependenciesL2vpnRef, String dependenciesL3vpnRef,
//			String dependenciesPwRef, String label, String mongoDataCreateTime, String name, ProtectPath protectPath,
//			String protectType, WorkPath workPath) {
//		super();
//		this.circuitId = circuitId;
//		this.dependenciesL2vpnRef = dependenciesL2vpnRef;
//		this.dependenciesL3vpnRef = dependenciesL3vpnRef;
//		this.dependenciesPwRef = dependenciesPwRef;
//		this.label = label;
//		this.mongoDataCreateTime = mongoDataCreateTime;
//		this.name = name;
//		this.protectPath = protectPath;
//		this.protectType = protectType;
//		this.workPath = workPath;
//	}



	public String getDependenciesPwRef() {
		return dependenciesPwRef;
	}

	public void setDependenciesPwRef(String dependenciesPwRef) {
		this.dependenciesPwRef = dependenciesPwRef;
	}

	public String getCircuitId() {
		return circuitId;
	}
	public void setCircuitId(String circuitId) {
		this.circuitId = circuitId;
	}
	public String getDependenciesL2vpnRef() {
		return dependenciesL2vpnRef;
	}
	public void setDependenciesL2vpnRef(String dependenciesL2vpnRef) {
		this.dependenciesL2vpnRef = dependenciesL2vpnRef;
	}
	public String getDependenciesL3vpnRef() {
		return dependenciesL3vpnRef;
	}
	public void setDependenciesL3vpnRef(String dependenciesL3vpnRef) {
		this.dependenciesL3vpnRef = dependenciesL3vpnRef;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getMongoDataCreateTime() {
		return mongoDataCreateTime;
	}
	public void setMongoDataCreateTime(String mongoDataCreateTime) {
		this.mongoDataCreateTime = mongoDataCreateTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ProtectPath getProtectPath() {
		return protectPath;
	}
	public void setProtectPath(ProtectPath protectPath) {
		this.protectPath = protectPath;
	}
	
	public String getProtectType() {
		return protectType;
	}
	public void setProtectType(String protectType) {
		this.protectType = protectType;
	}
	public WorkPath getWorkPath() {
		return workPath;
	}
	public void setWorkPath(WorkPath workPath) {
		this.workPath = workPath;
	}
	
	

}
