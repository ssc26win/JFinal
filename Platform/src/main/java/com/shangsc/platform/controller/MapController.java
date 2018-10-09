package com.shangsc.platform.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.code.MapState;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.model.*;
import com.shangsc.platform.util.CodeNumUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/8/26.
 */
public class MapController extends BaseController {

    @RequiresPermissions(value = {"/map/baidu"})
    public void index() {
        Company.me.setGlobalInnerCode(getInnerCode());
        List<Record> records = new ArrayList<>();
        JSONArray array = new JSONArray();
        String type = this.getPara("type");
        if (StringUtils.isNotEmpty(type)) {
            records = Company.me.getCompanyByType(type);
            this.setAttr("type", type);
        } else {
            String innerCode = this.getPara("innerCode");
            records = Company.me.getCompanyAll(innerCode);
        }
        Date date = new Date();
        Set<String> warnCodes = Company.me.getWarnExceptionInnerCode(date);
        int month = new Date().getMonth() + 1;
        for (Record record : records) {
            String innerCode = record.get("inner_code").toString();
            JSONObject object = new JSONObject();
            object.put("longitude", record.get("longitude"));
            object.put("latitude", record.get("latitude"));
            object.put("innerCode", record.get("inner_code"));
            object.put("state", MapState.NORMAL);
            if (warnCodes.contains(innerCode)) {
                if (CodeNumUtil.isOdd(month)) {//预警
                    object.put("state", MapState.WARN);
                } else {//告警
                    object.put("state", MapState.OVER);
                }
            }
            object.put("name", record.get("name"));
            if (record.get("waterUseNum") != null) {
                object.put("waterUseNum", record.get("waterUseNum"));
            } else {
                object.put("waterUseNum", 0);
            }
            if ("0".equals(object.get("waterUseNum").toString())) {
                object.put("waterUseNum", 0);
            }
            object.put("address", record.get("address"));
            array.add(object);
        }
        if (CollectionUtils.isNotEmpty(records) && records.size() == 1) {
            String centerPosition = records.get(0).get("longitude") + "," + records.get(0).get("latitude");
            this.setAttr("position", centerPosition);
        }
        this.setAttr("companys", array.toJSONString());
        render("map.jsp");
    }

}
