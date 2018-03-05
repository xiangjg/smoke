package com.jone.smoke.controller.expert;

import com.alibaba.fastjson.JSONArray;
import com.jone.smoke.controller.BaseController;
import com.jone.smoke.dao.IDao;
import com.jone.smoke.dao.custom.Criteria;
import com.jone.smoke.dao.custom.Restrictions;
import com.jone.smoke.dao.expert.SmokeExpertRepository;
import com.jone.smoke.entity.common.ResultUtil;
import com.jone.smoke.entity.expert.SmokeExpert;
import com.jone.smoke.util.ExcelExportUtil;
import com.jone.smoke.util.ExcelUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@EnableAutoConfiguration
@RequestMapping(value = "expert")
public class ExpertController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(ExpertController.class);

    @Autowired
    private SmokeExpertRepository smokeExpertRepository;
    @Autowired
    private IDao dao;

    @RequestMapping(value = "list")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("expert/list");
        return mv;
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public void upload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            List<SmokeExpert> list = new ArrayList<>();
            FileInputStream is = (FileInputStream) file.getInputStream();
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();
            for (int r = 3; r < rowCount; r++) {
                Row row = sheet.getRow(r);
                if (null == row) {
                    continue;
                }
                SmokeExpert se = new SmokeExpert();
                boolean haveFiled = false;
                for (int i = 0; i < 9; i++) {
                    Cell cell = row.getCell(i);
                    if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                        continue;
                    }
                    if (i > 0)
                        haveFiled = true;
                    String val = ExcelUtil.getCellValueForCell(cell);
                    switch (i) {
                        case 1:
                            se.setProName(val);
                            break;
                        case 2:
                            se.setReviewType(decodeType(val));
                            break;
                        case 3:
                            se.setReviewTime(sdf.parse(val));
                            break;
                        case 4:
                            se.setExpNameSkill(val);
                            break;
                        case 5:
                            se.setExpUnitSkill(val);
                            break;
                        case 6:
                            se.setExpNameManage(val);
                            break;
                        case 7:
                            se.setExpUnitManage(val);
                            break;
                        case 8:
                            se.setReviewCost(new BigDecimal(val));
                            break;
                        default:
                            break;
                    }
                }
                if (haveFiled)
                    list.add(checkSmokeExpert(se));
            }
            printJson(ResultUtil.success(list), response);
        } catch (Exception e) {
            e.printStackTrace();
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public void saveProjectBatch(HttpServletResponse response, @RequestBody List<SmokeExpert> list) {
        try {
            smokeExpertRepository.save(list);
            printJson(ResultUtil.success(null, "保存成功"), response);
        } catch (Exception e) {
            e.printStackTrace();
            printJson(ResultUtil.error(-1, "保存失败" + e.getMessage()), response);
        }
    }

    @RequestMapping(value = "query", method = RequestMethod.POST)
    public void query(HttpServletRequest request,HttpServletResponse response){
        String proName = request.getParameter("proName");
        String unitName = request.getParameter("unitName");
        String expName = request.getParameter("expName");
        String reviewType = request.getParameter("reviewType");
        String stTime = request.getParameter("stTime");
        String eTime = request.getParameter("eTime");
        Criteria<SmokeExpert> criteria = new Criteria<>();
        try {
            if(!StringUtils.isEmpty(proName))
                criteria.add(Restrictions.like("proName", proName, true));
            if(!StringUtils.isEmpty(unitName)){
                criteria.add(Restrictions.like("expUnitSkill", unitName, true));
                criteria.add(Restrictions.like("expUnitManage", unitName, true));
            }
            if(!StringUtils.isEmpty(expName)){
                criteria.add(Restrictions.like("expNameSkill", expName, true));
                criteria.add(Restrictions.like("expNameManage", expName, true));
            }
            if(!StringUtils.isEmpty(reviewType))
                criteria.add(Restrictions.eq("reviewType", reviewType, true));
            if(!StringUtils.isEmpty(stTime))
                criteria.add(Restrictions.gt("reviewTime", sdf.parse(stTime), true));//"STR_TO_DATE("+stTime+",'%Y-%m-%d')"
            if(!StringUtils.isEmpty(eTime))
                criteria.add(Restrictions.lt("reviewTime", sdf.parse(eTime), true));
            List<SmokeExpert> list = smokeExpertRepository.findAll(criteria);
            printJson(ResultUtil.success(list),response);
        }catch (Exception e){
            e.printStackTrace();
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }

    }

    @RequestMapping(value = "count")
    public ModelAndView count() {
        ModelAndView mv = new ModelAndView("expert/count");
        return mv;
    }

    @RequestMapping(value = "count/query", method = RequestMethod.POST)
    public void countQuery(HttpServletRequest request,HttpServletResponse response){
        String unitName = request.getParameter("unitName");
        String expName = request.getParameter("expName");
        String reviewType = request.getParameter("reviewType");
        String stTime = request.getParameter("stTime");
        String eTime = request.getParameter("eTime");
        StringBuffer sql = new StringBuffer("select expert_unit_skill as unit,expert_name_skill as name,count(1) as num,sum(review_cost) as cost from s_expert where 1=1 ");
        try {
            if(!StringUtils.isEmpty(unitName)){
                sql.append(" and expert_unit_skill like '%"+unitName+"%' ");
            }
            if(!StringUtils.isEmpty(expName)){
                sql.append(" and expert_name_skill like '%"+expName+"%' ");
            }
            if(!StringUtils.isEmpty(reviewType))
                sql.append(" and review_type="+reviewType);
            if(!StringUtils.isEmpty(stTime))
                sql.append(" and review_time>=STR_TO_DATE("+stTime+",'%Y-%m-%d')");
            if(!StringUtils.isEmpty(eTime))
                sql.append(" and review_time<=STR_TO_DATE("+eTime+",'%Y-%m-%d')");
            sql.append(" group by expert_unit_skill,expert_name_skill");
            List<Map<String,Object>> list = dao.findBySqlToMap(sql.toString());
            printJson(ResultUtil.success(list),response);
        }catch (Exception e){
            e.printStackTrace();
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "count/export", method = RequestMethod.GET)
    public void countExport(HttpServletRequest request,HttpServletResponse response){
        String unitName = request.getParameter("unitName");
        String expName = request.getParameter("expName");
        String reviewType = request.getParameter("reviewType");
        String stTime = request.getParameter("stTime");
        String eTime = request.getParameter("eTime");
        StringBuffer sql = new StringBuffer("select expert_unit_skill as unit,expert_name_skill as name,count(1) as num,sum(review_cost) as cost from s_expert where 1=1 ");
        try {
            if(!StringUtils.isEmpty(unitName)){
                sql.append(" and expert_unit_skill like '%"+unitName+"%' ");
            }
            if(!StringUtils.isEmpty(expName)){
                sql.append(" and expert_name_skill like '%"+expName+"%' ");
            }
            if(!StringUtils.isEmpty(reviewType))
                sql.append(" and review_type="+reviewType);
            if(!StringUtils.isEmpty(stTime))
                sql.append(" and review_time>=STR_TO_DATE("+stTime+",'%Y-%m-%d')");
            if(!StringUtils.isEmpty(eTime))
                sql.append(" and review_time<=STR_TO_DATE("+eTime+",'%Y-%m-%d')");
            sql.append(" group by expert_unit_skill,expert_name_skill");
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
                headMap.put("unit","单位");
                headMap.put("name","姓名");
                headMap.put("num","评审次数");
                headMap.put("cost","评审费用(元)");
                ExcelExportUtil.downloadExcelFile("科技项目评审专家工作量及评审费用统计表",headMap,ja,response);
            }else{
                logger.error("可导出数据为空");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/delete")
    public void delete(@RequestParam String id, HttpServletResponse response){
        try {
            smokeExpertRepository.delete(Integer.parseInt(id));
            printJson(ResultUtil.success(),response);
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }

    @RequestMapping(value = "/deleteDataArr")
    public void deleteDataArr(@RequestParam String dataIds, HttpServletResponse response){
        try {
            String[] ids = dataIds.split(",");
            for (String id: ids
                    ) {
                smokeExpertRepository.delete(smokeExpertRepository.findOne(Integer.parseInt(id)));
            }
            printJson(ResultUtil.success(),response);
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }

    private SmokeExpert checkSmokeExpert(SmokeExpert se) {
        if (se.getReviewCost() == null)
            se.setRemark("评审费用不能为空");
        if (se.getReviewType() == null)
            se.setRemark("评审类别不能为空");
        if (se.getExpNameSkill() == null || se.getExpNameManage() == null)
            se.setRemark("姓名不能为空");
        if (se.getExpUnitManage() == null || se.getExpUnitSkill() == null)
            se.setRemark("单位不能为空");
        return se;
    }

    private Integer decodeType(String str) {
        //1-立项评审 2-检查评价 3-变更评审 4-撤销评审 5-结题评审
        Integer val = 0;
        switch (str) {
            case "立项评审":
                val = 1;
                break;
            case "检查评价":
                val = 2;
                break;
            case "变更评审":
                val = 3;
                break;
            case "撤销评审":
                val = 4;
                break;
            case "结题评审":
                val = 5;
                break;
            default:
                break;
        }
        return val;
    }
}

