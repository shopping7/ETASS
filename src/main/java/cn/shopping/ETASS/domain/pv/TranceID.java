package cn.shopping.ETASS.domain.pv;

import lombok.Data;

import java.io.Serializable;

@Data
public class TranceID implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String theta_id;
}
