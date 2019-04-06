package mmall.dto;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mmall.model.ACLModule;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by Raymond on 2019/1/12.
 */
@Getter
@Setter
@ToString
public class ACLModuleDTO extends ACLModule {

    private List<ACLModuleDTO> aclModuleList = Lists.newArrayList();

    private List<ACLDTO> aclList = Lists.newArrayList();

    public static ACLModuleDTO adapt(ACLModule module){
        ACLModuleDTO dto = new ACLModuleDTO();
        BeanUtils.copyProperties(module,dto);
        return dto;
    }
}
