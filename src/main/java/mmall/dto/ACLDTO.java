package mmall.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mmall.model.ACL;
import org.springframework.beans.BeanUtils;

/**
 * Created by Raymond on 2019/1/13.
 */
@Getter
@Setter
@ToString
public class ACLDTO extends ACL {

    private boolean checked = false;

    private boolean hasAcl = false;

    public static ACLDTO adapt(ACL acl){
        ACLDTO acldto = new ACLDTO();
        BeanUtils.copyProperties(acl,acldto);
        return acldto;
    }
}
