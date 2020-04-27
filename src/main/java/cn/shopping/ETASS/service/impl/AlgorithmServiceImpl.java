package cn.shopping.ETASS.service.impl;


import cn.shopping.ETASS.dao.AlgorithmDao;
import cn.shopping.ETASS.dao.impl.AlgorithmDaoImpl;
import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.service.AlgorithmService;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.springframework.util.DigestUtils;
import java.math.BigInteger;
import java.util.Base64;



public class AlgorithmServiceImpl implements AlgorithmService {

    private AlgorithmDao dao = new AlgorithmDaoImpl();

    private int l1;
    private Pairing pairing;
    private Field G1, GT, Zr, K;
    private Element a, b, g, Y, k1, k2,lambda, f;
    private Element u,kappa;
    private Element L,V;
//    private Element s,, pi[];
//    private Element pi[];


    public AlgorithmServiceImpl() {
    }


    public void setup(){
        //
//        Element a_1, b_1, g_1, Y_1, k1_1, k2_1,lambda_1, f_1;
//        pairing = PairingFactory.getPairing("a.properties");
//        PairingFactory.getInstance().setUsePBCWhenPossible(true);
//        if (!pairing.isSymmetric()) {
//            throw new RuntimeException("密钥不对称!");
//        }
//        Zr = pairing.getZr();
//        K = pairing.getG2();
//        G1 = pairing.getG1();
//        GT = pairing.getGT();
//
//        a_1 = Zr.newRandomElement().getImmutable();
//        b_1 = Zr.newRandomElement().getImmutable();
//        lambda_1 = Zr.newRandomElement().getImmutable();
//
//        f_1 = G1.newRandomElement().getImmutable();
//        k1_1 = K.newRandomElement().getImmutable();
//        k2_1 = K.newRandomElement().getImmutable();
//
//        g_1 = G1.newRandomElement().getImmutable();
//
//        Y_1 = pairing.pairing(g_1, g_1).powZn(a_1).getImmutable();
//
//        MSK msk_1 = new MSK();
//        msk_1.setA(a_1.toBytes());
//        msk_1.setB(b_1.toBytes());
//        msk_1.setLambda(lambda_1.toBytes());
//        msk_1.setK1(k1_1.toBytes());
//        msk_1.setK2(k2_1.toBytes());
//
//        PP pp_1 = new PP();
//        pp_1.setF(f_1.toBytes());
//        pp_1.setG(g_1.toBytes());
//        pp_1.setGb((g_1.powZn(b_1)).toBytes());
//        pp_1.setGlambda((g_1.powZn(lambda_1)).toBytes());
//        pp_1.setY(Y_1.toBytes());
//
//        PPAndMSK ppandmsk_1 = new PPAndMSK();
//        ppandmsk_1.setMsk(msk_1);
//        ppandmsk_1.setPp(pp_1);
//
//        dao.setup(ppandmsk_1);

        pairing = PairingFactory.getPairing("a.properties");
        PairingFactory.getInstance().setUsePBCWhenPossible(true);
        if (!pairing.isSymmetric()) {
            throw new RuntimeException("密钥不对称!");
        }
        Zr = pairing.getZr();
        K = pairing.getG2();
        G1 = pairing.getG1();
        GT = pairing.getGT();

        PPAndMSK ppandmsk = dao.getPpAndMsk();
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


    public void KeyGen(String id, String attributes[]) {
        Element  D1, D1_1, D2, D2_1, D4, xid, Yid;
        Element[] D3;
        Element t = Zr.newRandomElement().getImmutable();
        kappa = Zr.newRandomElement().getImmutable();
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

    @Override
    public PKAndSKAndID getPKAndSKAndID(String id) {
        PKAndSKAndID pkAndSKAndID = dao.getPKAndSKAndID(id);
        if(pkAndSKAndID != null){
            return pkAndSKAndID;
        }else {
            return null;
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


    public void Enc(String user_id,String msg, String[] KW, double[][] lsss,String[] attributes) {

        l1 = KW.length;
        Element y2, y3, y4;
        y2 = Zr.newRandomElement().getImmutable();
        y3 = Zr.newRandomElement().getImmutable();
        y4 = Zr.newRandomElement().getImmutable();
        Element s = Zr.newElementFromBytes(dao.getS(user_id)).getImmutable();
        Element[] lambda_i = new Element[lsss.length];
        for (int i = 0; i < lsss.length; i++) {
            lambda_i[i] = s.mul((int) lsss[i][0]).add(y2.mul((int) lsss[i][1])).add(y3.mul((int) lsss[i][2])).add(y4.mul((int) lsss[i][3])).getImmutable();
        }
//        （2）加密密文
        Element gamma = GT.newRandomElement().getImmutable();
        String kse_t = DigestUtils.md5DigestAsHex(gamma.toBytes());
        Element kse = K.newElementFromBytes(kse_t.getBytes()).getImmutable();
        byte[] CM = Crytpto.SEnc(msg.getBytes(), kse.toBytes());

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


        Element[] Ci = new Element[lsss.length];
        byte[][] Ci_bytes = new byte[lsss.length][];

        //pi后续再处理一下
        Element[] pi = new Element[attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            String md5 = DigestUtils.md5DigestAsHex(attributes[i].getBytes());
            pi[i] = G1.newElementFromHash(md5.getBytes(), 0, md5.length()).getImmutable();
        }
        for (int i = 0; i < lsss.length; i++) {
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

        CTAndVKM ctandvkm = new CTAndVKM();
        ctandvkm.setCt(ct);
        ctandvkm.setVkm(vkm);

        dao.upload(ctandvkm);
    }



    public TKW Trapdoor(SK sk, String[] kw_1) {
        u = Zr.newRandomElement().getImmutable();
        Element sigma2 = Zr.newRandomElement().getImmutable();


        Element D1 = G1.newElementFromBytes(sk.getD1()).getImmutable();
        Element D1_1 = Zr.newElementFromBytes(sk.getD1_1()).getImmutable();
        Element D2 = G1.newElementFromBytes(sk.getD2()).getImmutable();
        Element D2_1 = G1.newElementFromBytes(sk.getD2_1()).getImmutable();

        byte[][] d3 = sk.getD3();
        Element D3[] = new Element[d3.length];
        for (int i = 0; i < D3.length; i++) {
            D3[i] = G1.newElementFromBytes(d3[i]).getImmutable();
        }
        Element D4 = Zr.newElementFromBytes(sk.getD4()).getImmutable();
        Element xid = Zr.newElementFromBytes(sk.getXid()).getImmutable();
        //  计算TKW
        Element T1 = D1.powZn(u.mulZn(D4)).getImmutable();
        Element T1_1 = D1_1.getImmutable().getImmutable();
        Element T2 = D2.powZn(u.mulZn(D4)).getImmutable();
        Element T2_1 = D2_1.powZn(u.mulZn(D4)).getImmutable();
        int l2 = kw_1.length;
        Element l2_E = Zr.newElement(l2).getImmutable();
        Element[] T3 = new Element[D3.length];
        for (int i = 0; i < D3.length; i++) {
            T3[i] = D3[i].powZn((u.mulZn(D4).mulZn(sigma2)).mul(l2_E.invert())).getImmutable();
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
        l1 = l2;
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

    public boolean Test(CT ct, TKW tkw,double[][] lsssD1, Element Did,int lsssIndex[]) {
        if(Did == null){
            return false;
        }

        //计算秘密值sss
        int[] w = new int[lsssD1.length];
        RealMatrix matrix = new Array2DRowRealMatrix(lsssD1);
        RealMatrix vm = null;   // M 的逆阵
        try {
            vm = new LUDecomposition(matrix).getSolver().getInverse();
        } catch (SingularMatrixException e) {
            System.out.println("您输入的矩阵无法取逆");
            System.exit(1);
        }
        // w = (1,0,...,0)*M^{-1}, 即为M的第一行
        for (int i=0; i<lsssD1.length; i++) {
            int i2 = (int)vm.getEntry(0, i);
            w[i] = i2;
        }
        //测试解出来s值是否一样
//        Element sss = (lambda_i[0].mul(w[0])).add(lambda_i[1].mul(w[1])).add(lambda_i[2].mul(w[2])).add(lambda_i[3].mul(w[3]));

        Element T1 = tkw.getT1().getImmutable();
        Element T1_1 = tkw.getT1_1().getImmutable();
        Element T2 = tkw.getT2().getImmutable();
        Element T2_1 = tkw.getT2_1().getImmutable();
        Element[] T3 = tkw.getT3();
        Element T4 = tkw.getT4().getImmutable();
        Element T5 = tkw.getT5().getImmutable();
        Element[] T6 = tkw.getT6();

        Element C0 = G1.newElementFromBytes(ct.getC0()).getImmutable();
        Element C0_1 = G1.newElementFromBytes(ct.getC0_1()).getImmutable();
        Element C0_11 = G1.newElementFromBytes(ct.getC0_11()).getImmutable();
        byte[][] ci = ct.getCi();
        Element Ci[] = new Element[ci.length];
        for (int i = 0; i < Ci.length; i++) {
            Ci[i] = G1.newElementFromBytes(ci[i]).getImmutable();
        }
        byte[][] cj = ct.getCj();
        Element Cj[] = new Element[cj.length];
        for (int i = 0; i < Cj.length; i++) {
            Cj[i] = Zr.newElementFromBytes(cj[i]).getImmutable();
        }

        Element E = GT.newElementFromBytes(ct.getE()).getImmutable();

        L = pairing.pairing(T1,(C0.powZn(T1_1)).mul(C0_1)).getImmutable();

        Element temp1;
        Element temp2 = Ci[lsssIndex[0]].powZn(Zr.newElement(w[0])).getImmutable();
        Element temp3 = T3[lsssIndex[0]].powZn(Zr.newElement(w[0])).getImmutable();
        Element temp4_1 = Cj[0];
        Element temp4_2 = T6[0];
        Element temp4 = Cj[0].mul(T6[0]).getImmutable();
        temp1 = (T2.powZn(T1_1)).mul(T2_1).getImmutable();

        for (int i = 1; i < lsssD1.length; i++) {
            Element t = Zr.newElement(w[i]).getImmutable();
            temp2 = temp2.mul(Ci[lsssIndex[i]].powZn(t));
            temp3 = temp3.mul(T3[lsssIndex[i]].powZn(t));

        }

        for (int i = 1; i < l1; i++) {
            temp4 = temp4.add(Cj[i].mul(T6[i]));
        }


        V = pairing.pairing(temp1, temp2).mul((pairing.pairing(C0_11,temp3)).powZn(temp4)).getImmutable();


        Element temp5 = (E.powZn(T4.mul(temp4))).mul(Did).getImmutable();

        Element temp6 = T5.mul(L.sub(V)).getImmutable();

        if(temp6.equals(temp5)){
            return true;
        }

        //是否相等，返回1或0

        return false;
    }

    public CTAndVKM getCtAndVkm(){
        CTAndVKM ctAndVkm = dao.getCtAndVkm();
        return ctAndVkm;
    }


    public CTout Transform(CT ct, TKW tkw, Element Did,double[][] lsssD1,int lsssIndex[]) {

        if(Test(ct,tkw,lsssD1,Did,lsssIndex)){
            Element C = GT.newElementFromBytes(ct.getC()).getImmutable();
            byte[] CM = ct.getCM();
            CTout ctout = new CTout();
            ctout.setC(C);
            ctout.setL(L);
            ctout.setV(V);
            ctout.setCM(CM);
            return ctout;
        }else{
            System.out.println("Test失败");
            return null;
        }

    }


    public byte[] Dec(CTout ctout, SK sk, VKM vkm) {
        if(ctout == null&& u == null){
            return null;
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
            byte[] m = Crytpto.SDec(CM,kse_verify.toBytes());
            return m;
        }else{
            byte[] fail = "vkmfail".getBytes();
            return fail;
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













