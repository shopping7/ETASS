package cn.shopping.ETASS.domain.pv;

import it.unisa.dia.gas.jpbc.Element;
import lombok.Data;

import java.io.Serializable;

@Data
public class CT implements Serializable {
    private static final long serialVersionUID = 1L;

    private byte[] C;

    private byte[] C0;

    private byte[] C0_1;

    private byte[] C0_11;

    private byte[][] Ci;

    private byte[][] Cj;

    private byte[] E;

    private byte[] CM;
}

