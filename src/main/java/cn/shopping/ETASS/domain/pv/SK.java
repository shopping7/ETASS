package cn.shopping.ETASS.domain.pv;

import it.unisa.dia.gas.jpbc.Element;
import lombok.Data;

import java.io.Serializable;

@Data
public class SK implements Serializable {

    private static final long serialVersionUID = 1L;

    private byte[] D1;

    private byte[] D1_1;

    private byte[] D2;

    private byte[] D2_1;

//    private byte[][] D3;
    private D3_Map[] D3;

    private byte[] D4;

    private byte[] xid;

    private String zeta;

    private byte[] kappa;

}
