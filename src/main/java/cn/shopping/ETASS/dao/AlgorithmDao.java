package cn.shopping.ETASS.dao;

import cn.shopping.ETASS.domain.pv.CTAndVKM;
import cn.shopping.ETASS.domain.pv.MSK;
import cn.shopping.ETASS.domain.pv.PP;
import it.unisa.dia.gas.jpbc.Element;

import java.io.IOException;
import java.sql.SQLException;


public interface AlgorithmDao {

    PP getPp() throws Exception;

    MSK getMsk();

    void addUL(String encoded, Element Did) throws IOException, Exception;

    byte[] getDid(String theta) throws SQLException, Exception;

    void upload(CTAndVKM ctandvkm);

    CTAndVKM getCtAndVkm();

}
