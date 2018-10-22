package com.shangsc.platform.controller.report;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.code.MonthCode;
import com.shangsc.platform.code.ReportTypeEnum;
import com.shangsc.platform.conf.GlobalConfig;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.export.ExportByDataTypeService;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.ActualDataReport;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.util.ToolDateTime;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author ssc
 * @Date 2017/8/21 23:06
 * @Version 1.0.0
 * @Desc
 */
public class ReportDailyChartController extends BaseController {

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/report/daily/chart"})
    public void index() {
        ActualData.me.setGlobalInnerCode(getInnerCodesSQLStr());

        String path = this.getRequest().getServletContext().getContextPath();
        String contextPath = path.equals("/") ? "" : path;

        List<ActualData> datas = ActualData.me.find("select date_format(write_time, '%Y') as yearTime from t_actual_data" +
                " group by yearTime order by yearTime asc");
        String companyTitle = "各单位用水统计图表";
        if (CollectionUtils.isNotEmpty(datas)) {
            String strYear = "";
            if (datas.size() == 1) {
                strYear = datas.get(0).get("yearTime");
            } else {
                strYear = datas.get(0).get("yearTime") + "—" + datas.get(datas.size() - 1).get("yearTime");
            }
            companyTitle = strYear + " 年 " + companyTitle;
        }
        this.setAttr("streetTitle", companyTitle);


        List<Record> records = ActualData.me.getCPADailyActualData();

        List<String> day = new ArrayList<String>();
        for (Record record : records) {
            day.add(record.get("DAY").toString());
        }
        this.setAttr("days", day);
        render("daily_report_chart.jsp");
    }


}
