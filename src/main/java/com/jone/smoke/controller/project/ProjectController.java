package com.jone.smoke.controller.project;

import com.alibaba.fastjson.JSONArray;
import com.jone.smoke.controller.BaseController;
import com.jone.smoke.dao.IDao;
import com.jone.smoke.dao.expert.SmokeExpertRepository;
import com.jone.smoke.entity.common.ResultUtil;
import com.jone.smoke.util.ExcelExportUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@EnableAutoConfiguration
@RequestMapping(value = "project")
public class ProjectController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private SmokeExpertRepository smokeExpertRepository;
    @Autowired
    private IDao dao;

    @RequestMapping(value = "list")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("project/list");
        return mv;
    }

    @RequestMapping(value = "query", method = RequestMethod.POST)
    public void countQuery(HttpServletRequest request, HttpServletResponse response){
        String unitName = request.getParameter("unitName");
        String expName = request.getParameter("expName");
        String expType = request.getParameter("expType");
        String reviewType = request.getParameter("reviewType");
        String stTime = request.getParameter("stTime");
        String eTime = request.getParameter("eTime");
        StringBuffer sql = new StringBuffer("select project_name as projectName,count(distinct(expert_name_skill)) as pNum,count(1) as num,sum(review_cost) as cost from s_expert where 1=1 ");
        try {
            if(!StringUtils.isEmpty(unitName)){
                sql.append(" and expert_unit_skill like '%"+unitName+"%' ");
            }
            if(!StringUtils.isEmpty(expName)){
                sql.append(" and expert_name_skill like '%"+expName+"%' ");
            }
            if(!StringUtils.isEmpty(reviewType))
                sql.append(" and review_type="+reviewType);
            if(!StringUtils.isEmpty(expType))
                sql.append(" and expert_type="+expType);
            if(!StringUtils.isEmpty(stTime))
                sql.append(" and DATE_FORMAT(review_time,'%Y%m%d')>="+stTime.replace("-","")+"");
            //sql.append(" and review_time>=STR_TO_DATE("+stTime+",'%Y-%m-%d')");
            if(!StringUtils.isEmpty(eTime))
                sql.append(" and DATE_FORMAT(review_time,'%Y%m%d')<="+eTime.replace("-","")+"");
            sql.append(" group by project_name");
            List<Map<String,Object>> list = dao.findBySqlToMap(sql.toString());
            printJson(ResultUtil.success(list),response);
        }catch (Exception e){
            e.printStackTrace();
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public void countExport(HttpServletRequest request,HttpServletResponse response){
        String unitName = request.getParameter("unitName");
        String expName = request.getParameter("expName");
        String expType = request.getParameter("expType");
        String reviewType = request.getParameter("reviewType");
        String stTime = request.getParameter("stTime");
        String eTime = request.getParameter("eTime");
        StringBuffer sql = new StringBuffer("select project_name as projectName,count(distinct(expert_name_skill)) as pNum,count(1) as num,sum(review_cost) as cost from s_expert where 1=1 ");
        try {
            if(!StringUtils.isEmpty(unitName)){
                sql.append(" and expert_unit_skill like '%"+unitName+"%' ");
            }
            if(!StringUtils.isEmpty(expName)){
                sql.append(" and expert_name_skill like '%"+expName+"%' ");
            }
            if(!StringUtils.isEmpty(reviewType))
                sql.append(" and review_type="+reviewType);
            if(!StringUtils.isEmpty(expType))
                sql.append(" and expert_type="+expType);
            if(!StringUtils.isEmpty(stTime))
                sql.append(" and DATE_FORMAT(review_time,'%Y%m%d')>="+stTime.replace("-","")+"");
            //sql.append(" and review_time>=STR_TO_DATE("+stTime+",'%Y-%m-%d')");
            if(!StringUtils.isEmpty(eTime))
                sql.append(" and DATE_FORMAT(review_time,'%Y%m%d')<="+eTime.replace("-","")+"");
            sql.append(" group by project_name");
            List<Map<String,Object>> list = dao.findBySqlToMap(sql.toString());
            if(list!=null&&list.size()>0){
                JSONArray ja = new JSONArray();
                int i=1;
                for (Map<String,Object> map:list
                        ) {
                    map.put("no",i);
                    ja.add(map);
                    i++;
                }
                Map<String,String> headMap = new LinkedHashMap<>();
                headMap.put("no","序号");
                headMap.put("projectName","单位名称");
                headMap.put("pNum","专家人数");
                headMap.put("num","评审次数");
                headMap.put("cost","评审费用(元)");
                ExcelExportUtil.downloadExcelFile("科技项目单位评审费用统计表",headMap,ja,response);
            }else{
                logger.error("可导出数据为空");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
