package com.jone.smoke.controller;

import com.jone.smoke.entity.common.ResultUtil;
import com.jone.smoke.entity.system.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@EnableAutoConfiguration
public class LoginController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView toLogin() {
        ModelAndView mv = new ModelAndView("login");
        //mv.addObject("title", systemProperties.getName());
        return mv;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@RequestParam String userid, @RequestParam String password, HttpServletResponse response, HttpSession session) {
        if (StringUtils.isEmpty(userid)) {
            printJson(ResultUtil.error(-1, "用户名不能为空"), response);
        } else {
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userid, password);
            Subject subject = SecurityUtils.getSubject();
            try {
                subject.login(usernamePasswordToken);   //完成登录
                subject.getSession().setTimeout(1800000);//设置超时时间 为负数则永久
                User user = (User) subject.getPrincipal();
                printJson(ResultUtil.success(user), response);
            } catch (IncorrectCredentialsException e) {
                printJson(ResultUtil.error(-1, "密码不正确"), response);
            } catch (UnknownAccountException e) {
                printJson(ResultUtil.error(-1, "账号不存在"), response);
            } catch (AuthenticationException e) {
                printJson(ResultUtil.error(-1, "账号不存在"), response);
            } catch (Exception e) {
                printJson(ResultUtil.error(-1, e.getMessage()), response);
            }
        }
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public ModelAndView logout() {
        ModelAndView mv = new ModelAndView("login");
        try {
            SecurityUtils.getSubject().logout();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return mv;
    }

    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        //mv.addObject("title", systemProperties.getName());
        return mv;
    }
}
