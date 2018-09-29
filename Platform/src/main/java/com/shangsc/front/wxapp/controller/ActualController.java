package com.shangsc.front.wxapp.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.shangsc.front.validate.bean.CommonDes;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.Company;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

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
        String meterAddress = getPara("meterAddress");

        StringBuffer sqlExceptSelect = new StringBuffer("select tad.*, tc.name as companyName,tc.real_code from " +
                " (select * from t_actual_data order by id desc )" +
                " tad left join t_company tc on " +
                "tc.inner_code=tad.inner_code " +
                "where 1=1 ");

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
}
