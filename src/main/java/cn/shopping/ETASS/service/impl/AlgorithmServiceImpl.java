package cn.shopping.ETASS.service.impl;


import cn.shopping.ETASS.dao.AlgorithmDao;
import cn.shopping.ETASS.dao.CommonDao;
import cn.shopping.ETASS.dao.impl.AlgorithmDaoImpl;
import cn.shopping.ETASS.dao.impl.CommonDaoImpl;
import cn.shopping.ETASS.domain.User;
import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.domain.lsss.LSSSShares;
import cn.shopping.ETASS.domain.lsss.Vector;
import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.service.AlgorithmService;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.math.BigInteger;
import java.util.Base64;


public class AlgorithmServiceImpl implements AlgorithmService {

    private AlgorithmDao dao = new AlgorithmDaoImpl();
    private CommonDao commonDao = new CommonDaoImpl();

    private int l1;
    private Pairing pairing;
    public static Field G1, GT, Zr, K;
    private Element a, b, g, Y, k1, k2,lambda, f;
    private Element u;
    private Element L,V;
//    private Element s,, pi[];
//    private Element pi[];


    public AlgorithmServiceImpl() {
    }


    public void setup(){
        //kgc分配pp和msk
//        Element a_1, b_1, g_1, Y_1, k1_1, k2_1,lambda_1, f_1;
////        pairing = PairingFactory.getPairing("a.properties");
////        PairingFactory.getInstance().setUsePBCWhenPossible(true);
////        if (!pairing.isSymmetric()) {
////            throw new RuntimeException("密钥不对称!");
////        }
////        Zr = pairing.getZr();
////        K = pairing.getG2();
////        G1 = pairing.getG1();
////        GT = pairing.getGT();
////
////        a_1 = Zr.newRandomElement().getImmutable();
////        b_1 = Zr.newRandomElement().getImmutable();
////        lambda_1 = Zr.newRandomElement().getImmutable();
////
////        f_1 = G1.newRandomElement().getImmutable();
////        k1_1 = K.newRandomElement().getImmutable();
////        k2_1 = K.newRandomElement().getImmutable();
////
////        g_1 = G1.newRandomElement().getImmutable();
////
////        Y_1 = pairing.pairing(g_1, g_1).powZn(a_1).getImmutable();
////
////        MSK msk_1 = new MSK();
////        msk_1.setA(a_1.toBytes());
////        msk_1.setB(b_1.toBytes());
////        msk_1.setLambda(lambda_1.toBytes());
////        msk_1.setK1(k1_1.toBytes());
////        msk_1.setK2(k2_1.toBytes());
////
////        PP pp_1 = new PP();
////        pp_1.setF(f_1.toBytes());
////        pp_1.setG(g_1.toBytes());
////        pp_1.setGb((g_1.powZn(b_1)).toBytes());
////        pp_1.setGlambda((g_1.powZn(lambda_1)).toBytes());
////        pp_1.setY(Y_1.toBytes());
////
////        PPAndMSK ppandmsk_1 = new PPAndMSK();
////        ppandmsk_1.setMsk(msk_1);
////        ppandmsk_1.setPp(pp_1);
////
////        dao.setup(ppandmsk_1);



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




    public void CreateUL(String id, PK pk) {
        Element Did;
        Element Yid = GT.newElementFromBytes(pk.getYid());
        Element s = Zr.newRandomElement().getImmutable();
        dao.addS(id,s);
        Did = Yid.powZn(s);
        byte[] theta_id = Crytpto.SEnc(id.getBytes(), k1.toBytes());
        String encoded = Base64.getEncoder().encodeToString(theta_id);
        try {
            dao.addUL(encoded,Did);
        } catch (Exception e) {
            System.out.println("UL添加失败");
        }
    }


    public Element getDid(String theta){
        byte[] did = dao.getDid(theta);
        Element Did = GT.newElementFromBytes(did).getImmutable();
        if(Did != null){
            return Did;
        }else{
            System.out.println("Did为空");
            return null;
        }

    }


    public void Enc(String user_id, File file, String[] KW, LSSSMatrix lsss) {

        l1 = KW.length;
        String attributes[] = lsss.getMap();
        Element s = Zr.newElementFromBytes(dao.getS(user_id)).getImmutable();
        Element[] Yn2 = new Element[lsss.getMartix().getCols()];//  get zr secret share matrix
        Yn2[0] = s;
        for(int i = 1 ; i < Yn2.length; i++)
        {
            Yn2[i] = Zr.newRandomElement().getImmutable();
        }
        Vector secret2 = new Vector(false, Yn2);
        LSSSShares shares2 = lsss.genShareVector2(secret2);
//        （2）加密密文
        Element gamma = GT.newRandomElement().getImmutable();
        String kse_t = DigestUtils.md5DigestAsHex(gamma.toBytes());
        Element kse = K.newElementFromBytes(kse_t.getBytes()).getImmutable();
//        byte[] CM = Crytpto.SEnc(msg.getBytes(), kse.toBytes());
        byte[] CM = new byte[0];
        try {
            CM = crytptpFile.encrypt(file, kse.toBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //（3）计算检验密钥
        String vkm_t = gamma.toString() + Base64.getEncoder().encodeToString(CM);
        String VKM_t = DigestUtils.md5DigestAsHex(vkm_t.getBytes());
        Element VKM = G1.newElementFromHash(VKM_t.getBytes(),0,VKM_t.length()).getImmutable();

        //（4）多项式
        Element[] H_kw = new Element[l1];
        Element[][] hash_kw = new Element[l1][l1];
        Element[] hash_b = new Element[l1];



        for (int i = 0; i < l1; i++) {
            String word = DigestUtils.md5DigestAsHex(KW[i].getBytes());
            H_kw[i] = Zr.newElementFromHash(word.getBytes(), 0, word.length()).getImmutable();
            for (int j = 0; j < l1; j++) {
                hash_kw[i][j] = H_kw[i].pow(BigInteger.valueOf(l1-j-1)).getImmutable();
            }
            hash_b[i] = Zr.newOneElement().getImmutable();
        }

        PolynomialSoluter ps = new PolynomialSoluter();
        Element nj[] = ps.getResult(hash_kw, hash_b);


        Element sigma1 = Zr.newRandomElement().getImmutable();
        Element C = gamma.mul(pairing.pairing(g,g).powZn(a.mulZn(s))).getImmutable();
        Element C0 = g.powZn(s).getImmutable();
        Element C0_1 = g.powZn(lambda.mulZn(s)).getImmutable();
        Element C0_11 = g.powZn(sigma1.square()).getImmutable();


        //pi后续再处理一下
        Element[] pi = new Element[attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            String md5 = DigestUtils.md5DigestAsHex(attributes[i].getBytes());
            pi[i] = G1.newElementFromHash(md5.getBytes(), 0, md5.length()).getImmutable();
        }

        Element[] Ci = new Element[lsss.getMartix().getRows()];
        Element[]  lambda_i = new Element[Ci.length];
        byte[][] Ci_bytes = new byte[Ci.length][];
        for (int i = 0; i < Ci.length; i++) {
            lambda_i[i] = shares2.getVector().getValue2(i);
            Ci[i] = (g.powZn(lambda_i[i].mulZn(b))).mul(pi[i].powZn(sigma1.negate())).getImmutable();
            Ci_bytes[i] = Ci[i].toBytes();
        }
        byte[][] Cj_bytes = new byte[l1][];
        Element[] Cj = new Element[l1];
        for (int i = 0; i < l1; i++) {
            Cj[i] = (sigma1.invert()).mul(nj[l1-i-1]).getImmutable();
            Cj_bytes[i] = Cj[i].toBytes();
        }

        Element E = pairing.pairing(g, f).powZn(sigma1).getImmutable();
        E = E.mul(pairing.pairing(g, g).powZn(a.mulZn(s).mulZn(sigma1)));

        CT ct = new CT();
        ct.setC(C.toBytes());
        ct.setC0(C0.toBytes());
        ct.setC0_1(C0_1.toBytes());
        ct.setC0_11(C0_11.toBytes());
        ct.setE(E.toBytes());
        ct.setCM(CM);
        ct.setCi(Ci_bytes);
        ct.setCj(Cj_bytes);

        VKM vkm = new VKM();
        vkm.setVKM(VKM.toBytes());

//        dao.upload(ctandvkm);
        dao.uploadFile(ct,vkm,KW,lsss);
    }


    public TKW Trapdoor(SK sk, String[] kw_1) {
        u = Zr.newRandomElement().getImmutable();
        Element sigma2 = Zr.newRandomElement().getImmutable();


        Element D1 = G1.newElementFromBytes(sk.getD1()).getImmutable();
        Element D1_1 = Zr.newElementFromBytes(sk.getD1_1()).getImmutable();
        Element D2 = G1.newElementFromBytes(sk.getD2()).getImmutable();
        Element D2_1 = G1.newElementFromBytes(sk.getD2_1()).getImmutable();
        D3_Map[] D3 = sk.getD3();
        Element D4 = Zr.newElementFromBytes(sk.getD4()).getImmutable();
        Element xid = Zr.newElementFromBytes(sk.getXid()).getImmutable();
        //  计算TKW
        Element T1 = D1.powZn(u.mulZn(D4)).getImmutable();
        Element T1_1 = D1_1.getImmutable().getImmutable();
        Element T2 = D2.powZn(u.mulZn(D4)).getImmutable();
        Element T2_1 = D2_1.powZn(u.mulZn(D4)).getImmutable();
        int l2 = kw_1.length;
        Element l2_E = Zr.newElement(l2).getImmutable();
        T3_Map[] T3 = new T3_Map[D3.length];
        for (int i = 0; i < D3.length; i++) {
            Element D3_t = G1.newElementFromBytes(D3[i].getD3()).getImmutable();
            Element T3_t1 = D3_t.powZn((u.mulZn(D4).mulZn(sigma2)).mul(l2_E.invert())).getImmutable();
            T3_Map T3_t = new T3_Map();
            T3_t.setAttr(D3[i].getAttr());
            T3_t.setT3(T3_t1);
            T3[i] = T3_t;
        }

        Element T4 = (u.mulZn(D4).sub(xid)).mul(sigma2).mul(l2_E.invert()).getImmutable();
        Element T5 = (pairing.pairing(g, f)).powZn(u.mulZn(D4).sub(xid)).getImmutable();

        String kw_u;
        Element[] kw_us = new Element[l2];
        for (int i = 0; i < l2; i++) {
            kw_u= DigestUtils.md5DigestAsHex(kw_1[i].getBytes());
            kw_us[i] = Zr.newElementFromHash(kw_u.getBytes(),0,kw_u.length()).getImmutable();
        }
        //这里不明白怎么获得l1
        int l1 = l2;
        Element[] T6 = new Element[l1];
        for (int i = 0; i < l1; i++) {
            Element sum = Zr.newZeroElement().getImmutable();
            for (int j = 0; j < l2; j++) {
                sum = sum.add(kw_us[i].pow(BigInteger.valueOf(i))).getImmutable();
            }
          T6[i] = (sigma2.invert()).mul(sum).getImmutable();
        }


        TKW tkw = new TKW();
        tkw.setT1(T1);
        tkw.setT1_1(T1_1);
        tkw.setT2(T2);
        tkw.setT2_1(T2_1);
        tkw.setT3(T3);
        tkw.setT4(T4);
        tkw.setT5(T5);
        tkw.setT6(T6);

        return tkw;
    }




    public void Dec(CTout ctout, SK sk, VKM vkm,String filename) {
        if(ctout == null&& u == null){
            return ;
        }
        Element VKM = G1.newElementFromBytes(vkm.getVKM()).getImmutable();
        Element C = ctout.getC().getImmutable();
        Element L = ctout.getL().getImmutable();
        Element V = ctout.getV().getImmutable();
        byte[] CM = ctout.getCM();

        Element D4 = Zr.newElementFromBytes(sk.getD4()).getImmutable();

        Element gamma_verify = C.sub((L.sub(V)).powZn((u.mulZn(D4)).invert())).getImmutable();
        String vkm_t = gamma_verify.toString() + Base64.getEncoder().encodeToString(CM);
        String VKM_T = DigestUtils.md5DigestAsHex(vkm_t.getBytes());
        Element VKM_verify = G1.newElementFromHash(VKM_T.getBytes(),0,VKM_T.length()).getImmutable();
        if(VKM_verify.equals(VKM)){
            String kse_v = DigestUtils.md5DigestAsHex(gamma_verify.toBytes());
            Element kse_verify = K.newElementFromBytes(kse_v.getBytes()).getImmutable();
//            byte[] m = Crytpto.SDec(CM,kse_verify.toBytes());

            try {
                crytptpFile.decrypt(CM, filename,kse_verify.toBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("解密成功");

        }else{
            System.out.println("校验不合格");
        }


    }


//    public boolean KeySanityCheck(SK sk) {
//        Element D1 = sk.getD1().getImmutable();
//        Element D1_1 = sk.getD1_1().getImmutable();
//        Element D2 = sk.getD2().getImmutable();
//        Element D2_1 = sk.getD2_1().getImmutable();
//        Element[] D3 = sk.getD3();
//        Element D4 = sk.getD4().getImmutable();
//        Element xid = sk.getXid().getImmutable();
//
//        Element temp1 = pairing.pairing(g,D2_1).getImmutable();
//        Element temp2 = pairing.pairing(g.powZn(lambda),D2).getImmutable();
//        boolean b1 = temp1.equals(temp2);
//
//        Element temp3 = pairing.pairing(g.powZn(lambda).mul(g.powZn(D1_1)),D1).getImmutable();
//        Element temp4 = Y.mul(pairing.pairing(D2.powZn(D1_1).mul(D2_1),g.powZn(b))).getImmutable();
//        boolean b2 = temp3.equals(temp4);
//        Element pi_sum = pi[0].getImmutable();
//        Element D3_sum = D3[0].getImmutable();
//        for (int i = 1; i < D3.length; i++) {
//            pi_sum = pi_sum.mul(pi[i]);
//            D3_sum = D3_sum.mul(D3[i]);
//        }
//        Element temp5 = pairing.pairing(pi_sum,(D2.powZn(D1_1)).mul(D2_1)).getImmutable();
//        Element temp6 = pairing.pairing(g,D3_sum).getImmutable();
//        boolean b3 = temp5.equals(temp6);
//        if(b1 && b2 && b3){
//            return true;
//        }
//        return false;
//    }


//    public String Trance(SK sk) {
//
//        if(KeySanityCheck(sk)){
//            String zeta = sk.getZeta();
//            byte[] zeta_t2 = Base64.getDecoder().decode(zeta);
//            byte[] theta_id_kappa = Crytpto.SDec(zeta_t2,k2.toBytes());
//            String theta_id_kappa_s = new String(theta_id_kappa);
//            String theta_t = theta_id_kappa_s.replace(kappa.toString(),"");
//            byte[] decoded = Base64.getDecoder().decode(theta_t);
//            byte[] id_t = Crytpto.SDec(decoded,k1.toBytes());
//            String id_revo = new String(id_t);
//            return id_revo;
//        }
//
//        return null;
//    }



}













