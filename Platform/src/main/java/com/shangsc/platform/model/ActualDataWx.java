package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.code.MonthCode;
import com.shangsc.platform.model.base.BaseActualData;
import com.shangsc.platform.util.ToolDateTime;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class ActualDataWx extends BaseActualData<ActualDataWx> {

    public static final ActualDataWx me = new ActualDataWx();
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Page<ActualData> getWxActualDataPage(int page, int rows, String keyword, String orderbyStr, String globalInnerCode) {
        String select = "select * ";
        StringBuffer sqlExceptSelect = new StringBuffer(" from (" +
                "select tad.*,tc.name as companyName,tc.real_code,tc.water_unit,tc.county,twm.line_num,twm.waters_type from (select t.* from (select id,inner_code,meter_address,net_water,sum_water,write_time from t_actual_data order by write_time desc) t group by t.meter_address)  tad " +
                "left join t_company tc on tad.inner_code=tc.inner_code " +
                "left join t_water_meter twm on tad.meter_address=twm.meter_address where 1=1) alld ");
        sqlExceptSelect.append(" where 1=1");
        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (alld.real_code='" + StringUtils.trim(keyword) + "' or alld.meter_address='" + StringUtils.trim(keyword)
                    + "' or alld.companyName like '%" + StringUtils.trim(keyword) + "%') ");
        }
        if (StringUtils.isNotEmpty(globalInnerCode)) {
            sqlExceptSelect.append(" and alld.inner_code in (" + StringUtils.trim(globalInnerCode) + ") ");
        }
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        return ActualData.me.paginate(page, rows, select, sqlExceptSelect.toString());
    }


    /**********************************
     * WxApp use
     ***************************************/

    public Page<ActualData> getWxActualDataPageAll(int page, int rows, String keyword, String orderbyStr, String globalInnerCode) {
        String select = "select * ";
        StringBuffer sqlExceptSelect = new StringBuffer(" from (" +
                "select tad.*,tc.name as companyName,tc.real_code,tc.water_unit,tc.county,twm.line_num,twm.waters_type,twm.memo,twm.vender from " +
                "(select t.* from (select id,inner_code,meter_address,net_water,sum_water,state,write_time from t_actual_data" +
                " where " + (StringUtils.isNotEmpty(globalInnerCode) ? "inner_code in (" + StringUtils.trim(globalInnerCode) + ") " : "1=1") + " order by write_time desc) t group by t.meter_address) tad " +
                "left join  t_company tc on tad.inner_code=tc.inner_code " +
                "left join t_water_meter twm on tad.meter_address=twm.meter_address where 1=1 " +
                "union all (SELECT tad.id,tc.inner_code,tm.meter_address,tad.net_water,tad.sum_water,tad.state," +
                "tad.write_time,companyName,tc.real_code,tc.water_unit,tc.county,tm.line_num,tm.waters_type,tm.memo,tm.vender FROM t_water_meter tm " +
                "left join (select name as companyName,water_unit,county,inner_code,real_code from t_company) tc on tm.inner_code=tc.inner_code " +
                "left join t_actual_data tad on tad.meter_address=tm.meter_address " +
                "where tm.meter_address not in (select meter_address from t_actual_data))) alld");
        sqlExceptSelect.append(" where 1=1");
        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (alld.real_code='" + StringUtils.trim(keyword) + "' or alld.meter_address='" + StringUtils.trim(keyword)
                    + "' or alld.companyName like '%" + StringUtils.trim(keyword) + "%') ");
        }
        if (StringUtils.isNotEmpty(globalInnerCode)) {
            sqlExceptSelect.append(" and alld.inner_code in (" + StringUtils.trim(globalInnerCode) + ") ");
        }
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        return ActualData.me.paginate(page, rows, select, sqlExceptSelect.toString());
    }

    public Page<ActualData> getWxActualDataPageByDisable(int page, int rows, String keyword, String orderbyStr, String globalInnerCode) {
        String select = "select * ";
        StringBuffer sqlExceptSelect = new StringBuffer("from (SELECT tad.id,tc.inner_code,tc.real_code,tm.meter_address,tad.net_water,tad.sum_water,tad.state," +
                "tad.write_time,companyName,tc.water_unit,tc.county,tm.line_num,tm.waters_type,tm.memo,tm.vender FROM t_water_meter tm " +
                "left join (select name as companyName,water_unit,county,inner_code,real_code from t_company) tc on tm.inner_code=tc.inner_code " +
                "left join (select * from t_actual_data where 1<>1) tad on tad.meter_address=tm.meter_address " +
                "where tm.meter_address not in (select meter_address from t_actual_data)) alld ");
        sqlExceptSelect.append(" where 1=1");
        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (alld.real_code='" + StringUtils.trim(keyword) + "' or alld.meter_address='" + StringUtils.trim(keyword)
                    + "' or alld.companyName like '%" + StringUtils.trim(keyword) + "%') ");
        }
        if (StringUtils.isNotEmpty(globalInnerCode)) {
            sqlExceptSelect.append(" and alld.inner_code in (" + StringUtils.trim(globalInnerCode) + ") ");
        }
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        return ActualData.me.paginate(page, rows, select, sqlExceptSelect.toString());
    }

    public Page<ActualData> getWxActualDataPageByStatus(int page, int rows, String keyword, String orderbyStr, String status, int exceptionTime, String globalInnerCode) {
        //select * from (select * from t_actual_data order by write_time desc) a group by a.meter_address order by write_time desc
        String select = "select * ";
        StringBuffer sqlExceptSelect = new StringBuffer("from (" +
                "select al.*," +
                "(case" +
                "  when (al.write_time < date_sub(now(), interval " + exceptionTime + " hour)) then 1" +
                //"  when (unix_timestamp(NOW())-unix_timestamp(al.write_time))>" + 3600 * exceptionTime + " then 1" +
                "  when net_water=0 then 2" +
                "  else 0" +
                "  end) as stats" +
                " from " +
                "(select tad.*,tc.name as companyName,tc.real_code,tc.water_unit,tc.county,twm.line_num,twm.waters_type,twm.memo,twm.vender from " +
                "(select t.* from (select id,inner_code,meter_address,net_water,sum_water,state,write_time from t_actual_data " +
                " where " + (StringUtils.isNotEmpty(globalInnerCode) ? "inner_code in (" + StringUtils.trim(globalInnerCode) + ") " : "1=1") + " order by write_time desc) t group by t.meter_address) tad " +
                "left join t_company tc on tad.inner_code=tc.inner_code " +
                "left join t_water_meter twm on tad.meter_address=twm.meter_address) al) alld ");
        sqlExceptSelect.append(" where 1=1");
        sqlExceptSelect.append(" and alld.stats = " + status);
        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (alld.real_code='" + StringUtils.trim(keyword) + "' or alld.meter_address='" + StringUtils.trim(keyword)
                    + "' or alld.companyName like '%" + StringUtils.trim(keyword) + "%') ");
        }
        if (StringUtils.isNotEmpty(globalInnerCode)) {
            sqlExceptSelect.append(" and alld.inner_code in (" + StringUtils.trim(globalInnerCode) + ") ");
        }
        sqlExceptSelect.append(" group by alld.meter_address ");
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        return ActualData.me.paginate(page, rows, select, sqlExceptSelect.toString());
    }

    @Deprecated
    public List<Record> findWxActualChart(String wxInnerCode, String meterAddress) {
        Map<String, String> map = ToolDateTime.getBefore30DateTime();
        String start = map.get(MonthCode.warn_start_date);
        String end = map.get(MonthCode.warn_end_date);
        String sql = "select COALESCE(t.net_water, 0) as sumWater,date_format(t.write_time, '%Y-%m-%d') as DAY,t.* from t_actual_data t" +
                " where " + (StringUtils.isNotEmpty(wxInnerCode) ? " t.inner_code in (" + wxInnerCode + ") " : " 1=1 ") +
                " and t.write_time >= '" + start + "' " +
                " and t.write_time <= '" + end + "' " +
                " GROUP BY date_format(t.write_time, '%Y-%m-%d')";
        return Db.find(sql);
    }

    public List<Record> getWxDailyActualData(String wxInnerCode, String startTime, String endTime, String keyword) {
        Map<String, String> map = ToolDateTime.getBefore30DateTime();
        String start = map.get(MonthCode.warn_start_date);
        String end = map.get(MonthCode.warn_end_date);
        String sql = "select COALESCE(sum(t.net_water), 0) as sumWater,date_format(t.write_time, '%Y-%m-%d') as DAY,t.* from t_actual_data t" +
                " left join (select real_code,inner_code,name from t_company) tc on tc.inner_code=t.inner_code " +
                " where " + (StringUtils.isNotEmpty(wxInnerCode) ? " t.inner_code in (" + wxInnerCode + ") " : " 1=1 ") +
                (StringUtils.isNotEmpty(startTime) ? " and t.write_time >= '" + startTime + "'" : " and t.write_time >= '" + start + "' ") +
                (StringUtils.isNotEmpty(endTime) ? " and t.write_time <= '" + endTime + "'" : " and t.write_time <= '" + end + "' ") +
                (StringUtils.isNotEmpty(keyword) ? (" and (tc.real_code='" + StringUtils.trim(keyword) + "' or t.meter_address='" + StringUtils.trim(keyword)
                        + "' or tc.name like '%" + StringUtils.trim(keyword) + "%') ") : " ") +
                " GROUP BY date_format(t.write_time, '%Y-%m-%d')";
        return Db.find(sql);
    }

    public List<Record> getWxMonthActualData(String wxInnerCode, String startTime, String endTime, String keyword) {
        Map<String, String> map = ToolDateTime.getBefore12MonthDateTime();
        String start = map.get(MonthCode.warn_start_date);
        String end = map.get(MonthCode.warn_end_date);
        String sql = "select  COALESCE(sum(t.net_water), 0) as sumWater,date_format(t.write_time, '%Y-%m') as month,t.* from t_actual_data t" +
                " left join (select real_code,inner_code,name from t_company) tc on tc.inner_code=t.inner_code " +
                " where " + (StringUtils.isNotEmpty(wxInnerCode) ? " t.inner_code in (" + wxInnerCode + ") " : " 1=1 ") +
                (StringUtils.isNotEmpty(startTime) ? " and t.write_time >= '" + startTime + "'" : " and t.write_time >= '" + start + "' ") +
                (StringUtils.isNotEmpty(endTime) ? " and t.write_time <= '" + endTime + "'" : " and t.write_time <= '" + end + "' ") +
                (StringUtils.isNotEmpty(keyword) ? (" and (tc.real_code='" + StringUtils.trim(keyword) + "' or t.meter_address='" + StringUtils.trim(keyword)
                        + "' or tc.name like '%" + StringUtils.trim(keyword) + "%') ") : " ") +
                " GROUP BY date_format(t.write_time, '%Y-%m')";
        return Db.find(sql);
    }

    public List<Record> getWxYearActualData(String wxInnerCode, String startTime, String endTime, String keyword) {
        String sql = "select COALESCE(sum(t.net_water), 0) as sumWater,date_format(t.write_time, '%Y') as year,t.* from t_actual_data t" +
                " left join (select real_code,inner_code,name from t_company) tc on tc.inner_code=t.inner_code " +
                " where " + (StringUtils.isNotEmpty(wxInnerCode) ? " t.inner_code in (" + wxInnerCode + ") " : " 1=1 ") +
                (StringUtils.isNotEmpty(startTime) ? " and t.write_time >= '" + startTime + "'" : " ") +
                (StringUtils.isNotEmpty(endTime) ? " and t.write_time <= '" + endTime + "'" : " ") +
                (StringUtils.isNotEmpty(keyword) ? (" and (tc.real_code='" + StringUtils.trim(keyword) + "' or t.meter_address='" + StringUtils.trim(keyword)
                        + "' or tc.name like '%" + StringUtils.trim(keyword) + "%') ") : " ") +
                " GROUP BY date_format(t.write_time, '%Y')";
        return Db.find(sql);
    }

    public Page<ActualData> findWxDailyList(int pageNo, int pageSize, String orderbyStr, String startTime, String endTime, String keyword, String wxInnerCode) {
        String select = " select COALESCE(sum(tad.net_water), 0) as absNetWater,tc.name,tc.real_code,tc.inner_code,tc.address,tc.water_unit," +
                "tc.county,tc.company_type," +
                "date_format(tad.write_time, '%Y-%m-%d') as todays ";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data tad " +
                " inner join t_company  tc on tad.inner_code=tc.inner_code " +
                " inner join t_water_meter twm on tad.meter_address=twm.meter_address " +
                " where 1=1 ");
        if (StringUtils.isNotEmpty(wxInnerCode)) {
            sqlExceptSelect.append(" and tc.inner_code in (" + StringUtils.trim(wxInnerCode) + ") ");
        }
        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (tc.real_code='" + StringUtils.trim(keyword) + "' or tad.meter_address='" + StringUtils.trim(keyword)
                    + "' or tc.name like '%" + StringUtils.trim(keyword) + "%') ");
        }
        if (StringUtils.isNotEmpty(startTime)) {
            sqlExceptSelect.append(" and tad.write_time >= '" + startTime + "' ");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sqlExceptSelect.append(" and tad.write_time <= '" + endTime + "' ");
        }
        sqlExceptSelect.append(" group by tad.inner_code,date_format(tad.write_time, '%Y-%m-%d') ");

        if (StringUtils.isEmpty(orderbyStr)) {
            sqlExceptSelect.append(" order by date_format(tad.write_time, '%Y-%m-%d') desc ");
        } else {
            sqlExceptSelect.append(orderbyStr);
        }
        return ActualData.me.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }

    public Page<ActualData> findWxMonthList(int pageNo, int pageSize, String orderbyStr, String startTime, String endTime, String keyword, String wxInnerCode) {

        String select = " select tc.inner_code,tc.name,tc.real_code,tc.address,tc.water_unit,tc.county,tc.company_type," +
                /*"twm.waters_type,twm.meter_attr,twm.meter_num,twm.line_num,twm.meter_address," +*/
                "date_format(tad.write_time, '%Y-%m') as months,sum(COALESCE(tad.net_water, 0)) as monthTotal";

        StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data tad " +
                " inner join t_company tc on tad.inner_code=tc.inner_code " +
                " inner join t_water_meter twm on tad.meter_address=twm.meter_address " +
                " where 1=1 ");
        if (StringUtils.isNotEmpty(wxInnerCode)) {
            sqlExceptSelect.append(" and tc.inner_code in (" + StringUtils.trim(wxInnerCode) + ") ");
        }
        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (tc.real_code='" + StringUtils.trim(keyword) + "' or tad.meter_address='" + StringUtils.trim(keyword)
                    + "' or tc.name like '%" + StringUtils.trim(keyword) + "%') ");
        }
        if (StringUtils.isNotEmpty(startTime)) {
            sqlExceptSelect.append(" and tad.write_time >= '" + startTime + "'");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sqlExceptSelect.append(" and tad.write_time <= '" + endTime + "'");
        }
        sqlExceptSelect.append(" group by tad.inner_code,date_format(tad.write_time, '%Y-%m') ");

        if (StringUtils.isEmpty(orderbyStr)) {
            sqlExceptSelect.append(" order by date_format(tad.write_time, '%Y-%m') desc ");
        } else {
            sqlExceptSelect.append(orderbyStr);
        }
        return ActualData.me.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }

    public Page<ActualData> findWxYearList(int pageNo, int pageSize, String orderbyStr, String startTime, String endTime, String keyword, String wxInnerCode) {
        String select = " select tc.inner_code,tc.name,tc.real_code,tc.address,tc.water_unit,tc.county,tc.company_type, " +
                /*" twm.waters_type,twm.meter_attr,twm.meter_num,twm.line_num,twm.meter_address, " +*/
                " date_format(tad.write_time, '%Y') as years,sum(COALESCE(tad.net_water, 0)) as yearTotal ";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data tad " +
                " inner join t_company tc on tad.inner_code=tc.inner_code " +
                " inner join t_water_meter twm on tad.meter_address=twm.meter_address " +
                " where 1=1 ");
        if (StringUtils.isNotEmpty(wxInnerCode)) {
            sqlExceptSelect.append(" and tc.inner_code in (" + StringUtils.trim(wxInnerCode) + ") ");
        }
        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (tc.real_code='" + StringUtils.trim(keyword) + "' or tad.meter_address='" + StringUtils.trim(keyword)
                    + "' or tc.name like '%" + StringUtils.trim(keyword) + "%') ");
        }
        if (StringUtils.isNotEmpty(startTime)) {
            sqlExceptSelect.append(" and tad.write_time >= '" + startTime + "'");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sqlExceptSelect.append(" and tad.write_time <= '" + endTime + "'");
        }
        sqlExceptSelect.append(" group by tad.inner_code,date_format(tad.write_time, '%Y') ");

        if (StringUtils.isEmpty(orderbyStr)) {
            sqlExceptSelect.append("order by date_format(tad.write_time, '%Y') desc");
        } else {
            sqlExceptSelect.append(orderbyStr);
        }
        return ActualData.me.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }

    public List<Record> getWxMeterDailyActualData(String wxInnerCode, String meterAddress, String startTime, String endTime, String keyword) {
        Map<String, String> map = ToolDateTime.getBefore30DateTime();
        String start = map.get(MonthCode.warn_start_date);
        String end = map.get(MonthCode.warn_end_date);
        String sql = "select COALESCE(sum(t.net_water), 0) as sumWater,date_format(t.write_time, '%Y-%m-%d') as DAY,t.* from t_actual_data t" +
                " left join (select real_code,inner_code,name from t_company) tc on tc.inner_code=t.inner_code " +
                " where " + (StringUtils.isNotEmpty(wxInnerCode) ? " t.inner_code in (" + wxInnerCode + ") " : " 1=1 ") +
                (StringUtils.isNotEmpty(meterAddress) ? " and t.meter_address = '" + meterAddress + "'" : " ") +
                (StringUtils.isNotEmpty(startTime) ? " and t.write_time >= '" + startTime + "'" : " and t.write_time >= '" + start + "' ") +
                (StringUtils.isNotEmpty(endTime) ? " and t.write_time <= '" + endTime + "'" : " and t.write_time <= '" + end + "' ") +
                (StringUtils.isNotEmpty(keyword) ? (" and (tc.real_code='" + StringUtils.trim(keyword) + "' or t.meter_address='" + StringUtils.trim(keyword)
                        + "' or tc.name like '%" + StringUtils.trim(keyword) + "%') ") : " ") +
                " GROUP BY date_format(t.write_time, '%Y-%m-%d')";
        return Db.find(sql);
    }

    public List<Record> getWxMeterMonthActualData(String wxInnerCode, String meterAddress, String startTime, String endTime, String keyword) {
        Map<String, String> map = ToolDateTime.getBefore12MonthDateTime();
        String start = map.get(MonthCode.warn_start_date);
        String end = map.get(MonthCode.warn_end_date);
        String sql = "select  COALESCE(sum(t.net_water), 0) as sumWater,date_format(t.write_time, '%Y-%m') as month,t.* from t_actual_data t" +
                " left join (select real_code,inner_code,name from t_company) tc on tc.inner_code=t.inner_code " +
                " where " + (StringUtils.isNotEmpty(wxInnerCode) ? " t.inner_code in (" + wxInnerCode + ") " : " 1=1 ") +
                (StringUtils.isNotEmpty(meterAddress) ? " and t.meter_address = '" + meterAddress + "'" : " ") +
                (StringUtils.isNotEmpty(startTime) ? " and t.write_time >= '" + startTime + "'" : " and t.write_time >= '" + start + "' ") +
                (StringUtils.isNotEmpty(endTime) ? " and t.write_time <= '" + endTime + "'" : " and t.write_time <= '" + end + "' ") +
                (StringUtils.isNotEmpty(keyword) ? (" and (tc.real_code='" + StringUtils.trim(keyword) + "' or t.meter_address='" + StringUtils.trim(keyword)
                        + "' or tc.name like '%" + StringUtils.trim(keyword) + "%') ") : " ") +
                " GROUP BY date_format(t.write_time, '%Y-%m')";
        return Db.find(sql);
    }

    public List<Record> getWxMeterYearActualData(String wxInnerCode, String meterAddress, String startTime, String endTime, String keyword) {
        String sql = "select COALESCE(sum(t.net_water), 0) as sumWater,date_format(t.write_time, '%Y') as year,t.* from t_actual_data t" +
                " left join (select real_code,inner_code,name from t_company) tc on tc.inner_code=t.inner_code " +
                " where " + (StringUtils.isNotEmpty(wxInnerCode) ? " t.inner_code in (" + wxInnerCode + ") " : " 1=1 ") +
                (StringUtils.isNotEmpty(meterAddress) ? " and t.meter_address = '" + meterAddress + "'" : " ") +
                (StringUtils.isNotEmpty(startTime) ? " and t.write_time >= '" + startTime + "'" : " ") +
                (StringUtils.isNotEmpty(endTime) ? " and t.write_time <= '" + endTime + "'" : " ") +
                (StringUtils.isNotEmpty(keyword) ? (" and (tc.real_code='" + StringUtils.trim(keyword) + "' or t.meter_address='" + StringUtils.trim(keyword)
                        + "' or tc.name like '%" + StringUtils.trim(keyword) + "%') ") : " ") +
                " GROUP BY date_format(t.write_time, '%Y')";
        return Db.find(sql);
    }

    public Page<ActualData> findWxMeterDailyList(int pageNo, int pageSize, String orderbyStr, String startTime, String endTime, String keyword, String wxInnerCode, String meterAddress) {
        String select = " select sum(COALESCE(tad.net_water, 0)) as absNetWater,tc.name,tc.real_code,tc.inner_code,tc.address,tc.water_unit," +
                "tc.county,tc.company_type," +
                "date_format(tad.write_time, '%Y-%m-%d') as todays ";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data tad " +
                " inner join t_company  tc on tad.inner_code=tc.inner_code " +
                " inner join t_water_meter twm on tad.meter_address=twm.meter_address " +
                " where 1=1 ");
        if (StringUtils.isNotEmpty(wxInnerCode)) {
            sqlExceptSelect.append(" and tc.inner_code in (" + StringUtils.trim(wxInnerCode) + ") ");
        }
        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (tc.real_code='" + StringUtils.trim(keyword) + "' or tad.meter_address='" + StringUtils.trim(keyword)
                    + "' or tc.name like '%" + StringUtils.trim(keyword) + "%') ");
        }
        if (StringUtils.isNotEmpty(meterAddress)) {
            sqlExceptSelect.append(" and tad.meter_address ='" + meterAddress + "'");
        }
        if (StringUtils.isNotEmpty(startTime)) {
            sqlExceptSelect.append(" and tad.write_time >= '" + startTime + "' ");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sqlExceptSelect.append(" and tad.write_time <= '" + endTime + "' ");
        }
        sqlExceptSelect.append(" group by tad.inner_code,date_format(tad.write_time, '%Y-%m-%d') ");
        if (StringUtils.isEmpty(orderbyStr)) {
            sqlExceptSelect.append(" order by date_format(tad.write_time, '%Y-%m-%d') desc ");
        } else {
            sqlExceptSelect.append(orderbyStr);
        }
        return ActualData.me.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }

    public Page<ActualData> findWxMeterMonthList(int pageNo, int pageSize, String orderbyStr, String startTime, String endTime, String keyword, String wxInnerCode, String meterAddress) {

        String select = " select tc.inner_code,tc.name,tc.real_code,tc.address,tc.water_unit,tc.county,tc.company_type," +
                /*"twm.waters_type,twm.meter_attr,twm.meter_num,twm.line_num,twm.meter_address," +*/
                "date_format(tad.write_time, '%Y-%m') as months,sum(COALESCE(tad.net_water, 0)) as monthTotal";

        StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data tad " +
                " inner join t_company tc on tad.inner_code=tc.inner_code " +
                " inner join t_water_meter twm on tad.meter_address=twm.meter_address " +
                " where 1=1 ");
        if (StringUtils.isNotEmpty(wxInnerCode)) {
            sqlExceptSelect.append(" and tc.inner_code in (" + StringUtils.trim(wxInnerCode) + ") ");
        }
        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (tc.real_code='" + StringUtils.trim(keyword) + "' or tad.meter_address='" + StringUtils.trim(keyword)
                    + "' or tc.name like '%" + StringUtils.trim(keyword) + "%') ");
        }
        if (StringUtils.isNotEmpty(meterAddress)) {
            sqlExceptSelect.append(" and tad.meter_address ='" + meterAddress + "'");
        }

        if (StringUtils.isNotEmpty(startTime)) {
            sqlExceptSelect.append(" and tad.write_time >= '" + startTime + "'");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sqlExceptSelect.append(" and tad.write_time <= '" + endTime + "'");
        }
        sqlExceptSelect.append(" group by tad.inner_code,date_format(tad.write_time, '%Y-%m') ");
        if (StringUtils.isEmpty(orderbyStr)) {
            sqlExceptSelect.append(" order by date_format(tad.write_time, '%Y-%m') desc ");
        } else {
            sqlExceptSelect.append(orderbyStr);
        }
        return ActualData.me.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }

    public Page<ActualData> findWxMeterYearList(int pageNo, int pageSize, String orderbyStr, String startTime, String endTime, String keyword, String wxInnerCode, String meterAddress) {
        String select = " select tc.inner_code,tc.name,tc.real_code,tc.address,tc.water_unit,tc.county,tc.company_type, " +
                /*" twm.waters_type,twm.meter_attr,twm.meter_num,twm.line_num,twm.meter_address, " +*/
                " date_format(tad.write_time, '%Y') as years,sum(COALESCE(tad.net_water, 0)) as yearTotal ";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data tad " +
                " inner join t_company tc on tad.inner_code=tc.inner_code " +
                " inner join t_water_meter twm on tad.meter_address=twm.meter_address " +
                " where 1=1 ");
        if (StringUtils.isNotEmpty(wxInnerCode)) {
            sqlExceptSelect.append(" and tc.inner_code in (" + StringUtils.trim(wxInnerCode) + ") ");
        }
        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (tc.real_code='" + StringUtils.trim(keyword) + "' or tad.meter_address='" + StringUtils.trim(keyword)
                    + "' or tc.name like '%" + StringUtils.trim(keyword) + "%') ");
        }
        if (StringUtils.isNotEmpty(meterAddress)) {
            sqlExceptSelect.append(" and tad.meter_address ='" + meterAddress + "'");
        }

        if (StringUtils.isNotEmpty(startTime)) {
            sqlExceptSelect.append(" and tad.write_time >= '" + startTime + "'");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sqlExceptSelect.append(" and tad.write_time <= '" + endTime + "'");
        }
        sqlExceptSelect.append(" group by tad.inner_code,date_format(tad.write_time, '%Y') ");
        if (StringUtils.isEmpty(orderbyStr)) {
            sqlExceptSelect.append("order by date_format(tad.write_time, '%Y') desc");
        } else {
            sqlExceptSelect.append(orderbyStr);
        }
        return ActualData.me.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }

    public List<Record> getWxMeterReadActualDataOnUse(String wxInnerCode, String meterAddress, String startTime, String endTime, String keyword) {
        Map<String, String> map = ToolDateTime.getBefore10DateTime();
        String start = map.get(MonthCode.warn_start_date);
        String end = map.get(MonthCode.warn_end_date);
        String sql = "select COALESCE(t.net_water, 0) as sumWater,t.write_time as DAY,t.* from t_actual_data t" +
                " left join (select real_code,inner_code,name from t_company) tc on tc.inner_code=t.inner_code " +
                " where " + (StringUtils.isNotEmpty(wxInnerCode) ? " t.inner_code in (" + wxInnerCode + ") " : " 1=1 ") +
                (StringUtils.isNotEmpty(meterAddress) ? " and t.meter_address = '" + meterAddress + "'" : " ") +
                (StringUtils.isNotEmpty(startTime) ? " and t.write_time >= '" + startTime + "'" : " and t.write_time >= '" + start + "' ") +
                (StringUtils.isNotEmpty(endTime) ? " and t.write_time <= '" + endTime + "'" : " and t.write_time <= '" + end + "' ") +
                (StringUtils.isNotEmpty(keyword) ? (" and (tc.real_code='" + StringUtils.trim(keyword) + "' or t.meter_address='" + StringUtils.trim(keyword)
                        + "' or tc.name like '%" + StringUtils.trim(keyword) + "%') ") : " ") +
                " GROUP BY t.write_time";
        return Db.find(sql);
    }

    public Page<ActualData> findWxMeterReadListOnUse(int pageNo, int pageSize, String orderbyStr, String startTime, String endTime, String keyword, String wxInnerCode, String meterAddress) {
        String select = " select COALESCE(tad.net_water, 0) as absNetWater,tad.sum_water,tc.name,tc.real_code,tc.inner_code,tc.address,tc.water_unit," +
                "tc.county,tc.company_type," +
                "tad.write_time as todays ";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data tad " +
                " inner join t_company  tc on tad.inner_code=tc.inner_code " +
                " inner join t_water_meter twm on tad.meter_address=twm.meter_address " +
                " where 1=1 ");
        if (StringUtils.isNotEmpty(wxInnerCode)) {
            sqlExceptSelect.append(" and tc.inner_code in (" + StringUtils.trim(wxInnerCode) + ") ");
        }
        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (tc.real_code='" + StringUtils.trim(keyword) + "' or tad.meter_address='" + StringUtils.trim(keyword)
                    + "' or tc.name like '%" + StringUtils.trim(keyword) + "%') ");
        }
        if (StringUtils.isNotEmpty(meterAddress)) {
            sqlExceptSelect.append(" and tad.meter_address ='" + meterAddress + "'");
        }
        Map<String, String> map = ToolDateTime.getBefore10DateTime();
        String start = map.get(MonthCode.warn_start_date);
        String end = map.get(MonthCode.warn_end_date);
        if (StringUtils.isEmpty(startTime)) {
            startTime = start;
        }
        sqlExceptSelect.append(" and tad.write_time >= '" + startTime + "' ");
        if (StringUtils.isEmpty(endTime)) {
            endTime = end;
        }
        sqlExceptSelect.append(" and tad.write_time <= '" + endTime + "' ");
        sqlExceptSelect.append(" group by tad.write_time ");
        if (StringUtils.isEmpty(orderbyStr)) {
            sqlExceptSelect.append(" order by tad.write_time desc ");
        } else {
            sqlExceptSelect.append(orderbyStr);
        }
        return ActualData.me.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }
}
