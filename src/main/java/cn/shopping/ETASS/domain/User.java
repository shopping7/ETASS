package cn.shopping.ETASS.domain;

import lombok.Data;

import javax.xml.bind.Element;

/**
 * 用户的实体类
 */

@Data
public class User {
    private static final long serialVersionUID = 1L;

    private int id;

    private String username;

    private String password;

    private String[] attribute;

    private Element pk;

    private Element sk;

    private Element theta;


}
