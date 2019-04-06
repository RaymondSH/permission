package mmall.service;

import com.google.common.base.Preconditions;
import mmall.beans.PageQuery;
import mmall.beans.PageResult;
import mmall.common.RequestHolder;
import mmall.dao.ACLMapper;
import mmall.exception.ParamException;
import mmall.model.ACL;
import mmall.param.ACLParam;
import mmall.util.BeanValidator;
import mmall.util.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Raymond on 2019/1/13.
 */
@Service
public class ACLService {

    @Autowired
    private ACLMapper aclMapper;

    public void save(ACLParam param){
        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(),param.getName(),param.getId())){
            throw new ParamException("当前权限模块下面存在相同的权限点名称");
        }
        ACL acl = ACL.builder().name(param.getName()).aclModuleId(param.getAclModuleId()).url(param.getUrl())
                .type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
        acl.setCode(generateCode());
        acl.setOperator(RequestHolder.getUser().getUsername());
        acl.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
        acl.setOperateTime(new Date());
        aclMapper.insertSelective(acl);
    }
    public void update(ACLParam param){
        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(),param.getName(),param.getId())){
            throw new ParamException("当前权限模块下面存在相同的权限点名称");
        }
        ACL before = aclMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的权限点不存在");
        ACL after = ACL.builder().id(param.getId()).name(param.getName()).aclModuleId(param.getAclModuleId()).url(param.getUrl())
                .type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getUser().getUsername());
        after.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
        after.setOperateTime(new Date());
        aclMapper.updateByPrimaryKeySelective(after);
    }

    private boolean checkExist(int moduleId,String name,Integer id){
        return aclMapper.countByNameAndModuleId(moduleId,name,id) > 0;
    }

    private String generateCode(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date()) + "_" + (int)(Math.random() * 100);
    }

    public PageResult<ACL> getPageByModuleId(Integer id, PageQuery page){
        BeanValidator.check(page);
        int count = aclMapper.countByACLModuleId(id);
        if (count > 0){
            List<ACL> aclList = aclMapper.getPageByACLModuleId(id,page);
            return PageResult.<ACL>builder().data(aclList).total(count).build();
        }
        return PageResult.<ACL>builder().build();
    }
}
