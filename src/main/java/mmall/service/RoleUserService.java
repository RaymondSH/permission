package mmall.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import mmall.common.RequestHolder;
import mmall.dao.RoleUserMapper;
import mmall.dao.UserMapper;
import mmall.model.RoleUser;
import mmall.model.User;
import mmall.util.IPUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Raymond on 2019/1/14.
 */
@Service
public class RoleUserService {
    @Autowired
    private RoleUserMapper roleUserMapper;
    @Autowired
    private UserMapper userMapper;

    public List<User> getUsersByRoleId(Integer roleId){
        List<Integer> userIdList = roleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)){
            return Lists.newArrayList();
        }
        return userMapper.getUserByIdList(userIdList);
    }

    public void changRoleUsers(int roleId,List<Integer> userIdList){
        List<Integer> originUserList = roleUserMapper.getUserIdListByRoleId(roleId);
        if (originUserList.size() == userIdList.size()){
            Set<Integer> originUserSet = Sets.newHashSet(originUserList);
            Set<Integer> userIdSet = Sets.newHashSet(userIdList);
            originUserSet.removeAll(userIdSet);
            if (CollectionUtils.isEmpty(originUserSet))
                return;
        }
        updateRoleWithUsers(roleId,userIdList);
    }

    @Transactional
    private void updateRoleWithUsers(int roleId, List<Integer> userIdList) {
        roleUserMapper.deleteByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)){
            return;
        }
        List<RoleUser> roleUserList = Lists.newArrayList();
        for (Integer userId : userIdList){
            RoleUser roleUser = RoleUser.builder().roleId(roleId).userId(userId).build();
            roleUser.setOperator(RequestHolder.getUser().getUsername());
            roleUser.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
            roleUser.setOperateTime(new Date());
            roleUserList.add(roleUser);
        }
        roleUserMapper.batchInsert(roleUserList);
    }
}
