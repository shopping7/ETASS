package cn.shopping.ETASS.dao.impl;

import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.service.impl.AlgorithmServiceImpl;
import it.unisa.dia.gas.jpbc.Element;

public class AlgoUploadFileTest {
    public static void main(String[] args) {
        AlgorithmServiceImpl algorithmService = new AlgorithmServiceImpl();
        double[][] lsss = new double[][]{
                {1, 1, 0, 0}, // H1
                {0,-1, 1, 0}, // P1
                {0, 0,-1, 0}, // D1
                {0, 0,-1, 0}, // D2
                {1, 1, 0, 0}, // H2
                {0,-1, 1, 1}, // P2
                {0, 0, 0,-1}, // D3
                {0, 0,-1, 1}, // P3
                {0, 0, 0,-1}  // D4
        };
        algorithmService.setup();
        String[] KW = {"123","456","789"};
        algorithmService.Enc("crypto", KW, lsss);



    }
}
