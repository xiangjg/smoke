package com.jone.smoke.controller.expert;

import com.jone.smoke.controller.BaseController;
import com.jone.smoke.dao.expert.SmokeExpertRepository;
import com.jone.smoke.entity.common.ResultUtil;
import com.jone.smoke.entity.expert.SmokeExpert;
import com.jone.smoke.util.ExcelUtil;
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
import java.util.List;

@RestController
@EnableAutoConfiguration
@RequestMapping(value = "expert")
public class ExpertController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(ExpertController.class);

    @Autowired
    private SmokeExpertRepository smokeExpertRepository;

    @RequestMapping(value = "list")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("expert/list");
        //mv.addObject("title", systemProperties.getName());
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
                            //se.setReviewTime(val);
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
        List<SmokeExpert> list = smokeExpertRepository.findAll();
        printJson(ResultUtil.success(list),response);
    }


    private SmokeExpert checkSmokeExpert(SmokeExpert se) {
        if (se.getReviewCost() == null)
            se.setRemark("评审费用不能为空");
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

