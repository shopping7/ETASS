package cn.shopping.ETASS;

import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.service.CloudServer;
import cn.shopping.ETASS.service.CommonService;
import cn.shopping.ETASS.service.impl.AlgorithmServiceImpl;
import cn.shopping.ETASS.service.impl.CloudServerImpl;
import cn.shopping.ETASS.service.impl.CommonServiceImpl;
import it.unisa.dia.gas.jpbc.Element;

import java.io.File;
import java.util.List;

public class AlgoGetFileTest {
    public static void main(String[] args) {


        // 1 用户端获得用户的公钥和私钥
//        String id = "123";
        String id = "Alice";

        AlgorithmServiceImpl algorithmService = new AlgorithmServiceImpl();
        CommonService commonService = new CommonServiceImpl();
        algorithmService.setup();
        PKAndSKAndID pkAndsk = commonService.getPKAndSKAndID(id);
        SK sk = pkAndsk.getSk();
        String theta_id = pkAndsk.getTheta_id();
        Element Did = algorithmService.getDid(theta_id);

        //2 根据用户输入的关键字生成陷门
        String[] KW1 = {"测试","Alice"};

        TKW tkw = algorithmService.Trapdoor(sk,KW1);


        //3 云服务器寻找对应的文件
        CloudServer CS = new CloudServerImpl();
        CS.setup();
        String[] attrs = CS.getAttr(id);
        List<Encrypt_File> file_list = CS.getFile(KW1);
        int i = 0;
        if(file_list != null){
            for (Encrypt_File file : file_list) {
                CT ct = file.getCt();
                VKM vkm = file.getVkm();
                LSSSMatrix lsss = file.getLsss();

                LSSSMatrix lsssD1 = lsss.extract(attrs);
                int lsssIndex[] = lsssD1.getIndex();
                CTout ctout = CS.Transform(ct, tkw, Did,lsssD1,lsssIndex);

                //4 云服务传送ctout给用户，用户解密
                if(ctout != null){
                    String filename = "C:\\Users\\shopping\\Documents\\test\\decrypt"+i+".txt";
                    i++;
                    File de_file = new File(filename);
                    algorithmService.Dec(ctout, sk, vkm,filename);
                }
            }
        }else{
            System.out.println("无匹配文件");
        }
    }
}
