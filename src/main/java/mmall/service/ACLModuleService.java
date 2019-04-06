package mmall.service;

import com.google.common.base.Preconditions;
import mmall.common.RequestHolder;
import mmall.dao.ACLMapper;
import mmall.dao.ACLModuleMapper;
import mmall.exception.ParamException;
import mmall.model.ACLModule;
import mmall.param.ACLModuleParam;
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
 * Created by Raymond on 2019/1/12.
 */
@Service
public class ACLModuleService {
    @Autowired
    private ACLModuleMapper moduleMapper;
    @Autowired
    private ACLMapper aclMapper;

    public void save(ACLModuleParam param){
        BeanValidator.check(param);
        if (checkExist(param.getParentId(),param.getName(),param.getId())){
            throw new ParamException("同一层级下不能存在名称相同的权限模块");
        }
        ACLModule module = ACLModule.builder().name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).status(param.getStatus()).remark(param.getRemark()).build();
        module.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId()));
        module.setOperator(RequestHolder.getUser().getUsername());
        module.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
        module.setOperateTime(new Date());
        moduleMapper.insertSelective(module);
    }

    private boolean checkExist(Integer parentId,String departmentName,Integer departmentId){
        return moduleMapper.countByNameAndParentId(parentId,departmentName,departmentId) > 0;
    }

    public void update(ACLModuleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getParentId(),param.getName(),param.getId())){
            throw new ParamException("同一层级下存在名称相同的权限模块");
        }
        ACLModule before = moduleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的部门不存在");
        if (checkExist(param.getParentId(),param.getName(),param.getId())){
            throw new ParamException("同一层级下存在名称相同的权限模块");
        }
        ACLModule after =ACLModule.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).status(param.getStatus()).remark(param.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId()));
        after.setOperator(RequestHolder.getUser().getUsername());
        after.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
        after.setOperateTime(new Date());

        updateWithChild(before,after);
    }

    @Transactional
    private void updateWithChild(ACLModule before, ACLModule after){
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())){
            List<ACLModule> moduleList = moduleMapper.getChildACLModuleListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(moduleList)){
                for (ACLModule module : moduleList){
                    String level = module.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0){
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        module.setLevel(level);
                    }
                }
                moduleMapper.batchUpdateLevel(moduleList);
            }
        }
        moduleMapper.updateByPrimaryKeySelective(after);
    }


    private String getLevel(Integer id){
        ACLModule module = moduleMapper.selectByPrimaryKey(id);
        if (module == null){
            return null;
        }
        return module.getLevel();
    }

    public void delete(Integer id){
        ACLModule module = moduleMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(module,"当前权限模块下有权限点，无法删除");
        if (moduleMapper.countByParentId(module.getId()) > 0){
            throw new ParamException("当前模块下面有子模块，无法删除");
        }
        if (aclMapper.countByACLModuleId(id) > 0) {
            throw new ParamException("当前模块下面有用户，无法删除");
        }
        moduleMapper.deleteByPrimaryKey(id);
    }

}
