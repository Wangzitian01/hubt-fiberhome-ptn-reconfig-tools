package cn.edu.hbut.hfcrt.service;

import cn.edu.hbut.hfcrt.service.message.Assessment;
import cn.edu.hbut.hfcrt.service.nodeTunnelMap.NodeTunnelMap;

import java.util.List;

public class FiberPair {
    //输出光纤对列表
    Assessment assessment = new Assessment();
    List<FiberPairRecord>  fiberPairRecords = assessment.getFiberPairRecords();

    //得到巨环最优的所断链路，输出该最优链路
    BestLink bestLink = new BestLink();

    //判断与该断点网元相连的所选汇聚网元是否为原汇聚网元
    //如果是
    NodeTunnelMap nodeTunnelMap = new NodeTunnelMap();
}
