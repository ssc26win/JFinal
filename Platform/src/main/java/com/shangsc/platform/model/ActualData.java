package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.code.MonthCode;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.base.BaseActualData;
import com.shangsc.platform.util.ToolDateTime;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class ActualData extends BaseActualData<ActualData> {

	public static final ActualData me = new ActualData();//status : 正常 异常 停用

	public InvokeResult save(Long id, String innerCode, String meter_address,
							 String alarm, BigDecimal netWater, BigDecimal sumWater, Integer state, String voltage, Date writeTime) {
		if (null != id && id > 0l) {
			ActualData actualData = this.findById(id);
			if (actualData == null) {
				return InvokeResult.failure("更新失败, 该记录不存在");
			}
			actualData = setProp(actualData, innerCode, meter_address, alarm, netWater, sumWater, state, voltage, writeTime);
			actualData.update();
		} else {
			ActualData actualData = new ActualData();
			actualData = setProp(actualData, innerCode, meter_address, alarm, netWater, sumWater, state, voltage, writeTime);
			actualData.save();
		}
		return InvokeResult.success();
	}

	private ActualData setProp(ActualData actualData, String innerCode, String meter_address,
							   String alarm, BigDecimal netWater, BigDecimal sumWater, Integer state, String voltage, Date writeTime) {
		actualData.setInnerCode(innerCode);
		actualData.setMeterAddress(meter_address);
		actualData.setAlarm(alarm);
		actualData.setNetWater(netWater);
		actualData.setState(state);
        actualData.setSumWater(sumWater);
		if (writeTime == null) {
			actualData.setWriteTime(new Date());
		} else {
			actualData.setWriteTime(writeTime);
		}
		actualData.setVoltage(voltage);
		return actualData;
	}

	public InvokeResult deleteData(String idStrs) {
		List<Long> ids = CommonUtils.getLongListByStrs(idStrs);
		for (int i = 0; i < ids.size(); i++) {
			this.deleteById(ids.get(i));
		}
		return InvokeResult.success();
	}

	public Page<ActualData> getActualDataPage(int page, int rows, String keyword, String orderbyStr) {
		//select * from (select * from t_actual_data order by write_time desc) a group by a.meter_address order by write_time desc
		//String select = "select tad.*,tc.name as companyName,tc.water_unit,tc.county,twm.line_num,twm.waters_type ";
		//StringBuffer sqlExceptSelect = new StringBuffer(" from (select * from t_actual_data order by write_time desc)  tad left join " +
		//		" t_company tc on tad.inner_code=tc.inner_code left join t_water_meter twm on tad.meter_address=twm.meter_address");
        //
		//sqlExceptSelect.append(" where 1=1");
		//if (StringUtils.isNotEmpty(keyword)) {
		//	sqlExceptSelect.append(" and (tad.inner_code='" + StringUtils.trim(keyword) + "' or tad.meter_address='" + StringUtils.trim(keyword)
		//			+ "' or tc.name like '%" + StringUtils.trim(keyword) + "%') ");
		//}
		//sqlExceptSelect.append(" group by tad.meter_address ");
		//if (StringUtils.isNotEmpty(orderbyStr)) {
		//	sqlExceptSelect.append(orderbyStr);
		//}
		String select = "select * ";
		StringBuffer sqlExceptSelect = new StringBuffer("from (" +
				"select tad.*,tc.name as companyName,tc.water_unit,tc.county,twm.line_num,twm.waters_type   from (select * from t_actual_data order by write_time desc)  tad " +
				"left join  t_company tc on tad.inner_code=tc.inner_code " +
				"left join t_water_meter twm on tad.meter_address=twm.meter_address where 1=1 group by tad.meter_address " +
				"union all (SELECT tad.id,tc.inner_code,tm.meter_address,tad.alarm,tad.net_water,tad.sum_water,tad.state," +
				"tad.write_time,tad.voltage,companyName,tc.water_unit,tc.county,tm.line_num,tm.waters_type FROM t_water_meter tm " +
				"left join (select name as companyName,water_unit,county,inner_code from t_company) tc on tm.inner_code=tc.inner_code " +
				"left join t_actual_data tad on tad.meter_address=tm.meter_address " +
				"where tm.meter_address not in (select meter_address from t_actual_data))) alld");
		sqlExceptSelect.append(" where 1=1");
		if (StringUtils.isNotEmpty(keyword)) {
			sqlExceptSelect.append(" and (alld.inner_code='" + StringUtils.trim(keyword) + "' or alld.meter_address='" + StringUtils.trim(keyword)
					+ "' or alld.companyName like '%" + StringUtils.trim(keyword) + "%') ");
		}
		if (StringUtils.isNotEmpty(orderbyStr)) {
			sqlExceptSelect.append(orderbyStr);
		}
		return this.paginate(page, rows, select, sqlExceptSelect.toString());
	}

	public Page<ActualData> getActualDataPageByDisable(int page, int rows, String keyword, String orderbyStr) {
		String select = "select * ";
		StringBuffer sqlExceptSelect = new StringBuffer("from (SELECT tad.id,tc.inner_code,tm.meter_address,tad.alarm,tad.net_water,tad.sum_water,tad.state," +
				"tad.write_time,tad.voltage,companyName,tc.water_unit,tc.county,tm.line_num,tm.waters_type FROM t_water_meter tm " +
				"left join (select name as companyName,water_unit,county,inner_code from t_company) tc on tm.inner_code=tc.inner_code " +
				"left join t_actual_data tad on tad.meter_address=tm.meter_address " +
				"where tm.meter_address not in (select meter_address from t_actual_data)) alld ");
		sqlExceptSelect.append(" where 1=1");
		if (StringUtils.isNotEmpty(keyword)) {
			sqlExceptSelect.append(" and (alld.inner_code='" + StringUtils.trim(keyword) + "' or alld.meter_address='" + StringUtils.trim(keyword)
					+ "' or alld.companyName like '%" + StringUtils.trim(keyword) + "%') ");
		}
		if (StringUtils.isNotEmpty(orderbyStr)) {
			sqlExceptSelect.append(orderbyStr);
		}
		return this.paginate(page, rows, select, sqlExceptSelect.toString());
	}


	public Page<ActualData> getActualDataPageByStatus(int page, int rows, String keyword, String orderbyStr, String status) {
		//select * from (select * from t_actual_data order by write_time desc) a group by a.meter_address order by write_time desc
		String select = "select * ";
		StringBuffer sqlExceptSelect =	new StringBuffer("from (" +
				"select al.*," +
				"(case" +
				"  when (unix_timestamp(NOW())-unix_timestamp(al.write_time))>86400 then 1" +
				"  when net_water<=0 then 2" +
				"  else 0" +
				"  end) as stats" +
				" from " +
				"(select tad.*,tc.name as companyName,tc.water_unit,tc.county,twm.line_num,twm.waters_type from " +
				"(select * from t_actual_data order by write_time desc)  tad " +
				"left join  t_company tc on tad.inner_code=tc.inner_code " +
				"left join t_water_meter twm on tad.meter_address=twm.meter_address where 1=1 group by tad.meter_address) al) alld ");
		sqlExceptSelect.append(" where 1=1");
		sqlExceptSelect.append(" and alld.stats = " + status);
		if (StringUtils.isNotEmpty(keyword)) {
			sqlExceptSelect.append(" and (alld.inner_code='" + StringUtils.trim(keyword) + "' or alld.meter_address='" + StringUtils.trim(keyword)
					+ "' or alld.companyName like '%" + StringUtils.trim(keyword) + "%') ");
		}
		sqlExceptSelect.append(" group by alld.meter_address ");
		if (StringUtils.isNotEmpty(orderbyStr)) {
			sqlExceptSelect.append(orderbyStr);
		}
		return this.paginate(page, rows, select, sqlExceptSelect.toString());
	}

	public Page<ActualData> getReadnumStatis(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime,
											 String name, String innerCode, Integer street, Integer watersType, String meterAttr, String meterAddress) {
		String select=" select twm.*,tc.name,tc.address,tc.street,tc.water_unit,tc.county,tc.company_type,tm.waters_type,tm.meter_attr," +
				"tm.meter_address,tm.meter_num,tm.line_num ";
		StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data twm inner join " +
				" t_company tc on twm.inner_code=tc.inner_code " +
				" left join t_water_meter tm on twm.meter_address=tm.meter_address");
		sqlExceptSelect.append(" where 1=1 ");
		if (startTime != null) {
			sqlExceptSelect.append(" and twm.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "'");
		}
		if (endTime != null) {
			sqlExceptSelect.append(" and twm.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "'");
		}
		if (StringUtils.isNotEmpty(name)) {
			name = StringUtils.trim(name);
			if (StringUtils.isNotEmpty(name)) {
				sqlExceptSelect.append(" and tc.name like '%" + name + "%'");
			}
		}
		if (StringUtils.isNotEmpty(meterAttr)) {
			meterAttr = StringUtils.trim(meterAttr);
			if (StringUtils.isNotEmpty(meterAttr)) {
				sqlExceptSelect.append(" and tm.meter_attr like '%" + meterAttr + "%'");
			}
		}
		if (StringUtils.isNotEmpty(meterAddress)) {
			meterAddress = StringUtils.trim(meterAddress);
			if (StringUtils.isNotEmpty(meterAddress)) {
				sqlExceptSelect.append(" and tm.meter_address like '%" + meterAddress + "%'");
			}
		}
		if (street != null && street > 0) {
			sqlExceptSelect.append(" and tc.street=" + street);
		}
		if (StringUtils.isNotEmpty(innerCode)) {
			innerCode = StringUtils.trim(innerCode);
			if (StringUtils.isNotEmpty(innerCode)) {
				sqlExceptSelect.append(" and twm.inner_code ='" + innerCode + "'");
			}
		}
		if (watersType != null) {
			sqlExceptSelect.append(" and tm.waters_type=" + watersType);
		}
		sqlExceptSelect.append(" group by twm.write_time ");

		if (StringUtils.isNotEmpty(orderbyStr)) {
			sqlExceptSelect.append(orderbyStr);
		} else {
			sqlExceptSelect.append(" order by twm.write_time desc ");
		}
		return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
	}

	public Page<ActualData> getDailyStatis(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime,
										   String name, String innerCode, Integer street, Integer watersType, String meterAttr, String meterAddress, String type) {
		/*select tad.*,
		tc.name,tc.address,tc.water_unit,tc.county,tc.company_type,
				twm.waters_type,twm.meter_attr,twm.meter_num,twm.line_num,twm.billing_cycle,
				date_format(tad.write_time, '%Y-%m-%d') as todays

		from t_actual_data tad
		inner join t_company  tc on tad.inner_code=tc.inner_code
		inner join t_water_meter twm on tad.meter_address=twm.meter_address
		where 1=1

		and tc.name like '%潞洲水务有限公司%'
		and tad.write_time >= '2018-03-18 00:00:00' and tad.write_time < '2019-01-01 00:00:00'
		group by tad.meter_address,date_format(tad.write_time, '%Y-%m-%d')
		order by todays desc,tad.meter_address desc*/
		String select=" select tad.net_water,tc.name,tc.inner_code,tc.address,tc.water_unit,tc.county,tc.company_type," +
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
		if (street != null && street > 0) {
			sqlExceptSelect.append(" and tc.street=" + street);
		}
		if (StringUtils.isNotEmpty(innerCode)) {
			innerCode = StringUtils.trim(innerCode);
			if (StringUtils.isNotEmpty(innerCode)) {
				sqlExceptSelect.append(" and tc.inner_code ='" + innerCode + "' ");
			}
		}

		if (startTime != null) {
			sqlExceptSelect.append(" and tad.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "' ");
		}
		if (endTime != null) {
			sqlExceptSelect.append(" and tad.write_time < '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss")  + "' ");
		}
		if (StringUtils.isNotEmpty(meterAddress)) {
			meterAddress = StringUtils.trim(meterAddress);
			if (StringUtils.isNotEmpty(meterAddress)) {
				sqlExceptSelect.append(" and tad.meter_address = '" + meterAddress + "'");
			}
		}

		if (StringUtils.isNotEmpty(meterAttr)) {
			meterAttr = StringUtils.trim(meterAttr);
			if (StringUtils.isNotEmpty(meterAttr)) {
				sqlExceptSelect.append(" and twm.meter_attr like '%" + meterAttr + "%'");
			}
		}
		if (watersType != null) {
			sqlExceptSelect.append(" and twm.waters_type=" + watersType);
		}
		sqlExceptSelect.append(" group by tad.meter_address,date_format(tad.write_time, '%Y-%m-%d') ");
		if (StringUtils.isNotEmpty(orderbyStr)) {
			sqlExceptSelect.append(orderbyStr);
		} else {
			sqlExceptSelect.append(" order by date_format(tad.write_time, '%Y-%m-%d') desc,tad.meter_address desc ");
		}
		System.out.println("---");
		System.out.println(select);
		System.out.println(sqlExceptSelect.toString());
		System.out.println("---");
		return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
	}

	public Page<ActualData> getMonthStatis(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime,
										   String name, String innerCode, Integer street, Integer watersType, String meterAttr,
                                           String meterAddress, String type) {
		/*select tad.*,
		tc.name,tc.address,tc.water_unit,tc.county,tc.company_type,
				twm.waters_type,twm.meter_attr,twm.meter_num,twm.line_num,
				date_format(tad.write_time, '%Y-%m') as months,sum(tad.net_water) as monthTotal

		from t_actual_data tad
		inner join t_company  tc on tad.inner_code=tc.inner_code
		inner join t_water_meter twm on tad.meter_address=twm.meter_address
		where 1=1

		and tc.name like '%潞洲水务有限公司%'
		and tad.write_time >= '2018-02-01 00:00:00' and tad.write_time < '2019-01-01 00:00:00' and tad.meter_address = '201707000000936'
		group by tad.meter_address,date_format(tad.write_time, '%Y-%m')
		order by months desc,tad.meter_address desc*/

		String select = " select tc.inner_code,tc.name,tc.address,tc.water_unit,tc.county,tc.company_type," +
				"twm.waters_type,twm.meter_attr,twm.meter_num,twm.line_num,twm.meter_address," +
				"date_format(tad.write_time, '%Y-%m') as months,sum(tad.net_water) as monthTotal";

		StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data tad " +
				" inner join t_company  tc on tad.inner_code=tc.inner_code " +
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
		if (street != null && street > 0) {
			sqlExceptSelect.append(" and tc.street=" + street);
		}
		if (StringUtils.isNotEmpty(innerCode)) {
			innerCode = StringUtils.trim(innerCode);
			if (StringUtils.isNotEmpty(innerCode)) {
				sqlExceptSelect.append(" and tc.inner_code ='" + innerCode + "'");
			}
		}

		if (startTime != null) {
			sqlExceptSelect.append(" and tad.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "'");
		}
		if (endTime != null) {
			sqlExceptSelect.append(" and tad.write_time < '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss")  + "'");
		}
        if (StringUtils.isNotEmpty(meterAddress)) {
            meterAddress = StringUtils.trim(meterAddress);
            if (StringUtils.isNotEmpty(meterAddress)) {
                sqlExceptSelect.append(" and tad.meter_address = '" + meterAddress + "' ");
            }
        }

		if (StringUtils.isNotEmpty(meterAttr)) {
			meterAttr = StringUtils.trim(meterAttr);
			if (StringUtils.isNotEmpty(meterAttr)) {
				sqlExceptSelect.append(" and twm.meter_attr like '%" + meterAttr + "%' ");
			}
		}
		if (watersType != null) {
			sqlExceptSelect.append(" and twm.waters_type=" + watersType);
		}
		sqlExceptSelect.append(" group by tad.meter_address,date_format(tad.write_time, '%Y-%m') ");
		if (StringUtils.isNotEmpty(orderbyStr)) {
			sqlExceptSelect.append(orderbyStr);
		} else {
			sqlExceptSelect.append(" order by date_format(tad.write_time, '%Y-%m') desc,tad.meter_address desc ");
		}
        System.out.println("---");
        System.out.println(select);
        System.out.println(sqlExceptSelect.toString());
        System.out.println("---");
		return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
	}

	public Page<ActualData> getYearStatis(int pageNo, int pageSize, String orderbyStr, Integer year,String name,
										  String innerCode, Integer street, Integer watersType, String meterAttr,
                                          String meterAddress, String type) {
		/*select
		tc.inner_code,tc.name,tc.address,tc.water_unit,tc.county,tc.company_type,
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
		String select=" select tc.inner_code,tc.name,tc.address,tc.water_unit,tc.county,tc.company_type, " +
				" twm.waters_type,twm.meter_attr,twm.meter_num,twm.line_num,twm.meter_address, " +
				" date_format(tad.write_time, '%Y') as years,sum(tad.net_water) as yearTotal ";
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
		if (street != null && street > 0) {
			sqlExceptSelect.append(" and tc.street=" + street);
		}
		if (StringUtils.isNotEmpty(innerCode)) {
			innerCode = StringUtils.trim(innerCode);
			if (StringUtils.isNotEmpty(innerCode)) {
				sqlExceptSelect.append(" and tc.inner_code ='" + innerCode + "'");
			}
		}
        if (StringUtils.isNotEmpty(type)) {
            type = StringUtils.trim(type);
            if (StringUtils.isNotEmpty(type)) {
                sqlExceptSelect.append(" and tc.company_type=" + type + " ");
            }
        }

		if (year != null && year > 0) {
			String yearStart = DateUtils.formatDate(DateUtils.getStrDate(String.valueOf(year) + "-01-01 00:00:00"));
			String yearEnd = DateUtils.formatDate(DateUtils.getStrDate(String.valueOf(year+1) + "-01-01 00:00:00"));
			sqlExceptSelect.append(" and tad.write_time >= '" +  yearStart + "'");
			sqlExceptSelect.append(" and tad.write_time < '" +  yearEnd + "'");
		}
        if (StringUtils.isNotEmpty(meterAddress)) {
            meterAddress = StringUtils.trim(meterAddress);
            if (StringUtils.isNotEmpty(meterAddress)) {
                sqlExceptSelect.append(" and tad.meter_address = '" + meterAddress + "'");
            }
        }

		if (StringUtils.isNotEmpty(meterAttr)) {
			meterAttr = StringUtils.trim(meterAttr);
			if (StringUtils.isNotEmpty(meterAttr)) {
				sqlExceptSelect.append(" and twm.meter_attr like '%" + meterAttr + "%'");
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
		System.out.println(select);
		System.out.println(sqlExceptSelect.toString());
		return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
	}

	public List<Record> getNormalMeter() {
		Date date = new Date();
		String now = ToolDateTime.getDateStr(date);
		String nowBefore = ToolDateTime.getYesterdayStr(date);
		StringBuffer sqlExceptSelect = new StringBuffer("select t.* from t_actual_data t where 1=1");
		sqlExceptSelect.append(" and t.net_water > 0");
		sqlExceptSelect.append(" and t.write_time >='" + nowBefore + "'");
		sqlExceptSelect.append(" and t.write_time <='" + now + "'");
		sqlExceptSelect.append(" GROUP BY t.meter_address");
		return Db.find(sqlExceptSelect.toString());
	}

	public List<Record> getStopMeter() {
		Date date = new Date();
		String now = ToolDateTime.getDateStr(date);
		String nowBefore = ToolDateTime.getYesterdayStr(date);
		StringBuffer sqlExceptSelect = new StringBuffer("select t.* from t_actual_data t where 1=1");
		sqlExceptSelect.append(" and t.net_water <= 0");
		sqlExceptSelect.append(" and t.write_time >='" + nowBefore + "'");
		sqlExceptSelect.append(" and t.write_time <='" + now + "'");
		sqlExceptSelect.append(" GROUP BY t.meter_address");
		return Db.find(sqlExceptSelect.toString());
	}

	public List<Record> getDisableMeter() {
		StringBuffer sqlExceptSelect = new StringBuffer("select * from t_water_meter where meter_address not in (select meter_address from t_actual_data) ");
		sqlExceptSelect.append(" GROUP BY meter_address");
		return Db.find(sqlExceptSelect.toString());
	}

	public List<Record> getMonthActualDataPage(String inner_code ) {
		String select = "select sum(t.net_water) as total ,date_format(t.write_time, '%m') as time ,t.* from t_actual_data t ";
		StringBuffer sqlExceptSelect = new StringBuffer();
		sqlExceptSelect.append(select);
		sqlExceptSelect.append(" where t.inner_code='"+inner_code+"'");
		sqlExceptSelect.append(" group by date_format(write_time, '%m')");
		return Db.find(sqlExceptSelect.toString());
	}

	//select * from `ht_invoice_information` where YEAR(create_date)=YEAR(NOW());
	//net_water
	public Record getYearActual(String inner_code) {
		String select = "select sum(net_water) as yearTotal from t_actual_data where YEAR(write_time)=YEAR(NOW()) and inner_code="+inner_code;
		return Db.find(select).get(0);
	}

	public List<Record> getDailyActualData( ) {
		String sql = "select  sum(t.net_water) as sumWater,date_format(t.write_time, '%Y-%m-%d') as DAY,t.* from t_actual_data t" +
				" where inner_code in (select inner_code from t_company where company_type=1)" +
				" GROUP BY date_format(t.write_time, '%Y-%m-%d')";
		return Db.find(sql);
	}

	public List<Record> getMonthActualData( ) {
		String sql = "select  sum(t.net_water) as sumWater,date_format(t.write_time, '%Y-%m') as month,t.* from t_actual_data t" +
				" where inner_code in (select inner_code from t_company where company_type=1)" +
				" GROUP BY date_format(t.write_time, '%Y-%m')";
		return Db.find(sql);
	}

	public List<Record> getSupplyDailyActualData( ) {
		String sql = "select  sum(t.net_water) as sumWater,date_format(t.write_time, '%Y-%m-%d') as DAY,t.* from t_actual_data t" +
				" where inner_code in (select inner_code from t_company where company_type=2)" +
				" GROUP BY date_format(t.write_time, '%Y-%m-%d')";
		return Db.find(sql);
	}

	public List<Record> getSupplyMonthActualData( ) {
		String sql = "select  sum(t.net_water) as sumWater,date_format(t.write_time, '%Y-%m') as month,t.* from t_actual_data t" +
				" where inner_code in (select inner_code from t_company where company_type=2)" +
				" GROUP BY date_format(t.write_time, '%Y-%m')";
		return Db.find(sql);
	}

	public ActualData getLastMeterAddress(String meter_address) {
		return ActualData.me.findFirst("select t.* from t_actual_data t where 1=1 and t.meter_address='" + meter_address + "' order by t.write_time desc limit 1");
	}

	@Deprecated
	private String normalMeterConditionSql(){
		Date date = new Date();
		String nowBefore = ToolDateTime.getYesterdayStr(date);
		String now = ToolDateTime.getDateStr(date);
		StringBuffer exceptionMeterSql = new StringBuffer("select t.* from t_actual_data t where 1=1");
		exceptionMeterSql.append(" and t.write_time >='" + nowBefore + "'");
		exceptionMeterSql.append(" and t.write_time <='" + now + "'");
		exceptionMeterSql.append(" GROUP BY t.meter_address");
		return exceptionMeterSql.toString();
	}

	@Deprecated
	public Set<String> getNormalAddress() {
		Set<String> address = new HashSet<>();
		String normalMeterSql = normalMeterConditionSql();
		List<ActualData> datas = this.find(normalMeterSql);
		for (ActualData actualData:datas) {
			address.add(actualData.getMeterAddress());
		}
		return address;
	}

    public static void main(String[] args) {
        Map<Integer, String> map = MonthCode.getMap();
        Map<String, String> monthDateBetween = ToolDateTime.get2MonthDateBetween(new Date());
        String start = monthDateBetween.get("start");
        String end = monthDateBetween.get("end");
        String month_str = monthDateBetween.get("month_str");
        Integer month = Integer.parseInt(monthDateBetween.get("month"));
        System.out.println(start + end + month_str +month);
    }
}
