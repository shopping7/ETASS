package cn.shopping.ETASS.service.impl;

import cn.shopping.ETASS.domain.lsss.LSSSEngine;
import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.service.UploadFile;

public class UploadFileImpl implements UploadFile {
    AlgorithmServiceImpl algorithmService = new AlgorithmServiceImpl();
    LSSSEngine engine = new LSSSEngine();
    String policy = "hospital&(doctor|director)&(heart|(flu&headache))";
    String[] attributes = {"hospital","doctor","director","heart","flu","headache"};
    String[] attrs = {"hospital","doctor","heart"};
    LSSSMatrix lsss = engine.genMatrix(policy);

    @Override
    public void uploadFile(String user_id, String msg, String[] kw){
        algorithmService.setup();
        algorithmService.Enc(user_id,msg, kw, lsss,attributes);
    }

}
