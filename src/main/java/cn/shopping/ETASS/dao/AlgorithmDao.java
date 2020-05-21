package cn.shopping.ETASS.dao;

import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.domain.pv.*;
import it.unisa.dia.gas.jpbc.Element;

import java.util.List;


public interface AlgorithmDao {

    void addUL(String encoded, Element Did);

    void addS(String user_id,Element s);

    byte[] getS(String user_id);

    byte[] getDid(String theta);

    void uploadFile(CT ct, VKM vkm, String[] kw_s, LSSSMatrix lsss);

}
