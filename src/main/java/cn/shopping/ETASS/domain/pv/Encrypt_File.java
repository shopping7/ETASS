package cn.shopping.ETASS.domain.pv;


import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import lombok.Data;

import java.io.Serializable;


@Data
public class Encrypt_File implements Serializable {
    private static final long serialVersionUID = 1L;

    private CT ct;

    private VKM vkm;

    private String[] KW;

    private LSSSMatrix lsss;
}
