package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.code.MonthCode;
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
public class ActualDataResport extends BaseActualData<ActualData> {

    public static final ActualDataResport me = new ActualDataResport();//status : 正常 异常 停用
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String globalInnerCode;

    public void setGlobalInnerCode(String globalInnerCode) {
        this.globalInnerCode = globalInnerCode;
    }

    public Page<ActualData> getStreet(int pageNo, int pageSize, String orderbyStr, Integer street, Integer watersType, String type) {
        /*select
        tc.inner_code,tc.name,tc.real_code,tc.address,tc.water_unit,tc.county,tc.company_type,
				twm.waters_type,twm.meter_attr,twm.meter_num,twm.line_num,twm.meter_address,
				date_format(tad.write_time, '%Y') as years,sum(tad.net_water) as yearTotal

		from t_actual_data tad
		inner join t_company tc on tad.inner_code=tc.inner_code
		inner join t_water_meter twm on tad.meter_address=twm.meter_address
		where 1=1

		and tc.name like '%潞洲水务有限公司%'
		and tad.write_time >= '2017-01-01 00:00:00' and tad.write_time < '2019-01-01 00:00:00'
		group by tad.meter_address,date_format(tad.write_time, '%Y')
		order by years desc,tad.meter_address desc*/
        String select = " select tc.inner_code,tc.name,tc.real_code,tc.address,tc.water_unit,tc.county,tc.company_type, " +
                " twm.waters_type,twm.meter_attr,twm.meter_num,twm.line_num,twm.meter_address, " +
                " date_format(tad.write_time, '%Y') as years,sum(abs(tad.net_water)) as yearTotal ";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data tad " +
                " inner join t_company tc on tad.inner_code=tc.inner_code " +
                " inner join t_water_meter twm on tad.meter_address=twm.meter_address " +
                " where 1=1 ");
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
        sqlExceptSelect.append(" group by tad.meter_address,date_format(tad.write_time, '%Y') ");
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        } else {
            sqlExceptSelect.append("order by date_format(tad.write_time, '%Y') desc,tad.meter_address desc");
        }
        logger.info("--- 所属乡镇用水量sql开始 ---");
        logger.info(select);
        logger.info(sqlExceptSelect.toString());
        logger.info("--- 所属乡镇用水量sql结束 ---");
        return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }

    public List<String> getYearColumns() {
        String sql = "select date_format(write_time, '%Y') as years from t_actual_data " +
                "GROUP BY date_format(write_time, '%Y') order by  date_format(write_time, '%Y')";
        List<Record> records = Db.find(sql);
        List<String> years = new ArrayList<>();
        for (Record record : records) {
            if (StringUtils.isNotEmpty(record.getStr("years"))) {
                years.add(record.getStr("years"));
            }
        }
        return years;
    }

    public List<String> getMonthColumns() {
        Calendar date = Calendar.getInstance();
        Integer year = Integer.valueOf(date.get(Calendar.YEAR));
        String start = DateUtils.formatDate(DateUtils.getStrDate(String.valueOf(year) + "-01-01 00:00:00"));
        String end = DateUtils.formatDate(DateUtils.getStrDate(String.valueOf(year + 1) + "-01-01 00:00:00"));
        String sql = "select date_format(write_time, '%Y-%m') as months from t_actual_data " +
                "where write_time >='" + start + "' and write_time <='" + end + "'" +
                "GROUP BY date_format(write_time, '%Y-%m') order by date_format(write_time, '%Y-%m')";
        List<Record> records = Db.find(sql);
        List<String> months = new ArrayList<>();
        for (Record record : records) {
            if (StringUtils.isNotEmpty(record.getStr("months"))) {
                months.add(record.getStr("months"));
            }
        }
        return months;
    }

    public List<String> getDayColumns() {
        Map<String, String> monthDateStartAndEnd = ToolDateTime.getMonthDateStartAndEnd(new Date());
        String start = monthDateStartAndEnd.get(MonthCode.warn_start_date);
        String end = monthDateStartAndEnd.get(MonthCode.warn_end_date);
        String sql = "select date_format(write_time, '%Y-%m-%d') as days from t_actual_data " +
                "where write_time >='" + start + "' and write_time <='" + end + "'" +
                "GROUP BY date_format(write_time, '%Y-%m-%d') order by date_format(write_time, '%Y-%m-%d')";
        List<Record> records = Db.find(sql);
        List<String> days = new ArrayList<>();
        for (Record record : records) {
            if (StringUtils.isNotEmpty(record.getStr("days"))) {
                days.add(record.getStr("days"));
            }
        }
        return days;
    }

    public Page<ActualData> getYear(int pageNo, int pageSize, String orderbyStr, String name, String innerCode, String type) {
        /**
         * select tc.name as companyName,tad.inner_code,date_format(tad.write_time, '%Y') as Years, sum(tad.net_water) as YearTotal from t_actual_data tad
         * inner join t_company tc on tc.inner_code=tad.inner_code group by inner_code,date_format(tad.write_time, '%Y') order by tad.inner_code asc,Years asc;
         *
         * */
        String yearSql = "select tad.inner_code,date_format(tad.write_time, '%Y') as Years, sum(tad.net_water) as YearTotal from t_actual_data tad " +
                "group by tad.inner_code,date_format(tad.write_time, '%Y') order by tad.inner_code asc,Years asc";
        List<Record> records = Db.find(yearSql);

        String select = " select tc.name as companyName,tc.inner_code ";

        StringBuffer sqlExceptSelect= new StringBuffer("from t_company tc where 1=1 ");
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
        sqlExceptSelect.append("order by tc.inner_code asc");
        logger.info("--- 单位年用水量sql开始 ---");
        logger.info(select);
        logger.info(sqlExceptSelect.toString());
        logger.info("--- 单位年用水量sql结束 ---");
        return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }


    public Page<ActualData> getMonth(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime, String name, String innerCode, String type) {
        /**/
        String select = " select tc.inner_code,tc.name,tc.real_code,tc.address,tc.water_unit,tc.county,tc.company_type," +
                "twm.waters_type,twm.meter_attr,twm.meter_num,twm.line_num,twm.meter_address," +
                "date_format(tad.write_time, '%Y-%m') as months,sum(abs(tad.net_water)) as monthTotal";

        StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data tad " +
                " inner join t_company tc on tad.inner_code=tc.inner_code " +
                " inner join t_water_meter twm on tad.meter_address=twm.meter_address " +
                " where 1=1 ");

        if (StringUtils.isNotEmpty(name)) {
            name = StringUtils.trim(name);
            if (StringUtils.isNotEmpty(name)) {
                sqlExceptSelect.append(" and tc.name like '%" + name + "%'");
            }
        }
        if (StringUtils.isNotEmpty(type)) {
            type = StringUtils.trim(type);
            if (StringUtils.isNotEmpty(type)) {
                sqlExceptSelect.append(" and tc.company_type=" + type + " ");
            }
        }
        if (StringUtils.isNotEmpty(globalInnerCode)) {
            sqlExceptSelect.append(" and tc.inner_code='" + StringUtils.trim(globalInnerCode) + "' ");
        } else {
            if (StringUtils.isNotEmpty(innerCode)) {
                sqlExceptSelect.append(" and tc.inner_code='" + StringUtils.trim(innerCode) + "' ");
            }
        }
        if (startTime != null) {
            sqlExceptSelect.append(" and tad.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "'");
        }
        if (endTime != null) {
            sqlExceptSelect.append(" and tad.write_time < '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "'");
        }

        sqlExceptSelect.append(" group by tad.meter_address,date_format(tad.write_time, '%Y-%m') ");
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        } else {
            sqlExceptSelect.append(" order by date_format(tad.write_time, '%Y-%m') desc,tad.meter_address desc ");
        }
        logger.info("--- 单位月用水量sql开始 ---");
        logger.info(select);
        logger.info(sqlExceptSelect.toString());
        logger.info("--- 单位月用水量sql结束 ---");
        return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }

    public Page<ActualData> getDaily(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime, String name,  String innerCode, String type) {
        /**/
        String select = " select abs(tad.net_water) as absNetWater,tc.name,tc.real_code,tc.inner_code,tc.address,tc.water_unit,tc.county,tc.company_type," +
                "twm.waters_type,twm.meter_attr,twm.meter_address,twm.meter_num,twm.line_num,twm.billing_cycle," +
                "date_format(tad.write_time, '%Y-%m-%d') as todays ";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data tad " +
                " inner join t_company  tc on tad.inner_code=tc.inner_code " +
                " inner join t_water_meter twm on tad.meter_address=twm.meter_address " +
                " where 1=1 ");
        if (StringUtils.isNotEmpty(name)) {
            name = StringUtils.trim(name);
            if (StringUtils.isNotEmpty(name)) {
                sqlExceptSelect.append(" and tc.name like '%" + name + "%' ");
            }
        }
        if (StringUtils.isNotEmpty(type)) {
            type = StringUtils.trim(type);
            if (StringUtils.isNotEmpty(type)) {
                sqlExceptSelect.append(" and tc.company_type=" + type + " ");
            }
        }
        if (StringUtils.isNotEmpty(globalInnerCode)) {
            sqlExceptSelect.append(" and tc.inner_code='" + StringUtils.trim(globalInnerCode) + "' ");
        } else {
            if (StringUtils.isNotEmpty(innerCode)) {
                sqlExceptSelect.append(" and tc.inner_code='" + StringUtils.trim(innerCode) + "' ");
            }
        }
        if (startTime != null) {
            sqlExceptSelect.append(" and tad.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' ");
        }
        if (endTime != null) {
            sqlExceptSelect.append(" and tad.write_time < '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "' ");
        }
        sqlExceptSelect.append(" group by tad.meter_address,date_format(tad.write_time, '%Y-%m-%d') ");
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        } else {
            sqlExceptSelect.append(" order by date_format(tad.write_time, '%Y-%m-%d') desc,tad.meter_address desc ");
        }
        logger.info("--- 单位日用水量sql开始 ---");
        logger.info(select);
        logger.info(sqlExceptSelect.toString());
        logger.info("--- 单位日用水量sql结束 ---");
        return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }
}
