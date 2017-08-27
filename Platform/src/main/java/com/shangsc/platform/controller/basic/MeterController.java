package com.shangsc.platform.controller.basic;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.export.WaterMeterExportService;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.DictData;
import com.shangsc.platform.model.WaterIndex;
import com.shangsc.platform.model.WaterMeter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author ssc
 * @Date 2017/8/20 16:39
 * @Version 1.0.0
 * @Desc
 */
public class MeterController extends BaseController {

    private List<String> arrayList = new ArrayList<String>();
    // 预警閥值
    private static final BigDecimal THRESHOLD = new BigDecimal("2");


    @RequiresPermissions(value = {"/basic/meter"})
    public void index() {
        render("meter_index.jsp");
    }

    @RequiresPermissions(value = {"/basic/meter"})
    public void warn() {
        this.setAttr("flag", "Warn");
        render("meter_index.jsp");
    }

    @RequiresPermissions(value = {"/basic/meter"})
    public void exption() {
        this.setAttr("flag", "Exception");
        render("meter_index.jsp");
    }

    @RequiresPermissions(value = {"/basic/meter"})
    public void getListData() {
        String keyword = this.getPara("name");
        Page<WaterMeter> pageInfo = WaterMeter.me.getWaterMeterPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        List<WaterMeter> list = pageInfo.getList();
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictData.WatersType);
            Map<String, Object> mapWaterUseType = DictData.dao.getDictMap(0, DictData.WaterUseType);
            Map<String, Object> mapChargeType = DictData.dao.getDictMap(0, DictData.ChargeType);
            for (int i = 0; i < list.size(); i++) {
                WaterMeter co = list.get(i);
                co.put("waterUseTypeName", String.valueOf(mapWaterUseType.get(String.valueOf(co.getWaterUseType()))));
                co.put("watersTypeName", String.valueOf(mapWatersType.get(String.valueOf(co.getWatersType()))));
                co.put("chargeTypeName", String.valueOf(mapChargeType.get(String.valueOf(co.getChargeType()))));
                list.set(i, co);
            }
        }
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value = {"/basic/meter"})
    public void getExceptionListData() {
        String keyword = this.getPara("name");
        Page<WaterMeter> pageInfo = WaterMeter.me.getExceptionWaterMeterPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        List<WaterMeter> list = pageInfo.getList();
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictData.WatersType);
            Map<String, Object> mapWaterUseType = DictData.dao.getDictMap(0, DictData.WaterUseType);
            Map<String, Object> mapChargeType = DictData.dao.getDictMap(0, DictData.ChargeType);
            for (int i = 0; i < list.size(); i++) {
                WaterMeter co = list.get(i);
                co.put("waterUseTypeName", String.valueOf(mapWaterUseType.get(String.valueOf(co.getWaterUseType()))));
                co.put("watersTypeName", String.valueOf(mapWatersType.get(String.valueOf(co.getWatersType()))));
                co.put("chargeTypeName", String.valueOf(mapChargeType.get(String.valueOf(co.getChargeType()))));
                list.set(i, co);
            }
        }
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value = {"/basic/meter"})
    public void getWarnListData() {

        // 用水量告警电表数量
        //取得用水指标数据
        List<WaterIndex> waterIndices = WaterIndex.me.getAllList();
        for (WaterIndex index : waterIndices) {
            index.getWaterIndex();//年
            WaterMeter waterMeter = WaterMeter.me.findByInnerCode(index.getInnerCode());

            if (null != waterMeter) {
                Record records1 = ActualData.me.getYearActual(index.getInnerCode());
                if (null != records1) {
                    comp((BigDecimal) records1.get("yearTotal"), (BigDecimal) index.getWaterIndex(), index.getInnerCode());

                    List<Record> records = ActualData.me.getMonthActualDataPage(index.getInnerCode());
                    for (int i = 0; i < records.size(); i++) {
                        Record record = records.get(i);
                        BigDecimal monthActTotal = new BigDecimal(record.get("total").toString());
                        switch ((record.get("time").toString())) {
                            case "01":
                                comp(monthActTotal, index.getJanuary(), index.getInnerCode());
                                break;
                            case "02":
                                comp(monthActTotal, index.getFebruary(), index.getInnerCode());
                                break;
                            case "03":
                                comp(monthActTotal, index.getMarch(), index.getInnerCode());
                                break;
                            case "04":
                                comp(monthActTotal, index.getApril(), index.getInnerCode());
                                break;
                            case "05":
                                comp(monthActTotal, index.getMay(), index.getInnerCode());
                                break;
                            case "06":
                                comp(monthActTotal, index.getJune(), index.getInnerCode());
                                break;
                            case "07":
                                comp(monthActTotal, index.getJuly(), index.getInnerCode());
                                break;
                            case "08":
                                comp(monthActTotal, index.getAugust(), index.getInnerCode());
                                break;
                            case "09":
                                comp(monthActTotal, index.getSeptember(), index.getInnerCode());
                                break;
                            case "10":
                                comp(monthActTotal, index.getOctober(), index.getInnerCode());
                                break;
                            case "11":
                                comp(monthActTotal, index.getNovember(), index.getInnerCode());
                                break;
                            case "12":
                                comp(monthActTotal, index.getDecember(), index.getInnerCode());
                                break;
                        }
                    }
                }
            }

        }


        String keyword = "";
        StringBuilder sb= new StringBuilder();
        for (int i = 0; i <arrayList.size() ; i++) {
            sb.append("'").append(arrayList.get(i)).append("'").append(",");
        }

        keyword = sb.toString().substring(0,sb.toString().length()-1);
        Page<WaterMeter> pageInfo = WaterMeter.me.getWarnWaterMeterPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        List<WaterMeter> list = pageInfo.getList();
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictData.WatersType);
            Map<String, Object> mapWaterUseType = DictData.dao.getDictMap(0, DictData.WaterUseType);
            Map<String, Object> mapChargeType = DictData.dao.getDictMap(0, DictData.ChargeType);
            for (int i = 0; i < list.size(); i++) {
                WaterMeter co = list.get(i);
                co.put("waterUseTypeName", String.valueOf(mapWaterUseType.get(String.valueOf(co.getWaterUseType()))));
                co.put("watersTypeName", String.valueOf(mapWatersType.get(String.valueOf(co.getWatersType()))));
                co.put("chargeTypeName", String.valueOf(mapChargeType.get(String.valueOf(co.getChargeType()))));
                list.set(i, co);
            }
        }
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));

    }

    @RequiresPermissions(value = {"/basic/meter"})
    public void add() {
        Integer id = this.getParaToInt("id");
        if (id != null) {
            this.setAttr("item", WaterMeter.me.findById(id));
        }
        this.setAttr("id", id);
        render("meter_add.jsp");
    }

    @RequiresPermissions(value = {"/basic/meter"})
    public void save() {
        Long id = this.getParaToLong("id");
        Long companyId = this.getParaToLong("companyId");
        String innerCode = this.getPara("innerCode");
        String lineNum = this.getPara("lineNum");
        String meterNum = this.getPara("meterNum");
        Integer watersType = this.getParaToInt("watersType");
        Integer waterUseType = this.getParaToInt("waterUseType");
        String meterAttr = this.getPara("meterAttr");
        Integer chargeType = this.getParaToInt("chargeType");
        String billingCycle = this.getPara("billingCycle");
        InvokeResult result = WaterMeter.me.save(id, companyId, innerCode, lineNum, meterNum,
                watersType, waterUseType, meterAttr, chargeType, billingCycle);
        this.renderJson(result);
    }

    @RequiresPermissions(value = {"/basic/meter"})
    public void delete() {
        Long id = this.getParaToLong("id");
        InvokeResult result = WaterMeter.me.deleteData(id);
        this.renderJson(result);
    }

    @RequiresPermissions(value = {"/basic/meter"})
    public void export() {
        String keyword = this.getPara("name");
        Set<Condition> conditions = new HashSet<Condition>();
        if (CommonUtils.isNotEmpty(keyword)) {
            conditions.add(new Condition("name", Operators.LIKE, keyword));
        }
        Page<WaterMeter> pageInfo = WaterMeter.me.getPage(getPage(), this.getRows(), conditions, this.getOrderby());

        WaterMeterExportService service = new WaterMeterExportService();
        String path = service.export(pageInfo);

        renderFile(new File(path));

    }

    private void comp(BigDecimal monthActTotal, BigDecimal moth, String innerCode) {
        if (null == moth) {
            moth = new BigDecimal(0);
        }
        if (moth.add(this.THRESHOLD).compareTo(monthActTotal) < 0) {
            arrayList.add(innerCode);
//            count.addAndGet(1);
        }
    }
}
