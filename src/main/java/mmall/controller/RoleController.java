package mmall.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import mmall.common.JSONData;
import mmall.model.User;
import mmall.param.RoleParam;
import mmall.service.*;
import mmall.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Raymond on 2019/1/13.
 */
@Controller
@RequestMapping("/sys/role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private TreeService treeService;
    @Autowired
    private RoleACLService roleACLService;
    @Autowired
    private RoleUserService roleUserService;
    @Autowired
    private UserService userService;

    @RequestMapping("role.page")
    public ModelAndView page(){
        return new ModelAndView("role");
    }

    @RequestMapping("/save.json")
    @ResponseBody
    public JSONData save(RoleParam param){
        roleService.save(param);
        return JSONData.success();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JSONData update(RoleParam param){
        roleService.update(param);
        return JSONData.success();
    }

    @RequestMapping("/list.json")
    @ResponseBody
    public JSONData getAll(){
        return JSONData.success(roleService.getAll());
    }


    @RequestMapping("/roleTree.json")
    @ResponseBody
    public JSONData roleTree(@RequestParam("roleId") Integer roleId){
        return JSONData.success(treeService.roleTree(roleId));
    }

    @RequestMapping("/changeAcls.json")
    @ResponseBody
    public JSONData changACL(@RequestParam("roleId") Integer roleId,@RequestParam(value = "aclIds",required = false,defaultValue = "") String aclIds){
        List<Integer> aclIdList = StringUtil.splitToListInt(aclIds);
        roleACLService.changACLs(roleId,aclIdList);
        return JSONData.success();
    }

    @RequestMapping("/changeUsers.json")
    @ResponseBody
    public JSONData changUsers(@RequestParam("roleId") Integer roleId,@RequestParam(value = "userIds",required = false,defaultValue = "") String userIds){
        List<Integer> userIdList = StringUtil.splitToListInt(userIds);
        roleUserService.changRoleUsers(roleId,userIdList);
        return JSONData.success();
    }

    @RequestMapping("/users.json")
    @ResponseBody
    public JSONData user(@RequestParam("roleId") Integer roleId){
        List<User> selectedUserList = roleUserService.getUsersByRoleId(roleId);
        List<User> allUsers = userService.getAllUser();
        List<User> unSelectedUserList = Lists.newArrayList();
        Set<Integer> selectedUserSet = selectedUserList.stream().map(user -> user.getId()).collect(Collectors.toSet());
        for (User user : allUsers){
            if (user.getStatus() == 1 && !selectedUserSet.contains(user.getId())){
                unSelectedUserList.add(user);
            }
        }
        Map<String,List<User>> map = Maps.newHashMap();
        map.put("selected",selectedUserList);
        map.put("unselected",unSelectedUserList);
        return JSONData.success(map);
    }
}
