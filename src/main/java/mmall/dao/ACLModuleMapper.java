package mmall.dao;

import mmall.model.ACLModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ACLModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ACLModule record);

    int insertSelective(ACLModule record);

    ACLModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ACLModule record);

    int updateByPrimaryKey(ACLModule record);

    int countByNameAndParentId(@Param("parentId")Integer parentId, @Param("name")String name, @Param("id")Integer id);

    List<ACLModule> getChildACLModuleListByLevel(@Param("level")String level);

    void batchUpdateLevel(@Param("moduleList") List<ACLModule> moduleList);

    List<ACLModule> getAllModule();

    int countByParentId(@Param("aclModuleId") Integer id);
}