package mmall.dao;

import mmall.beans.PageQuery;
import mmall.model.ACL;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ACLMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ACL record);

    int insertSelective(ACL record);

    ACL selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ACL record);

    int updateByPrimaryKey(ACL record);

    int countByACLModuleId(@Param("moduleId") Integer moduleId);

    List<ACL> getPageByACLModuleId(@Param("moduleId") Integer moduleId, @Param("page") PageQuery page);

    int countByNameAndModuleId(@Param("moduleId") Integer moduleId,@Param("name") String name,@Param("id") Integer id);

    List<ACL> getAll();

    List<ACL> getByIdList(@Param("idList") List<Integer> idList);

    List<ACL> getByUrl(@Param("url")String url);
}