package cn.shopping.ETASS.dao.test;

import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.service.CloudServer;
import cn.shopping.ETASS.service.CommonService;
import cn.shopping.ETASS.service.KGC;
import cn.shopping.ETASS.service.impl.AlgorithmServiceImpl;
import cn.shopping.ETASS.service.impl.CloudServerImpl;
import cn.shopping.ETASS.service.impl.CommonServiceImpl;
import cn.shopping.ETASS.service.impl.KGCImpl;
import it.unisa.dia.gas.jpbc.Element;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SerializeTest {
    public static void main(String[] args) {
        KGC kgc = new KGCImpl();
        kgc.getSetup();
        String user_id = "Alice";
        CommonService commonService = new CommonServiceImpl();
        PKAndSKAndID pkAndsk = commonService.getPKAndSKAndID(user_id);
        SK sk_1 = pkAndsk.getSk();
        String theta_id_1 = pkAndsk.getTheta_id();
        String sk_file = "C:\\Users\\shopping\\Documents\\test\\sk.txt";
        String theta_id_file = "C:\\Users\\shopping\\Documents\\test\\theta_id.txt";
        try {
            FileOutputStream fos = new FileOutputStream(sk_file);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(sk_1);
            fos = new FileOutputStream(theta_id_file);
            os = new ObjectOutputStream(fos);
            os.writeObject(theta_id_1);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //读取对象
        //读出来
        SK sk = null;
        String theta_id = null;
        try {
            FileInputStream fis = new FileInputStream(sk_file);
            ObjectInputStream is = new ObjectInputStream(fis);
            sk=(SK)is.readObject();              //读出对象
            fis = new FileInputStream(theta_id_file);
            is = new ObjectInputStream(fis);
            theta_id=(String)is.readObject();              //读出对象
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }



        AlgorithmServiceImpl algorithmService = new AlgorithmServiceImpl();
        algorithmService.setup();
        Element Did = algorithmService.getDid(theta_id);

        //2 根据用户输入的关键字生成陷门
        String[] KW1 = {"测试","Alice"};

        TKW tkw = algorithmService.Trapdoor(sk,KW1);


        //3 云服务器寻找对应的文件
        CloudServer CS = new CloudServerImpl();
        CS.setup();
        String[] attrs = CS.getAttr(user_id);
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
                    String filename = "C:\\Users\\shopping\\Documents\\test\\decrypt_1"+i+".txt";
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
