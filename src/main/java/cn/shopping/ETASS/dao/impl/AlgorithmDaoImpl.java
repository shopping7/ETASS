package cn.shopping.ETASS.dao.impl;

import cn.shopping.ETASS.dao.AlgorithmDao;
import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.util.JDBCUtils;
import it.unisa.dia.gas.jpbc.Element;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlgorithmDaoImpl implements AlgorithmDao {

//    private JdbcTemplate template = new JdbcTemplate(JDBCUtils_1.getDataSource());

    @Override
    public void setup(PPAndMSK ppandmsk) {
        Connection connection = null;
        PreparedStatement ps = null;
        PP pp = ppandmsk.getPp();
        MSK msk = ppandmsk.getMsk();
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
    public PPAndMSK getPpAndMsk() {
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
    public void setPKAndSK(String id,PKAndSKAndID pkandsk) {
        Connection connection = null;
        PreparedStatement ps = null;
        PK pk = pkandsk.getPk();
        SK sk = pkandsk.getSk();
        String theta = pkandsk.getTheta_id();
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
            ps = connection.prepareStatement("insert into user_basic (id,pk,sk,theta) value(?,?,?,?)");
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

    @Override
    public PKAndSKAndID getPKAndSKAndID(String id) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement("select pk,sk,theta from user_basic where id = ?");
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
