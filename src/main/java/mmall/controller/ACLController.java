package mmall.controller;

import com.google.common.collect.Maps;
import mmall.beans.PageQuery;
import mmall.common.JSONData;
import mmall.model.Role;
import mmall.param.ACLParam;
import mmall.service.ACLService;
import mmall.service.RoleService;
import mmall.service.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by Raymond on 2019/1/12.
 */
@Controller
@RequestMapping("/sys/acl")
public class ACLController {

    @Autowired
    private ACLService aclService;
    @Autowired
    private RoleService roleService;

    @RequestMapping("/save.json")
    @ResponseBody
    public JSONData save(ACLParam param){
        aclService.save(param);
        return JSONData.success();
    }


    @RequestMapping("/update.json")
    @ResponseBody
    public JSONData update(ACLParam param){
        aclService.update(param);
        return JSONData.success();
    }

    @RequestMapping("/list.json")
    @ResponseBody
    public JSONData list(@RequestParam("aclModuleId") Integer moduleId, PageQuery page){
        return JSONData.success(aclService.getPageByModuleId(moduleId,page));
    }

    @RequestMapping("/acls.json")
    @ResponseBody
    public JSONData acls(@RequestParam("aclId") int aclId){
        Map<String,Object> map = Maps.newHashMap();
        List<Role> roleList = roleService.getRoleListByACLId(aclId);
        map.put("roles",roleList);
        map.put("users",roleService.getUserListByRoleList(roleList));
        return JSONData.success(map);
    }

}
