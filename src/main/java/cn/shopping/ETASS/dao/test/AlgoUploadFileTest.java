package cn.shopping.ETASS.dao.test;

import cn.shopping.ETASS.domain.lsss.LSSSEngine;
import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.service.impl.AlgorithmServiceImpl;

import java.io.File;

public class AlgoUploadFileTest {
    public static void main(String[] args) {
        //1 用户设定策略
        String user_id="Alice";
        String policy = "hospital&doctor&(heart|(flu&headache))";

        //2 用户设置访问文件策略生成lsss矩阵
        LSSSEngine engine = new LSSSEngine();
        LSSSMatrix lsss = engine.genMatrix(policy);
        String[] attributes = lsss.getMap();
        System.out.println(lsss.toString());

        //3 加密上传
        AlgorithmServiceImpl algorithmService = new AlgorithmServiceImpl();
        String[] kw = {"测试","Alice"};
        File file = new File("C:\\Users\\shopping\\Documents\\test\\1.txt");
        algorithmService.setup();
        algorithmService.Enc(user_id,file, kw, lsss);


    }
}
