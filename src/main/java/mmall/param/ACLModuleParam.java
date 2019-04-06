package mmall.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Raymond on 2019/1/12.
 */
@Getter
@Setter
@ToString
public class ACLModuleParam {

    private Integer id;

    @NotBlank(message = "权限模块名称不能为空")
    @Length(min = 2,max = 20,message = "名字在2到20个字之间")
    private String name;

    private Integer parentId = 0;


    @NotNull(message = "展示顺序不能为空")
    private Integer seq;

    @NotNull(message = "模块状态不能为空")
    @Min(value = 0,message = "状态值不合法")
    @Max(value = 2,message = "状态值不合法")
    private Integer status;

    @Length(max = 200,message = "备注长度在64个字以内")
    private String remark;
}
