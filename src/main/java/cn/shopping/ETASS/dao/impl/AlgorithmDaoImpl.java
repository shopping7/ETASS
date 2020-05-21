package cn.shopping.ETASS.dao.impl;

import cn.shopping.ETASS.dao.AlgorithmDao;
import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.util.JDBCUtils;
import it.unisa.dia.gas.jpbc.Element;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlgorithmDaoImpl implements AlgorithmDao {



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
    public void addS(String user_id,Element s) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement("insert into user_s (user_id,s) value(?,?)");
            ps.setString(1,user_id);
            byte[] d = s.toBytes();
            ps.setBytes(2, d);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(ps, connection);
        }
    }

    @Override
    public byte[] getS(String user_id) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement("select s from user_s where user_id = ?");
            ps.setString(1,user_id);
            resultSet = ps.executeQuery();


            while(resultSet.next()){
                byte[] bytes = resultSet.getBytes(1);
                return bytes;
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(resultSet,ps, connection);
        }

//关闭连接

        return null;
    }

    //测试上传lsss
    public void uploadFile(CT ct, VKM vkm, String[] kw, LSSSMatrix lsss) {
        Connection connection = null;
        PreparedStatement ps = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ByteArrayOutputStream bos_1 = new ByteArrayOutputStream();
        ByteArrayOutputStream bos_2 = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        ObjectOutputStream oos_1 = null;
        ObjectOutputStream oos_2 = null;
        ResultSet resultSet = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(ct);
            oos_1 = new ObjectOutputStream(bos_1);
            oos_1.writeObject(vkm);
            oos_2 = new ObjectOutputStream(bos_2);
            oos_2.writeObject(lsss);
            connection = JDBCUtils.getConnection();
            //插入关键字
            ps = connection.prepareStatement("insert into file (ct,vkm,lsss) value(?,?,?)");
            ps.setBytes(1, bos.toByteArray());
            ps.setBytes(2,bos_1.toByteArray());
            ps.setBytes(3,bos_2.toByteArray());
            ps.executeUpdate();
            ps = connection.prepareStatement("SELECT LAST_INSERT_ID();");
            resultSet = ps.executeQuery();
            int id = 0;
            while(resultSet.next()){
                id = resultSet.getInt(1);
            }
            ps = connection.prepareStatement("insert into file_kw (file_id,kw) value(?,?)");

            for (int i = 0; i < kw.length; i++) {
                ps.setInt(1,id);
                ps.setString(2,kw[i]);
                ps.executeUpdate();
            }


        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(ps, connection);
        }

    }

//    @Override
//    public void upload(CTAndVKM ctandvkm) {
//        Connection connection = null;
//        PreparedStatement ps = null;
//        CT ct = ctandvkm.getCt();
//        VKM vkm = ctandvkm.getVkm();
//        String[] kw_s = ctandvkm.getKW();
//        String kw = kw_s[0];
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ByteArrayOutputStream bos_1 = new ByteArrayOutputStream();
//        ObjectOutputStream oos = null;
//        ObjectOutputStream oos_1 = null;
//        try {
//            oos = new ObjectOutputStream(bos);
//            oos.writeObject(ct);
//            oos_1 = new ObjectOutputStream(bos_1);
//            oos_1.writeObject(vkm);
//            connection = JDBCUtils.getConnection();
//            ps = connection.prepareStatement("insert into file (kw,ct,vkm) value(?,?,?)");
//            ps.setString(1, kw);
//            ps.setBytes(2, bos.toByteArray());
//            ps.setBytes(3,bos_1.toByteArray());
//            ps.executeUpdate();
//
//        } catch (IOException | SQLException e) {
//            e.printStackTrace();
//        }finally {
//            JDBCUtils.close(ps, connection);
//        }
//
//    }





//    public void test() throws Exception {




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

//        }

}
