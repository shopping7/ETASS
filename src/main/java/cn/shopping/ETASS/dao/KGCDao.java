package cn.shopping.ETASS.dao;

import cn.shopping.ETASS.domain.pv.PKAndSKAndID;
import cn.shopping.ETASS.domain.pv.PPAndMSK;

public interface KGCDao {

    void setup(PPAndMSK ppandmsk);

    void setPKAndSK(String id, PKAndSKAndID pkandsk);
}
