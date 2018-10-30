package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.base.BaseWaterIndex;
import com.shangsc.platform.util.CodeNumUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class WaterIndex extends BaseWaterIndex<WaterIndex> {
    public static final WaterIndex me = new WaterIndex();

    public String globalInnerCode;

    public void setGlobalInnerCode(String globalInnerCode) {
        this.globalInnerCode = globalInnerCode;
    }

    /**
     * 水表编号是否已存在
     *
     * @param innerCode
     * @return
     */
    public boolean hasExist(String innerCode) {
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("inner_code", Operators.EQ, innerCode));
        long num = this.getCount(conditions);
        return num > 0 ? true : false;
    }

    public boolean hasExist(Long id, String innerCode) {
        StringBuffer sqlSelect = new StringBuffer("select count(1) as existCount from t_water_index where 1=1 ");
        sqlSelect.append(" and inner_code='" + innerCode + "'");
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

    public InvokeResult save(Long id, String innerCode, Integer watersType, BigDecimal waterIndex, BigDecimal january,
                             BigDecimal february, BigDecimal march, BigDecimal april, BigDecimal may, BigDecimal june, BigDecimal july,
                             BigDecimal august, BigDecimal september, BigDecimal october, BigDecimal november, BigDecimal december) {
        if (!Company.me.hasExistCompany(innerCode)) {
            return InvokeResult.failure("保存失败, 公司编号不存在");
        }
        //一个单位可能存在多个指标
        //if (this.hasExist(id, innerCode)) {
        //	return InvokeResult.failure("该公司用水指标已存在");
        //}
        if (null != id && id > 0L) {
            WaterIndex index = this.findById(id);
            if (index == null) {
                return InvokeResult.failure("更新失败, 该单位用水指标不存在");
            }
            index = setProp(index, innerCode, watersType, waterIndex, january, february, march, april,
                    may, june, july, august, september, october, november, december);
            index.update();
        } else {
            WaterIndex index = new WaterIndex();
            index = setProp(index, innerCode, watersType, waterIndex, january, february, march, april,
                    may, june, july, august, september, october, november, december);
            index.save();

        }
        return InvokeResult.success();
    }

    private WaterIndex setProp(WaterIndex index, String innerCode, Integer watersType, BigDecimal waterIndex,
                               BigDecimal january, BigDecimal february, BigDecimal march, BigDecimal april, BigDecimal may, BigDecimal june,
                               BigDecimal july, BigDecimal august, BigDecimal september, BigDecimal october, BigDecimal november, BigDecimal december) {
        index.setInnerCode(innerCode);
        index.setWaterIndex(waterIndex);
        index.setWatersType(watersType);
        index.setJanuary(january);
        index.setFebruary(february);
        index.setMarch(march);
        index.setApril(april);
        index.setMay(may);
        index.setJune(june);
        index.setJuly(july);
        index.setAugust(august);
        index.setSeptember(september);
        index.setOctober(october);
        index.setNovember(november);
        index.setDecember(december);
        return index;
    }

    public InvokeResult deleteData(String idStrs) {
        List<Long> ids = CommonUtils.getLongListByStrs(idStrs);
        for (int i = 0; i < ids.size(); i++) {
            this.deleteById(ids.get(i));
        }
        return InvokeResult.success();
    }

    public Page<WaterIndex> getWaterIndexPage(int page, int rows, String keyword, String orderbyStr) {
        String select = "select twi.*,tc.name as companyName,tc.real_code,tc.water_unit,tc.county";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_water_index twi inner join " +
                " t_company tc on twi.inner_code=tc.inner_code ");
        sqlExceptSelect.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = StringUtils.trim(keyword);
            if (StringUtils.isNotEmpty(keyword)) {
                sqlExceptSelect.append(" and (tc.real_code='" + keyword + "' or tc.name like '%" + keyword + "%') ");
            }
        }
        if (StringUtils.isNotEmpty(globalInnerCode)) {
            sqlExceptSelect.append(" and twi.inner_code in (" + globalInnerCode + ")");
        }
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        this.paginate(page, rows, select, sqlExceptSelect.toString());
        return this.paginate(page, rows, select, sqlExceptSelect.toString());
    }

    public static int[] saveBatch(List<WaterIndex> modelOrRecordList, int batchSize) {
        String sql = "insert into t_water_index(inner_code,waters_type,water_index,january,february,march," +
                "april,may,june,july,august,september,october,november,december) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String columns = "inner_code,waters_type,water_index,january,february,march," +
                "april,may,june,july,august,september,october,november,december";
        int[] result = Db.batch(sql, columns, modelOrRecordList, batchSize);
        return result;
    }

    public void importData(List<Map<Integer, String>> maps) {
        //所属节水办	用户编号	用户名称	水源类型	小计	01月	02月	03月	04月	05月	06月	07月	08月	09月	10月	11月	12月
        List<WaterIndex> lists = new ArrayList<WaterIndex>();
        Map<String, Integer> dictNameMap = DictData.dao.getDictNameMap(DictCode.WatersType);
        for (int i = 0; i < maps.size(); i++) {
            WaterIndex index = new WaterIndex();
            Map<Integer, String> map = maps.get(i);
            String innerCode = "";
            if (StringUtils.isNotEmpty(map.get(1))) {
                innerCode = map.get(1).toString();
            }
            Integer watersType = null;
            if (StringUtils.isNotEmpty(map.get(3))) {
                watersType = dictNameMap.get(StringUtils.trim(map.get(3).toString()));
            }
            BigDecimal waterIndex = null;
            if (StringUtils.isNotEmpty(map.get(4))) {
                waterIndex = CodeNumUtil.getBigDecimal(map.get(4).toString(), 2);
            }
            BigDecimal january = null;
            if (StringUtils.isNotEmpty(map.get(5))) {
                january = CodeNumUtil.getBigDecimal(map.get(5).toString(), 2);
            }
            BigDecimal february = null;
            if (StringUtils.isNotEmpty(map.get(6))) {
                february = CodeNumUtil.getBigDecimal(map.get(6).toString(), 2);
            }
            BigDecimal march = null;
            if (StringUtils.isNotEmpty(map.get(7))) {
                march = CodeNumUtil.getBigDecimal(map.get(7).toString(), 2);
            }
            BigDecimal april = null;
            if (StringUtils.isNotEmpty(map.get(8))) {
                april = CodeNumUtil.getBigDecimal(map.get(8).toString(), 2);
            }
            BigDecimal may = null;
            if (StringUtils.isNotEmpty(map.get(9))) {

                may = CodeNumUtil.getBigDecimal(map.get(9).toString(), 2);
            }
            BigDecimal june = null;
            if (StringUtils.isNotEmpty(map.get(10))) {
                june = CodeNumUtil.getBigDecimal(map.get(10).toString(), 2);
            }
            BigDecimal july = null;
            if (StringUtils.isNotEmpty(map.get(11))) {
                july = CodeNumUtil.getBigDecimal(map.get(11).toString(), 2);
            }
            BigDecimal august = null;
            if (StringUtils.isNotEmpty(map.get(12))) {
                august = CodeNumUtil.getBigDecimal(map.get(12).toString(), 2);
            }
            BigDecimal september = null;
            if (StringUtils.isNotEmpty(map.get(13))) {
                september = CodeNumUtil.getBigDecimal(map.get(13).toString(), 2);
            }
            BigDecimal october = null;
            if (StringUtils.isNotEmpty(map.get(14))) {
                october = CodeNumUtil.getBigDecimal(map.get(14).toString(), 2);
            }
            BigDecimal november = null;
            if (StringUtils.isNotEmpty(map.get(15))) {
                november = CodeNumUtil.getBigDecimal(map.get(15).toString(), 2);
            }
            BigDecimal december = null;
            if (StringUtils.isNotEmpty(map.get(16))) {
                december = CodeNumUtil.getBigDecimal(map.get(16).toString(), 2);
            }
            index = setProp(index, innerCode, watersType, waterIndex, january, february, march, april,
                    may, june, july, august, september, october, november, december);
            lists.add(index);
        }
        WaterIndex.saveBatch(lists, lists.size());
    }
}
