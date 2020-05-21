package cn.shopping.ETASS.dao.impl;

import cn.shopping.ETASS.dao.KGCDao;
import cn.shopping.ETASS.domain.KGCUser;
import cn.shopping.ETASS.domain.User;
import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.util.JDBCUtils;
import cn.shopping.ETASS.util.JDBCUtils_1;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class KGCDaoImpl implements KGCDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils_1.getDataSource());

    @Override
    public KGCUser login(String username, String password){

        try {
            String sql = "select * from kgc_user where username = ? and password = ?";
            KGCUser user = template.queryForObject(sql,
                    new BeanPropertyRowMapper<KGCUser>(KGCUser.class),
                    username, password);

            return user;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setup(PP pp, MSK msk) {
        Connection connection = null;
        PreparedStatement ps = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ByteArrayOutputStream bos_1 = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        ObjectOutputStream oos_1 = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(pp);
            oos_1 = new ObjectOutputStream(bos_1);
            oos_1.writeObject(msk);
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement("insert into setup (pp,msk) value(?,?)");
            ps.setBytes(1, bos.toByteArray());
            ps.setBytes(2,bos_1.toByteArray());
            ps.executeUpdate();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(ps, connection);
        }
    }


    @Override
    public void setPKAndSK(String id,PK pk, SK sk,String theta) {
        Connection connection = null;
        PreparedStatement ps = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ByteArrayOutputStream bos_1 = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        ObjectOutputStream oos_1 = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(pk);
            oos_1 = new ObjectOutputStream(bos_1);
            oos_1.writeObject(sk);
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement("insert into user_basic (user_id,pk,sk,theta) value(?,?,?,?)");
            ps.setString(1,id);
            ps.setBytes(2, bos.toByteArray());
            ps.setBytes(3,bos_1.toByteArray());
            ps.setString(4,theta);
            ps.executeUpdate();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(ps, connection);
        }
    }
}
