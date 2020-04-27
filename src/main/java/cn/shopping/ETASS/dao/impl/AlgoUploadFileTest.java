package cn.shopping.ETASS.dao.impl;

import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.service.impl.AlgorithmServiceImpl;
import it.unisa.dia.gas.jpbc.Element;

public class AlgoUploadFileTest {
    public static void main(String[] args) {
        AlgorithmServiceImpl algorithmService = new AlgorithmServiceImpl();
        String[] attributes = new String[]{
                "H1",
                "P1",
                "D1",
                "D2",
                "H2",
                "P2",
                "D3",
                "P3",
                "D4"
        };
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
        String[] kw = {"oncology department","Raffles hospital","doctor"};
        algorithmService.setup();
        algorithmService.Enc("123","crypto", kw, lsss,attributes);



    }
}
