package com.shangsc.platform.controller.statis;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.code.AlarmType;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.code.MonthCode;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.DictData;
import com.shangsc.platform.util.ToolDateTime;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author ssc
 * @Date 2018/3/29 9:52
 * @Desc 用途：
 */
public class AlarmController extends BaseController {

    @RequiresPermissions(value={"/statis/alarm"})
    public void index() {
        render("alarm_company.jsp");
    }

    @RequiresPermissions(value={"/statis/alarm"})
    public void getListData() {
        String keyword=this.getPara("name");
        Map<String, String> monthDateBetween = ToolDateTime.get2MonthDateBetween(new Date());
        Page<Company> pageInfo = Company.me.getAlarmCompanyPage(getPage(), this.getRows(), keyword, this.getOrderbyStr(), monthDateBetween);
        List<Company> companies = pageInfo.getList();
        setVoProp(companies, monthDateBetween);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, companies)  );
    }

    private void setVoProp(List<Company> companies, Map<String, String> monthDateBetween) {
        if (CommonUtils.isNotEmpty(companies)) {
            Integer month = Integer.parseInt(monthDateBetween.get(MonthCode.warn_month));
            String month_str = monthDateBetween.get((MonthCode.warn_month_str));
            Integer month_target = Integer.parseInt(monthDateBetween.get(MonthCode.warn_target_month));
            String month_target_str= monthDateBetween.get(MonthCode.warn_target_month_str);
            Map<String, Object> mapWaterUseType = DictData.dao.getDictMap(0, DictCode.WaterUseType);
            Map<String, Object> mapUintType = DictData.dao.getDictMap(0, DictCode.UnitType);
            for (int i = 0; i < companies.size(); i++) {
                Company co = companies.get(i);
                if (co.getWaterUseType() != null) {
                    co.put("waterUseTypeName", String.valueOf(mapWaterUseType.get(String.valueOf(co.getWaterUseType()))));
                }
                if (co.getUnitType() != null) {
                    co.put("unitTypeName", String.valueOf(mapUintType.get(String.valueOf(co.getUnitType()))));
                }
                BigDecimal sumWater = new BigDecimal("0");
                if (co.get("sumWater") != null && StringUtils.isNotEmpty(co.get("sumWater").toString())) {
                    sumWater = new BigDecimal(co.get("sumWater").toString());
                }
                BigDecimal monthIndex = new BigDecimal("0");
                if (co.get(month_str) != null && StringUtils.isNotEmpty(co.get(month_str).toString())) {
                    monthIndex = new BigDecimal(co.get(month_str).toString());
                }
                BigDecimal monthTargetIndex = new BigDecimal("0");
                if (co.get(month_target_str) != null && StringUtils.isNotEmpty(co.get(month_target_str).toString())) {
                    monthTargetIndex = new BigDecimal(co.get(month_target_str).toString());
                }
                if (month < month_target) {
                    if (monthIndex.add(monthTargetIndex).compareTo(sumWater) > 0) {
                        co.put("alarmName", "<span style=\"color:green\">" + AlarmType.normal + "</span>");
                    } else {
                        co.put("alarmName", "<span style=\"color:#FFD306\">" + AlarmType.warn + "</span>");
                    }
                } else {
                    if (monthIndex.add(monthTargetIndex).compareTo(sumWater) > 0) {
                        co.put("alarmName", "<span style=\"color:green\">" + AlarmType.normal + "</span>");
                    } else {
                        co.put("alarmName", "<span style=\"color:red\">" + AlarmType.alarm + "</span>");
                    }
                }
                companies.set(i, co);
            }
        }
    }
}
