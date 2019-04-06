package mmall.controller;

import mmall.common.JSONData;
import mmall.dto.DepartmentLevelDTO;
import mmall.param.DepartmentParam;
import mmall.service.DepartmentService;
import mmall.service.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Raymond on 2019/1/10.
 */
@Controller
@RequestMapping("/sys/dept")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private TreeService treeService;

    @RequestMapping("/save.json")
    @ResponseBody
    public JSONData save(DepartmentParam departmentParam){
        departmentService.save(departmentParam);
        return JSONData.success();
    }

    @RequestMapping("/dept.page")
    public ModelAndView page(){
        return new ModelAndView("department");
    }

    @RequestMapping("/tree.json")
    @ResponseBody
    public JSONData tree(){
        List<DepartmentLevelDTO> departmentLevelDTOList = treeService.departmentTree();
        return JSONData.success(departmentLevelDTOList);
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JSONData update(DepartmentParam departmentParam){
        departmentService.update(departmentParam);
        return JSONData.success();
    }

    @RequestMapping("/delete.json")
    @ResponseBody
    public JSONData delete(@RequestParam("deptId")int id){
        departmentService.delete(id);
        return JSONData.success();
    }
}
