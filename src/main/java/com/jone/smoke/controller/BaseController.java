package com.jone.smoke.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jone.smoke.entity.system.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class BaseController {
    protected static Logger logger = LoggerFactory.getLogger(BaseController.class);
    /**
     * 打印json格式
     *
     * @param object
     * @param response
     */
    public void printJson(Object object, HttpServletResponse response) {
        OutputStream out = null;
        try {
            response.setContentType("application/json" + ";charset=UTF-8");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            out = response.getOutputStream();
            String jsonStr = JSON.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect);
            out.write(jsonStr.getBytes("UTF-8"));
        } catch (IOException e) {
            logger.error("IOException",e);
        } finally {
            if (null != out) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    logger.error("IOException",e);
                }
            }
        }
    }

    public User getUser(){
        User user = null;
        Subject subject = SecurityUtils.getSubject();
        try {
            user=(User) subject.getPrincipal();
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }
}
