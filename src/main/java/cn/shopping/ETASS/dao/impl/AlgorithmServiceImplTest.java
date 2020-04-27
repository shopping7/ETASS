package cn.shopping.ETASS.dao.impl;


import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.service.impl.AlgorithmServiceImpl;
import it.unisa.dia.gas.jpbc.Element;

import java.util.HashMap;


public class AlgorithmServiceImplTest {
    public static void main(String[] args) throws Exception {

        AlgorithmServiceImpl algorithmService = new AlgorithmServiceImpl();
        algorithmService.setup();

//        策略 a and (b or c) and (d or (e and f))
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

        double[][] lsssD1 = new double[][]{
                {1, 1, 0, 0}, // H1
                {0,-1, 1, 0}, // P1
                {0, 0,-1, 0}, // D1
                {0, 0, 0,-1}  // D4
        };

        String id = "123";
        algorithmService.KeyGen(id,attributes);
        PKAndSKAndID pkAndsk = algorithmService.getPKAndSKAndID(id);
        PK pk = pkAndsk.getPk();
        SK sk = pkAndsk.getSk();
        String theta_id = pkAndsk.getTheta_id();

        algorithmService.CreateUL(id,pk);
        Element Did = algorithmService.getDid(theta_id);
        String[] KW = {"doctor","oncology department","Raffles hospital"};
        algorithmService.Enc(id,"crypto", KW, lsss,attributes);
        CTAndVKM ctandvkm = algorithmService.getCtAndVkm();
        if(ctandvkm != null){
            CT ct = ctandvkm.getCt();
            VKM vkm = ctandvkm.getVkm();
            String[] KW1 = {"oncology department","Raffles hospital","doctor"};
            TKW tkw = algorithmService.Trapdoor(sk,KW1);
            int lsssIndex[] = new int [] {0,1,2,8};
            CTout ctout = algorithmService.Transform(ct, tkw, Did,lsssD1,lsssIndex);

            if(ctout != null){
                byte[] bytes = algorithmService.Dec(ctout, sk, vkm);
                String str = new String(bytes);
                System.out.println(str);

                //追溯id
//                String id_revocation = algorithmService.Trance(sk);
//                System.out.println(id_revocation);
            }else{
                System.out.println("Dec fail");
            }

        }
        else {
            System.out.println("Enc fail");
        }




    }

}
