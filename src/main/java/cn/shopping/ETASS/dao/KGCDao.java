package cn.shopping.ETASS.dao;

import cn.shopping.ETASS.domain.KGCUser;
import cn.shopping.ETASS.domain.User;
import cn.shopping.ETASS.domain.pv.*;

import java.util.List;

public interface KGCDao {

    KGCUser login(String username, String password);

    void setup(PP pp, MSK msk);


    void setPKAndSK(String id, PK pk, SK sk, String theta_id);
}
