package cn.shopping.ETASS.service;

import cn.shopping.ETASS.domain.KGCUser;
import cn.shopping.ETASS.domain.pv.MSK;
import cn.shopping.ETASS.domain.pv.PP;
import cn.shopping.ETASS.domain.pv.SK;
import cn.shopping.ETASS.domain.pv.TranceID;

public interface KGC {

     KGCUser login(KGCUser user);

     void setup();

     void getSetup();

     void updateSystem();

     void KeyGen(String id);

     TranceID Trance(SK sk);

     void userRevo(byte[] theta_id);



}
