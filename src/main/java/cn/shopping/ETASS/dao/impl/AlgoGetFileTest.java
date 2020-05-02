package cn.shopping.ETASS.dao.impl;

import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.service.CloudServer;
import cn.shopping.ETASS.service.impl.AlgorithmServiceImpl;
import cn.shopping.ETASS.service.impl.CloudServerImpl;
import it.unisa.dia.gas.jpbc.Element;

import java.util.List;

public class AlgoGetFileTest {
    public static void main(String[] args) {


        // 1 用户端获得用户的公钥和私钥
        String id = "123";
        AlgorithmServiceImpl algorithmService = new AlgorithmServiceImpl();
        algorithmService.setup();
        PKAndSKAndID pkAndsk = algorithmService.getPKAndSKAndID(id);
        SK sk = pkAndsk.getSk();
        String theta_id = pkAndsk.getTheta_id();
        Element Did = algorithmService.getDid(theta_id);
        String[] KW1 = {"school"};
        TKW tkw = algorithmService.Trapdoor(sk,KW1);

//      1 CS获得文件属性和用户属性
        String[] attrs = {"hospital","doctor","flu","headache"};
        //2 cloudserver寻找对应的文件
        CloudServer CS = new CloudServerImpl();
        CS.setup();
        List<Encrypt_File> file_list = CS.getFile(KW1);
        if(file_list != null){
            for (Encrypt_File file : file_list) {
                CT ct = file.getCt();
                VKM vkm = file.getVkm();
                LSSSMatrix lsss = file.getLsss();
                System.out.println(lsss);

                LSSSMatrix lsssD1 = lsss.extract(attrs);
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
