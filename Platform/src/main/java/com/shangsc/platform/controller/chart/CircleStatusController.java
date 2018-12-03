package com.shangsc.platform.controller.chart;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.code.ActualState;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.DictData;
import com.shangsc.platform.model.WaterMeter;
import com.shangsc.platform.util.CodeNumUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;

/**
 * @Author ssc
 * @Date 2018/10/9 18:07
 * @Desc 用途：
 */
public class CircleStatusController extends BaseController {

    @RequiresPermissions(value = {"/chart/circleStatus"})
    public void index() {
        render("circle_chart.jsp");
    }

    @RequiresPermissions(value = {"/chart/circleStatus"})
    public void companiesByTerm() {
        JSONObject object = new JSONObject();
        //取得用水指标数据
        int total = Company.me.totalCount();
        object.put("total", total);//单位总数

        Map<Integer, Object> termGroup = Company.me.getTermGroup();
        String contextPath = this.getRequest().getServletContext().getContextPath();
        String context_path = contextPath.equals("/") ? "" : contextPath;
        JSONArray array = new JSONArray();
        Map<String, Object> termType = DictData.dao.getDictMap(0, DictCode.Term);
        //{name:'一期' + '(' + stage1Total + ')', y:stage1Total,url:'${context_path}/basic/meter/byTerm?term=1'},
        for (Integer termKey : termGroup.keySet()) {
            JSONObject serObj = new JSONObject();
            serObj.put("name", termType.get(termKey.toString()) + "(" + termGroup.get(termKey).toString() + ")");
            serObj.put("y", (Long) termGroup.get(termKey));
            serObj.put("url", context_path + "/basic/company?term=" + termKey);
            array.add(serObj);
        }
        object.put("CompanyTermSerArray", array);
        this.renderJson(object);
    }

    @RequiresPermissions(value = {"/chart/circleStatus"})
    public void metersByTerm() {
        JSONObject object = new JSONObject();
        List<WaterMeter> waterMeters = WaterMeter.me.getAllList();
        int total = waterMeters.size();
        object.put("total", total);
        Map<Integer, Object> termGroup = WaterMeter.me.getTermGroup();
        String contextPath = this.getRequest().getServletContext().getContextPath();
        String context_path = contextPath.equals("/") ? "" : contextPath;
        JSONArray array = new JSONArray();
        Map<String, Object> termType = DictData.dao.getDictMap(0, DictCode.Term);
        //{name:'一期' + '(' + stage1Total + ')', y:stage1Total,url:'${context_path}/basic/meter/byTerm?term=1'},
        for (Integer termKey : termGroup.keySet()) {
            JSONObject serObj = new JSONObject();
            serObj.put("name", termType.get(termKey.toString()) + "(" + termGroup.get(termKey).toString() + ")");
            serObj.put("y", (Long) termGroup.get(termKey));
            serObj.put("url", context_path + "/basic/meter?term=" + termKey);
            array.add(serObj);
        }
        object.put("MeterTermSerArray", array);
        this.renderJson(object);
    }

    @RequiresPermissions(value = {"/chart/circleStatus"})
    public void company_bak() {
        JSONObject object = new JSONObject();
        //取得用水指标数据
        int total = Company.me.totalCount();
        object.put("total", total);//单位总数
        // 预警或报警用水单位
        Set<String> warnOrExceptionCompanies = Company.me.getWarnExceptionInnerCode(new Date());
        int warnOrExceptionCount = 0;
        int supplyCount = Company.me.getSupplyCompanyCount();
        if (CollectionUtils.isNotEmpty(warnOrExceptionCompanies)) {
            warnOrExceptionCount = warnOrExceptionCompanies.size();
        }
        int normalTotal = total - warnOrExceptionCount - supplyCount;

        object.put("warnTotal", warnOrExceptionCount); //预警总数
        object.put("normalTotal", normalTotal);
        object.put("supplyTotal", supplyCount);

        int month = new Date().getMonth() + 1;

        if (CodeNumUtil.isOdd(month)) {
            object.put("warnTitle", "预警");
        } else {
            object.put("warnTitle", "告警");
        }
        this.renderJson(object.toJSONString());
    }

