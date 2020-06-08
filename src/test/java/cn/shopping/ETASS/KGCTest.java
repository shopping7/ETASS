package cn.shopping.ETASS;

import cn.shopping.ETASS.domain.pv.PKAndSKAndID;
import cn.shopping.ETASS.domain.pv.SK;
import cn.shopping.ETASS.domain.pv.TranceID;
import cn.shopping.ETASS.service.CommonService;
import cn.shopping.ETASS.service.KGC;
import cn.shopping.ETASS.service.impl.CommonServiceImpl;
import cn.shopping.ETASS.service.impl.KGCImpl;

public class KGCTest {
    public static void main(String[] args) {
        CommonService service = new CommonServiceImpl();

        KGC kgc = new KGCImpl();
        //1 生成系统参数
//        kgc.setup();
        kgc.getSetup();

        //2 给用户分配属性
        //2.1 管理系统属性
        service.addAttr("nurse");
        service.deleteAttr(2);
        //2.2 管理用户属性
//        kgc.addUserAttr("Alice","nurse");
        service.deleteUserAttr("Alice","nurse");

        String[] attr = {"hospital"};
        //3 生成用户公钥和私钥
        kgc.KeyGen("Alice");

        //4 追溯恶意用户id
        PKAndSKAndID alice = service.getPKAndSKAndID("Alice");
        SK sk = alice.getSk();
        TranceID id = kgc.Trance(sk);

        if(id != null){
            System.out.println(id.getId());
        }else{
            System.out.println("追溯id失败");
        }

    }



}
