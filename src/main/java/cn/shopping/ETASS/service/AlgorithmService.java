package cn.shopping.ETASS.service;



import cn.shopping.ETASS.domain.pv.*;
import it.unisa.dia.gas.jpbc.Element;

import java.io.IOException;
import java.util.HashMap;

public interface AlgorithmService {
    void setup() throws IOException, Exception;
    PKAndSKAndID KeyGen(String id, String[] attributes);
    void CreateUL(String id, PK pk) throws Exception;
    void Enc(String msg, String[] KW, double[][] lsss);
    TKW Trapdoor(SK sk, String[] kw_1);
    CTout Transform(CT ct, TKW tkw, Element Did, double[][] lsssD1, int lsssIndex[]);
    byte[] Dec(CTout ctout, SK sk, VKM vkm);
    boolean KeySanityCheck(SK sk);
    String Trance(SK sk);

}