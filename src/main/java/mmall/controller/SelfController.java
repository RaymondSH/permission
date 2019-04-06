package mmall.controller;

import mmall.model.User;
import mmall.service.UserService;
import mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Raymond on 2019/1/11.
 */
@Controller
@RequestMapping("/")
public class SelfController {

    @Autowired
    private UserService userService;

    @RequestMapping("login.page")
    @ResponseBody
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userService.findByKeyword(username);
        String errorMsg = "";
        String result = request.getParameter("result");
        if (StringUtils.isBlank(username)){
            errorMsg = "用户名不可以为空";
        }else if (StringUtils.isBlank(password)){
            errorMsg = "密码不能为空";
        }else if (user == null){
            errorMsg = "查询不到指定的用户";
        }else if (!user.getPassword().equals(MD5Util.encrypt(password))){
            errorMsg = "用户名或密码错误！";
        }else if (user.getStatus() != 1){
            errorMsg = "用户已被冻结，请练习管理员";
        }else {
            request.getSession().setAttribute("user",user);
            if (StringUtils.isNotBlank(result)){
                response.sendRedirect(result);
            }else {
                response.sendRedirect("/admin/index.page");
            }
        }
        request.setAttribute("error",errorMsg);
        request.setAttribute("username",username);
        if (StringUtils.isNotBlank(result)){
            request.setAttribute("result",result);
        }
        String path = "signin.jsp";
        request.getRequestDispatcher(path).forward(request,response);
    }

    @RequestMapping("logout.page")
    public void logout(HttpServletRequest request,HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        String path = "signin.jsp";
        response.sendRedirect(path);
    }
}
