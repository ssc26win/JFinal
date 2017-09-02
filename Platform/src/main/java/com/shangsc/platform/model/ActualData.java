package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.base.BaseActualData;
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

	public InvokeResult save(Long id, Long companyId, String innerCode, String lineNum, String meterNum,
							 Integer watersType, String alarm, BigDecimal netWater, Integer state, String voltage) {
		if (null != id && id > 0l) {
			ActualData actualData = this.findById(id);
			if (actualData == null) {
				return InvokeResult.failure("更新失败, 该记录不存在");
			}
			actualData = setProp(actualData, companyId, innerCode, lineNum, meterNum, watersType, alarm, netWater, state, voltage);
			actualData.update();
		} else {
			ActualData actualData = new ActualData();
			actualData = setProp(actualData, companyId, innerCode, lineNum, meterNum, watersType, alarm, netWater, state, voltage);
			actualData.save();
		}
		return InvokeResult.success();
	}

	private ActualData setProp(ActualData actualData, Long companyId, String innerCode, String lineNum, String meterNum,
							   Integer watersType, String alarm, BigDecimal netWater, Integer state, String voltage) {
		actualData.setCompanyId(companyId);
		actualData.setInnerCode(innerCode);
		actualData.setLineNum(lineNum);
		actualData.setMeterNum(meterNum);
		actualData.setWatersType(watersType);
		actualData.setAlarm(alarm);
		actualData.setNetWater(netWater);
		actualData.setState(state);
		actualData.setWriteTime(new Date());
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
		String select = "select tad.*,(select tc.name from t_company tc where tc.inner_code=tad.inner_code) as companyName ";
		StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data tad ");
		sqlExceptSelect.append(" where 1=1 ");
		if (StringUtils.isNotEmpty(keyword)) {
			sqlExceptSelect.append(" and (tad.inner_code='" + keyword + "' or tad.meter_num='" + keyword + "' or companyName like '%" + keyword + "%') ");
		}
		if (StringUtils.isNotEmpty(orderbyStr)) {
			sqlExceptSelect.append(orderbyStr);
		}
		this.paginate(page, rows, select, sqlExceptSelect.toString());
		return this.paginate(page, rows, select, sqlExceptSelect.toString());
	}

	public Page<ActualData> getReadnumStatis(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime,
											 String name, String innerCode, String street) {
		String select=" select twm.*,tc.name,tc.address,tc.street ";
		StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data twm, t_company tc ");
		sqlExceptSelect.append(" where 1=1 and twm.inner_code=tc.inner_code");
		if (startTime != null) {
			sqlExceptSelect.append(" and twm.write_time >= '" + startTime + "'");
		}
		if (endTime != null) {
			sqlExceptSelect.append(" and twm.write_time <= '" + endTime + "'");
		}
		if (StringUtils.isNotEmpty(name)) {
			sqlExceptSelect.append(" and tc.name like '%" +name+"%'");
		}
		if (StringUtils.isNotEmpty(street)) {
			sqlExceptSelect.append(" and tc.street like '%" + street + "%'");
		}
		if (StringUtils.isNotEmpty(name)) {
			sqlExceptSelect.append(" and twm.inner_code ='" + innerCode + "'");
		}
		if (StringUtils.isNotEmpty(orderbyStr)) {
			sqlExceptSelect.append(orderbyStr);
		}
		return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
	}

	public Page<ActualData> getDailyStatis(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime,
										   String name, String innerCode, String street) {
		String select=" select twm.*,tc.name,tc.address ";
		StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data twm, t_company tc ");
		sqlExceptSelect.append(" where 1=1 and twm.inner_code=tc.inner_code");
		if (startTime != null) {
			sqlExceptSelect.append(" and twm.write_time >= '" + startTime + "'");
		}
		if (endTime != null) {
			sqlExceptSelect.append(" and twm.write_time <= '" + endTime + "'");
		}
		if (StringUtils.isNotEmpty(name)) {
			sqlExceptSelect.append(" and tc.name like '%" +name+"%'");
		}
		if (StringUtils.isNotEmpty(street)) {
			sqlExceptSelect.append(" and tc.street like '%" + street + "%'");
		}
		if (StringUtils.isNotEmpty(innerCode)) {
			sqlExceptSelect.append(" and twm.inner_code ='" + innerCode + "'");
		}
		if (StringUtils.isNotEmpty(orderbyStr)) {
			sqlExceptSelect.append(orderbyStr);
		}
		return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
	}

	public Page<ActualData> getMonthStatis(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime,
										   String name, String innerCode, String street) {
		String select=" select twm.*,tc.name,tc.address,sum(net_water) as netWaterNum";
		StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data twm, t_company tc ");
		sqlExceptSelect.append(" where 1=1 and twm.inner_code=tc.inner_code");
		if (startTime != null) {
			sqlExceptSelect.append(" and twm.write_time >= '" + DateUtils.formatDate(startTime) + "'");
		}
		if (endTime != null) {
			sqlExceptSelect.append(" and twm.write_time <= '" +  DateUtils.formatDate(endTime) + "'");
		}
		if (StringUtils.isNotEmpty(name)) {
			sqlExceptSelect.append(" and tc.name like '%" +name+"%'");
		}
		if (StringUtils.isNotEmpty(street)) {
			sqlExceptSelect.append(" and tc.street like '%" + street + "%'");
		}
		if (StringUtils.isNotEmpty(innerCode)) {
			sqlExceptSelect.append(" and twm.inner_code ='" + innerCode + "'");
		}
		sqlExceptSelect.append(" group by twm.inner_code ");
		if (StringUtils.isNotEmpty(orderbyStr)) {
			sqlExceptSelect.append(orderbyStr);
		}
		return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
	}

	public Page<ActualData> getYearStatis(int pageNo, int pageSize, String orderbyStr, Integer year,
										  String name, String innerCode, String street) {
		String select=" select twm.*,tc.name,tc.address,sum(net_water) as netWaterNum ";
		StringBuffer sqlExceptSelect = new StringBuffer(" from t_actual_data twm, t_company tc ");
		sqlExceptSelect.append(" where 1=1 and twm.inner_code=tc.inner_code");
		if (year != null && year > 0) {
			String yearStart = DateUtils.formatDate(DateUtils.getStrDate(String.valueOf(year) + "-01-01 00:00:00"));
			String yearEnd = DateUtils.formatDate(DateUtils.getStrDate(String.valueOf(year) + "-12-31 23:59:59"));
			sqlExceptSelect.append(" and twm.write_time >= '" +  yearStart + "'");
			sqlExceptSelect.append(" and twm.write_time >= '" +  yearEnd + "'");
		}
		if (StringUtils.isNotEmpty(name)) {
			sqlExceptSelect.append(" and tc.name like '%" +name+"%'");
		}
		if (StringUtils.isNotEmpty(street)) {
			sqlExceptSelect.append(" and tc.street like '%" + street + "%'");
		}
		if (StringUtils.isNotEmpty(innerCode)) {
			sqlExceptSelect.append(" and twm.inner_code ='" + innerCode + "'");
		}
		sqlExceptSelect.append(" group by twm.inner_code ");
		if (StringUtils.isNotEmpty(orderbyStr)) {
			sqlExceptSelect.append(orderbyStr);
		}
		return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
	}


	public List<Record> getToddayActualDataPage() {
		String select = "select t.* from t_actual_data t where to_days(t.write_time) = to_days(now()) GROUP BY t.meter_num ";
		StringBuffer sqlExceptSelect = new StringBuffer();
		sqlExceptSelect.append(select);
		sqlExceptSelect.append(" and  1=1 ");

		return Db.find(sqlExceptSelect.toString());
	}

	public List<Record> getMonthActualDataPage(String inner_code ) {
		String select = "select sum(t.net_water) as total ,date_format(t.write_time, '%m') as time ,t.* from t_actual_data t ";
		StringBuffer sqlExceptSelect = new StringBuffer();
		sqlExceptSelect.append(select);
		sqlExceptSelect.append(" where t. inner_code= "+inner_code);
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
