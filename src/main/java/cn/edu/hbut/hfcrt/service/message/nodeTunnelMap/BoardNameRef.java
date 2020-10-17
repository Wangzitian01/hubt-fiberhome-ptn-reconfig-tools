/**  

* <p>Title: BoardNameRef.java</p>  

* <p>Description: </p>  

* @author Aurore 

* @date 2020年4月14日  

* @version 1.0  

*/  
package cn.edu.hbut.hfcrt.service.message.nodeTunnelMap;


public class BoardNameRef {
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
