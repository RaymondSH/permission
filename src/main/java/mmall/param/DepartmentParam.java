package mmall.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by Raymond on 2019/1/10.
 */
@Setter
@Getter
@ToString
public class DepartmentParam {

    private Integer id;

    @NotBlank(message = "部门名称不能为空")
    @Length(max = 15,min = 3,message = "部门名称的长度需要在3-15个字之间")
    private String name;

    private Integer parentId = 0;

    @NotNull(message = "展示顺序不可以为空")
    private Integer seq;

    @Length(message = "备注的字数限制在150字之间")
    private String remark;

}
