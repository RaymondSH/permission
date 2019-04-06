package mmall.dao;

import mmall.model.RoleUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleUser record);

    int insertSelective(RoleUser record);

    RoleUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleUser record);

    int updateByPrimaryKey(RoleUser record);

    List<Integer> getRoleIdListByUserId(@Param("userId") Integer id);

    List<Integer> getUserIdListByRoleId(@Param("roleId") Integer roleId);

    void deleteByRoleId(@Param("roleId") Integer roleId);

    void batchInsert(@Param("roleUserList") List<RoleUser> roleUserList);

    List<Integer> getUserIdListByRoleIdList(@Param("roleIdList")List<Integer> roleIdList);
}