package cn.edu.hbut.hfcrt.service.Handle;
//package cn.edu.hbut.hftrt.service.Handle;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.edu.hbut.hftrt.utils.MongoDBHelper;
//import com.mongodb.MongoClient;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
//import org.bson.Document;
//
//
//public class Handle {
//
//    //连接芒果数据库并获取集合
//    MongoDBHelper mongoDBHelper = new  MongoDBHelper();
//    MongoClient mongoClient = new MongoClient();
//    MongoDatabase mongoDatabase=mongoDBHelper.getMongoDatabase(mongoClient);
//   // MongoCollection<Document> collection =  mongoDatabase.getCollection("nodeTunnelMap");
//    //从excel表格中获取环信息  path:路径  row:获取哪一行，如果只要一行就传递参数，如果要获取全部就传递0
//    public List GetExcelCircleMessage(String path,int row) throws Exception{
//        System.out.println("开始");
//        FileInputStream inputStream = new FileInputStream(path);
//        POIFSFileSystem fileSystem = new POIFSFileSystem(inputStream);
//        HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
//        HSSFSheet hssfSheet = workbook.getSheetAt(3);
//        List<String> list = new ArrayList<String>();
//        if(row ==0 ){//获取所有环信息
//            for (int i = 3; i < hssfSheet.getLastRowNum()-1; i++){
//                HSSFRow hssfRow = hssfSheet.getRow(i);//获取第一行
//                String string = hssfRow.getCell(1).toString();
//                list.add(string);
//                //System.out.println(string);
//            }
//        }else{//获取指定行的信息
//            HSSFRow hssfRow = hssfSheet.getRow(row);//获取第一行
//            String string = hssfRow.getCell(1).toString();
//            list.add(string);
//            //System.out.println(string);
//        }
//        return list;
//    }
//    //拆环成功则返回true否则返回false
//    public boolean HandleRingremoval(List list) throws Exception{
//        //定义第二个要处理的字符串
//        String neNoede1 = "";
//        String neNoede2 = "";
//        for (int i = 0; i < list.size(); i++) {
//            String[] split = list.get(i).toString().split(";");
//            int flag = split.length/2;
//            neNoede1 = split[flag];
//            neNoede2 = split[flag+1];
//            System.out.println(flag+"--"+neNoede1+"---"+neNoede2);
//            //删光纤（neNoede1-neNoede2）将两个网元之间的光纤删掉
//            //DeleteOpticaFiber(neNoede1,neNoede2);
//
//        }
//        return true;
//    }
//    //删光纤（neNoede1-neNoede2）将二个网元之间的光纤删掉
////    private void DeleteOpticaFiber(String neNoede1, String neNoede2) throws Exception {
////        //获取二个网元的ID  //黄石大冶炕头
////        String neNodeID1 = "";
////        String neNodeID2 = "";
////        MongoCollection<Document> collection =  mongoDatabase.getCollection("nodeNeList");
////        MongoCollection<Document> path = mongoDatabase.getCollection("nodeNeList");
////        File file = new File(String.valueOf(path));
////
////        for (int i = 0; i < file.length(); i++) {
////            String aaa = jsonArray.getJSONObject(i).get("neId").toString();
////            String bbb = jsonArray.getJSONObject(i).get("neName").toString();
////            if(bbb.equals(neNoede1)){
////                neNodeID1 = aaa;
////            }else if(bbb.equals(neNoede2)){
////                neNodeID2 = aaa;
////            }
////        }
////        //网元ID
////        System.out.println("neId:"+neNodeID1+"---"+"neId:"+neNodeID2);
////        //获取了要拆的网元的ID
////        //根据网元ID获取拓扑连接信息然后进行删除
////        String topoNodeId1 = "";
////        String topoNodeId2 = "";
////        String str02 = new JsonRead().JsonRead("C:\\Users\\大白\\Desktop\\nodeTopology");
////        JSONObject jsonObject = new JSONObject(str02);
////        String string = jsonObject.get("nodes").toString();
////        JSONArray jsonArray02 = new JSONArray(string);
////        System.out.println("===========");
////        for (int j = 0; j < jsonArray02.length(); j++) {
////            JSONObject jsonObject2 = new JSONObject(jsonArray02.get(j).toString());
////            String string3 = jsonObject2.getString("neId").toString();
////            String string4 = jsonObject2.getString("topoNodeId").toString();
////            if (string3.equals(neNodeID1)) {
////                System.out.println("neId:"+jsonObject2.getString("neId")+"----"+"topoNodeId1:"+jsonObject2.getString("topoNodeId"));
////                topoNodeId1 = string4;
////            }
////            if (string3.equals(neNodeID2)) {
////                System.out.println("neId:"+jsonObject2.getString("neId")+"----"+"topoNodeId2:"+jsonObject2.getString("topoNodeId"));
////                topoNodeId2 = string4;
////            }
////        }
////        //找到拓扑连纤，根据拓扑连纤找对应的需要柴拆环的第二个网元
////        String sss = "";
////        String sting12  = jsonObject.get("lines").toString();
////        JSONArray jsonArray12 = new JSONArray(sting12);
////        for (int k = 0; k < jsonArray12.length(); k++) {
////            JSONObject jsonObject13 = new JSONObject(jsonArray12.get(k).toString());
////            String string3 = jsonObject13.getString("topoNodeId1").toString();
////            String string4 = jsonObject13.getString("topoNodeId2").toString();
////            if (string3.equals(topoNodeId1)||string4.equals(topoNodeId1)) {
////                System.out.println(topoNodeId1+"---"+jsonObject13.getString("topoNodeId1")+"---"+jsonObject13.getString("topoNodeId2"));
////                String mm1 = topoNodeId1;
////                String mm2 = jsonObject13.getString("topoNodeId1");
////                String mm3 = jsonObject13.getString("topoNodeId2");
////                if(mm1.equals(mm2)){
////                    sss = sss + mm3+";";
////                }else{
////                    sss = sss + mm2+";";
////                }
////            }
////        }
////        System.out.println(sss);
////        //进行拼接
////        List<String> list12 = new ArrayList<String>();
////        String[] split = sss.split(";");
////        for (int i = 0; i < split.length; i++) {
////            String string2 = split[i];
////            list12.add(topoNodeId1+";"+string2);
////        }
////        System.out.println(list12);//获取每一对拓扑连接信息
////        //这里进行判断,看那一对是topoNodeId1 ---topoNodeId2 如果是就删除这条记录
//////		for (int i = 0; i < list12.size(); i++) {
//////			String[] split2 = list12.get(i).toString().split(";");
//////
//////		}
////        sss = "";
////        System.out.println("+++++++++++++++");
////        List<String> list13 = new ArrayList<String>();
////        for (int i = 0; i < list12.size(); i++) {//输出链上面网元  有多个就用;分割存在list13集合中
////            System.out.println("网元ID:"+list12.get(i));//这里可能是多个拓扑连纤ID
////            String[] split2 = list12.get(i).toString().split(";");//前面是用;进行分割的，这里解用;切分
////            for (int j = 0; j < split2.length; j++) {//进行遍历每个拓扑连纤ID
////                String string2 = split2[j];//得到每一个拓扑连纤ID
////                for (int k = 0; k < jsonArray02.length(); k++) {//在nodes里面查找每个网元ID信息
////                    JSONObject jsonObject2 = new JSONObject(jsonArray02.get(k).toString());
////                    String string3 = jsonObject2.getString("neId").toString();
////                    String string4 = jsonObject2.getString("topoNodeId").toString();
////                    if (string4.equals(string2)) {
////                        //System.out.println("neId:"+jsonObject2.getString("neId")+"----"+"topoNodeId:"+jsonObject2.getString("topoNodeId"));
////                        sss = sss + string3+";";
////                    }
////                }
////            }
////            list13.add(sss);
////            //System.out.println("��ԪID:"+sss);
////            sss = "";
////        }
////        int kkn = 0;
////        System.out.println("wwwwwwwwwwwwwww");
////        System.out.println(neNodeID1+"-"+neNodeID2);
////        for (int i = 0; i < list13.size(); i++) {//list13存储的是网元ID，在这里进行判断，然后就可以删除所在的光纤对
////            //System.out.println(list13.get(i));
////            String[] split2 = list13.get(i).toString().split(";");
////            String s1 = split2[0];
////            String s2 = split2[1];
////            //System.out.println(s1+"+"+s2);
////            if(s1.equals(neNodeID1)&&s2.equals(neNoede2)){
////                kkn = 0;
////            }else{
////                kkn = 1;
////            }
////
////        }
//////		System.out.println(list13.size());
//////		System.out.println(kkn);
////        //�ó����˶� list12
////        sss = "";
////        String[] split2 = list12.get(kkn).toString().split(";");
////        String gg1 = split2[0];
////        String gg2 = split2[1];
////        for (int k = 0; k < jsonArray12.length(); k++) {
////            JSONObject jsonObject13 = new JSONObject(jsonArray12.get(k).toString());
////            String string3 = jsonObject13.getString("topoNodeId1").toString();
////            String string4 = jsonObject13.getString("topoNodeId2").toString();
////            if (string3.equals(gg1)&&string4.equals(gg2)) {//成功找出
////                System.out.println("待删除的光纤对"+jsonArray12.get(k).toString());//删除这条记录
////            }else if(string4.equals(gg1)&&string3.equals(gg2)){//成功找出
////                System.out.println("待删除的光纤对"+jsonArray12.get(k).toString());//删除这条记录
////            }
////        }
////        //进行添加光纤  随机生成
////        String neid1 = "134220628";
////        String topoNodeId ="134226628";
////        String neid2 = "134220629";
////        String b1 = "";
////        String b2 = "";
////        //获取断开网元的信息
////        for (int i = 0; i < jsonArray.length(); i++) {
////            String aaa = jsonArray.getJSONObject(i).get("neId").toString();
////            String bbb = jsonArray.getJSONObject(i).get("neName").toString();
////            if(aaa.equals(neNodeID1)){
////                b1 = jsonArray.getJSONObject(i).toString();
////            }else if(aaa.equals(neNodeID2)){
////                b2 = jsonArray.getJSONObject(i).toString();
////            }
////        }
////        //b1 b2 为断开网元信息
////        System.out.println(b1);
////        System.out.println(b2);
////		/*1获取第二个汇聚网元的网元信息的
////		 * 2。添加光纤对
////		 * 		{
////			"boardId1": 100670133,
////			"boardId2": 100671398,
////			"linkSpeedName": "GE",
////			"portKey1": "GE_6",
////			"portKey2": "GE_1",
////			"portName1": "GE_6",
////			"portName2": "GE_1",
////			"topoLinkId": 570549099,
////			"topoNodeId1": 369099541,
////			"topoNodeId2": 369099696
////		}
////		 * */
////
//
//
//
//
//    }
//
//    //修改tunnel
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//}
//
