package cn.shopping.ETASS.service.impl;

import cn.shopping.ETASS.service.UploadFile;

public class UploadFileImpl implements UploadFile {
    AlgorithmServiceImpl algorithmService = new AlgorithmServiceImpl();
    String[] attributes = new String[]{
            "H1",
            "P1",
            "D1",
            "D2",
            "H2",
            "P2",
            "D3",
            "P3",
            "D4"
    };
    double[][] lsss = new double[][]{
            {1, 1, 0, 0}, // H1
            {0,-1, 1, 0}, // P1
            {0, 0,-1, 0}, // D1
            {0, 0,-1, 0}, // D2
            {1, 1, 0, 0}, // H2
            {0,-1, 1, 1}, // P2
            {0, 0, 0,-1}, // D3
            {0, 0,-1, 1}, // P3
            {0, 0, 0,-1}  // D4
    };

    @Override
    public void uploadFile(String user_id, String msg, String[] kw){
        algorithmService.setup();
        algorithmService.Enc(user_id,msg, kw, lsss,attributes);
    }

}
