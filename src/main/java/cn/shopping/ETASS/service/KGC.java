package cn.shopping.ETASS.service;

import cn.shopping.ETASS.domain.KGCUser;
import cn.shopping.ETASS.domain.User;
import cn.shopping.ETASS.domain.pv.Attr;
import cn.shopping.ETASS.domain.pv.SK;

import java.util.List;

public interface KGC {

     KGCUser login(KGCUser user);

     void setup();

     void getSetup();


     void KeyGen(String id);

     String Trance(SK sk);

     void userRevo(byte[] theta_id);


}
