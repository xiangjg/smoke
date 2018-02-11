package com.jone.smoke.controller.system;

import com.jone.smoke.api.system.RoleService;
import com.jone.smoke.api.system.UserService;
import com.jone.smoke.controller.BaseController;
import com.jone.smoke.entity.common.ResultUtil;
import com.jone.smoke.entity.system.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/user")
public class UserController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/index")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("system/user");
        mv.addObject("roleList",roleService.getAllRole());
        return mv;
    }

    @RequestMapping(value = "/list")
    public void list(HttpServletResponse response){
        try {
            List<User> userList = userService.queryUsers();
            printJson(ResultUtil.success(userList),response);
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }

    @RequestMapping(value = "/update")
    public void update(@RequestBody User user, HttpServletResponse response){
        try {
            userService.update(user);
            printJson(ResultUtil.success(),response);
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }

    @RequestMapping(value = "/save")
    public void save(@RequestBody User user, HttpServletResponse response){
        try {
            userService.save(user);
            printJson(ResultUtil.success(),response);
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }

    @RequestMapping(value = "/delete")
    public void delete(@RequestParam String userId, HttpServletResponse response){
        try {
            userService.delete(userId);
            printJson(ResultUtil.success(),response);
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }

    @RequestMapping(value = "/deleteUsers")
    public void deleteUsers(@RequestParam String userIds, HttpServletResponse response){
        try {
            userService.deleteUsers(userIds.split(","));
            printJson(ResultUtil.success(),response);
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }

    @RequestMapping(value = "/getUserById")
    public void getUserById(@RequestParam String userId, HttpServletResponse response){
        try {
            Map<String,Object> map = new HashMap<>();
            User user = userService.queryUserByUserId(userId);
            if(user!=null)
                map.put("user",user);
            map.put("roleList",roleService.getAllRole());
            printJson(ResultUtil.success(map),response);
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }
    @RequestMapping(value = "/getUserInfo",method = RequestMethod.GET)
    public void getUserInfo(@RequestParam String pwd, HttpSession session, HttpServletResponse response){
        User sUser = (User) session.getAttribute("User");
        try {
            User user = userService.findUserByIdAndPwd(sUser.getUserId(),pwd);
            printJson(ResultUtil.success(user),response);
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }

    @RequestMapping(value = "/update/pwd")
    public void updatePassword(@RequestParam String password, HttpSession session, HttpServletResponse response){
        User sUser = (User) session.getAttribute("User");
        try {
            if(sUser == null){
                printJson(ResultUtil.error(-1,"登录超时"),response);
            }else{
                userService.updatePassword(sUser.getUserId(),password);
                printJson(ResultUtil.success(),response);
            }
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }

    @RequestMapping(value = "/reset/password")
    public void resetPassword(@RequestParam String userId, HttpServletResponse response){
        try {
            userService.resetPassword(userId);
            printJson(ResultUtil.success(),response);
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }
}
