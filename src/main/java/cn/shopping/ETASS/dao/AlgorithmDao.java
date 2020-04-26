package cn.shopping.ETASS.dao;

import cn.shopping.ETASS.domain.pv.*;
import it.unisa.dia.gas.jpbc.Element;

import java.io.IOException;
import java.sql.SQLException;


public interface AlgorithmDao {

    void setup(PPAndMSK ppandmsk);

    PPAndMSK getPpAndMsk();

    void setPKAndSK(String id, PKAndSKAndID pkandsk);

    PKAndSKAndID getPKAndSKAndID(String id);

    void addUL(String encoded, Element Did);

    void addS(String user_id,Element s);

    byte[] getS(String user_id);

    byte[] getDid(String theta);

    void upload(CTAndVKM ctandvkm);

    CTAndVKM getCtAndVkm();

}
