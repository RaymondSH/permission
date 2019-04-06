package mmall.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import mmall.common.RequestHolder;
import mmall.dao.RoleACLMapper;
import mmall.dao.RoleMapper;
import mmall.dao.RoleUserMapper;
import mmall.dao.UserMapper;
import mmall.exception.ParamException;
import mmall.model.Role;
import mmall.model.User;
import mmall.param.RoleParam;
import mmall.util.BeanValidator;
import mmall.util.IPUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Raymond on 2019/1/13.
 */
@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleUserMapper roleUserMapper;
    @Autowired
    private RoleACLMapper roleACLMapper;
    @Autowired
    private UserMapper userMapper;

    public void save(RoleParam param){
        BeanValidator.check(param);
        if (checkExist(param.getName(),param.getId())){
            throw new ParamException("角色名称已经存在");
        }
        Role role = Role.builder().name(param.getName()).type(param.getType()).status(param.getStatus())
                .remark(param.getRemark()).build();
        role.setOperator(RequestHolder.getUser().getUsername());
        role.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
        role.setOperateTime(new Date());

        roleMapper.insertSelective(role);
    }

    public void update(RoleParam param){
        BeanValidator.check(param);
        if (checkExist(param.getName(),param.getId())){
            throw new ParamException("角色名称已存在");
        }
        Role before = roleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的角色不存在");
        Role after = Role.builder().id(param.getId()).name(param.getName()).type(param.getType())
                .status(param.getStatus()).remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getUser().getUsername());
        after.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
        after.setOperateTime(new Date());
        roleMapper.updateByPrimaryKeySelective(after);
    }

    public List<Role> getAll(){
        return roleMapper.getAll();
    }

    private boolean checkExist(String name,Integer id){
        return roleMapper.countByName(name,id) > 0;
    }

    public List<Role> getRoleListByUserId(int userId){
        List<Integer> idList = roleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(idList)){
            return Lists.newArrayList();
        }
        return roleMapper.getByIdList(idList);
    }

    public List<Role> getRoleListByACLId(int aclId){
        List<Integer> roleIdList = roleACLMapper.getRoleIdListByACLId(aclId);
        if (CollectionUtils.isEmpty(roleIdList)){
            return Lists.newArrayList();
        }
        return roleMapper.getByIdList(roleIdList);
    }

    public List<User> getUserListByRoleList(List<Role> roleList){
        if (CollectionUtils.isEmpty(roleList)){
            return Lists.newArrayList();
        }
        List<Integer> roleIdList = roleList.stream().map(role -> role.getId()).collect(Collectors.toList());
        List<Integer> userIdList = roleUserMapper.getUserIdListByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(userIdList)){
            return Lists.newArrayList();
        }
        return userMapper.getUserByIdList(userIdList);
    }
}
