package cn.shopping.ETASS.dao.test;

import cn.shopping.ETASS.domain.pv.PK;
import cn.shopping.ETASS.domain.pv.PKAndSKAndID;
import cn.shopping.ETASS.service.AlgorithmService;
import cn.shopping.ETASS.service.CommonService;
import cn.shopping.ETASS.service.KGC;
import cn.shopping.ETASS.service.impl.AlgorithmServiceImpl;
import cn.shopping.ETASS.service.impl.CommonServiceImpl;
import cn.shopping.ETASS.service.impl.KGCImpl;
import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer11_OmitComments;

public class KGCNewUserTest {
    public static void main(String[] args) {
//        String id = "Alice";
//        String id = "123";
        String username = "Alice";
        String password = "123456";
//        String[] user_attr = {"hospital","doctor","headache"};
        String id = username;


        KGC kgc = new KGCImpl();
        CommonService common = new CommonServiceImpl();
        kgc.getSetup();
        common.addUser(id,username);

        common.addUserAttr(id,"hospital");
        common.addUserAttr(id,"doctor");
        common.addUserAttr(id,"heart");


        kgc.KeyGen(id);

        CommonService commonService = new CommonServiceImpl();
        PKAndSKAndID pkAndSKAndID = commonService.getPKAndSKAndID(id);
        PK pk = pkAndSKAndID.getPk();
////
        AlgorithmService algorithmService = new AlgorithmServiceImpl();
        algorithmService.setup();
        algorithmService.CreateUL(id,pk);

    }
}
