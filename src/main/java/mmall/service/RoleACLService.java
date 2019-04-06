package mmall.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import mmall.common.RequestHolder;
import mmall.dao.RoleACLMapper;
import mmall.model.RoleACL;
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
public class RoleACLService {

    @Autowired
    private RoleACLMapper roleACLMapper;

    public void changACLs(Integer roleId,List<Integer> aclIdList){
        List<Integer> originAclIdList = roleACLMapper.getAclIdListByRoleIdList(Lists.newArrayList(aclIdList));
        if (originAclIdList.size() == aclIdList.size()){
            Set<Integer> originAclSet = Sets.newHashSet(originAclIdList);
            Set<Integer> aclIdSet = Sets.newHashSet(aclIdList);
            originAclSet.removeAll(aclIdSet);
            if (CollectionUtils.isEmpty(originAclSet))
                return;
        }
        updateRoleWithAcls(roleId,aclIdList);
    }

    @Transactional
    public void updateRoleWithAcls(Integer roleId,List<Integer> aclIdList){
        roleACLMapper.deleteByRoleId(roleId);
        if (CollectionUtils.isEmpty(aclIdList))
            return;
        List<RoleACL> roleACLList = Lists.newArrayList();
        for (Integer aclId : aclIdList){
            RoleACL roleACL = RoleACL.builder().roleId(roleId).aclId(aclId).operator(RequestHolder.getUser().getUsername())
                    .operateIp(IPUtil.getRemoteIp(RequestHolder.getRequest())).operateTime(new Date()).build();
            roleACLList.add(roleACL);
        }
        roleACLMapper.batchInsert(roleACLList);
    }
}
