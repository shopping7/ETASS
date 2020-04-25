package cn.shopping.ETASS.domain.pv;

import it.unisa.dia.gas.jpbc.Element;
import lombok.Data;

import java.io.Serializable;

@Data
public class PP implements Serializable {

    private static final long serialVersionUID = 1L;

    private byte[] f;

    private byte[] g;

    private byte[] gb;

    private byte[] glambda;

    private byte[] Y;
}
