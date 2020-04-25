package cn.shopping.ETASS.service.impl;

import cn.shopping.ETASS.dao.UserDao;
import cn.shopping.ETASS.dao.impl.UserDaoImpl;
import cn.shopping.ETASS.domain.User;
import cn.shopping.ETASS.service.UserService;

public class UserServiceImpl implements UserService {
    private UserDao dao =  new UserDaoImpl();

    @Override
    public User login(User user) {

        return dao.login(user.getUsername(),user.getPassword());
    }
}
