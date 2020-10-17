/**  

* <p>Title: MongoDBHelper.java</p>  

* <p>Description: </p>  

* @author Aurore 

* @date 2020年3月30日  

* @version 1.0  

*/  
package cn.edu.hbut.hfcrt.common;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import cn.edu.hbut.hfcrt.constant.Constant;


public class MongoDBHelper {

	private static MongoClient mongoClient;
	private MongoDatabase mongoDatabase;
	
	/**
	 * 启动
	 * @param host
	 * @param port
	 */
	public static MongoClient getMongoClient() {
		
		Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
		mongoLogger.setLevel(Level.SEVERE);
		
		MongoClient mongoClient = null;
		try {
			//连接mongodb
			 mongoClient = new MongoClient(Constant.SERVER_ADDRESS,Constant.PORT);
//			 System.out.println("Connect to mongoDB successfully");
//			 System.out.println("连接mongoDB成功");
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println(e.getClass().getName()+":"+e.getMessage());
		}
		return mongoClient;
	}
	
	public static MongoDatabase getMongoDatabase(MongoClient mongoClient) {
		//连接数据库
		MongoDatabase mongoDatabase = null;
		try {
			
			if (mongoClient != null) {
				mongoDatabase =mongoClient.getDatabase(Constant.DATABASE);
//				System.out.println("Connect to database successfully");
//				System.out.println("连接database成功");
			}else {
				throw new RuntimeException("mongoClient不能为空");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mongoDatabase;
	}
	
	/**
	 * 停止
	 * @return
	 */
	public void closeMongoClient() {
		if (mongoDatabase != null) {
			mongoDatabase = null;
		}
		if(mongoClient!=null) {
			mongoClient.close();
		}
//		System.out.println("CloseMongoClient successfully");
//		System.out.println("关闭MongoClient成功");
	}
	
	

}

