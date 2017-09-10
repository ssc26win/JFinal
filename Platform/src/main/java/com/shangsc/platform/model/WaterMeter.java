package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.base.BaseWaterMeter;
import com.shangsc.platform.util.ToolDateTime;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class WaterMeter extends BaseWaterMeter<WaterMeter> {

	public static final WaterMeter me = new WaterMeter();

    public Page<WaterMeter> getWaterMeterPage(int page, int rows, String keyword, String orderbyStr) {
        String select = "select twm.*,tc.name as companyName,tc.water_unit,tc.county";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_water_meter twm, t_company tc");
        sqlExceptSelect.append(" where 1=1 and twm.inner_code=tc.inner_code ");
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = StringUtils.trim(keyword);
            if (StringUtils.isNotEmpty(keyword)) {
                sqlExceptSelect.append(" and (twm.inner_code='" + keyword + "' or twm.meter_num='" + keyword + "' or tc.name like '%" + keyword + "%') ");
            }
        }
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        this.paginate(page, rows, select, sqlExceptSelect.toString());
        return this.paginate(page, rows, select, sqlExceptSelect.toString());
    }

    /**
     * 水表编号是否已存在
     * @param meterNum
     * @return
     */
    public boolean hasExist(String meterNum){
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("meter_num", Operators.EQ, meterNum));
        long num = this.getCount(conditions);
        return num>0?true:false;
    }


    public InvokeResult save(Long id, String innerCode, String lineNum, String meterNum,String meterAddress,
                            Integer watersType, String meterAttr, Integer chargeType, String billingCycle, Date registDate) {
        if (!Company.me.hasExistCompany(innerCode)) {
            return InvokeResult.failure("公司编号不存在");
        }
        if (null != id && id > 0l) {
            WaterMeter meter = this.findById(id);
            if (meter == null) {
                return InvokeResult.failure("更新失败, 该水表不存在");
            }
            meter = setProp(meter, innerCode, lineNum, meterNum, meterAddress, watersType, meterAttr, chargeType, billingCycle, registDate);
            meter.update();
        } else {
            if (this.hasExist(meterNum)) {
                return InvokeResult.failure("水表编号已存在");
            } else {
                WaterMeter meter = new WaterMeter();
                meter = setProp(meter, innerCode, lineNum, meterNum, meterAddress, watersType, meterAttr, chargeType, billingCycle, registDate);
                meter.setRegistDate(new Date());
                meter.save();
                Company.me.updateMeterNum(innerCode, true);
            }
        }
        return InvokeResult.success();
    }

    private WaterMeter setProp(WaterMeter meter, String innerCode, String lineNum, String meterNum,String meterAddress,
                            Integer watersType, String meterAttr, Integer chargeType, String billingCycle, Date registDate) {
        meter.setInnerCode(innerCode);
        meter.setLineNum(lineNum);
        meter.setMeterNum(meterNum);
        meter.setMeterAddress(meterAddress);
        meter.setWatersType(watersType);
        meter.setMeterAttr(meterAttr);
        meter.setChargeType(chargeType);
        meter.setBillingCycle(billingCycle);
        if (registDate == null) {
            meter.setRegistDate(ToolDateTime.getDate());
        } else {
            me.setRegistDate(registDate);
        }
        return meter;
    }

    public InvokeResult deleteData(String idStrs) {
        List<Long> ids = CommonUtils.getLongListByStrs(idStrs);
        for (int i = 0; i < ids.size(); i++) {
            WaterMeter meter = WaterMeter.me.findById(ids.get(i));
            Company.me.updateMeterNum(meter.getInnerCode(), false);
            this.deleteById(ids.get(i));
        }
        return InvokeResult.success();
    }

    public WaterMeter findByMeterNum(String meterNum){
        String sql = "SELECT * FROM t_Water_Meter WHERE meter_num= ?";
        return this.findFirst(sql,meterNum);
    }

    public  WaterMeter findByInnerCode(String innerCode){
        String sql = "SELECT * FROM t_Water_Meter WHERE inner_code= ?";
        return this.findFirst(sql,innerCode);
    }

    public Page<WaterMeter> getExceptionWaterMeterPage(int page, int rows, String keyword, String orderbyStr) {
        String select = "select twm.*,tc.name as companyName,tc.water_unit,tc.county";
        StringBuffer sqlExceptSelect = new StringBuffer("from (SELECT t.* FROM t_Water_Meter t WHERE t.meter_address NOT in  (select t.meter_address from t_actual_data t where to_days(t.write_time) = to_days(now()) GROUP BY t.meter_address)) twm, t_company tc ");
        sqlExceptSelect.append("where 1=1 and twm.inner_code=tc.inner_code");
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = StringUtils.trim(keyword);
            if (StringUtils.isNotEmpty(keyword)) {
                sqlExceptSelect.append(" and (twm.inner_code='" + keyword + "' or twm.meter_address='" + keyword + "' or tc.name like '%" + keyword + "%') ");
            }
        }
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        this.paginate(page, rows, select, sqlExceptSelect.toString());
        return this.paginate(page, rows, select, sqlExceptSelect.toString());
    }


    public Page<WaterMeter> getNormalMeterPage(int page, int rows, String keyword, String orderbyStr) {
        String select = "select twm.*,tc.name as companyName,tc.water_unit,tc.county";
        StringBuffer sqlExceptSelect = new StringBuffer("from (SELECT t.* FROM t_Water_Meter t WHERE t.meter_address in  (select t.meter_address from t_actual_data t where to_days(t.write_time) = to_days(now()) GROUP BY t.meter_address)) twm, t_company tc ");
        sqlExceptSelect.append("where 1=1 and twm.inner_code=tc.inner_code");
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = StringUtils.trim(keyword);
            if (StringUtils.isNotEmpty(keyword)) {
                sqlExceptSelect.append(" and (twm.inner_code='" + keyword + "' or twm.meter_address='" + keyword + "' or tc.name like '%" + keyword + "%') ");
            }
        }
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        this.paginate(page, rows, select, sqlExceptSelect.toString());
        return this.paginate(page, rows, select, sqlExceptSelect.toString());
    }

    public static int[] saveBatch(List<WaterMeter> modelOrRecordList, int batchSize) {
        String sql = "insert into t_water_meter(inner_code,line_num,meter_address,meter_num,waters_type," +
                "meter_attr,charge_type,billing_cycle,regist_date) values (?,?,?,?,?,?,?,?,?)";
        String columns = "inner_code,line_num,meter_address,meter_num,waters_type," +
                "meter_attr,charge_type,billing_cycle,regist_date";
        int[] result = Db.batch(sql, columns, modelOrRecordList, batchSize);
        return result;
    }

    public void importData(List<Map<Integer, String>> maps) {
        List<WaterMeter> lists = new ArrayList<WaterMeter>();
        Map<String, Integer> dictNameMap = DictData.dao.getDictNameMap(DictCode.WatersType);
        Map<String, Integer> dictNameCharge = DictData.dao.getDictNameMap(DictCode.ChargeType);
        for (int i = 0; i < maps.size(); i++) {
            Map<Integer, String> map = maps.get(i);
            WaterMeter meter = new WaterMeter();
            meter.setInnerCode(map.get(0));
            if (map.get(4) != null) {
                meter.setLineNum(map.get(4));
            }
            if (map.get(5) != null) {
                meter.setMeterAddress(map.get(5));
            }
            if (map.get(6) == null || hasExist(StringUtils.trim(map.get(6)))) {
                continue;
            }
            meter.setMeterNum(map.get(6));
            Company.me.updateMeterNum(map.get(0), true);
            if (map.get(7) != null) {
                meter.setWatersType(dictNameMap.get(map.get(7)));
            }
            if (map.get(8) != null) {
                meter.setMeterAttr(map.get(8));
            }
            if (map.get(9) != null) {
                meter.setChargeType(dictNameCharge.get(map.get(9)));
            }
            Date registDate = null;
            Object createDateObj = map.get(13);
            if (createDateObj != null) {
                String createDateStr = createDateObj.toString();
                String date = "";
                if (createDateStr.indexOf("-") > 0) {
                    date = ToolDateTime.format(StringUtils.trim(createDateStr) + " 00:00:00", ToolDateTime.pattern_ymd, ToolDateTime.pattern_ymd_hms);
                } else if (createDateStr.indexOf("/") > 0) {
                    date = ToolDateTime.format(StringUtils.trim(createDateStr) + " 00:00:00", ToolDateTime.pattern_ymd2, ToolDateTime.pattern_ymd_hms);
                } else {
                    date = ToolDateTime.format(new Date(), ToolDateTime.pattern_ymd_hms);
                }
                registDate = DateUtils.getDate(date, ToolDateTime.pattern_ymd);
            }
            meter.setRegistDate(registDate);
            lists.add(meter);
        }
        saveBatch(lists, lists.size());
    }
}
