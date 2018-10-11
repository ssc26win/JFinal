package com.shangsc.platform.controller.report;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.code.ReportColType;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.model.DictData;

import java.util.Map;

/**
 * @Author ssc
 * @Date 2018/10/11 15:44
 * @Desc 用途：
 */
public class ReportCompanyChartController extends BaseController {

    @RequiresPermissions(value = {"/report/company/chart"})
    public void index() {
        JSONArray array = new JSONArray();

        render("company_chart.jsp");
    }
}
