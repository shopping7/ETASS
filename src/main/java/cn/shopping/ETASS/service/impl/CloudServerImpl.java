package cn.shopping.ETASS.service.impl;

import cn.shopping.ETASS.dao.CloudServerDao;
import cn.shopping.ETASS.dao.impl.CloudServerDaoImpl;
import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.domain.lsss.Vector;
import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.service.CloudServer;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;


import java.util.List;

public class CloudServerImpl implements CloudServer {

    private CloudServerDao dao = new CloudServerDaoImpl();

    private Pairing pairing;
    public static Field G1, GT, Zr, K;
    private Element L,V;


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

//        PPAndMSK ppandmsk = dao.getPpAndMsk();
//        PP pp = ppandmsk.getPp();
//        MSK msk = ppandmsk.getMsk();
//
//        if(pp != null){
//            f = G1.newElementFromBytes(pp.getF()).getImmutable();
//            g = G1.newElementFromBytes(pp.getG()).getImmutable();
//            Y = GT.newElementFromBytes(pp.getY()).getImmutable();
//        }else{
//            System.out.println("pp为空");
//        }
//
//        if(msk != null){
//            a = Zr.newElementFromBytes(msk.getA()).getImmutable();
//            b = Zr.newElementFromBytes(msk.getB()).getImmutable();
//            lambda = Zr.newElementFromBytes(msk.getLambda()).getImmutable();
//            k1 = K.newElementFromBytes(msk.getK1()).getImmutable();
//            k2 = K.newElementFromBytes(msk.getK2()).getImmutable();
//        }else{
//            System.out.println("msk为空");
//        }

    }
    public List<Encrypt_File> getFile(String[] KW1){
        List<Encrypt_File> file_list = dao.getFile(KW1);
        return file_list;
    }

    public String[] getAttr(String id){
        List<String> list = dao.getAttr(id);
        String[] attr = (String[]) list.toArray(new String[list.size()]);
        return attr;
    }

    public boolean Test(CT ct, TKW tkw, LSSSMatrix lsssD1, Element Did,int lsssIndex[]) {
        if(Did == null){
            return false;
        }

        //计算秘密值sss
        Vector w = lsssD1.getRv();
        if(w == null){
            return false;
        }

        Element T1 = tkw.getT1().getImmutable();
        Element T1_1 = tkw.getT1_1().getImmutable();
        Element T2 = tkw.getT2().getImmutable();
        Element T2_1 = tkw.getT2_1().getImmutable();
        T3_Map[] T3_t = tkw.getT3();
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
        int l1 = Cj.length;

        Element E = GT.newElementFromBytes(ct.getE()).getImmutable();

        L = pairing.pairing(T1,(C0.powZn(T1_1)).mul(C0_1)).getImmutable();

        String[] user_file_attr = lsssD1.getMap();
        Element temp3 = G1.newOneElement();
        int k = 0;
        for (int i = 0; i < T3_t.length; i++) {
            for (int j = 0; j < user_file_attr.length; j++) {
                if(T3_t[i].getAttr().equals(user_file_attr[j])){
                    temp3 = temp3.mul(T3_t[i].getT3().powZn(Zr.newElement(w.getValue(k))).getImmutable());
                    k++;
                }
            }

        }
        //注意T3的取值
        Element temp1 =(T2.powZn(T1_1)).mul(T2_1).getImmutable();
        Element temp2 = Ci[lsssIndex[0]].powZn(Zr.newElement(w.getValue(0))).getImmutable();
//        Element temp3 = T3[0].powZn(Zr.newElement(w.getValue(0))).getImmutable();
//        Element temp3_t = T3_t[0].getT3().powZn(Zr.newElement(w.getValue(0))).getImmutable();
        Element temp4 = Cj[0].mul(T6[0]).getImmutable().getImmutable();


        for (int i = 1; i < lsssD1.getMartix().getRows(); i++) {
            Element t = Zr.newElement(w.getValue(i)).getImmutable();
            temp2 = temp2.mul(Ci[lsssIndex[i]].powZn(t));
//            temp3 = temp3.mul(T3[i].powZn(t));

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




    public CTout Transform(CT ct, TKW tkw, Element Did, LSSSMatrix lsssD1,int lsssIndex[]) {

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
            System.out.println("您的权限不够，无法获得此文件");
            return null;
        }

    }

    @Override
    public void userRevo(String id) {
        dao.userRevo(id);
    }
}
