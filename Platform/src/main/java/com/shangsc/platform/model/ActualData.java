package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.base.BaseActualData;
import com.shangsc.platform.util.ToolDateTime;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class ActualData extends BaseActualData<ActualData> {

	public static final ActualData me = new ActualData();

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
		String select = "select tad.*,tc.name as companyName,tc.water_unit,tc.county,(select tm.line_num from t_water_meter tm where tad.meter_address=tm.meter_address) as line_num" +
				",(select tm.waters_type from t_water_meter tm where tad.meter_address=tm.meter_address) as waters_type";
		StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data tad, t_company tc");
		sqlExceptSelect.append(" where 1=1 and tad.inner_code=tc.inner_code");
		if (StringUtils.isNotEmpty(keyword)) {
			sqlExceptSelect.append(" and (tad.inner_code='" + StringUtils.trim(keyword) + "' or tad.meter_address='" + StringUtils.trim(keyword)
					+ "' or tc.name like '%" + StringUtils.trim(keyword) + "%') ");
		}
		if (StringUtils.isNotEmpty(orderbyStr)) {
			sqlExceptSelect.append(orderbyStr);
		}
		this.paginate(page, rows, select, sqlExceptSelect.toString());
		return this.paginate(page, rows, select, sqlExceptSelect.toString());
	}

	public Page<ActualData> getReadnumStatis(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime,
											 String name, String innerCode, Integer street, Integer watersType) {
		String select=" select twm.*,tc.name,tc.address,tc.street,tc.water_unit,tc.county,tm.meter_attr,tm.meter_address,tm.meter_num,tm.line_num ";
		StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data twm, t_company tc, t_water_meter tm");
		sqlExceptSelect.append(" where 1=1 and twm.inner_code=tc.inner_code and twm.inner_code=tm.inner_code");
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
			sqlExceptSelect.append(" and twm.waters_type=" + watersType);
		}
		sqlExceptSelect.append(" group by twm.write_time ");
		if (StringUtils.isNotEmpty(orderbyStr)) {
			sqlExceptSelect.append(orderbyStr);
		}
		return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
	}

	public Page<ActualData> getDailyStatis(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime,
										   String name, String innerCode, Integer street, Integer watersType) {
		String select=" select twm.*,tc.name,tc.address,tc.water_unit,tc.county,tm.meter_attr,tm.meter_address,tm.meter_num,tm.line_num ";
		StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data twm, t_company tc, t_water_meter tm");
		sqlExceptSelect.append(" where 1=1 and twm.inner_code=tc.inner_code and twm.inner_code=tm.inner_code");
		if (startTime != null) {
			sqlExceptSelect.append(" and twm.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "'");
		}
		if (endTime != null) {
			sqlExceptSelect.append(" and twm.write_time <= '" + ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss")  + "'");
		}
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
				sqlExceptSelect.append(" and twm.inner_code ='" + innerCode + "'");
			}
		}
		if (watersType != null) {
			sqlExceptSelect.append(" and twm.waters_type=" + watersType);
		}
		sqlExceptSelect.append(" group by twm.inner_code ");
		if (StringUtils.isNotEmpty(orderbyStr)) {
			sqlExceptSelect.append(orderbyStr);
		}
		return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
	}

	public Page<ActualData> getMonthStatis(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime,
										   String name, String innerCode, Integer street, Integer watersType) {
		String select=" select twm.*,tc.name,tc.address,tc.water_unit,tc.county,sum(net_water) as netWaterNum,tm.billing_cycle," +
				"tm.meter_num,tm.meter_attr,tm.meter_address,tm.line_num";
		StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data twm, t_company tc, t_water_meter tm");
		sqlExceptSelect.append(" where 1=1 and twm.inner_code=tc.inner_code and twm.inner_code=tm.inner_code");
		if (startTime != null) {
			sqlExceptSelect.append(" and twm.write_time >= '" + ToolDateTime.format(startTime, "yyyy-MM-dd HH:mm:ss") + "'");
		}
		if (endTime != null) {
			sqlExceptSelect.append(" and twm.write_time <= '" +  ToolDateTime.format(endTime, "yyyy-MM-dd HH:mm:ss") + "'");
		}
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
				sqlExceptSelect.append(" and twm.inner_code ='" + innerCode + "'");
			}
		}
		if (watersType != null) {
			sqlExceptSelect.append(" and twm.waters_type=" + watersType);
		}
		sqlExceptSelect.append(" group by twm.inner_code ");
		if (StringUtils.isNotEmpty(orderbyStr)) {
			sqlExceptSelect.append(orderbyStr);
		}
		return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
	}

	public Page<ActualData> getYearStatis(int pageNo, int pageSize, String orderbyStr, Integer year,
										  String name, String innerCode, Integer street, Integer watersType) {
		String select=" select twm.*,tc.name,tc.address,tc.water_unit,tc.county,sum(net_water) as netWaterNum," +
				"tm.meter_attr,tm.meter_num,tm.meter_address,tm.line_num";
		StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data twm, t_company tc, t_water_meter tm");
		sqlExceptSelect.append(" where 1=1 and twm.inner_code=tc.inner_code and twm.inner_code=tm.inner_code");
		if (year != null && year > 0) {
			String yearStart = DateUtils.formatDate(DateUtils.getStrDate(String.valueOf(year) + "-01-01 00:00:00"));
			String yearEnd = DateUtils.formatDate(DateUtils.getStrDate(String.valueOf(year) + "-12-31 23:59:59"));
			sqlExceptSelect.append(" and twm.write_time >= '" +  yearStart + "'");
			sqlExceptSelect.append(" and twm.write_time <= '" +  yearEnd + "'");
		}
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
				sqlExceptSelect.append(" and twm.inner_code ='" + innerCode + "'");
			}
		}
		if (watersType != null) {
			sqlExceptSelect.append(" and twm.waters_type=" + watersType);
		}
		sqlExceptSelect.append(" group by twm.inner_code ");
		if (StringUtils.isNotEmpty(orderbyStr)) {
			sqlExceptSelect.append(orderbyStr);
		}
		return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
	}


	public List<Record> getToddayActualDataPage() {
		String select = "select t.* from t_actual_data t where to_days(t.write_time) = to_days(now()) GROUP BY t.meter_address ";
		StringBuffer sqlExceptSelect = new StringBuffer();
		sqlExceptSelect.append(select);
		sqlExceptSelect.append(" and  1=1 ");

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
		String sql = "select  sum(t.net_water) as sumWater,date_format(t.write_time, '%Y-%m-%d') as DAY,t.* from t_actual_data t GROUP BY  date_format(t.write_time, '%Y-%m-%d')";

		return Db.find(sql);
	}

	public List<Record> getMonthActualData( ) {
		String sql = "select  sum(t.net_water) as sumWater,date_format(t.write_time, '%Y-%m') as month,t.* from t_actual_data t GROUP BY  date_format(t.write_time, '%Y-%m')";

		return Db.find(sql);
	}
}
