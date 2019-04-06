package mmall.service;

import com.google.common.base.Preconditions;
import mmall.common.RequestHolder;
import mmall.dao.DepartmentMapper;
import mmall.dao.UserMapper;
import mmall.exception.ParamException;
import mmall.model.Department;
import mmall.param.DepartmentParam;
import mmall.util.BeanValidator;
import mmall.util.IPUtil;
import mmall.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Raymond on 2019/1/10.
 */
@Service
public class DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private UserMapper userMapper;

    public void save(DepartmentParam param){
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())){
            throw new ParamException("同一层级下不能存在名称相同的部门");
        }
        Department department = Department.builder().name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).remark(param.getRemark()).build();
        department.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        department.setOperator(RequestHolder.getUser().getUsername());
        department.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
        department.setOperateTime(new Date());
        departmentMapper.insertSelective(department);
    }

    private boolean checkExist(Integer parentId,String name,Integer id){
        return departmentMapper.countByNameAndParentId(parentId,name,id) > 0;
    }

    private String getLevel(Integer id){
        Department department = departmentMapper.selectByPrimaryKey(id);
        if (department == null){
            return null;
        }
        return department.getLevel();
    }

    public void update(DepartmentParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())){
            throw new ParamException("同一层级下存在名称相同的部门");
        }
        Department before = departmentMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的部门不存在");
        if (checkExist(param.getParentId(), param.getName(), param.getId())){
            throw new ParamException("同一层级下存在名称相同的部门");
        }
        Department after = Department.builder().id(param.getId()).name(param.getName()).
                parentId(param.getParentId()).seq(param.getSeq()).remark(param.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        after.setOperator(RequestHolder.getUser().getUsername());
        after.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
        after.setOperateTime(new Date());
        updateWithChild(before,after);
    }

    @Transactional
    private void updateWithChild(Department before,Department after){
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())){
            List<Department> departmentList = departmentMapper.getChildDepartmentListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(departmentList)){
                for (Department department : departmentList){
                    String level = department.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0){
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        department.setLevel(level);
                    }
                }
                departmentMapper.batchUpdateLevel(departmentList);
            }
        }
        departmentMapper.updateByPrimaryKeySelective(after);
    }

    public void delete(Integer id){
        Department department = departmentMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(department,"待删除的部门不存在");
        if (departmentMapper.countByParentId(department.getId()) > 0){
            throw new ParamException("当前部门下面有子部门，无法删除");
        }
        if (userMapper.countByDeptId(department.getId()) > 0){
            throw new ParamException("当前部门下面有用户，无法删除");
        }
        departmentMapper.deleteByPrimaryKey(id);
    }
}
