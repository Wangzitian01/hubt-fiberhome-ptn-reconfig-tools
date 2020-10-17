package cn.edu.hbut.hfcrt.service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bson.Document;
public class ChangeTunnel {	
	//调用前需确定A_neName与connectionNeName不是同一个网元
	public static void changeTunnel(String A_neName,int A_boardId,String A_portName,
										String F_neName,int F_boardId,String F_portName,
										List<String> longchain,List<String> ring,String connectionNeName,
										String path) {		
		int A_index = ring.indexOf(A_neName);//A网元在环上的位置
		int connection_index = ring.indexOf(connectionNeName);//交叉网元在环上的位置
		int F_index = longchain.indexOf(F_neName);//F网元在链上的位置
		NeService Ne = new NeService();
		TunnelService Tunnel = new TunnelService();		
		//获得长链最后一个网元与connectionNeName连接的板卡和端口
		String longChain_lastNe = longchain.get(longchain.size()-1);
		List<Object> lastList = Ne.neInformation(connectionNeName, longChain_lastNe);
		double boardId_C = Double.valueOf(lastList.get(1).toString());
		String portName_C = lastList.get(2).toString();
		System.out.println("longChainLastNe="+longChain_lastNe);
		System.out.println("boardId_C="+boardId_C);
		System.out.println("portName_C="+portName_C);
		
		//网元A位于交叉网元的右侧
		if (A_index > connection_index) {//将环倒序
			Collections.reverse(ring);
		}
		//环上,A网元与A下一个网元连接的板卡
		String ring_A_nestNe = ring.get(A_index+1);//环上,A网元的下一个网元
		List<Object> A_list = Ne.neInformation(A_neName, ring_A_nestNe);
		double boardId_A = Double.valueOf(A_list.get(1).toString());
		String portName_A = A_list.get(2).toString();
		
		/**
		 * 在Path查找
		 */
		List<Object> tunnelList = Tunnel.selecTunnels(boardId_A, portName_A, path);
		for (int i = 0; i < tunnelList.size(); i++) {
			Document tunnel = (Document) tunnelList.get(i);
			int _id = Integer.valueOf( tunnel.get("_id").toString());
			Document Path = (Document) ((ArrayList) tunnel.get(path)).get(0);//获取该Tunnel中的Path
			ArrayList pathsList = (ArrayList) Path.get("pathsList");
			int firstNeId = Integer.valueOf( ((Document)pathsList.get(0)).get("neId").toString());
			System.out.println("_id="+_id);
			System.out.println("firstNeId="+firstNeId);
			int count = 1;
			//进行五种情况的讨论
			for (int j = 0; j < ring.size(); j++) {
			//在环上遍历,查找tunnel的起点位于环上的位置，从而确定这条tunnel属于ABD类中的哪一类
				String neName = ring.get(j);
				int neId = Ne.findNeId(neName);
				if (neId == firstNeId) {
					
					if (j<=A_index) {//A类
						/**
						 **A类
						 **删除A与交叉网元之间的网元
						 **修改A网元的输出端
						 **插入F网元
						 **插入链上除首尾两个网元之间的网元
						 **插入链上最后一个网元
						 **修改C网元的输入端
						 **/
						count = A_index-j+1;
						for (int k = A_index+1; k < connection_index; k++) {//删除A与交叉网元之间的网元
							String aimNeName = ring.get(k);
							Tunnel.deleteOneNe(_id, Ne.findNeId(aimNeName), path);
						}
						Tunnel.updateNe(_id, Ne.findNeId(A_neName), A_boardId,A_portName, 2*(count-1)+1, path);//修改A网元的输出端
						count++;
						for (int k = F_index; k < longchain.size(); k++) {
							if (k == F_index) {//插入F网元
								Tunnel.insertOneNe(_id, F_neName, A_neName,longchain.get(F_index+1) , 2*(count-1), path);
								count++;
							}
							if (k != F_index && k != longchain.size()-1) {//插入链上除首尾两个网元之间的网元
								Tunnel.insertOneNe(_id, longchain.get(k), longchain.get(k-1), longchain.get(k+1), 2*(count-1), path);
								count++;
							}
							if (k == longchain.size()-1) {//插入链上最后一个网元
								Tunnel.insertOneNe(_id, longChain_lastNe,longchain.get(k-1) , connectionNeName, 2*(count-1), path);
								count++;
							}
						}
						Tunnel.updateNe(_id, Ne.findNeId(connectionNeName), (int)boardId_C, portName_C, 2*(count-1), path);
					}
					
					if (j>A_index && j<connection_index) {//B类
						/**
						 **B类
						 **/
					}
					
					if (j >= connection_index ) {//D类
						/**
						 **D类
						 **删除A与交叉网元之间的网元
						 **修改C网元的输出端
						 **插入链上最后一个网元
						 **插入链上除首尾两个网元之间的网元
						 **插入F网元
						 **修改A网元的输入端
						 **/
						count = j-connection_index+1;
						for (int k = A_index+1; k < connection_index; k++) {//删除A与交叉网元之间的网元
							String aimNeName = ring.get(k);
							Tunnel.deleteOneNe(_id, Ne.findNeId(aimNeName), path);
						}
						Tunnel.updateNe(_id, Ne.findNeId(connectionNeName), (int)boardId_C, portName_C, 2*(count-1)+1, path);
						count++;
						for (int k = longchain.size()-1; k >= F_index;k-- ) {
							if (k == longchain.size()-1) {
								Tunnel.insertOneNe(_id, longchain.get(k), connectionNeName, longchain.get(k-1), 2*(count-1), path);
								count++;
							}
							if (k != longchain.size()-1 && k !=F_index) {
								Tunnel.insertOneNe(_id, longchain.get(k), longchain.get(k+1), longchain.get(k-1),  2*(count-1), path);
								count++;
							}
							if (k == F_index) {
								Tunnel.insertOneNe(_id, F_neName, longchain.get(k+1), A_neName, 2*(count-1), path);
								count++;
							}
						}
						Tunnel.updateNe(_id, Ne.findNeId(A_neName), (int)boardId_A, portName_A, 2*(count-1), path);
					}
					
				}else {//neId != firstNeId
					continue;
				}
			}//在环上遍历
			for (int j = 0; j < longchain.size(); j++) {
			//在链上遍历,查找tunnel的起点位于环上的位置，从而确定这条tunnel属于EF类中的哪一类
				String neName = longchain.get(j);
				int neId = Ne.findNeId(neName);
				if (neId == firstNeId) {
					if (j > F_index) {//E类
						/**
						 **E类
						 **删除A与交叉网元之间的网元,包括C网元
						 **删除交叉网元与tunnel起点网元firstNeId之间的所有网元
						 **插入tunnel起点网元firstNeId与F网元之间的所有网元
						 **插入F网元
						 **修改A网元的输入端
						 **/
						for (int k = A_index+1; k <= connection_index; k++) {//删除A与交叉网元之间的网元
							String aimNeName = ring.get(k);
							Tunnel.deleteOneNe(_id, Ne.findNeId(aimNeName), path);
						}
						for (int k = j; k < longchain.size(); k++) {//删除交叉网元与tunnel起点网元firstNeId之间的所有网元
							String aimNeName = longchain.get(k);
							Tunnel.deleteOneNe(_id, Ne.findNeId(aimNeName), path);
						}
						for (int k = j; k > F_index; k--) {//插入tunnel起点网元firstNeId与F网元之间的所有网元
							Tunnel.insertOneNe(_id, longchain.get(k), longchain.get(k+1), longchain.get(k-1),2*(count-1), path);
							count++;
						}
						Tunnel.insertOneNe(_id, F_neName, longchain.get(F_index+1), A_neName, 2*(count-1), path);//插入F网元
						count++;
						Tunnel.updateNe(_id, Ne.findNeId(A_neName), (int)boardId_A, portName_A, 2*(count-1), path);//修改A网元的输入端
					}
					if (j <= F_index) {//F类
						/**
						 **F类
						 **删除A与交叉网元之间的网元,包括C网元
						 **删除交叉网元与F网元之间的所有网元
						 **修改F网元的输出端
						 **修改F网元的输入端
						 **/
						count = F_index-j+1;
						for (int k = A_index+1; k <= connection_index; k++) {//删除A与交叉网元之间的网元
							String aimNeName = ring.get(k);
							Tunnel.deleteOneNe(_id, Ne.findNeId(aimNeName), path);
						}
						for (int k = F_index; k < longchain.size(); k++) {//删除交叉网元与tunnel起点网元firstNeId之间的所有网元
							String aimNeName = longchain.get(k);
							Tunnel.deleteOneNe(_id, Ne.findNeId(aimNeName), path);
						}
						Tunnel.updateNe(_id, Ne.findNeId(F_neName), F_boardId,F_portName, 2*(count-1), path);
						count++;
						Tunnel.updateNe(_id, Ne.findNeId(A_neName), A_boardId, A_portName, 2*(count-1), path);
					}
				}else {//neId != firstNeId
					continue;
				}
			}//在链上遍历
		}
	}
}
