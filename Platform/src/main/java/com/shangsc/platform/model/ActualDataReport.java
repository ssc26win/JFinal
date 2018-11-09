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
            sqlExceptSelect.append(" and tc.inner_code in (" + StringUtils.trim(globalInnerCode) + ") ");
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

    public Page<Company> getCompany(int pageNo, int pageSize, String orderbyStr, Integer street, String name, String innerCode, Integer watersType, String type) {
        String select = " select tc.water_unit,tc.name,tc.inner_code,tc.real_code,tc.street,twm.waters_type ";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_water_meter twm " +
                " inner join t_company tc on twm.inner_code=tc.inner_code " +
                " where (tc.street<>'' or tc.street is not null) and (twm.waters_type<>'' or twm.waters_type is not null) ");
        if (StringUtils.isNotEmpty(globalInnerCode)) {
            sqlExceptSelect.append(" and tc.inner_code in (" + StringUtils.trim(globalInnerCode) + ") ");
        }
        if (StringUtils.isNotEmpty(innerCode)) {
            sqlExceptSelect.append(" and tc.real_code='" + StringUtils.trim(innerCode) + "' ");
        }
        if (street != null && street > 0) {
            sqlExceptSelect.append(" and tc.street=" + street);
        }
        if (StringUtils.isNotEmpty(name)) {
            sqlExceptSelect.append(" and tc.name='" + name + "'");
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
        sqlExceptSelect.append(" group by tc.inner_code,twm.waters_type ");
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        } else {
            sqlExceptSelect.append("order by tc.inner_code asc");
        }
        logger.info("--- 所属单位用水量sql开始 ---");
        logger.info(select);
        logger.info(sqlExceptSelect.toString());
        logger.info("--- 所属单位用水量sql结束 ---");
        return Company.me.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }

    public Map<String, String> getYearColumns(Date startTime, Date endTime) {
        String sql = "select date_format(tad.write_time, '%Y') as years from (select write_time from t_actual_data order by write_time asc) tad " +
                " where 1=1 " +

                (startTime != null ? " and tad.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : " ") +
                (endTime != null ? " and tad.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : " ") +

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

    public Map<String, String> getMonthColumns(Date startTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        Integer year = Integer.valueOf(date.get(Calendar.YEAR));
        String start = String.valueOf(year) + "-01-01 00:00:00";
        String end = String.valueOf(year + 1) + "-01-01 00:00:00";

        String sql = "select date_format(tad.write_time, '%Y-%m') as months from (select write_time from t_actual_data order by write_time asc) tad " +
                " where 1=1 " +

                (startTime != null ? " and tad.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : " and tad.write_time >='" + start + "'") +
                (endTime != null ? " and tad.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : " and tad.write_time <'" + end + "'") +

                " GROUP BY date_format(tad.write_time, '%Y-%m')";
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

    public Map<String, String> getDayColumns(Date startTime, Date endTime) {
        Map<String, String> monthDateStartAndEnd = ToolDateTime.getMonthDateStartAndEnd(new Date());
        String start = monthDateStartAndEnd.get(MonthCode.warn_start_date);
        String end = monthDateStartAndEnd.get(MonthCode.warn_end_date);
        String sql = "select date_format(tad.write_time, '%Y-%m-%d') as days from (select write_time from t_actual_data order by write_time asc) tad " +
                " where 1=1 " +

                (startTime != null ? " and tad.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : " and tad.write_time >='" + start + "'") +
                (endTime != null ? " and tad.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : " and tad.write_time <='" + end + "'") +

                " GROUP BY date_format(tad.write_time, '%Y-%m-%d')";
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

    public Page<Company> getCompanies(int pageNo, int pageSize, String orderbyStr, Integer street, String name, String innerCode, String type) {
        String select = "select tc.name as companyName,tc.inner_code,tc.real_code ";

        StringBuffer sqlExceptSelect = new StringBuffer(" from t_company tc where 1=1 ");
        if (StringUtils.isNotEmpty(name)) {
            name = StringUtils.trim(name);
            if (StringUtils.isNotEmpty(name)) {
                sqlExceptSelect.append(" and tc.name like '%" + name + "%'");
            }
        }
        if (StringUtils.isNotEmpty(globalInnerCode)) {
            sqlExceptSelect.append(" and tc.inner_code in (" + StringUtils.trim(globalInnerCode) + ") ");
        }
        if (StringUtils.isNotEmpty(innerCode)) {
            sqlExceptSelect.append(" and tc.real_code='" + StringUtils.trim(innerCode) + "' ");
        }
        if (StringUtils.isNotEmpty(type)) {
            type = StringUtils.trim(type);
            if (StringUtils.isNotEmpty(type)) {
                sqlExceptSelect.append(" and tc.company_type=" + type + " ");
            }
        }
        if (street != null) {
            sqlExceptSelect.append(" and tc.street=" + street + " ");
        }
        sqlExceptSelect.append(" group by tc.inner_code");
        sqlExceptSelect.append(" order by tc.inner_code asc");
        logger.info("--- 单位用水量sql开始 ---");
        logger.info(select);
        logger.info(sqlExceptSelect.toString());
        logger.info("--- 单位用水量sql结束 ---");
        return Company.me.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }


    public List<Record> getCPADailyActualData(String name, String innerCode, Integer street, Date startTime, Date endTime,
                                              Integer watersType, Integer meterAttr, String type) {
        Map<String, String> map = ToolDateTime.getBefore30DateTime();
        String start = map.get(MonthCode.warn_start_date);
        String end = map.get(MonthCode.warn_end_date);
        String sql = "select COALESCE(sum(t.net_water), 0) as sumWater,date_format(t.write_time, '%Y-%m-%d') as DAY,t.* from t_actual_data t" +
                " inner join (select name,inner_code,real_code,street,company_type from t_company) tc on tc.inner_code=t.inner_code " +
                " inner join (select waters_type,meter_attr,meter_address from t_water_meter) twm on twm.meter_address=t.meter_address " +
                " where " + (StringUtils.isNotEmpty(globalInnerCode) ? " t.inner_code in (" + globalInnerCode + ") " : " 1=1 ") +
                (StringUtils.isNotEmpty(name) ? " and tc.name='" + name + "'" : "") +
                (StringUtils.isNotEmpty(innerCode) ? " and tc.real_code='" + innerCode + "'" : "") +
                (street != null ? " and tc.street=" + street : "") +
                (startTime != null ? " and t.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : " and t.write_time >='" + start + "'") +
                (endTime != null ? " and t.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : " and t.write_time <='" + end + "'") +
                (type != null ? " and tc.company_type=" + type : "") +
                (meterAttr != null ? " and twm.meter_attr=" + meterAttr : "") +
                (watersType != null ? " and twm.waters_type=" + watersType : "") +
                " GROUP BY date_format(t.write_time, '%Y-%m-%d')";
        return Db.find(sql);
    }

    public List<Record> getCPAMonthActualData(String name, String innerCode, Integer street, Date startTime, Date endTime,
                                              Integer watersType, Integer meterAttr, String type) {
        Map<String, String> map = ToolDateTime.getBefore12MonthDateTime();
        String start = map.get(MonthCode.warn_start_date);
        String end = map.get(MonthCode.warn_end_date);
        String sql = "select COALESCE(sum(t.net_water), 0) as sumWater,date_format(t.write_time, '%Y-%m') as month,t.* from t_actual_data t" +
                " inner join (select name,inner_code,real_code,street,company_type from t_company) tc on tc.inner_code=t.inner_code " +
                " inner join (select waters_type,meter_attr,meter_address from t_water_meter) twm on twm.meter_address=t.meter_address " +
                " where " + (StringUtils.isNotEmpty(globalInnerCode) ? " t.inner_code in (" + globalInnerCode + ") " : " 1=1 ") +

                (StringUtils.isNotEmpty(name) ? " and tc.name='" + name + "'" : "") +
                (StringUtils.isNotEmpty(innerCode) ? " and tc.real_code='" + innerCode + "'" : "") +
                (street != null ? " and tc.street=" + street : "") +
                (startTime != null ? " and t.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : " and t.write_time >='" + start + "'") +
                (endTime != null ? " and t.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : " and t.write_time <='" + end + "'") +
                (type != null ? " and tc.company_type=" + type : "") +
                (meterAttr != null ? " and twm.meter_attr=" + meterAttr : "") +
                (watersType != null ? " and twm.waters_type=" + watersType : "") +

                " GROUP BY date_format(t.write_time, '%Y-%m')";
        return Db.find(sql);
    }

    public List<Record> getCPAYearActualData(String name, String innerCode, Integer street, Date startTime, Date endTime,
                                             Integer watersType, Integer meterAttr, String type) {
        String sql = "select COALESCE(sum(t.net_water), 0) as sumWater,date_format(t.write_time, '%Y') as year,t.* from t_actual_data t" +
                " inner join (select name,inner_code,real_code,street,company_type from t_company) tc on tc.inner_code=t.inner_code " +
                " inner join (select waters_type,meter_attr,meter_address from t_water_meter) twm on twm.meter_address=t.meter_address " +
                " where " + (StringUtils.isNotEmpty(globalInnerCode) ? " t.inner_code in (" + globalInnerCode + ") " : " 1=1 ") +

                (StringUtils.isNotEmpty(name) ? " and tc.name='" + name + "'" : "") +
                (StringUtils.isNotEmpty(innerCode) ? " and tc.real_code='" + innerCode + "'" : "") +
                (street != null ? " and tc.street=" + street : "") +
                (startTime != null ? " and t.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' " : " ") +
                (endTime != null ? " and t.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' " : " ") +
                (type != null ? " and tc.company_type=" + type : "") +
                (meterAttr != null ? " and twm.meter_attr=" + meterAttr : "") +
                (watersType != null ? " and twm.waters_type=" + watersType : "") +

                " GROUP BY date_format(t.write_time, '%Y')";
        return Db.find(sql);
    }


}
