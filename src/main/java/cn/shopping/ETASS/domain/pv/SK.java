package cn.shopping.ETASS.domain.pv;

import it.unisa.dia.gas.jpbc.Element;
import lombok.Data;

import java.io.Serializable;

@Data
public class SK implements Serializable {

    private static final long serialVersionUID = 1L;

    private Element D1;

    private Element D1_1;

    private Element D2;

    private Element D2_1;

    private Element D3[];

    private Element D4;

    private Element xid;

    private String zeta;

}
