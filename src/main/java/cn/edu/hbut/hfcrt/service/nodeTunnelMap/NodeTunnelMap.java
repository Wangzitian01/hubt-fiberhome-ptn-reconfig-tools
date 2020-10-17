package cn.edu.hbut.hfcrt.service.nodeTunnelMap;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import cn.edu.hbut.hfcrt.utils.MongoDBHelper;

import org.bson.Document;

import java.util.*;

public class NodeTunnelMap {
    //连接芒果数据库并获取集合
    MongoDBHelper mongoDBHelper = new  MongoDBHelper();
    MongoClient mongoClient = new MongoClient();
    MongoDatabase mongoDatabase=mongoDBHelper.getMongoDatabase(mongoClient);
    MongoCollection<Document> collection =  mongoDatabase.getCollection("nodeTunnelMap");

    public void updateTunnel(){

        
        //查找指定的tunnel
        Document query = new Document().append("workPath.pathsList.boardId",100671958).append("workPath.pathsList.portNo",1);
        //查找tunnel中的第一条数据
        //Document tunnel = (Document) collection.find(query);

        FindIterable findIterable = collection.find(query);
        MongoCursor iterator= findIterable.iterator();
        while(iterator.hasNext()){
            Document tunnel = (Document) iterator.next();

            //得到该tunnel的workPath(Document) ((ArrayList) tunnel.get("workPath")).get(0)
            Document workPath = (Document) ((ArrayList) tunnel.get("workPath")).get(0);
            System.out.println("workPath"+workPath);
            // 得到workPath的pathsList
            ArrayList<Document> w_pathsList = (ArrayList) workPath.get("pathsList");
            System.out.println("w_pathsList"+w_pathsList);


            //定义一个新的数组存放pathsList
            ArrayList pathsList=new ArrayList<Object>();
            //循环workPath的PathsList
            for (int i = 0;i<w_pathsList.size();i++){
                int boardId =(Integer) w_pathsList.get(i).get("boardId");

                // 将网元ID换位宿网元id
                if (boardId != 100686293){

                    pathsList.add(w_pathsList.get(i));
                }else{
                    // w_pathsList.get(i);
                    pathsList.add(w_pathsList.get(i));
                    pathsList.add(w_pathsList.get(w_pathsList.size()-2));
                    pathsList.add(w_pathsList.get(w_pathsList.size()-1));
                    break;
                }

            }

            Document document = new Document().append("workPath.0.pathsList", pathsList);
            collection.updateOne(tunnel, new Document("$set", document));

        }
    }
    //Document tunnel = (Document) iterator.hasNext();

//
//        //得到该tunnel的workPath
//        Document workPath = (Document) ((ArrayList) tunnel.get("workPath")).get(0);
//        System.out.println("workPath"+workPath);
//       // 得到workPath的pathsList
//        ArrayList<Document> w_pathsList = (ArrayList) workPath.get("pathsList");
//        System.out.println("w_pathsList"+w_pathsList);
//
////        ArrayList<Document> w_pathsList =  (ArrayList) ((Document) tunnel.get("workPath.0.pathsList"));
////        //System.out.println("w_pathsList"+w_pathsList);
//
//        //定义一个新的数组存放pathsList
//        ArrayList pathsList=new ArrayList<Object>();
//        //循环workPath的PathsList
//        for (int i = 0;i<w_pathsList.size();i++){
//            int boardId =(Integer) w_pathsList.get(i).get("boardId");
//
//            // 将网元ID换位宿网元id
//            if (boardId != 100686293){
//
//                pathsList.add(w_pathsList.get(i));
//            }else{
//               // w_pathsList.get(i);
//                pathsList.add(w_pathsList.get(i));
//                pathsList.add(w_pathsList.get(w_pathsList.size()-1));
//                break;
//            }
//
//        }
//
//            Document document = new Document().append("workPath.0.pathsList", pathsList);
//            collection.updateOne(tunnel, new Document("$set", document));
//
//    }
}