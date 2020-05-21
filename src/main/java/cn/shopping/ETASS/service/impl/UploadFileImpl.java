package cn.shopping.ETASS.service.impl;

import cn.shopping.ETASS.domain.lsss.LSSSEngine;
import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.service.UploadFile;

import java.io.File;

public class UploadFileImpl implements UploadFile {

    @Override
    public void uploadFile(String user_id, String policy, File file, String[] kw) {
        LSSSEngine engine = new LSSSEngine();
        LSSSMatrix lsss = engine.genMatrix(policy);
        String[] attributes = lsss.getMap();
//        System.out.println(lsss.toString());

        //3 加密上传
        AlgorithmServiceImpl algorithmService = new AlgorithmServiceImpl();
//        String filename = "C:\\Users\\shopping\\Documents\\test\\1.txt";
        algorithmService.setup();
        algorithmService.Enc(user_id,file, kw, lsss);
    }
}
