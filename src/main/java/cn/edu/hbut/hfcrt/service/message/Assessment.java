package cn.edu.hbut.hfcrt.service.message;

import java.util.List;

import cn.edu.hbut.hfcrt.service.AddFiber;
import cn.edu.hbut.hfcrt.service.FiberPair;
import cn.edu.hbut.hfcrt.service.FiberPairRecord;

public class Assessment {
    //读取评估结果输出巨环信息

    public String execAssessment() {

        return null;
    }
    //得到光纤对列表

    public List<FiberPairRecord> getFiberPairRecords() {

        return null;
    }
    //判断链路两端与对方汇聚网元及下一跳是否存在光纤


    //
    // 如果存在
    FiberPair fiberPair = new FiberPair();
    //如果不存在，判断链路两端端点与对方汇聚网元是否存在路由，如果存在
    AddFiber addFiber = new AddFiber();//执行加光纤的操作


}
