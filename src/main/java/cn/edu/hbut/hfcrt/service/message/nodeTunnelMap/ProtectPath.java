package cn.edu.hbut.hfcrt.service.message.nodeTunnelMap;

public class ProtectPath {
	
	private String flag;
	private String id;
	private String name;
	private String pathDirection;
	private PathsList pathsList;
	private String routeDir;
	private String serviceLayerId;
	@Override
	public String toString() {
		return "ProtectPath [flag=" + flag + ", id=" + id + ", name=" + name + ", pathDirection=" + pathDirection
				+ ", pathsList=" + pathsList + ", routeDir=" + routeDir + ", serviceLayerId=" + serviceLayerId + "]";
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPathDirection() {
		return pathDirection;
	}
	public void setPathDirection(String pathDirection) {
		this.pathDirection = pathDirection;
	}
	public PathsList getPathsList() {
		return pathsList;
	}
	public void setPathsList(PathsList pathsList) {
		this.pathsList = pathsList;
	}
	public String getRouteDir() {
		return routeDir;
	}
	public void setRouteDir(String routeDir) {
		this.routeDir = routeDir;
	}
	public String getServiceLayerId() {
		return serviceLayerId;
	}
	public void setServiceLayerId(String serviceLayerId) {
		this.serviceLayerId = serviceLayerId;
	}
	
	

}
