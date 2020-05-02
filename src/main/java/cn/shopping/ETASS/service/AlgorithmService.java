package cn.shopping.ETASS.service;

import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.domain.pv.*;
import it.unisa.dia.gas.jpbc.Element;

import java.util.List;

public interface AlgorithmService {
    void setup();

    PKAndSKAndID getPKAndSKAndID(String id);

    void CreateUL(String id, PK pk);

    Element getDid(String theta);

    void Enc(String user_id, String msg, String[] KW, LSSSMatrix lsss, String[] attributes);

    TKW Trapdoor(SK sk, String[] kw_1);

    byte[] Dec(CTout ctout, SK sk, VKM vkm);


}