package cn.shopping.ETASS.service.impl;

import cn.shopping.ETASS.dao.CommonDao;
import cn.shopping.ETASS.dao.impl.CommonDaoImpl;
import cn.shopping.ETASS.domain.User;
import cn.shopping.ETASS.domain.pv.Attr;
import cn.shopping.ETASS.domain.pv.PKAndSKAndID;
import cn.shopping.ETASS.service.CommonService;

import java.util.Iterator;
import java.util.List;

public class CommonServiceImpl implements CommonService {
    private CommonDao commonDao = new CommonDaoImpl();

    @Override
    public PKAndSKAndID getPKAndSKAndID(String id) {
        PKAndSKAndID pkAndSKAndID = commonDao.getPKAndSKAndID(id);
        if(pkAndSKAndID != null){
            return pkAndSKAndID;
        }else {
            return null;
        }

    }

    @Override
    public List<String> getUserAttr(String user_id) {
        return commonDao.getUserAttr(user_id);
    }

    @Override
    public List<Attr> getAllAttr() {
        return commonDao.getAllAttr();
    }


    @Override
    public void addAttr(String attr) {
        commonDao.addAttr(attr);
    }

    @Override
    public void deleteAttr(int id) {
        commonDao.deleteAttr(id);
    }

    @Override
    public void AlterAttr(String attr) {

    }

    @Override
    public void addUser(String id, String username) {
        commonDao.addUser(id, username);
    }

    @Override
    public void deleteUser(String user_id) {
        commonDao.deleteUser(user_id);
    }

    @Override
    public List<User> getAllUser() {
        List<User> users = commonDao.getAllUser();
        Iterator<User> iterator = users.iterator();
        while(iterator.hasNext()){
            User user = iterator.next();
            String user_id = user.getUser_id();
            List<String> userAttr = commonDao.getUserAttr(user_id);
            user.setAttribute(userAttr);
            user.setAttrs(userAttr.toString());
        }
        return users;
    }




    @Override
    public void addUserAttr(String id,String attr) {
        commonDao.addUserAttr(id,attr);
    }

    @Override
    public void addUserAttrById(String id, String attr_id) {
        commonDao.addUserAttrById(id,attr_id);
    }

    @Override
    public void deleteUserAttr(String id, String attr) {
        commonDao.deleteUserAttr(id,attr);
    }

    @Override
    public void editUsername(String id, String username) {
        commonDao.editUsername(id,username);
    }

    @Override
    public User getOneUser(String user_id) {
        return commonDao.getOneUser(user_id);
    }

    @Override
    public void updateUser(String user_id, String username, String[] attr_update) {
        commonDao.updateUsername(user_id,username);
        commonDao.updateUserAttr(user_id,attr_update);

    }


}
