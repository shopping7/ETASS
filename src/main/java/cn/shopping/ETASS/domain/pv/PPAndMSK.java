package cn.shopping.ETASS.domain.pv;


import lombok.Data;

import java.io.Serializable;


@Data
public class PPAndMSK implements Serializable {
    private static final long serialVersionUID = 1L;

    MSK msk;
    PP pp;
}
