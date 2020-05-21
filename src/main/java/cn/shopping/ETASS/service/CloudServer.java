package cn.shopping.ETASS.service;

import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.domain.pv.CT;
import cn.shopping.ETASS.domain.pv.CTout;
import cn.shopping.ETASS.domain.pv.Encrypt_File;
import cn.shopping.ETASS.domain.pv.TKW;
import it.unisa.dia.gas.jpbc.Element;

import java.util.List;

public interface CloudServer {

    void setup();

    List<Encrypt_File> getFile(String[] KW1);

    public String[] getAttr(String id);

    CTout Transform(CT ct, TKW tkw, Element Did, LSSSMatrix lsssD1, int lsssIndex[]);

    void userRevo(String id);

    //    boolean KeySanityCheck(SK sk);
//    String Trance(SK sk);
}
