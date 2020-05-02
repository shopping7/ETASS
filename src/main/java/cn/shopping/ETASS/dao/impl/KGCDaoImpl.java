package cn.shopping.ETASS.dao.impl;

import cn.shopping.ETASS.dao.KGCDao;
import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.util.JDBCUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class KGCDaoImpl implements KGCDao {
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
