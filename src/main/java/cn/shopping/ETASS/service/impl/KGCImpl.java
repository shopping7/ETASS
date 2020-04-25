package cn.shopping.ETASS.service.impl;

import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.service.KGC;
import cn.shopping.ETASS.util.JDBCUtils;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class KGCImpl implements KGC {
    private Pairing pairing;
    private Field G1, GT, Zr, K;
    private Element a, b, g, Y, k1, k2,lambda, f;

    @Override
    public PPAndMSK setup(){
        pairing = PairingFactory.getPairing("a.properties");
        PairingFactory.getInstance().setUsePBCWhenPossible(true);
        if (!pairing.isSymmetric()) {
            throw new RuntimeException("密钥不对称!");
        }
        Zr = pairing.getZr();
        K = pairing.getG2();
        G1 = pairing.getG1();
        GT = pairing.getGT();

        a = Zr.newRandomElement().getImmutable();
        b = Zr.newRandomElement().getImmutable();
        lambda = Zr.newRandomElement().getImmutable();

        f = G1.newRandomElement().getImmutable();
        k1 = K.newRandomElement().getImmutable();
        k2 = K.newRandomElement().getImmutable();

        g = G1.newRandomElement().getImmutable();

        Y = pairing.pairing(g, g).powZn(a).getImmutable();


        MSK msk = new MSK();
        msk.setA(a.toBytes());
        msk.setB(b.toBytes());
        msk.setLambda(lambda.toBytes());
        msk.setK1(k1.toBytes());
        msk.setK2(k2.toBytes());

        PP pp = new PP();
        pp.setF(f.toBytes());
        pp.setG(g.toBytes());
        pp.setGb(g.powZn(b).toBytes());
        pp.setGlambda(g.powZn(lambda).toBytes());
        pp.setY(Y.toBytes());

        PPAndMSK ppandmsk = new PPAndMSK();
        ppandmsk.setMsk(msk);
        ppandmsk.setPp(pp);

        return ppandmsk;


//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(bos);
//        oos.writeObject(pp);
//
//        ByteArrayOutputStream bos_1 = new ByteArrayOutputStream();
//        ObjectOutputStream oos_1 = new ObjectOutputStream(bos_1);
//        oos_1.writeObject(msk);
//
//        Connection connection = JDBCUtils.getConnection();
//        PreparedStatement ps = connection.prepareStatement("insert into pp (PP) value(?)");
//        PreparedStatement ps_1 = connection.prepareStatement("insert into msk (MSK) value(?)");
//
//        ps.setBytes(1, bos.toByteArray());
//        ps.executeUpdate();
//        ps_1.setBytes(1, bos_1.toByteArray());
//        ps_1.executeUpdate();

//        PreparedStatement ps = connection.prepareStatement("select id,pp from pp ");
//        ResultSet resultSet = ps.executeQuery();
//
//
//        while(resultSet.next()){
//            System.out.println(resultSet.getInt(1));
//            ByteArrayInputStream bis = new ByteArrayInputStream(resultSet.getBytes(2));
//            ObjectInputStream ois = new ObjectInputStream(bis);
//            PP pn = (PP)ois.readObject();
//            byte[] f_bytes = pn.getF();
//            byte[] g_bytes = pn.getG();
//            byte[] Y_bytes = pn.getY();
//            Element f_1 = G1.newElementFromBytes(f_bytes);
//            Element g_1 = G1.newElementFromBytes(g_bytes);
//            Element Y_1 = GT.newElementFromBytes(Y_bytes);
//            System.out.println(Y_1);
//        }
////关闭连接
//        JDBCUtils.close(resultSet,ps, connection);
    }
}
