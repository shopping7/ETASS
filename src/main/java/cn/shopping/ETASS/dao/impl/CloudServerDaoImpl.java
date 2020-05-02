package cn.shopping.ETASS.dao.impl;

import cn.shopping.ETASS.dao.CloudServerDao;
import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.domain.pv.CT;
import cn.shopping.ETASS.domain.pv.Encrypt_File;
import cn.shopping.ETASS.domain.pv.VKM;
import cn.shopping.ETASS.util.JDBCUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CloudServerDaoImpl implements CloudServerDao {

//    @Override
//    public List<CTAndVKM> getCtAndVkm(String[] KW) {
//        Connection connection = null;
//        PreparedStatement ps = null;
//        ResultSet resultSet = null;
//        try {
//            connection = JDBCUtils.getConnection();
//            ps = connection.prepareStatement("select ct,vkm from file where kw = ?");
//            ps.setString(1,KW[0]);
//            resultSet = ps.executeQuery();
//            List<CTAndVKM> file_list = new ArrayList<>();
//            while (resultSet.next()) {
//                ByteArrayInputStream bis = new ByteArrayInputStream(resultSet.getBytes(1));
//                ObjectInputStream ois = new ObjectInputStream(bis);
//                CT ct = (CT) ois.readObject();
//                bis = new ByteArrayInputStream(resultSet.getBytes(2));
//                ois = new ObjectInputStream(bis);
//                VKM vkm = (VKM) ois.readObject();
//
//                CTAndVKM ctandvkm = new CTAndVKM();
//                ctandvkm.setCt(ct);
//                ctandvkm.setVkm(vkm);
//                file_list.add(ctandvkm);
//            }
//            return file_list;
//        } catch (SQLException | IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            JDBCUtils.close(resultSet, ps, connection);
//        }
//
//        return null;
//    }

    @Override
    public List<Encrypt_File> getFile(String[] KW) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement("select ct,vkm,lsss from file where kw = ?");
            ps.setString(1,KW[0]);
            resultSet = ps.executeQuery();
            List<Encrypt_File> file_list = new ArrayList<>();
            while (resultSet.next()) {
                ByteArrayInputStream bis = new ByteArrayInputStream(resultSet.getBytes(1));
                ObjectInputStream ois = new ObjectInputStream(bis);
                CT ct = (CT) ois.readObject();
                bis = new ByteArrayInputStream(resultSet.getBytes(2));
                ois = new ObjectInputStream(bis);
                VKM vkm = (VKM) ois.readObject();
                bis = new ByteArrayInputStream(resultSet.getBytes(3));
                ois = new ObjectInputStream(bis);
                LSSSMatrix lsss = (LSSSMatrix) ois.readObject();

                Encrypt_File encryptFile = new Encrypt_File();
                encryptFile.setCt(ct);
                encryptFile.setVkm(vkm);
                encryptFile.setLsss(lsss);
                file_list.add(encryptFile);
            }
            return file_list;
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(resultSet, ps, connection);
        }

        return null;
    }


}
