package cn.shopping.ETASS.dao.impl;

import cn.shopping.ETASS.dao.AlgorithmDao;
import cn.shopping.ETASS.dao.People;
import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.util.JDBCUtils;
import cn.shopping.ETASS.util.JDBCUtils_1;
import it.unisa.dia.gas.jpbc.Element;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class AlgorithmDaoImpl implements AlgorithmDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils_1.getDataSource());

    //
//
    @Override
    public PP getPp() {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(bos);
//        oos.writeObject(pp);
////
//        ByteArrayOutputStream bos_1 = new ByteArrayOutputStream();
//        ObjectOutputStream oos_1 = new ObjectOutputStream(bos_1);
//        oos_1.writeObject(msk);
//
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement("select id,pp from pp");
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1));
                ByteArrayInputStream bis = new ByteArrayInputStream(resultSet.getBytes(2));
                ObjectInputStream ois = new ObjectInputStream(bis);
                PP pp = (PP) ois.readObject();

                return pp;

            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(resultSet, ps, connection);
        }
//        PreparedStatement ps = connection.prepareStatement("insert into pp (PP) value(?)");
//        PreparedStatement ps_1 = connection.prepareStatement("insert into msk (MSK) value(?)");
//
//        ps.setBytes(1, bos.toByteArray());
//        ps.executeUpdate();
//        ps_1.setBytes(1, bos_1.toByteArray());
//        ps_1.executeUpdate();


        return null;
    }

    @Override
    public MSK getMsk() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement("select id,msk from msk");
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1));
                ByteArrayInputStream bis = new ByteArrayInputStream(resultSet.getBytes(2));
                ObjectInputStream ois = new ObjectInputStream(bis);
                MSK msk = (MSK) ois.readObject();

                return msk;
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(resultSet, ps, connection);
        }
        return null;
    }

    @Override
    public void addUL(String encoded, Element Did){

        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement("insert into ul (theta,Did) value(?,?)");
            ps.setString(1,encoded);
            byte[] d = Did.toBytes();
            ps.setBytes(2, d);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(ps, connection);
        }

    }

    @Override
    public byte[] getDid(String theta){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement("select did from ul where theta = ?");
            ps.setString(1,theta);
            resultSet = ps.executeQuery();


            while(resultSet.next()){
                byte[] bytes = resultSet.getBytes(1);
                return bytes;
//            System.out.println(bytes);
//                ByteArrayInputStream bis = new ByteArrayInputStream(resultSet.getBytes(2));
//                ObjectInputStream ois = new ObjectInputStream(bis);
//                People pn = (People) ois.readObject();
//                System.out.println(pn.getName());
//                System.out.println(pn.getAge());
//                System.out.println(pn.getBirthday());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(resultSet,ps, connection);
        }

//关闭连接

        return null;
    }

    @Override
    public void upload(CTAndVKM ctandvkm) {
        Connection connection = null;
        PreparedStatement ps = null;
        CT ct = ctandvkm.getCt();
        VKM vkm = ctandvkm.getVkm();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ByteArrayOutputStream bos_1 = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        ObjectOutputStream oos_1 = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(ct);
            oos_1 = new ObjectOutputStream(bos_1);
            oos_1.writeObject(vkm);
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement("insert into file (ct,vkm) value(?,?)");
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
    public CTAndVKM getCtAndVkm() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement("select ct,vkm from file");
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                ByteArrayInputStream bis = new ByteArrayInputStream(resultSet.getBytes(1));
                ObjectInputStream ois = new ObjectInputStream(bis);
                CT ct = (CT) ois.readObject();
                bis = new ByteArrayInputStream(resultSet.getBytes(2));
                ois = new ObjectInputStream(bis);
                VKM vkm = (VKM) ois.readObject();

                CTAndVKM ctandvkm = new CTAndVKM();
                ctandvkm.setCt(ct);
                ctandvkm.setVkm(vkm);
                return ctandvkm;
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(resultSet, ps, connection);
        }

        return null;
    }


    @Test
    public void test() throws Exception {




        //people序列化存进数据库
//            People p = new People();
//            p.setName("哈哈".getBytes());
//            p.setAge(10);
//            p.setBirthday(Calendar.getInstance().getTime());
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            ObjectOutputStream oos = new ObjectOutputStream(bos);
//            oos.writeObject(p);
//
//            Connection connection = JDBCUtils.getConnection();
//            PreparedStatement ps = connection.prepareStatement("insert into people (people) value(?)");
//
//            ps.setBytes(1, bos.toByteArray());
//            ps.executeUpdate();
//
//            ps = connection.prepareStatement("select id,people from people");
//            ResultSet resultSet = ps.executeQuery();
//
//
//            while(resultSet.next()){
//                System.out.println(resultSet.getInt(1));
//                ByteArrayInputStream bis = new ByteArrayInputStream(resultSet.getBytes(2));
//                ObjectInputStream ois = new ObjectInputStream(bis);
//                People pn = (People) ois.readObject();
//                System.out.println(pn.getName());
//                System.out.println(pn.getAge());
//                System.out.println(pn.getBirthday());
//            }
////关闭连接
//            JDBCUtils.close(resultSet,ps, connection);

        }

}
