package cn.shopping.ETASS.dao.impl;

import cn.shopping.ETASS.dao.UserDao;
import cn.shopping.ETASS.domain.User;
import cn.shopping.ETASS.util.JDBCUtils_1;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 操作数据中user表的类
 */
public class UserDaoImpl implements UserDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils_1.getDataSource());

    /**
     * 登录方法
     * @param username
     * @param password
     * @return
     */
    @Override
    public User login(String username, String password){

        try {
            String sql = "select * from user_login where username = ? and password = ?";
            User user = template.queryForObject(sql,
                    new BeanPropertyRowMapper<User>(User.class),
                    username, password);

            return user;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
