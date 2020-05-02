package cn.shopping.ETASS.dao.impl;


import cn.shopping.ETASS.domain.lsss.LSSSEngine;
import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.service.CloudServer;
import cn.shopping.ETASS.service.KGC;
import cn.shopping.ETASS.service.impl.AlgorithmServiceImpl;
import cn.shopping.ETASS.service.impl.CloudServerImpl;
import cn.shopping.ETASS.service.impl.KGCImpl;
import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;
import java.util.List;


public class AlgorithmServiceImplTest {
    public static void main(String[] args){
        LSSSEngine engine = new LSSSEngine();
        String policy = "hospital&(doctor|director)&(heart|(flu&headache))";
        String[] attributes = {"hospital","doctor","director","heart","flu","headache"};
        String[] attrs = {"hospital","doctor","heart"};
        LSSSMatrix lsss = engine.genMatrix(policy);
        System.out.println(lsss.toString());
        LSSSMatrix lsssD1 = lsss.extract(attrs);
        System.out.println(lsssD1);

        KGC kgc = new KGCImpl();
        //1 KGC分配pp和msk
        kgc.setup();
        //
        AlgorithmServiceImpl algorithmService = new AlgorithmServiceImpl();
        algorithmService.setup();
        String id = "123";
        //生成pk和sk
        kgc.KeyGen(id,attributes);
        //获得pk和sk
        PKAndSKAndID pkAndsk = algorithmService.getPKAndSKAndID(id);
        PK pk = pkAndsk.getPk();
        SK sk = pkAndsk.getSk();
        String theta_id = pkAndsk.getTheta_id();

        algorithmService.CreateUL(id,pk);
        Element Did = algorithmService.getDid(theta_id);
        String[] KW = {"doctor","oncology department","Raffles hospital"};
        algorithmService.Enc(id,"crypto", KW, lsss,attributes);
        String[] KW1 = {"oncology department","Raffles hospital","doctor"};
        List<Encrypt_File> file_list = new ArrayList<>();

        CloudServer CS = new CloudServerImpl();
        file_list = CS.getFile(KW1);
        if(file_list != null){
            for (Encrypt_File file : file_list) {
                CT ct = file.getCt();
                VKM vkm = file.getVkm();
                TKW tkw = algorithmService.Trapdoor(sk,KW1);
                //这里要完善
                int lsssIndex[] = lsssD1.getIndex();
                CTout ctout = CS.Transform(ct, tkw, Did,lsssD1,lsssIndex);

                if(ctout != null){
                    byte[] bytes = algorithmService.Dec(ctout, sk, vkm);
                    String str = new String(bytes);
                    System.out.println(str);

                }else{
                    System.out.println("Dec fail");

                }
            }


        }else{
            System.out.println("无匹配文件");
        }



    }

}
