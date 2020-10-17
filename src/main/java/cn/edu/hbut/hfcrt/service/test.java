/**  

* <p>Title: test.java</p>  

* <p>Description: </p>  

* @author Aurore 

* @date 2020年6月2日  

* @version 1.0  

*/  
package cn.edu.hbut.hfcrt.service;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hbut.hfcrt.common.MongoDBHelper;

/**
*/



/**
 * @author sha
 *
 */
public class test {
	
	public static void main( String[] args ) throws Exception
    {
    	System.out.println( "Hello World!" );
        MongoDBHelper mongoDB = new MongoDBHelper();
        mongoDB.getMongoClient();
       
        //查找
        List<Object> selectResult = new ArrayList<Object>();
        selectResult=TunnelService.selecTunnels((double) 100676685,"GE_6","protectPath");
        //System.out.println(selectResult);
    	
       
        
//        nodeTunnelMap.insertTunnel();
//        nodeTunnelMap.updateTunnel();
//       
       // TunnelService.insertOneNe(111111111,504475424);
        
 
//        nodeTunnelMap1.deleteOneNe();
//          nodeTunnelMap1.updateBoardID();
//        nodeTunnelMap1.updataTunnels(100665933, 100685505);

       
        
        System.out.println( "成功" );
    }

}
