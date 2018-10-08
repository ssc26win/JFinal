package com.shangsc.front.wxapp.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jfinal.aop.Clear;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.shangsc.front.validate.bean.CommonDes;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.FileUtils;
import com.shangsc.platform.core.util.IWebUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.Image;
import com.shangsc.platform.model.SysUser;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author ssc
 * @Date 2018/9/2 10:29
 * @Version 1.0.0
 * @Desc
 */
@Clear(AuthorityInterceptor.class)
@RequiresPermissions(value = {"/"})
public class ActualController extends BaseController {

    @Clear(AuthorityInterceptor.class)
    public void findLatestData() {
        SysUser byWxAccount = findByWxAccount();
        String companyName = getPara("companyName");
        Company first = Company.me.findFirst("select * from t_company where name=?", companyName);
        if (StringUtils.isNotEmpty(companyName) && first == null) {
            CommonDes commonDes = new CommonDes();
            commonDes.setCode(-1);
            commonDes.setMessage("未找到单位名称！");
            renderJson(commonDes);
        }
        String innerCode = "";
        if (first != null) {
            innerCode = first.getInnerCode();
        }
        if (StringUtils.isEmpty(innerCode)) {
            innerCode = byWxAccount.getInnerCode();
        }
        String meterAddress = getPara("meterAddress");

        StringBuffer sqlExceptSelect = new StringBuffer("select tad.*, tc.name as companyName,tc.real_code from " +
                " (select * from t_actual_data order by id desc )" +
                " tad left join t_company tc on tc.inner_code=tad.inner_code where 1=1 ");

        if (StringUtils.isNotEmpty(innerCode)) {
            sqlExceptSelect.append(" and tad.inner_code='" + innerCode + "'");
        }
        if (StringUtils.isNotEmpty(meterAddress)) {
            sqlExceptSelect.append(" and tad.meter_address='" + meterAddress + "'");
        }

        sqlExceptSelect.append(" group by tad.inner_code,tad.meter_address");

        List<ActualData> actualDatas = ActualData.me.find(sqlExceptSelect.toString());

        //String s = JsonUtil.obj2Json(actualDatas);

        JSONArray array = new JSONArray();
        JSONObject result = new JSONObject();

        for (ActualData data : actualDatas) {
            if (StringUtils.isEmpty(companyName)) {
                companyName = data.get("companyName");
            }
            JSONObject object = new JSONObject();
            object.put("meter_address", data.get("meter_address"));
            object.put("net_water", data.get("net_water"));
            object.put("sum_water", data.get("sum_water"));
            object.put("write_time", data.get("write_time"));
            array.add(object);
        }
        result.put("companyName", companyName);
        result.put("waterUseInfo", array);
        renderJson(result);
    }

    @Clear(AuthorityInterceptor.class)
    public void daily() {
        SysUser byWxAccount = findByWxAccount();
        if (byWxAccount == null) {
            this.renderJson(InvokeResult.failure("未找到该微信账号"));
        }
        List<Record> records = ActualData.me.getWxDailyActualData(byWxAccount.getInnerCode());
        Company byInnerCode = Company.me.findByInnerCode(byWxAccount.getInnerCode());
        String subtitle = "日用水量";
        String seriesName = "日用水量";
        Integer type = 1;
        if (byInnerCode != null) {
            type = byInnerCode.getCompanyType();
            if (type == 2) {
                subtitle = "日供水量";
                seriesName = "日供水量";
            }
        }
        JSONObject obj = new JSONObject();
        JSONArray sumWater = new JSONArray();
        List<String> day = new ArrayList<String>();
        for (Record record : records) {
            sumWater.add(record.get("sumWater"));
            day.add(record.get("DAY").toString());
        }
        obj.put("sumWater", sumWater);
        obj.put("day", day);
        obj.put("subtitle", subtitle);
        obj.put("seriesName", seriesName);
        obj.put("type", type);
        this.renderJson(obj);
    }

    @Clear(AuthorityInterceptor.class)
    public void month() {
        SysUser byWxAccount = findByWxAccount();
        if (byWxAccount == null) {
            this.renderJson(InvokeResult.failure("未找到该微信账号"));
        }
        List<Record> records = ActualData.me.getWxMonthActualData(byWxAccount.getInnerCode());
        Company byInnerCode = Company.me.findByInnerCode(byWxAccount.getInnerCode());
        Integer type = 1;
        String subtitle = "月用水量";
        String seriesName = "月用水量";
        if (byInnerCode != null) {
            type = byInnerCode.getCompanyType();
            subtitle = "月用水量";
            seriesName = "月用水量";
            if (type == 2) {
                subtitle = "月供水量";
                seriesName = "月供水量";
            }
        }
        JSONObject obj = new JSONObject();
        JSONArray sumWater = new JSONArray();
        List<String> month = new ArrayList<String>();
        for (Record record : records) {
            sumWater.add(record.get("sumWater"));
            month.add(record.get("month").toString());
        }
        obj.put("sumWater", sumWater);
        obj.put("month", month);
        obj.put("subtitle", subtitle);
        obj.put("seriesName", seriesName);
        obj.put("type", type);
        this.renderJson(obj);
    }

    @Clear(AuthorityInterceptor.class)
    public void readSearchList() {
        SysUser byWxAccount = findByWxAccount();
        if (byWxAccount == null) {
            this.renderJson(InvokeResult.failure("未找到该微信账号"));
        }
    }

    @Clear(AuthorityInterceptor.class)
    public void readSearchChart() {
        SysUser byWxAccount = findByWxAccount();
        if (byWxAccount == null) {
            this.renderJson(InvokeResult.failure("未找到该微信账号"));
        }
    }
}
