package cn.shopping.ETASS.dao;

import cn.shopping.ETASS.domain.User;
import cn.shopping.ETASS.domain.pv.Attr;
import cn.shopping.ETASS.domain.pv.PKAndSKAndID;
import cn.shopping.ETASS.domain.pv.PPAndMSK;

import java.util.List;

public interface CommonDao {

    PPAndMSK getSetUp();

    PKAndSKAndID getPKAndSKAndID(String id);

    List<Attr> getAllAttr();

    void addAttr(String attr);

    void deleteAttr(int id);

    void AlterAttr(String id, String attr);

    List<User> getAllUser();

    void addUser(String id, String username);

    void deleteUser(String id);

    void addUserAttr(String id,String attr);

    void addUserAttrById(String id,String attr_id);

    void deleteUserAttr(String id, String attr);

    List<String> getUserAttr(String id);

    void editUsername(String id, String username);

    User getOneUser(String user_id);

    void updateUsername(String user_id, String username);

    void updateUserAttr(String user_id, String[] attr_update);

//    User getOneUser(String user_id);

}
