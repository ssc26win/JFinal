package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.base.BaseWaterMeter;
import com.shangsc.platform.util.ToolDateTime;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class WaterMeter extends BaseWaterMeter<WaterMeter> {

    public static final WaterMeter me = new WaterMeter();

    public Page<WaterMeter> getWaterMeterPage(int page, int rows, String keyword, String orderbyStr, Integer term) {
        String select = "select twm.*,tc.name as companyName,tc.real_code,tc.water_unit,tc.county,tc.gb_industry,tc.main_industry,tc.water_use_type";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_water_meter twm inner join " +
                " t_company tc on twm.inner_code=tc.inner_code ");
        sqlExceptSelect.append(" where 1=1 ");
        if (term != null) {
            sqlExceptSelect.append("and twm.term=" + term);
        }
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = StringUtils.trim(keyword);
            if (StringUtils.isNotEmpty(keyword)) {
                sqlExceptSelect.append(" and (twm.inner_code='" + keyword + "' or twm.meter_num='" + keyword +
                        "' or twm.meter_address='" + keyword + "' or tc.name like '%" + keyword + "%') ");
            }
        }
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        this.paginate(page, rows, select, sqlExceptSelect.toString());
        return this.paginate(page, rows, select, sqlExceptSelect.toString());
    }

    public boolean hasExistMeterNum(Long id, String meterNum) {
        StringBuffer sqlSelect = new StringBuffer("select count(1) as existCount from t_water_meter where 1=1 ");
        sqlSelect.append(" and meter_num='" + meterNum + "'");
        if (id != null) {
            sqlSelect.append(" and id<>" + id + "");
        }
        Record record = Db.findFirst(sqlSelect.toString());
        if (record != null) {
            return record.getLong("existCount") == 1L;
        } else {
            return false;
        }
    }

    public boolean hasExistAddress(Long id, String meterAddress) {
        StringBuffer sqlSelect = new StringBuffer("select count(1) as existCount from t_water_meter where 1=1 ");
        sqlSelect.append(" and meter_address='" + meterAddress + "'");
        if (id != null) {
            sqlSelect.append(" and id<>" + id + "");
        }
        Record record = Db.findFirst(sqlSelect.toString());
        if (record != null) {
            return record.getLong("existCount") == 1L;
        } else {
            return false;
        }
    }

    public InvokeResult save(Long id, String innerCode, String lineNum, String meterNum, String meterAddress, BigDecimal times,
                             Integer watersType, Integer meterAttr, Integer chargeType, String billingCycle, Date registDate,
                             String vender, String memo, Integer term) {
        if (!Company.me.hasExistCompany(innerCode)) {
            return InvokeResult.failure("保存失败，公司编号不存在");
        }
        if (this.hasExistMeterNum(id, meterNum)) {
            return InvokeResult.failure("保存失败，水表编号已存在");
        }
        if (this.hasExistAddress(id, meterAddress)) {
            return InvokeResult.failure("保存失败，水表表计地址已存在");
        }
        if (null != id && id > 0L) {
            WaterMeter meter = this.findById(id);
            if (meter == null) {
                return InvokeResult.failure("更新失败, 该水表不存在");
            }
            meter = setProp(meter, innerCode, lineNum, meterNum, meterAddress, times, watersType, meterAttr,
                    chargeType, billingCycle, registDate, vender, memo, term);
            meter.update();
        } else {
            WaterMeter meter = new WaterMeter();
            meter = setProp(meter, innerCode, lineNum, meterNum, meterAddress, times, watersType, meterAttr,
                    chargeType, billingCycle, registDate, vender, memo, term);
            meter.setRegistDate(new Date());
            meter.save();
            Company.me.updateMeterNum(innerCode, true);

        }
        return InvokeResult.success();
    }

    private WaterMeter setProp(WaterMeter meter, String innerCode, String lineNum, String meterNum, String meterAddress, BigDecimal times,
                               Integer watersType, Integer meterAttr, Integer chargeType, String billingCycle,
                               Date registDate, String vender, String memo, Integer term) {
        meter.setInnerCode(innerCode);
        meter.setLineNum(lineNum);
        meter.setMeterNum(meterNum);
        meter.setMeterAddress(meterAddress);
        meter.setTimes(times);
        meter.setWatersType(watersType);
        meter.setMeterAttr(meterAttr);
        meter.setChargeType(chargeType);
        meter.setBillingCycle(billingCycle);
        if (registDate == null) {
            meter.setRegistDate(new Date());
        } else {
            meter.setRegistDate(registDate);
        }
        meter.setVender(vender);
        meter.setMemo(memo);
        meter.setTerm(term);
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

    public WaterMeter findByMeterAddress(String meterAddress) {
        if (StringUtils.isNotEmpty(meterAddress)) {
            String sql = " SELECT * FROM t_Water_Meter WHERE meter_address='" + StringUtils.trim(meterAddress) + "'";
            return this.findFirst(sql);
        } else {
            return null;
        }
    }

    public WaterMeter findByInnerCode(String innerCode) {
        String sql = "SELECT * FROM t_Water_Meter WHERE inner_code= ?";
        return this.findFirst(sql, innerCode);
    }

    public Set<String> findAddressByInnerCode(String innerCode) {
        String sql = "SELECT * FROM t_Water_Meter WHERE 1=1 ";
        if (StringUtils.isNotEmpty(innerCode)) {
            sql = sql + " and inner_code='" + innerCode + "'";
        }
        List<WaterMeter> waterMeters = WaterMeter.me.find(sql);
        Set<String> addresss = new HashSet<>();
        for (WaterMeter meter : waterMeters) {
            addresss.add(meter.getMeterAddress());
        }
        return addresss;
    }

    private String actualMeterConditionSql(boolean isStop) {
        Date date = new Date();
        String nowBefore = ToolDateTime.getYesterdayStr(date);
        String now = ToolDateTime.getDateStr(date);
        StringBuffer sqlExceptSelect = new StringBuffer("select t.meter_address from t_actual_data t where 1=1");
        if (isStop) {
            sqlExceptSelect.append(" and t.net_water <= 0");
        } else {
            sqlExceptSelect.append(" and t.net_water > 0");
        }
        sqlExceptSelect.append(" and t.write_time >='" + nowBefore + "'");
        sqlExceptSelect.append(" and t.write_time <='" + now + "'");
        sqlExceptSelect.append(" GROUP BY t.meter_address");
        return sqlExceptSelect.toString();
    }

    private String actualMeterConditionSql() {
        Date date = new Date();
        String nowBefore = ToolDateTime.getYesterdayStr(date);
        String now = ToolDateTime.getDateStr(date);
        StringBuffer sqlExceptSelect = new StringBuffer("select t.meter_address from t_actual_data t where 1=1");
        sqlExceptSelect.append(" and t.write_time >='" + nowBefore + "'");
        sqlExceptSelect.append(" and t.write_time <='" + now + "'");
        sqlExceptSelect.append(" GROUP BY t.meter_address");
        return sqlExceptSelect.toString();
    }

    private String disableMeterConditionSql() {
        String sql = " select meter_address from t_Water_Meter where meter_address not in (select meter_address from t_actual_data) ";
        return sql;
    }

    public Page<WaterMeter> getExceptionWaterMeterPage(int page, int rows, String keyword, String orderbyStr) {
        String actualMeterSql = actualMeterConditionSql();
        String select = "select twm.*,tc.name as companyName,tc.real_code,tc.water_unit,tc.county";
        StringBuffer sqlExceptSelect = new StringBuffer("from (SELECT t.* FROM t_Water_Meter t WHERE t.meter_address NOT in  (" +
                actualMeterSql + ")) twm, t_company tc ");
        sqlExceptSelect.append("where 1=1 and twm.inner_code=tc.inner_code");
        sqlExceptSelect.append(" and twm.meter_address not in (" + disableMeterConditionSql() + ") ");
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = StringUtils.trim(keyword);
            if (StringUtils.isNotEmpty(keyword)) {
                sqlExceptSelect.append(" and (twm.inner_code='" + keyword + "' or twm.meter_num='" + keyword +
                        "' or twm.meter_address='" + keyword + "' or tc.name like '%" + keyword + "%') ");
            }
        }
        sqlExceptSelect.append(" GROUP BY twm.meter_address ");
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        this.paginate(page, rows, select, sqlExceptSelect.toString());
        return this.paginate(page, rows, select, sqlExceptSelect.toString());
    }

    public Page<WaterMeter> getDisableWaterMeterPage(int page, int rows, String keyword, String orderbyStr) {
        String select = "select twm.*,tc.name as companyName,tc.real_code,tc.water_unit,tc.county";
        StringBuffer sqlExceptSelect = new StringBuffer("from (SELECT t.* FROM t_Water_Meter t WHERE t.meter_address NOT in " +
                "(select meter_address from t_actual_data)) twm, t_company tc ");
        sqlExceptSelect.append("where 1=1 and twm.inner_code=tc.inner_code");

        if (StringUtils.isNotEmpty(keyword)) {
            keyword = StringUtils.trim(keyword);
            if (StringUtils.isNotEmpty(keyword)) {
                sqlExceptSelect.append(" and (twm.inner_code='" + keyword + "' or twm.meter_num='" + keyword +
                        "' or twm.meter_address='" + keyword + "' or tc.name like '%" + keyword + "%') ");
            }
        }
        sqlExceptSelect.append(" GROUP BY twm.meter_address ");
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        this.paginate(page, rows, select, sqlExceptSelect.toString());
        return this.paginate(page, rows, select, sqlExceptSelect.toString());
    }

    public Page<WaterMeter> getStopMeterPage(int page, int rows, String keyword, String orderbyStr) {
        String normalMeterSql = actualMeterConditionSql(true);
        String select = "select twm.*,tc.name as companyName,tc.real_code,tc.water_unit,tc.county,tc.water_use_type";
        StringBuffer sqlExceptSelect = new StringBuffer("from (SELECT t.* FROM t_Water_Meter t WHERE t.meter_address in (" +
                normalMeterSql + ")) twm, t_company tc ");
        sqlExceptSelect.append("where 1=1 and twm.inner_code=tc.inner_code");
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = StringUtils.trim(keyword);
            if (StringUtils.isNotEmpty(keyword)) {
                sqlExceptSelect.append(" and (twm.inner_code='" + keyword + "' or twm.meter_num='" + keyword +
                        "' or twm.meter_address='" + keyword + "' or tc.name like '%" + keyword + "%') ");
            }
        }
        sqlExceptSelect.append(" GROUP BY twm.meter_address ");
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        this.paginate(page, rows, select, sqlExceptSelect.toString());
        return this.paginate(page, rows, select, sqlExceptSelect.toString());
    }

    public Page<WaterMeter> getNormalMeterPage(int page, int rows, String keyword, String orderbyStr) {
        String normalMeterSql = actualMeterConditionSql(false);
        String select = "select twm.*,tc.name as companyName,tc.real_code,tc.water_unit,tc.county,tc.water_use_type";
        StringBuffer sqlExceptSelect = new StringBuffer("from (SELECT t.* FROM t_Water_Meter t WHERE t.meter_address in (" +
                normalMeterSql + ")) twm, t_company tc ");
        sqlExceptSelect.append("where 1=1 and twm.inner_code=tc.inner_code");
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = StringUtils.trim(keyword);
            if (StringUtils.isNotEmpty(keyword)) {
                sqlExceptSelect.append(" and (twm.inner_code='" + keyword + "' or twm.meter_num='" + keyword +
                        "' or twm.meter_address='" + keyword + "' or tc.name like '%" + keyword + "%') ");
            }
        }
        sqlExceptSelect.append(" GROUP BY twm.meter_address ");
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        this.paginate(page, rows, select, sqlExceptSelect.toString());
        return this.paginate(page, rows, select, sqlExceptSelect.toString());
    }

    /**
     * id
     * inner_code
     * line_num
     * meter_num
     * meter_address
     * times
     * waters_type
     * meter_attr
     * charge_type
     * billing_cycle
     * regist_date
     *
     * @param modelOrRecordList
     * @param batchSize
     * @return
     */
    public static int[] saveBatch(List<WaterMeter> modelOrRecordList, int batchSize) {
        String sql = "insert into t_water_meter(inner_code,line_num,meter_num,meter_address,times,waters_type," +
                "meter_attr,charge_type,billing_cycle,regist_date,term,vender,memo) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String columns = "inner_code,line_num,meter_num,meter_address,times,waters_type," +
                "meter_attr,charge_type,billing_cycle,regist_date,term,vender,memo";
        int[] result = Db.batch(sql, columns, modelOrRecordList, batchSize);
        return result;
    }

    public void importData(List<Map<Integer, String>> maps) {
        List<WaterMeter> lists = new ArrayList<WaterMeter>();
        Map<String, Integer> dictNameMap = DictData.dao.getDictNameMap(DictCode.WatersType);
        Map<String, Integer> dictNameCharge = DictData.dao.getDictNameMap(DictCode.ChargeType);
        Map<String, Integer> dictNameMeterAttr = DictData.dao.getDictNameMap(DictCode.MeterAttr);
        Map<String, Integer> termType = DictData.dao.getDictNameMap(DictCode.Term);

        //单位编号	单位名称	所属节水办	所属区县	路别	表计地址	最小单位	表号	水源类型	国标行业	主要行业	取水用途	水表属性	收费类型	注册日期

        Set<String> checkRepeat = new HashSet<>();

        for (int i = 0; i < maps.size(); i++) {
            Map<Integer, String> map = maps.get(i);
            WaterMeter meter = new WaterMeter();

            meter.setInnerCode(map.get(0));
            if (StringUtils.isNotEmpty(map.get(4))) {
                meter.setLineNum(map.get(4));
            }

            if (StringUtils.isNotEmpty(map.get(5))) {
                meter.setMeterAddress(map.get(5));
                if (hasExistAddress(null, StringUtils.trim(meter.getMeterAddress())) || checkRepeat.contains(meter.getMeterAddress())) {
                    continue;
                } else {
                    checkRepeat.add(meter.getMeterAddress());
                }
            } else {
                continue;
            }

            if (StringUtils.isNotEmpty(map.get(6))) {
                meter.setTimes(new BigDecimal(map.get(6).toString()));
            } else {
                meter.setTimes(new BigDecimal("1"));
            }
            if (StringUtils.isNotEmpty(map.get(7)) || hasExistMeterNum(null, StringUtils.trim(map.get(7)))) {
                meter.setMeterNum(map.get(7));
            }
            Company.me.updateMeterNum(map.get(0), true);
            if (StringUtils.isNotEmpty(map.get(8))) {
                meter.setWatersType(dictNameMap.get(map.get(8)));
            }
            if (StringUtils.isNotEmpty(map.get(9))) {
                meter.setMeterAttrSrc(map.get(9));
                if (dictNameMeterAttr.get(map.get(9)) == null) {
                    DictData latestDictData = DictData.dao.getLatestDictData(DictCode.MeterAttr);
                    if (latestDictData != null) {
                        String value = latestDictData.getValue();
                        if (StringUtils.isNotEmpty(value)) {
                            String remark = map.get(9);
                            DictData.dao.insertDictData(remark, remark, latestDictData.getSeq() + 1,
                                    String.valueOf(Integer.parseInt(value)) + 1, latestDictData.getDictTypeId());
                            meter.setMeterAttr(dictNameMeterAttr.get(map.get(9)));
                        }
                    }
                } else {
                    meter.setMeterAttr(dictNameMeterAttr.get(map.get(9)));
                }
            }
            if (StringUtils.isNotEmpty(map.get(10))) {
                meter.setChargeType(dictNameCharge.get(map.get(10)));
            }
            if (StringUtils.isEmpty(meter.getInnerCode()) && StringUtils.isEmpty(meter.getMeterNum()) && StringUtils.isEmpty(meter.getMeterAddress())) {
                continue;
            }
            Date registDate = null;
            Object createDateObj = map.get(14);
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
            } else {
                registDate = DateUtils.getDate(ToolDateTime.format(new Date(), ToolDateTime.pattern_ymd_hms), ToolDateTime.pattern_ymd);
            }
            meter.setRegistDate(registDate);
            Integer term = null;
            if (StringUtils.isNotEmpty(map.get(15))) {
                term = termType.get(map.get(15).toString());
            }
            meter.setTerm(term);
            String vender = null;
            if (StringUtils.isNotEmpty(map.get(16))) {
                vender = map.get(16).toString();
            }
            meter.setVender(vender);
            String memo = null;
            if (StringUtils.isNotEmpty(map.get(17))) {
                memo = map.get(17).toString();
            }
            meter.setMemo(memo);
            lists.add(meter);
        }
        saveBatch(lists, lists.size());
    }

    public Map<Integer, Object> getTermGroup() {
        List<Record> records = Db.find("select term,count(term) as termTotal from t_water_meter group by term");
        Map<Integer, Object> result = new LinkedHashMap<>();
        for (Record record : records) {
            result.put(record.getInt("term"), record.get("termTotal"));
        }
        return result;
    }
}
