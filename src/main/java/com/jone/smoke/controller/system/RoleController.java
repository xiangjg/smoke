package com.jone.smoke.controller.system;

import com.jone.smoke.api.system.RoleService;
import com.jone.smoke.controller.BaseController;
import com.jone.smoke.entity.common.ResultUtil;
import com.jone.smoke.entity.system.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/role")
public class RoleController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/index")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("system/role");
        return mv;
    }
    @RequestMapping(value = "/list")
    public void list(HttpServletResponse response){
        try {
            List<Role> roleList = roleService.getAllRole();
            printJson(ResultUtil.success(roleList),response);
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }

    @RequestMapping(value = "/getRole")
    public void getRole(@RequestParam String roleId, HttpServletResponse response){
        try {
            Role role = roleService.getRoleMenuInfo(roleId);
            printJson(ResultUtil.success(role),response);
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }

    @RequestMapping(value = "/saveRight")
    public void saveRight(@RequestBody Role role, HttpServletResponse response){
        try {
            roleService.updateRights(role.getRoleId(),role.getMis());
            printJson(ResultUtil.success(),response);
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }

    @RequestMapping(value = "/save")
    public void save(@RequestBody Role role, HttpServletResponse response){
        try {
            roleService.save(role);
            printJson(ResultUtil.success(),response);
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }

    @RequestMapping(value = "/update")
    public void update(@RequestBody Role role, HttpServletResponse response){
        try {
            roleService.update(role);
            printJson(ResultUtil.success(),response);
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }

    @RequestMapping(value = "/delete")
    public void delete(@RequestParam String roleId, HttpServletResponse response){
        try {
            roleService.deleteById(roleId);
            printJson(ResultUtil.success(),response);
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }

    @RequestMapping(value = "/deleteRoles")
    public void deleteUsers(@RequestParam String roleIds, HttpServletResponse response){
        try {
            roleService.deleteByRoles(roleIds.split(","));
            printJson(ResultUtil.success(),response);
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }
}
