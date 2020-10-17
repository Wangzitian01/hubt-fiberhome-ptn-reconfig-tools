/**  

* <p>Title: TopoGainAdjustRange.java</p>  

* <p>Description: </p>  

* @author Aurore 

* @date 2020年4月22日  

* @version 1.0  

*/  
package cn.edu.hbut.hfcrt.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
/**
*/import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.formula.functions.Value;
import org.bson.Document;

import com.mongodb.client.MongoCollection;

import cn.edu.hbut.cs.hfcrt.databridge.dao.DataBridgeDAO;
import cn.edu.hbut.hfcrt.service.message.LongChain;
import cn.edu.hbut.hfcrt.service.message.Ring;
import cn.edu.hbut.hfcrt.service.message.neMap.ne;

/**
 * @author sha
 *
 */
public class TopoGainAdjustRange {
	
	static MongoCollection<Document> neMapCol = new DataBridgeDAO().getCollection("neMap");
	
	//选择最优光纤连接点，接入or汇聚
	public static Map<Map<List<String>,Document>,Integer> topoGainAdjustRange(List<String> longChain,List<String> ring){
			
			//将所有的增益及对应的信息放在一个map中
			Map<Map<List<String>, Document>, Integer> gainMap = new HashMap<Map<List<String>,Document>, Integer>();
			Map<List<String>, Document> topoResult = null;//保存网元是否有光纤的结果
			int gain=0;//增益
			TopoService topo = new TopoService();
//			Ring ring=new Ring();
//			LongChain longChain = new LongChain();
			int ringLen = ring.size();
			int longChainLen = longChain.size();
			NeService nel = new NeService();
			String C_neName =longChain.get(0);//网元C的neName,网元C为环与链的连接网元，处于长链表中的第一个
			
			//判断网元是接入网元or汇聚网元
			int aimType = getIntFromString( C_neName);
			if (aimType<660) {//660以下为接入
				int index= ring.indexOf(C_neName);
				//计算增益
				for (int i = longChainLen; i >0; i--) {//在长链上循环
					String F_neName= longChain.get(i);
					int x = i-1;//长链上导致其成环的网元个数
					for (int j = index-1; j >0 ; j--) {//寻找环上网元C之前的网元
						String A_neName = ring.get(j);
						int y= (index-1)-j;//环上导致其成链的网元个数
						topoResult = topo.checkAdditionalTopos(A_neName, F_neName);
						if (topoResult!=null) {
							gain= x-y;//导致成环的减去导致成链的
							gainMap.put(topoResult, gain);//将网元光纤信息与增益放在一个map中
						}
					}
					for (int j = index+1; j <ringLen ; j++) {//寻找环上网元C之后的网元
						String A_neName = ring.get(j);
						int y = j-(index+1);//环上导致其成链的网元个数
						topoResult = topo.checkAdditionalTopos(A_neName, F_neName);
						if (topoResult!=null) {
							gain= x-y;//导致成环的减去导致成链的
							gainMap.put(topoResult, gain);//将网元光纤信息与增益放在一个map中
						}
					}
				}
	
			}else {//660及以上为汇聚
			//TODO   汇聚网元上的链的处理方式
			}
		return gainMap;
	}
		
	//获取网元类型:汇聚or接入
	public static int getIntFromString(String neName) {
		//String str = "CiTRANS 690 U20";
		Document query=new Document().append("neName", String.valueOf(neName));
		Document doc = neMapCol.find(query).first();
		String str = (String) doc.get("neTypeName");
		String str2 = str.substring(8, 11);
		Integer n = Integer.parseInt(str2);
		System.out.println("n="+n);
		return n;//660、690、640
	}

	//取出map中value值最大的key值
	public static Object getMaxValue(Map<Map<List<String>, Document>, Integer> map) {
			if (map == null)
				return null;//map为空，则代表链与环没有可用的光纤，此时需要加光纤
			Collection<Integer> c = map.values();
			Object[] obj = c.toArray();
			Arrays.sort(obj);
			return obj[obj.length-1];//最大增益的光纤，将其连接后，切换Tunnel即解决了此条链的问题
		}
			
	
}
