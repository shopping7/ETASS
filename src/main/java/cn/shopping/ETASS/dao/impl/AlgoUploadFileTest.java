package cn.shopping.ETASS.dao.impl;

import cn.shopping.ETASS.domain.lsss.LSSSEngine;
import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.service.impl.AlgorithmServiceImpl;

public class AlgoUploadFileTest {
    public static void main(String[] args) {
        //1 用户设定策略
        LSSSEngine engine = new LSSSEngine();
        String policy = "hospital&(doctor|director)&(heart|(flu&headache))";
//        String[] attributes = {"hospital","doctor","director","heart","flu","headache"};
        //2 用户策略生成lsss矩阵
        LSSSMatrix lsss = engine.genMatrix(policy);
        String[] attributes = lsss.getMap();
        System.out.println(lsss.toString());

        //3 加密上传
        AlgorithmServiceImpl algorithmService = new AlgorithmServiceImpl();
        String[] kw = {"school"};
        algorithmService.setup();
        algorithmService.Enc("123","crypto_teacher", kw, lsss,attributes);


    }
}
