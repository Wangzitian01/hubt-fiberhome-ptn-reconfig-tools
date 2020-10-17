package cn.edu.hbut.hfcrt.service.Handle;

import cn.edu.hbut.hfcrt.service.message.Assessment;

public class JuhuanHandle {

    public void Handle() throws Exception {
        //对原始网络结构进行评估并保存
        Assessment assessment = new Assessment();
        String result = assessment.execAssessment();

        //输出已有光纤对应的结果和所加光纤对应的结果


    }




}
