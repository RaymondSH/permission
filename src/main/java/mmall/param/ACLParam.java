package mmall.param;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by Raymond on 2019/1/12.
 */
@Getter
@Setter
@ToString
public class ACLParam {

    private Integer id;

    @NotBlank(message = "权限名称不可以为空")
    @Length(min = 1,max = 20,message = "名称长度在2到20个字之间")
    private String name;

    @NotNull(message = "必须指定权限模块")
    private Integer aclModuleId;

    @Length(min = 6,max = 100,message = "权限点URL长度需要在6到150个字符之间")
    private String url;

    @NotNull(message = "必须指定权限点的类型")
    @Max(value = 2,message = "权限点类型不合法")
    @Min(value = 1,message = "权限点类型不合法")
    private Integer type;

    @NotNull(message = "必须指定权限点的状态")
    @Max(value = 1,message = "权限点状态不合法")
    @Min(value = 0,message = "权限点状态不合法")
    private Integer status;

    @NotNull(message = "必须指定权限点的展示顺序")
    private Integer seq;

    @Length(min = 0,max = 200,message = "权限点备注长度在200个字符以内")
    private String remark;
}