    @RequiresPermissions(value = {"/chart/circleStatus"})
    public void company() {
        JSONObject object = new JSONObject();
        String contextPath = this.getRequest().getServletContext().getContextPath();
        String context_path = contextPath.equals("/") ? "" : contextPath;
        //取得用水指标数据
        int total = Company.me.totalCount();
        object.put("totalTitle", "单位总数量（" + total + "）");//单位总数
        Map<Integer, String> dictValMap = DictData.dao.getDictValMap(DictCode.CompanyType);

        List<Company> companies = Company.me.find("select count(company_type) as TypeCounts,company_type from t_company group by company_type");

        JSONArray array = new JSONArray();
        for (Company company : companies) {
            JSONObject obj = new JSONObject();
            obj.put("name", dictValMap.get(company.getCompanyType()) + "(" + company.getLong("TypeCounts") + ")");
            obj.put("y", company.getLong("TypeCounts"));
            obj.put("url", context_path + "/basic/company?companyType=" + company.getCompanyType());
            array.add(obj);
        }
        object.put("seriesDataArray", array);

        this.renderJson(object.toJSONString());
    }

    @RequiresPermissions(value = {"/chart/circleStatus"})
    public void meter() {
        JSONObject object = new JSONObject();
        List<WaterMeter> waterMeters = WaterMeter.me.getAllList();
        int total = waterMeters.size();
        object.put("total", total);
        // 异常水表数量24小时之内都会接收到数据 否则就异常
       /* List<Record> normalMeter = ActualData.me.getNormalMeter();
        Set<String> normalMeterSets = new HashSet<>();
        for (Record record : normalMeter) {
            String meter_address = record.getStr("meter_address");
            if (StringUtils.isNotEmpty(meter_address)) {
                normalMeterSets.add(meter_address);
            }
        }
        int normalTotal = ActualData.me.getNormalMeter().size();//正常水表
        List<Record> stopMeter = ActualData.me.getStopMeter();
        Set<String> stopMeterSets = new HashSet<>();
        for (Record record : stopMeter) {
            String meter_address = record.getStr("meter_address");
            if (StringUtils.isNotEmpty(meter_address) && !normalMeterSets.contains(meter_address)) {
                stopMeterSets.add(meter_address);
            }
        }
        int stopTotal = stopMeterSets.size();//停用水表*/
        int disableTotal = ActualData.me.getDisableMeter().size();//未启用水表

        Page<ActualData> pageInfo = new Page<>();
        int exceptionTime = 24;
        Map<String, Object> dictMap = DictData.dao.getDictMap(null, DictCode.ACTUAL_EXCEPTION_TIME_OUT);
        if (dictMap.size() == 1) {
            Object[] objects = dictMap.keySet().toArray();
            String num = objects[0].toString();
            if (NumberUtils.isDigits(num)) {
                exceptionTime = Integer.parseInt(num);
            }
        }
        int normalTotal = 0;
        int exptionTotal = 0;
        int stopTotal = 0;
        for (String status : ActualState.Actual_List()) {
            if (!ActualState.EXCEPTION.equals(status)) {
                pageInfo = ActualData.me.getActualDataStatusForCircle(1, 1, "", this.getOrderbyStr(), status, exceptionTime);
                if (ActualState.NORMAL.equals(status)) {
                    normalTotal = pageInfo.getTotalRow();
                } else if (ActualState.EXCEPTION.equals(status)) {
                    exptionTotal = pageInfo.getTotalRow();
                } else if (ActualState.STOP.equals(status)) {
                    stopTotal = pageInfo.getTotalRow();
                }
            }
        }
        object.put("normalTotal", normalTotal);
        object.put("stopTotal", stopTotal);
        object.put("disableTotal", disableTotal);
        object.put("exptionTotal", total - normalTotal - stopTotal - disableTotal);//异常水表
        this.renderJson(object.toJSONString());
    }
}