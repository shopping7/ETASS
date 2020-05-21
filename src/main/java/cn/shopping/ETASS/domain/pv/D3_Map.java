package cn.shopping.ETASS.domain.pv;

import it.unisa.dia.gas.jpbc.Element;
import lombok.Data;

import java.io.Serializable;

@Data
public class D3_Map implements Serializable {
    private byte[] D3;
    private String attr;
}
