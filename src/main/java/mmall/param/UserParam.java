package mmall.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * Created by Raymond on 2019/1/11.
 */
@Setter
@Getter
@ToString
public class UserParam {

    private Integer id;

    @NotBlank(message = "用户名不能为空")
    @Length(max = 20,min = 1,message = "")
    private String username;

    @NotBlank(message = "电话不能为空")
    @Length(max = 13,min = 1,message = "电话长度在13个字以内")
    private String telephone;

    @NotBlank(message = "邮箱不能为空")
    @Length(max = 20,min = 5,message = "邮箱长度在5到20个字以内")
    private String mail;

    @NotNull(message = "必须提供用户所在部门")
    private Integer deptId;

    @NotNull(message = "必须指定用户的状态")
    @Min(value = 0,message = "用户状态不合法")
    @Max(value = 2,message = "用户状态不合法")
    private Integer status;

    @Length(min = 0,max = 200,message = "备注长度在200个字以内")
    private String remark = "";
}
