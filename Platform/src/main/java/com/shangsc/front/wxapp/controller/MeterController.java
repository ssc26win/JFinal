package com.shangsc.front.wxapp.controller;

import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.WaterMeter;

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
        WaterMeter waterMeter = WaterMeter.me.findById(id);
        if (waterMeter != null) {
            Company byInnerCode = Company.me.findByInnerCode(waterMeter.getInnerCode());
            waterMeter.put("company", byInnerCode);
        }
        this.renderJson(waterMeter);
    }
}
