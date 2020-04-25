package cn.shopping.ETASS.dao;


import cn.shopping.ETASS.domain.User;

public interface UserDao {

    User login(String username, String password);

}
