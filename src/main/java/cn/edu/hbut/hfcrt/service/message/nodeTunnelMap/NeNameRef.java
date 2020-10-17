package cn.edu.hbut.hfcrt.service.message.nodeTunnelMap;


public class NeNameRef {
	 private String id;
	 private String collectionName;

    @Override
    public String toString() {
        return "{"
                + "\"id\":\""
                + id + '\"'
                + ",\"collectionName\":\""
                + collectionName + '\"'
                + "}";

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
}
