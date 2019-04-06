package mmall.dao;

import mmall.model.RoleACL;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleACLMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleACL record);

    int insertSelective(RoleACL record);

    RoleACL selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleACL record);

    int updateByPrimaryKey(RoleACL record);

    List<Integer> getAclIdListByRoleIdList(@Param("userRoleIdList") List<Integer> userRoleIdList);

    void deleteByRoleId(@Param("roleId") int roleId);

    void batchInsert(@Param("roleAclList") List<RoleACL> roleACLList);

    List<Integer> getRoleIdListByACLId(@Param("aclId")int aclId);
}