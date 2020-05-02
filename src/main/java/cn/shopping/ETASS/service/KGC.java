package cn.shopping.ETASS.service;

import cn.shopping.ETASS.domain.pv.PPAndMSK;

public interface KGC {

     void setup();

     void KeyGen(String id, String[] attributes);
}
