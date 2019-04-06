package mmall.controller;

import com.google.common.collect.Maps;
import mmall.beans.PageQuery;
import mmall.beans.PageResult;
import mmall.common.JSONData;
import mmall.param.UserParam;
import mmall.service.RoleService;
import mmall.service.TreeService;
import mmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by Raymond on 2019/1/11.
 */
@Controller
@RequestMapping("/sys/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TreeService treeService;
    @Autowired
    private RoleService roleService;

    @RequestMapping("/save.json")
    @ResponseBody
    public JSONData save(UserParam userParam){
        userService.save(userParam);
        return JSONData.success();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JSONData update(UserParam userParam){
        userService.update(userParam);
        return JSONData.success();
    }

    @RequestMapping("/list.json")
    @ResponseBody
    public JSONData page(@RequestParam("deptId") Integer departmentId, PageQuery pageQuery){
        PageResult pageResult = userService.getPageByDeptId(departmentId, pageQuery);
        return JSONData.success(pageResult);
    }

    @RequestMapping("/acls.json")
    @ResponseBody
    public JSONData acls(@RequestParam("userId") int userId){
        Map<String,Object> map = Maps.newHashMap();
        map.put("acls",treeService.userAclTree(userId));
        map.put("roles",roleService.getRoleListByUserId(userId));
        return JSONData.success(map);
    }
}
