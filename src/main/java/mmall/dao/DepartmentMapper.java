package mmall.dao;

import mmall.model.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepartmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Department record);

    int insertSelective(Department record);

    Department selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Department record);

    int updateByPrimaryKey(Department record);

    List<Department> getAllDepartment();

    List<Department> getChildDepartmentListByLevel(@Param("level") String level);

    void batchUpdateLevel(@Param("departmentList") List<Department> departmentList);

    int countByNameAndParentId(@Param("parentId")Integer parentId,@Param("name")String name,@Param("id")Integer id);

    int countByParentId(@Param("deptId") Integer id);
}