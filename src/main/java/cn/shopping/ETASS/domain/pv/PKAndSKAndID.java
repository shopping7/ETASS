package cn.shopping.ETASS.domain.pv;

import lombok.Data;

import java.io.Serializable;

@Data
public class PKAndSKAndID implements Serializable {

    private static final long serialVersionUID = 1L;

    private PK pk;

    private SK sk;

    private String theta_id;
}
