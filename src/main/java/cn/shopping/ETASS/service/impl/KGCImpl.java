package cn.shopping.ETASS.service.impl;


import cn.shopping.ETASS.dao.CommonDao;
import cn.shopping.ETASS.dao.KGCDao;
import cn.shopping.ETASS.dao.impl.CommonDaoImpl;
import cn.shopping.ETASS.dao.impl.KGCDaoImpl;
import cn.shopping.ETASS.domain.KGCUser;
import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.service.KGC;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.springframework.util.DigestUtils;

import java.util.Base64;
import java.util.List;

public class KGCImpl implements KGC {
    private KGCDao dao = new KGCDaoImpl();
    private CommonDao commonDao = new CommonDaoImpl();
    private Pairing pairing;
    private Field G1, GT, Zr, K;
    private Element a, b, g, Y, k1, k2,lambda, f;

    @Override
    public KGCUser login(KGCUser user) {
        return dao.login(user.getUsername(),user.getPassword());
    }

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

        dao.setup(pp,msk);
    }

    @Override
    public void getSetup() {
        pairing = PairingFactory.getPairing("a.properties");
        PairingFactory.getInstance().setUsePBCWhenPossible(true);
        if (!pairing.isSymmetric()) {
            throw new RuntimeException("密钥不对称!");
        }
        Zr = pairing.getZr();
        K = pairing.getG2();
        G1 = pairing.getG1();
        GT = pairing.getGT();

        PPAndMSK ppandmsk = commonDao.getSetUp();
        PP pp = ppandmsk.getPp();
        MSK msk = ppandmsk.getMsk();

        if(pp != null){
            f = G1.newElementFromBytes(pp.getF()).getImmutable();
            g = G1.newElementFromBytes(pp.getG()).getImmutable();
            Y = GT.newElementFromBytes(pp.getY()).getImmutable();
        }else{
            System.out.println("pp为空");
        }

        if(msk != null){
            a = Zr.newElementFromBytes(msk.getA()).getImmutable();
            b = Zr.newElementFromBytes(msk.getB()).getImmutable();
            lambda = Zr.newElementFromBytes(msk.getLambda()).getImmutable();
            k1 = K.newElementFromBytes(msk.getK1()).getImmutable();
            k2 = K.newElementFromBytes(msk.getK2()).getImmutable();
        }else{
            System.out.println("msk为空");
        }
    }

    @Override
    public void updateSystem() {
        dao.deleteSystem();
        setup();
    }


    @Override
    public void KeyGen(String id) {
        List<String> list = commonDao.getUserAttr(id);
        String[] attributes = list.toArray(new String[list.size()]);
        Element  D1, D1_1, D2, D2_1, D4, xid, Yid;
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
        D1 = D1.mul(g.powZn(b.mul(t))).getImmutable();
        D1_1 = zeta.getImmutable().getImmutable();
        D2 = g.powZn(t).getImmutable().getImmutable();
        D2_1 = g.powZn(lambda.mulZn(t)).getImmutable();
        D3_Map[] D3 = new D3_Map[attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            String md5 = DigestUtils.md5DigestAsHex(attributes[i].getBytes());
            Element pi = G1.newElementFromHash(md5.getBytes(), 0, md5.length()).getImmutable();
            Element D3_t = pi.powZn((lambda.add(zeta)).mul(t)).getImmutable();
            D3_Map d3_t = new D3_Map();
            d3_t.setAttr(attributes[i]);
            d3_t.setD3(D3_t.toBytes());
            D3[i] = d3_t;
        }
        D4 = Zr.newRandomElement().getImmutable();

        SK sk = new SK();
        sk.setD1(D1.toBytes());
        sk.setD1_1(D1_1.toBytes());
        sk.setD2(D2.toBytes());
        sk.setD2_1(D2_1.toBytes());
        sk.setD3(D3);
        sk.setD4(D4.toBytes());
        sk.setXid(xid.toBytes());
        sk.setZeta(zeta_s);
        sk.setKappa(kappa.toBytes());

        PK pk = new PK();
        pk.setYid(Yid.toBytes());

        PKAndSKAndID pkAndSKAndID = new PKAndSKAndID();
        pkAndSKAndID.setSk(sk);
        pkAndSKAndID.setPk(pk);
        pkAndSKAndID.setTheta_id(encoded);
        dao.setPKAndSK(id,pk, sk,encoded);
//        return pkAndSKAndID;
    }

    public boolean KeySanityCheck(SK sk) {
        Element D1 = G1.newElementFromBytes(sk.getD1()).getImmutable();
        Element D1_1 = Zr.newElementFromBytes(sk.getD1_1()).getImmutable();
        Element D2 = G1.newElementFromBytes(sk.getD2()).getImmutable();
        Element D2_1 = G1.newElementFromBytes(sk.getD2_1()).getImmutable();
        D3_Map[] D3 = sk.getD3();
        Element D4 = Zr.newElementFromBytes(sk.getD4()).getImmutable();
        Element xid = Zr.newElementFromBytes(sk.getXid()).getImmutable();

        Element temp1 = pairing.pairing(g,D2_1).getImmutable();
        Element temp2 = pairing.pairing(g.powZn(lambda),D2).getImmutable();
        boolean b2 = temp1.equals(temp2);

        Element temp3 = pairing.pairing(g.powZn(lambda).mul(g.powZn(D1_1)),D1).getImmutable();
        Element temp4 = Y.mul(pairing.pairing(D2.powZn(D1_1).mul(D2_1),g.powZn(b))).getImmutable();
        boolean b3 = temp3.equals(temp4);


        Element D3_sum = G1.newElementFromBytes(D3[0].getD3()).getImmutable().getImmutable();
        String md5 = DigestUtils.md5DigestAsHex(D3[0].getAttr().getBytes());
        Element pi_sum = G1.newElementFromHash(md5.getBytes(), 0, md5.length()).getImmutable();
        for (int i = 1; i < D3.length; i++) {
            String pi_t1 = DigestUtils.md5DigestAsHex(D3[i].getAttr().getBytes());
            Element pi_t2 = G1.newElementFromHash(pi_t1.getBytes(), 0, pi_t1.length()).getImmutable();
            pi_sum = pi_sum.mul(pi_t2);
            D3_sum = D3_sum.mul(G1.newElementFromBytes(D3[i].getD3()));
        }
        Element temp5 = pairing.pairing(pi_sum,(D2.powZn(D1_1)).mul(D2_1)).getImmutable();
        Element temp6 = pairing.pairing(g,D3_sum).getImmutable();
        boolean b4 = temp5.equals(temp6);
        if(b2 && b3 && b4){
            return true;
        }
        return false;
    }


    public TranceID Trance(SK sk) {

        if(KeySanityCheck(sk)){
            String zeta = sk.getZeta();
            Element kappa = Zr.newElementFromBytes(sk.getKappa());
            byte[] zeta_t2 = Base64.getDecoder().decode(zeta);
            byte[] theta_id_kappa = Crytpto.SDec(zeta_t2,k2.toBytes());
            String theta_id_kappa_s = new String(theta_id_kappa);
            String theta_t = theta_id_kappa_s.replace(kappa.toString(),"");
            byte[] decoded = Base64.getDecoder().decode(theta_t);
            byte[] id_t = Crytpto.SDec(decoded,k1.toBytes());
            String id_revo = new String(id_t);
            TranceID trance = new TranceID();
            trance.setId(id_revo);
            trance.setTheta_id(theta_t);
            return trance;
        }else{
            System.out.println("密钥结构不完整");
        }

        return null;
    }

    @Override
    public void userRevo(byte[] theta_id) {

    }


}
