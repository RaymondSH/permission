package mmall.controller;

import mmall.common.JSONData;
import mmall.param.ACLModuleParam;
import mmall.service.ACLModuleService;
import mmall.service.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Raymond on 2019/1/12.
 */
@Controller
@RequestMapping("/sys/module")
public class ACLModuleController {

    @Autowired
    private ACLModuleService moduleService;
    @Autowired
    private TreeService treeService;

    @RequestMapping("/save.json")
    @ResponseBody
    public JSONData save(ACLModuleParam param){
        moduleService.save(param);
        return JSONData.success();
    }

    @RequestMapping("/tree.json")
    @ResponseBody
    public JSONData tree(){
        return JSONData.success(treeService.moduleTree());
    }

    @RequestMapping("/acl.page")
    public ModelAndView page(){
        return new ModelAndView("acl");
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JSONData update(ACLModuleParam param){
        moduleService.update(param);
        return JSONData.success();
    }

    @RequestMapping("/delete.json")
    @ResponseBody
    public JSONData delete(@RequestParam("aclModuleId")int id){
        moduleService.delete(id);
        return JSONData.success();
    }
}
