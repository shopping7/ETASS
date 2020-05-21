package cn.shopping.ETASS.domain.pv;

import cn.shopping.ETASS.domain.User;
import lombok.Data;
import java.util.List;

@Data
public class UserAlter {

    private User user;

    private List<Attr> attrs;
}
