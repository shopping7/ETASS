package cn.shopping.ETASS.dao;

import cn.shopping.ETASS.domain.pv.Encrypt_File;

import java.util.List;

public interface CloudServerDao {

    List<Encrypt_File> getFile(String[] KW);

    List<String> getAttr(String id);

    void userRevo(String id);

//    List<CTAndVKM> getCtAndVkm(String[] KW);
}
