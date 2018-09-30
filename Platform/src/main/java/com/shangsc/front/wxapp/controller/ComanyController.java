package com.shangsc.front.wxapp.controller;

import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.DictData;
import com.shangsc.platform.model.SysUser;
import com.shangsc.platform.model.WaterMeter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author ssc
 * @Date 2018/9/30 12:03
 * @Desc 用途：
 */
@Clear(AuthorityInterceptor.class)
@RequiresPermissions(value = {"/"})
public class ComanyController extends BaseController {

    @Clear(AuthorityInterceptor.class)
    public void findList() {
        String keyword = this.getPara("name");
        String companyType = this.getPara("companyType");
        Page<Company> pageInfo = Company.me.findWxList(getPage(), this.getRows(), keyword, companyType);
        List<Company> companies = pageInfo.getList();
        setVoProp(companies);
        this.renderJson(companies);
    }

    @Clear(AuthorityInterceptor.class)
    private void setVoProp(List<Company> companies) {
        if (CommonUtils.isNotEmpty(companies)) {
            Map<String, Object> mapUserType = DictData.dao.getDictMap(0, DictCode.UserType);
            Map<String, Object> mapWaterUseType = DictData.dao.getDictMap(0, DictCode.WaterUseType);
            Map<String, Object> mapUintType = DictData.dao.getDictMap(0, DictCode.UnitType);
            Map<String, Object> mapStreetType = DictData.dao.getDictMap(0, DictCode.Street);
            Map<String, Object> termType = DictData.dao.getDictMap(0, DictCode.Term);
            for (int i = 0; i < companies.size(); i++) {
                Company co = companies.get(i);
                if (co.getCustomerType() != null && mapUserType.size() > 0) {
                    co.put("customerTypeName", String.valueOf(mapUserType.get(String.valueOf(co.getCustomerType()))));
                }
                if (co.getWaterUseType() != null && mapWaterUseType.size() > 0) {
                    co.put("waterUseTypeName", String.valueOf(mapWaterUseType.get(String.valueOf(co.getWaterUseType()))));
                }
                if (co.getUnitType() != null && mapUintType.size() > 0) {
                    co.put("unitTypeName", String.valueOf(mapUintType.get(String.valueOf(co.getUnitType()))));
                }
                if (co.getStreet() != null && mapStreetType.size() > 0) {
                    co.put("streetName", String.valueOf(mapStreetType.get(String.valueOf(co.getStreet()))));
                }
                if (co.getTerm() != null && termType.size() > 0) {
                    co.put("termName", String.valueOf(termType.get(String.valueOf(co.getTerm()))));
                }
                companies.set(i, co);
            }
        }
    }

    @Clear(AuthorityInterceptor.class)
    public void findMeterList() {
        SysUser byWxAccount = findByWxAccount();
        String innerCode = this.getPara("innerCode");
        if (StringUtils.isEmpty(innerCode)) {
            innerCode = byWxAccount.getInnerCode();
        }
        Page<WaterMeter> pageInfo = WaterMeter.me.findWxList(getPage(), this.getRows(), innerCode);
        List<WaterMeter> waterMeters = pageInfo.getList();
        this.renderJson(waterMeters);
    }
}
