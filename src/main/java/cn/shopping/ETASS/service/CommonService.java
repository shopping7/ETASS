package cn.shopping.ETASS.service;

import cn.shopping.ETASS.domain.User;
import cn.shopping.ETASS.domain.pv.Attr;
import cn.shopping.ETASS.domain.pv.PKAndSKAndID;

import java.util.List;

public interface CommonService {

    PKAndSKAndID getPKAndSKAndID(String id);

    List<String> getUserAttr(String user_id);

    List<Attr> getAllAttr();

    void addAttr(String attr);

    void deleteAttr(int id);

    void AlterAttr(String attr);

    void addUser(String id, String username);

    void deleteUser(String user_id);

    List<User> getAllUser();

    void addUserAttr(String id, String attr);

    void addUserAttrById(String id, String attr_id);

    void deleteUserAttr(String id, String attr);

    void editUsername(String id,String username);

    User getOneUser(String user_id);

    void updateUser(String user_id, String username, String[] attr_update);

    String getUsername(String id);
}
