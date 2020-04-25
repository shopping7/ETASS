package cn.shopping.ETASS.service;

import cn.shopping.ETASS.domain.User;

/**
 * 用户管理的接口
 */
public interface UserService {

    /**
     * 登录方法
     * @param user
     * @return
     */
    User login(User user);
}
