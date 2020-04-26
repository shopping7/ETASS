package cn.shopping.ETASS.service.impl;

import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.service.AlgorithmService;
import cn.shopping.ETASS.service.GetFile;
import cn.shopping.ETASS.service.impl.AlgorithmServiceImpl;
import it.unisa.dia.gas.jpbc.Element;

public class GetFileImpl implements GetFile {
        private AlgorithmService algorithmService = new AlgorithmServiceImpl();
        private double[][] lsssD1 = new double[][]{
                {1, 1, 0, 0}, // H1
                {0,-1, 1, 0}, // P1
                {0, 0,-1, 0}, // D1
                {0, 0, 0,-1}  // D4
        };

        @Override
        public String getFile(String id, String[] kw_trapdoor) {
            algorithmService.setup();
            PKAndSKAndID pkAndsk = algorithmService.getPKAndSKAndID(id);
            SK sk = pkAndsk.getSk();
            String theta_id = pkAndsk.getTheta_id();
            Element Did = algorithmService.getDid(theta_id);

            CTAndVKM ctandvkm = algorithmService.getCtAndVkm();
            if(ctandvkm != null){
                CT ct = ctandvkm.getCt();
                VKM vkm = ctandvkm.getVkm();
                TKW tkw = algorithmService.Trapdoor(sk,kw_trapdoor);
                int lsssIndex[] = new int [] {0,1,2,8};
                CTout ctout = algorithmService.Transform(ct, tkw, Did,lsssD1,lsssIndex);

                if(ctout != null){
                    byte[] bytes = algorithmService.Dec(ctout, sk, vkm);
                    String str = new String(bytes);
                    System.out.println(str);

                    return str;
                }else{
                    System.out.println("Dec fail");

                }

            }
            return null;
        }



}
