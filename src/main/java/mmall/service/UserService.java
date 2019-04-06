package mmall.service;

import com.google.common.base.Preconditions;
import mmall.beans.PageQuery;
import mmall.beans.PageResult;
import mmall.common.RequestHolder;
import mmall.dao.UserMapper;
import mmall.exception.ParamException;
import mmall.model.User;
import mmall.param.UserParam;
import mmall.util.BeanValidator;
import mmall.util.IPUtil;
import mmall.util.MD5Util;
import mmall.util.PasswordUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by Raymond on 2019/1/11.
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public void save(UserParam param){
        BeanValidator.check(param);
        if (checkEmailExists(param.getMail(),param.getId())){
            throw new ParamException("此邮箱已经存在，请换个邮箱注册");
        }
        if (checkTelephoneExists(param.getTelephone(),param.getId())){
            throw new ParamException("电话已经存在，请换个电话注册");
        }

        String password = PasswordUtil.randomPassword();
        password = "123456789";
        //todo
        String encryptedPassword = MD5Util.encrypt(password);
        User user = User.builder().username(param.getUsername()).mail(param.getMail())
                .password(encryptedPassword).telephone(param.getTelephone()).deptId(param.getDeptId())
                .status(param.getStatus()).remark(param.getRemark()).build();
        user.setOperator(RequestHolder.getUser().getUsername());
        user.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
        user.setOperateTime(new Date());

        //todo sendEmail
        userMapper.insertSelective(user);
    }


    public void update(UserParam param){
        BeanValidator.check(param);
        if (checkEmailExists(param.getMail(),param.getId())){
            throw new ParamException("此邮箱已经存在，请换个邮箱");
        }
        if (checkTelephoneExists(param.getTelephone(),param.getId())){
            throw new ParamException("电话已经存在，请换个电话");
        }
        User before = userMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的用户不存在");
        User after = User.builder().id(param.getId()).username(param.getUsername()).mail(param.getMail())
                .telephone(param.getTelephone()).deptId(param.getDeptId())
                .status(param.getStatus()).remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getUser().getUsername());
        after.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
        after.setOperateTime(new Date());
        userMapper.updateByPrimaryKeySelective(after);
    }

    private boolean checkTelephoneExists(String phone,Integer userId){
        return userMapper.countByTelephone(phone,userId) > 0;
    }

    private boolean checkEmailExists(String email,Integer userId){
        return userMapper.countByEmail(email,userId) > 0;
    }

    public User findByKeyword(String keyword){
        return userMapper.findByKeyword(keyword);
    }


    public PageResult<User> getPageByDeptId(Integer id, PageQuery pageQuery){
        BeanValidator.check(pageQuery);
        int count = userMapper.countByDeptId(id);
        if (count > 0){
            List<User> userList = userMapper.getUserByDeptId(id,pageQuery);
            return PageResult.<User>builder().total(count).data(userList).build();
        }
        return PageResult.<User>builder().build();
    }

    public List<User> getAllUser(){
        return userMapper.getAll();
    }
}
