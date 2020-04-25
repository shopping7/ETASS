package cn.shopping.ETASS.dao;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class People implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private byte[] name;
    private int age;
    private Date birthday;


}
