package cn.shopping.ETASS.domain.pv;


import lombok.Data;

import java.io.Serializable;


@Data
public class CTAndVKM implements Serializable {
    private static final long serialVersionUID = 1L;

    private CT ct;

    private VKM vkm;
}
