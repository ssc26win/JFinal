package com.shangsc.front.wxapp.controller;

import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.DictData;
import com.shangsc.platform.model.WaterMeter;

import java.util.Map;

/**
 * @Author ssc
 * @Date 2018/10/27 17:13
 * @Version 1.0.0
 * @Desc
 */
public class MeterController extends BaseController {

    @Clear(AuthorityInterceptor.class)
    public void findMeterList() {
        String keyword = this.getPara("name");
        String innerCode = this.getPara("innerCode");
        Page<WaterMeter> pageInfo = WaterMeter.me.findWxList(getPage(), this.getRows(), keyword, getWxInnerCodeSQLStr(), innerCode);
        this.renderJson(pageInfo);
    }

    @Clear(AuthorityInterceptor.class)
    public void findById() {
        Long id = this.getParaToLong("id");
        WaterMeter co = WaterMeter.me.findById(id);
        if (co != null) {
            Company byId = Company.me.findByInnerCode(co.getInnerCode());
            if (byId != null) {
                Map<String, Object> mapWaterUseType = DictData.dao.getDictMap(0, DictCode.WaterUseType);
                Map<String, Object> mapUserType = DictData.dao.getDictMap(0, DictCode.UserType);
                Map<String, Object> mapUintType = DictData.dao.getDictMap(0, DictCode.UnitType);
                Map<String, Object> mapStreetType = DictData.dao.getDictMap(0, DictCode.Street);
                Map<String, Object> termType = DictData.dao.getDictMap(0, DictCode.Term);
                if (byId.getCustomerType() != null && mapUserType.size() > 0) {
                    byId.put("customerTypeName", String.valueOf(mapUserType.get(String.valueOf(byId.getCustomerType()))));
                }
                if (byId.getWaterUseType() != null && mapWaterUseType.size() > 0) {
                    byId.put("waterUseTypeName", String.valueOf(mapWaterUseType.get(String.valueOf(byId.getWaterUseType()))));
                }
                if (byId.getUnitType() != null && mapUintType.size() > 0) {
                    byId.put("unitTypeName", String.valueOf(mapUintType.get(String.valueOf(byId.getUnitType()))));
                }
                if (byId.getStreet() != null && mapStreetType.size() > 0) {
                    byId.put("streetName", String.valueOf(mapStreetType.get(String.valueOf(byId.getStreet()))));
                }
                if (byId.getTerm() != null && termType.size() > 0) {
                    byId.put("termName", String.valueOf(termType.get(String.valueOf(byId.getTerm()))));
                }
            }
            co.put("company", byId);
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);
            Map<String, Object> mapChargeType = DictData.dao.getDictMap(0, DictCode.ChargeType);
            Map<String, Object> meterAttrType = DictData.dao.getDictMap(0, DictCode.MeterAttr);
            Map<String, Object> termType = DictData.dao.getDictMap(0, DictCode.Term);
            if (co.getWatersType() != null && mapWatersType.size() > 0) {
                co.put("watersTypeName", String.valueOf(mapWatersType.get(String.valueOf(co.getWatersType()))));
            }
            if (co.getChargeType() != null && mapChargeType.size() > 0) {
                co.put("chargeTypeName", String.valueOf(mapChargeType.get(String.valueOf(co.getChargeType()))));
            }
            if (co.getMeterAttr() != null && meterAttrType.size() > 0) {
                co.put("meterAttrName", String.valueOf(meterAttrType.get(String.valueOf(co.getMeterAttr()))));
            }
            if (co.getTerm() != null && termType.size() > 0) {
                co.put("termName", String.valueOf(termType.get(String.valueOf(co.getTerm()))));
            }

        }
        this.renderJson(co);
    }
}
