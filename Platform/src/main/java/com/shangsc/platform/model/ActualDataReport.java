package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.code.MonthCode;
import com.shangsc.platform.code.ReportColType;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.model.base.BaseActualData;
import com.shangsc.platform.util.ToolDateTime;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @Author ssc
 * @Date 2018/9/6 14:28
 * @Desc 用途：
 */
public class ActualDataReport extends BaseActualData<ActualData> {

    public static final ActualDataReport me = new ActualDataReport();//status : 正常 异常 停用
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String globalInnerCode;

    public void setGlobalInnerCode(String globalInnerCode) {
        this.globalInnerCode = globalInnerCode;
    }

    public Page<Company> getStreet(int pageNo, int pageSize, String orderbyStr, Integer street, Integer watersType, String type) {
        String select = " select tc.street,twm.waters_type ";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_water_meter twm " +
                " inner join t_company tc on twm.inner_code=tc.inner_code " +
                " where (tc.street<>'' or tc.street is not null) and (twm.waters_type<>'' or twm.waters_type is not null) ");
        if (StringUtils.isNotEmpty(globalInnerCode)) {
            sqlExceptSelect.append(" and tc.inner_code='" + StringUtils.trim(globalInnerCode) + "' ");
        }
        if (street != null && street > 0) {
            sqlExceptSelect.append(" and tc.street=" + street);
        }
        if (StringUtils.isNotEmpty(type)) {
            type = StringUtils.trim(type);
            if (StringUtils.isNotEmpty(type)) {
                sqlExceptSelect.append(" and tc.company_type=" + type + " ");
            }
        }
        if (watersType != null) {
            sqlExceptSelect.append(" and twm.waters_type=" + watersType);
        }
        sqlExceptSelect.append(" group by tc.street,twm.waters_type ");
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        } else {
            sqlExceptSelect.append("order by tc.street asc");
        }
        logger.info("--- 所属乡镇用水量sql开始 ---");
        logger.info(select);
        logger.info(sqlExceptSelect.toString());
        logger.info("--- 所属乡镇用水量sql结束 ---");
        return Company.me.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }

    public Map<String, String> getYearColumns() {
        String sql = "select date_format(tad.write_time, '%Y') as years from (select write_time from t_actual_data order by write_time asc) tad " +
                " GROUP BY date_format(tad.write_time, '%Y')";
        List<Record> records = Db.find(sql);
        Map<String, String> years = new LinkedHashMap<>();
        for (Record record : records) {
            if (StringUtils.isNotEmpty(record.getStr("years"))) {
                years.put(record.getStr("years"), ReportColType.year_col + record.getStr("years"));
            }
        }
        return years;
    }

    public Map<String, String> getMonthColumns() {
        Calendar date = Calendar.getInstance();
        Integer year = Integer.valueOf(date.get(Calendar.YEAR));
        String start = DateUtils.formatDate(DateUtils.getStrDate(String.valueOf(year) + "-01-01 00:00:00"));
        String end = DateUtils.formatDate(DateUtils.getStrDate(String.valueOf(year + 1) + "-01-01 00:00:00"));
        String sql = "select date_format(tad.write_time, '%Y-%m') as months from (select write_time from t_actual_data order by write_time asc) tad " +
                "where tad.write_time >='" + start + "' and tad.write_time <='" + end + "'" +
                "GROUP BY date_format(tad.write_time, '%Y-%m')";
        List<Record> records = Db.find(sql);

        Map<String, String> months = new LinkedHashMap<>();
        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            if (StringUtils.isNotEmpty(record.getStr("months"))) {
                months.put(record.getStr("months"), ReportColType.month_col + record.getStr("months"));
            }
        }
        return months;
    }

    public Map<String, String> getDayColumns() {
        Map<String, String> monthDateStartAndEnd = ToolDateTime.getMonthDateStartAndEnd(new Date());
        String start = monthDateStartAndEnd.get(MonthCode.warn_start_date);
        String end = monthDateStartAndEnd.get(MonthCode.warn_end_date);
        String sql = "select date_format(tad.write_time, '%Y-%m-%d') as days from (select write_time from t_actual_data order by write_time asc) tad " +
                "where tad.write_time >='" + start + "' and tad.write_time <='" + end + "'" +
                "GROUP BY date_format(tad.write_time, '%Y-%m-%d')";
        List<Record> records = Db.find(sql);
        Map<String, String> days = new LinkedHashMap<>();
        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            if (StringUtils.isNotEmpty(record.getStr("days"))) {
                days.put(record.getStr("days"), ReportColType.day_col + record.getStr("days"));
            }
        }
        return days;
    }

    public Page<Company> getCompanies(int pageNo, int pageSize, String orderbyStr, String name, String innerCode, String type) {
        String select = "select tc.name as companyName,tc.inner_code,tc.real_code ";

        StringBuffer sqlExceptSelect = new StringBuffer(" from t_company tc where 1=1 ");
        if (StringUtils.isNotEmpty(name)) {
            name = StringUtils.trim(name);
            if (StringUtils.isNotEmpty(name)) {
                sqlExceptSelect.append(" and tc.name like '%" + name + "%'");
            }
        }
        if (StringUtils.isNotEmpty(globalInnerCode)) {
            sqlExceptSelect.append(" and tc.inner_code='" + StringUtils.trim(globalInnerCode) + "' ");
        } else {
            if (StringUtils.isNotEmpty(innerCode)) {
                sqlExceptSelect.append(" and tc.inner_code='" + StringUtils.trim(innerCode) + "' ");
            }
        }
        if (StringUtils.isNotEmpty(type)) {
            type = StringUtils.trim(type);
            if (StringUtils.isNotEmpty(type)) {
                sqlExceptSelect.append(" and tc.company_type=" + type + " ");
            }
        }
        sqlExceptSelect.append(" group by tc.inner_code");
        sqlExceptSelect.append(" order by tc.inner_code asc");
        logger.info("--- 单位用水量sql开始 ---");
        logger.info(select);
        logger.info(sqlExceptSelect.toString());
        logger.info("--- 单位用水量sql结束 ---");
        return Company.me.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }
}
