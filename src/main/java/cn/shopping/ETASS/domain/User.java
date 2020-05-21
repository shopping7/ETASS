package cn.shopping.ETASS.domain;

import cn.shopping.ETASS.domain.pv.Attr;
import lombok.Data;

import javax.xml.bind.Element;
import java.util.List;

/**
 * 用户的实体类
 */

@Data
public class User {
    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private String user_id;

    private List<String> attribute;

    private String attrs;

    private Element pk;

    private Element sk;

    private Element theta;


}
