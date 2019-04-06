package mmall.service;

import com.google.common.collect.Lists;
import mmall.common.RequestHolder;
import mmall.dao.ACLMapper;
import mmall.dao.RoleACLMapper;
import mmall.dao.RoleUserMapper;
import mmall.model.ACL;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Raymond on 2019/1/13.
 */
@Service
public class CoreService {

    @Autowired
    private ACLMapper aclMapper;
    @Autowired
    private RoleUserMapper roleUserMapper;
    @Autowired
    private RoleACLMapper roleACLMapper;

    public List<ACL> getCurrentUserACL(){
        int userId = RequestHolder.getUser().getId();
        return getUserACLById(userId);
    }

    public List<ACL> getRoleACL(Integer roleId){
        List<Integer> aclIdList = roleACLMapper.getAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
        if (CollectionUtils.isEmpty(aclIdList)){
            return Lists.newArrayList();
        }
        return aclMapper.getByIdList(aclIdList);
    }

    public List<ACL> getUserACLById(Integer id){
        if (isRoot()){
            return aclMapper.getAll();
        }
        List<Integer> userRoleIdList = roleUserMapper.getRoleIdListByUserId(id);
        if (CollectionUtils.isEmpty(userRoleIdList)){
            return Lists.newArrayList();
        }
        List<Integer> userAclIdList = roleACLMapper.getAclIdListByRoleIdList(userRoleIdList);
        if (CollectionUtils.isEmpty(userAclIdList)){
            return Lists.newArrayList();
        }
        return aclMapper.getByIdList(userAclIdList);
    }

    private boolean isRoot(){
        //todo
        return true;
    }

    public boolean hasUrlAcl(String url) {
        if (isRoot()){
            return true;
        }
        List<ACL> aclList = aclMapper.getByUrl(url);
        if (CollectionUtils.isEmpty(aclList)){
            return true;
        }
        List<ACL> userAclList = getCurrentUserACL();
        Set<Integer> userAclSet = userAclList.stream().map(acl -> acl.getId()).collect(Collectors.toSet());
        boolean hasVaildAcl = false;
        for (ACL acl : aclList){
            if (acl == null && acl.getStatus() != 1){
                continue;
            }
            hasVaildAcl = true;
            if (userAclSet.contains(acl.getId())){
                return true;
            }
        }
        if (!hasVaildAcl){
            return true;
        }
        return false;
    }
}
