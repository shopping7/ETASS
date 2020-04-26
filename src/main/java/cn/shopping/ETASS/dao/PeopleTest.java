package cn.shopping.ETASS.dao;

import cn.shopping.ETASS.util.JDBCUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

public class PeopleTest {
    public static void main(String[] args) throws Exception {
        People p = new People();
        p.setName("99".getBytes());
        p.setAge(10);
        p.setBirthday(Calendar.getInstance().getTime());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(p);

        Connection connection = JDBCUtils.getConnection();
        PreparedStatement ps = connection.prepareStatement("insert into people (people) value(?)");

        ps.setBytes(1, bos.toByteArray());
        ps.executeUpdate();

        ps = connection.prepareStatement("select id,people from people ");
        ResultSet resultSet = ps.executeQuery();


        while(resultSet.next()){
            System.out.println(resultSet.getInt(1));
            ByteArrayInputStream bis = new ByteArrayInputStream(resultSet.getBytes(2));
            ObjectInputStream ois = new ObjectInputStream(bis);
            People pn = (People)ois.readObject();
            byte[] bytes = pn.getName();
            String s = new String(bytes);
            System.out.println(s);
        }
//关闭连接
        JDBCUtils.close(resultSet,ps, connection);

    }
}
