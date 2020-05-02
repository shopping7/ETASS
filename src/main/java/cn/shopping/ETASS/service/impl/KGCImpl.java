package cn.shopping.ETASS.service.impl;

import cn.shopping.ETASS.dao.AlgorithmDao;
import cn.shopping.ETASS.dao.KGCDao;
import cn.shopping.ETASS.dao.impl.AlgorithmDaoImpl;
import cn.shopping.ETASS.dao.impl.KGCDaoImpl;
import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.service.KGC;
import cn.shopping.ETASS.util.JDBCUtils;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.springframework.util.DigestUtils;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;

public class KGCImpl implements KGC {
    private KGCDao dao = new KGCDaoImpl();
    private Pairing pairing;
    private Field G1, GT, Zr, K;
    private Element a, b, g, Y, k1, k2,lambda, f;

    @Override
    public void setup(){
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

        dao.setup(ppandmsk);


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

    @Override
    public void KeyGen(String id, String attributes[]) {
        Element  D1, D1_1, D2, D2_1, D4, xid, Yid;
        Element[] D3;
        Element t = Zr.newRandomElement().getImmutable();
        Element kappa = Zr.newRandomElement().getImmutable();
        xid = Zr.newRandomElement().getImmutable();
        Yid = Y.powZn(xid).getImmutable().getImmutable();

        //计算每个用户的theta
        byte[] theta_id = Crytpto.SEnc(id.getBytes(), k1.toBytes());
        //计算每个用户的zeta
        String encoded = Base64.getEncoder().encodeToString(theta_id);
        String zeta_t1 = encoded + kappa.toString();
        byte[] zeta_t2 = Crytpto.SEnc(zeta_t1.getBytes(), k2.toBytes());
        String zeta_s = Base64.getEncoder().encodeToString(zeta_t2);
        Element zeta= Zr.newElementFromBytes(zeta_t2).getImmutable();

        D1 = g.powZn(a.mulZn((lambda.add(zeta)).invert())).getImmutable();
        D1 = D1.mul(g.powZn(b.mul(t)));
        D1_1 = zeta.getImmutable();
        D2 = g.powZn(t).getImmutable();
        D2_1 = g.powZn(lambda.mulZn(t)).getImmutable();
        D3 = new Element[attributes.length];
        Element[] pi = new Element[attributes.length];
        byte[][] D3_byte = new byte[attributes.length][];
        for (int i = 0; i < attributes.length; i++) {
            String md5 = DigestUtils.md5DigestAsHex(attributes[i].getBytes());
            pi[i] = G1.newElementFromHash(md5.getBytes(), 0, md5.length()).getImmutable();
            D3[i] = pi[i].powZn((lambda.add(zeta)).mul(t)).getImmutable();
            D3_byte[i] = D3[i].toBytes();
        }
        D4 = Zr.newRandomElement().getImmutable();

        SK sk = new SK();
        sk.setD1(D1.toBytes());
        sk.setD1_1(D1_1.toBytes());
        sk.setD2(D2.toBytes());
        sk.setD2_1(D2_1.toBytes());
        sk.setD3(D3_byte);
        sk.setD4(D4.toBytes());
        sk.setXid(xid.toBytes());
        sk.setZeta(zeta_s);

        PK pk = new PK();
        pk.setYid(Yid.toBytes());

        PKAndSKAndID pkandsk = new PKAndSKAndID();
        pkandsk.setPk(pk);
        pkandsk.setSk(sk);
        pkandsk.setTheta_id(encoded);

        dao.setPKAndSK(id,pkandsk);
    }


}
