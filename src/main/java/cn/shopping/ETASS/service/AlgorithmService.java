package cn.shopping.ETASS.service;

import cn.shopping.ETASS.domain.pv.*;
import it.unisa.dia.gas.jpbc.Element;

public interface AlgorithmService {
    void setup();
    void KeyGen(String id, String[] attributes);
    PKAndSKAndID getPKAndSKAndID(String id);
    void CreateUL(String id, PK pk);
    Element getDid(String theta);
    void Enc(String user_id,String msg, String[] KW, double[][] lsss,String[] attributes);
    TKW Trapdoor(SK sk, String[] kw_1);
    CTAndVKM getCtAndVkm();
    CTout Transform(CT ct, TKW tkw, Element Did, double[][] lsssD1, int lsssIndex[]);
    byte[] Dec(CTout ctout, SK sk, VKM vkm);
//    boolean KeySanityCheck(SK sk);
//    String Trance(SK sk);

}