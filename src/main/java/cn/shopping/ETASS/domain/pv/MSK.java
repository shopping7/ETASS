package cn.shopping.ETASS.domain.pv;

import it.unisa.dia.gas.jpbc.Element;
import lombok.Data;

import java.io.Serializable;

@Data
public class MSK implements Serializable {
    private static final long serialVersionUID = 1L;

    private byte[] a;

    private byte[] b;

    private byte[] lambda;

    private byte[] k1;

    private byte[] k2;
}
