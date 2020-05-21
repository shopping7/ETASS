package cn.shopping.ETASS.dao.impl;

import cn.shopping.ETASS.dao.CommonDao;
import cn.shopping.ETASS.domain.User;
import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.util.JDBCUtils;
import cn.shopping.ETASS.util.JDBCUtils_1;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommonDaoImpl implements CommonDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils_1.getDataSource());

    @Override
    public PPAndMSK getSetUp() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement("select pp,msk from setup");
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                ByteArrayInputStream bis = new ByteArrayInputStream(resultSet.getBytes(1));
                ObjectInputStream ois = new ObjectInputStream(bis);
                PP pp = (PP) ois.readObject();
                bis = new ByteArrayInputStream(resultSet.getBytes(2));
                ois = new ObjectInputStream(bis);
                MSK msk = (MSK) ois.readObject();

                PPAndMSK ppandmsk = new PPAndMSK();
                ppandmsk.setMsk(msk);
                ppandmsk.setPp(pp);
                return ppandmsk;
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(resultSet, ps, connection);
        }
        return null;
    }


    @Override
    public PKAndSKAndID getPKAndSKAndID(String id) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement("select pk,sk,theta from user_basic where user_id = ?");
            ps.setString(1,id);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                ByteArrayInputStream bis = new ByteArrayInputStream(resultSet.getBytes(1));
                ObjectInputStream ois = new ObjectInputStream(bis);
                PK pk = (PK) ois.readObject();
                bis = new ByteArrayInputStream(resultSet.getBytes(2));
                ois = new ObjectInputStream(bis);
                SK sk = (SK) ois.readObject();

                String theta = resultSet.getString(3);

                PKAndSKAndID pkandsk = new PKAndSKAndID();
                pkandsk.setPk(pk);
                pkandsk.setSk(sk);
                pkandsk.setTheta_id(theta);
                return pkandsk;
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(resultSet, ps, connection);
        }
        return null;
    }

//    @Override
//    public User getUserInfo(String user_id) {
//        String sql = "SELECT a.user_id,a.username,c.attr FROM user_login a,user_attr b,attr c WHERE a.user_id=b.user_id AND b.attr_id=c.id AND a.user_id = ?";
////        KGCUser user = template.queryForObject(sql,
////                new BeanPropertyRowMapper<KGCUser>(KGCUser.class),
////                username, password);
//        User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), user_id);
//        return user;
//
//    }


    @Override
    public List<Attr> getAllAttr() {
        String sql = "select * from attr";
        return template.query(sql,new BeanPropertyRowMapper<Attr>(Attr.class));
    }


    @Override
    public void addAttr(String attr) {
        String sql = "insert into attr (attr) value(?)";
        template.update(sql,attr);
    }

    @Override
    public void deleteAttr(int id) {

        String sql = "delete from attr where id = ?";

        template.update(sql,id);
    }

    @Override
    public void AlterAttr(String id, String attr) {

    }

    @Override
    public List<User> getAllUser() {
        String sql = "select * from user_login";
        return template.query(sql,new BeanPropertyRowMapper<User>(User.class));
    }

    @Override
    public void addUser(String id, String username) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement("insert into user_login (user_id,username) value(?,?)");
            ps.setString(1,id);
            ps.setString(2,username);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(ps, connection);
        }
    }

    @Override
    public void deleteUser(String user_id) {
        String sql = "delete from user_login where user_id = ?";

        template.update(sql,user_id);
    }

    @Override
    public void addUserAttr(String id, String attr) {
        String sql = "INSERT INTO user_attr(user_id,attr_id) SELECT ?,(SELECT id FROM attr WHERE attr=?)";
        template.update(sql,id,attr);
    }

    @Override
    public void addUserAttrById(String id, String attr_id) {
        String sql = "INSERT INTO user_attr(user_id,attr_id) value (?,?)";
        template.update(sql,id,attr_id);
    }

    @Override
    public void deleteUserAttr(String id, String attr) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement("select id from attr where attr = ?");
            ps.setString(1,attr);
            resultSet = ps.executeQuery();
            int attr_id = 0;
            while(resultSet.next()) {
                attr_id = resultSet.getInt(1);
            }
            ps = connection.prepareStatement("DELETE FROM user_attr WHERE user_id=?AND attr_id=?");
            ps.setString(1,id);
            ps.setInt(2,attr_id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(resultSet,ps, connection);
        }
    }

    @Override
    public List<String> getUserAttr(String id){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        ArrayList<String> list = new ArrayList<>();
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement("SELECT attr FROM user_attr u INNER JOIN attr a ON u.`attr_id` = a.`id` WHERE u.`user_id` = ?;");
            ps.setString(1,id);
            resultSet = ps.executeQuery();


            while(resultSet.next()){
                String attr = resultSet.getString(1);
                list.add(attr);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(resultSet,ps, connection);
        }

//关闭连接

        return null;
    }

    @Override
    public void editUsername(String id, String username) {
        String sql = "UPDATE user_login SET username = ? WHERE user_id= ? ";
        template.update(sql,username,id);
    }

    @Override
    public User getOneUser(String user_id) {
        String sql = "select * from user_login where user_id = ?";
        return template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),user_id);
    }

    @Override
    public void updateUsername(String user_id, String username) {
        String sql = "update user_login set username = ? where user_id = ?";
        template.update(sql,username,user_id);
    }

    @Override
    public void updateUserAttr(String user_id, String[] attr_update) {
        String sql = "delete from user_attr where user_id = ?";
        template.update(sql,user_id);
        for (int i = 0; i < attr_update.length; i++) {
            String sql1 = "INSERT INTO user_Attr(user_id,attr_id) VALUE (?,(SELECT id FROM attr WHERE attr = ?))";
            template.update(sql1,user_id,attr_update[i]);
        }
    }


}
