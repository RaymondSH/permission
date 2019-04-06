package mmall.dao;

import mmall.beans.PageQuery;
import mmall.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User findByKeyword(@Param("keyword") String keyword);

    int countByEmail(@Param("email") String email,@Param("id") Integer id);

    int countByTelephone(@Param("telephone") String telephone,@Param("id") Integer id);

    int countByDeptId(@Param("deptId")Integer deptId);

    List<User> getUserByDeptId(@Param("deptId")Integer deptId, @Param("page")PageQuery pageQuery);

    List<User> getUserByIdList(@Param("idList") List<Integer> userIdList);

    List<User> getAll();
}