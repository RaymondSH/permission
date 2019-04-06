package mmall.dto;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mmall.model.Department;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by Raymond on 2019/1/10.
 */
@Getter
@Setter
@ToString
public class DepartmentLevelDTO extends Department {
    private List<DepartmentLevelDTO> deptList = Lists.newArrayList();

    public static DepartmentLevelDTO adapt(Department department){
        DepartmentLevelDTO dto = new DepartmentLevelDTO();
        BeanUtils.copyProperties(department,dto);
        return dto;
    }
}
