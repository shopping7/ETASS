package cn.shopping.ETASS.service.impl;

import cn.shopping.ETASS.domain.lsss.LSSSEngine;
import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.service.AlgorithmService;
import cn.shopping.ETASS.service.CloudServer;
import cn.shopping.ETASS.service.CommonService;
import cn.shopping.ETASS.service.GetFile;
import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;
import java.util.List;

public class GetFileImpl implements GetFile {
        private AlgorithmService algorithmService = new AlgorithmServiceImpl();
        private CommonService commonService = new CommonServiceImpl();
        LSSSEngine engine = new LSSSEngine();
        String policy = "hospital&(doctor|director)&(heart|(flu&headache))";
        String[] attributes = {"hospital","doctor","director","heart","flu","headache"};
        String[] attrs = {"hospital","doctor","heart"};
        LSSSMatrix lsss = engine.genMatrix(policy);
        LSSSMatrix lsssD1 = lsss.extract(attrs);

        @Override
        public String getFile(String id, String[] kw_trapdoor) {
            algorithmService.setup();
            PKAndSKAndID pkAndsk = commonService.getPKAndSKAndID(id);
            SK sk = pkAndsk.getSk();
            String theta_id = pkAndsk.getTheta_id();
            Element Did = algorithmService.getDid(theta_id);

            //获得文件
            CloudServer CS = new CloudServerImpl();
            List<Encrypt_File> file_list = new ArrayList<>();
            file_list = CS.getFile(kw_trapdoor);
            if(file_list != null){
                for (Encrypt_File file : file_list) {
                    CT ct = file.getCt();
                    VKM vkm = file.getVkm();
                    TKW tkw = algorithmService.Trapdoor(sk,kw_trapdoor);
                    //这里要完善
                    int lsssIndex[] = lsssD1.getIndex();
                    CTout ctout = CS.Transform(ct, tkw, Did,lsssD1,lsssIndex);

                    if(ctout != null){
                        String filename = "C:\\Users\\shopping\\Documents\\test\\1.txt";
                        algorithmService.Dec(ctout, sk, vkm,filename);

                    }else{
                        System.out.println("Dec fail");

                    }
                }


            }else{
                System.out.println("无匹配文件");
            }
            return null;
        }



}
