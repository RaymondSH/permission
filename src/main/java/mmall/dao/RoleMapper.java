package mmall.dao;

import mmall.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    int countByName(@Param("name") String name,@Param("id") Integer id);

    List<Role> getAll();

    List<Role> getByIdList(@Param("idList")List<Integer> idList);
}