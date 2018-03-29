package com.shangsc.platform.controller.statis;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.DictData;

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
        render("alarm.jsp");
    }

    @RequiresPermissions(value={"/statis/alarm"})
    public void getListData() {
        String keyword=this.getPara("name");
        Page<Company> pageInfo = Company.me.getAlarmCompanyPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        List<Company> companies = pageInfo.getList();
        setVoProp(companies);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, companies)  );
    }

    private void setVoProp(List<Company> companies) {
        if (CommonUtils.isNotEmpty(companies)) {
            Map<String, Object> mapUserType = DictData.dao.getDictMap(0, DictCode.UserType);
            Map<String, Object> mapWaterUseType = DictData.dao.getDictMap(0, DictCode.WaterUseType);
            Map<String, Object> mapUintType = DictData.dao.getDictMap(0, DictCode.UnitType);
            Map<String, Object> mapStreetType = DictData.dao.getDictMap(0, DictCode.Street);
            for (int i = 0; i < companies.size(); i++) {
                Company co = companies.get(i);
                if (co.getCustomerType() != null) {
                    co.put("customerTypeName", String.valueOf(mapUserType.get(String.valueOf(co.getCustomerType()))));
                }
                if (co.getWaterUseType() != null) {
                    co.put("waterUseTypeName", String.valueOf(mapWaterUseType.get(String.valueOf(co.getWaterUseType()))));
                }
                if (co.getUnitType() != null) {
                    co.put("unitTypeName", String.valueOf(mapUintType.get(String.valueOf(co.getUnitType()))));
                }
                if (co.getStreet() != null) {
                    co.put("streetName", String.valueOf(mapStreetType.get(String.valueOf(co.getStreet()))));
                }
                if (co.getAddress() != null) {
                    co.setAddress("<a href='#' title='点击查看导航地图' style='cursor: pointer' onclick=\"openMap('"
                            + co.get("inner_code") + "')\">" + co.getAddress() + "</a>");
                }
                companies.set(i, co);
            }
        }
    }
}
