package cn.shopping.ETASS.service;

import cn.shopping.ETASS.domain.User;
import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.domain.pv.*;
import it.unisa.dia.gas.jpbc.Element;

import java.io.File;
import java.util.List;

public interface AlgorithmService {
    User login(User user);

    void setup();

    void CreateUL(String id, PK pk);

    Element getDid(String theta);

    void Enc(String user_id, File file, String[] KW, LSSSMatrix lsss);

    TKW Trapdoor(SK sk, String[] kw_1);

    void Dec(CTout ctout, SK sk, VKM vkm, String filename);


}