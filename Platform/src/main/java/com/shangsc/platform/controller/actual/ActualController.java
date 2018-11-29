package com.shangsc.platform.controller.actual;

import com.alibaba.druid.support.json.JSONUtils;
import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.code.ActualState;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.conf.GlobalConfig;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.export.ActualExportService;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.DictData;
import com.shangsc.platform.util.CodeNumUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author ssc
 * @Date 2017/8/20 21:27
 * @Version 1.0.0
 * @Desc 实时数据
 */
public class ActualController extends BaseController {

    @RequiresPermissions(value = {"/statis/actual"})
    public void index() {
        render("actual_data.jsp");
    }

    @RequiresPermissions(value = {"/statis/actual"})
    public void getListData() {
        ActualData.me.setGlobalInnerCode(getInnerCodesSQLStr());
        String keyword = this.getPara("name");
        String status = this.getPara("status", "-1");
        Page<ActualData> pageInfo = new Page<>();
        int exceptionTime = 24;
        Map<String, Object> dictMap = DictData.dao.getDictMap(null, DictCode.ACTUAL_EXCEPTION_TIME_OUT);
        if (dictMap.size() == 1) {
            Object[] objects = dictMap.keySet().toArray();
            String num = objects[0].toString();
            if (NumberUtils.isDigits(num)) {
                exceptionTime = Integer.parseInt(num);
            }
        }
        Long start = System.currentTimeMillis();
        if (ActualState.Actual_List().contains(status)) {
            pageInfo = ActualData.me.getActualDataPageByStatus(getPage(), this.getRows(), keyword, this.getOrderbyStr(), status, exceptionTime);
        } else if (ActualState.DISABLE.equals(status)) {
            pageInfo = ActualData.me.getActualDataPageByDisable(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        } else {
            pageInfo = ActualData.me.getActualDataPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        }
        Long end = System.currentTimeMillis();
        logger.info("查询实时数据耗时：{}", end - start);
        List<ActualData> list = pageInfo.getList();
        setVoProp(list, exceptionTime);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, list));
    }

    @RequiresPermissions(value = {"/basic/actual"})
    public void exportData() {
        ActualExportService service = new ActualExportService();
        ActualData.me.setGlobalInnerCode(getInnerCodesSQLStr());
        String keyword = this.getPara("name");
        String status = this.getPara("status", "-1");
        Page<ActualData> pageInfo = new Page<>();
        int exceptionTime = 24;
        Map<String, Object> dictMap = DictData.dao.getDictMap(null, DictCode.ACTUAL_EXCEPTION_TIME_OUT);
        if (dictMap.size() == 1) {
            Object[] objects = dictMap.keySet().toArray();
            String num = objects[0].toString();
            if (NumberUtils.isDigits(num)) {
                exceptionTime = Integer.parseInt(num);
            }
        }
        if (ActualState.Actual_List().contains(status)) {
            pageInfo = ActualData.me.getActualDataPageByStatus(getPage(), GlobalConfig.EXPORT_SUM, keyword, this.getOrderbyStr(), status, exceptionTime);
        } else if (ActualState.DISABLE.equals(status)) {
            pageInfo = ActualData.me.getActualDataPageByDisable(getPage(), GlobalConfig.EXPORT_SUM, keyword, this.getOrderbyStr());
        } else {
            pageInfo = ActualData.me.getActualDataPage(getPage(), GlobalConfig.EXPORT_SUM, keyword, this.getOrderbyStr());
        }
        List<ActualData> list = pageInfo.getList();
        setVoProp(list, exceptionTime);
        String path = service.export(list);
        renderFile(new File(path));
    }

    private void setVoProp(List<ActualData> list, int exceptionTime) {
        Map<String, String> stateMap = ActualState.getMap();
        long dayTime = exceptionTime * 60 * 60 * 1000;
        Date now = new Date();
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);
            for (int i = 0; i < list.size(); i++) {
                ActualData co = list.get(i);
                if (co.get("waters_type") != null) {
                    co.put("watersTypeName", String.valueOf(mapWatersType.get(String.valueOf(co.get("waters_type")))));
                }
                //co.put("alarm", YesOrNo.getYesOrNoMap().get(String.valueOf(co.getAlarm())));
                // 正常 异常（24小时内没有数据传回来时是异常） 停用（一天传回来数没有增量是停用）
                if (co.getNetWater() != null && co.getNetWater().compareTo(new BigDecimal(0.00)) == 0) {
                    co.setState(Integer.parseInt(ActualState.STOP));
                }
                if (co.getWriteTime() != null) {
                    long target = now.getTime() - co.getWriteTime().getTime();
                    if (target > dayTime) {
                        co.setState(Integer.parseInt(ActualState.EXCEPTION));
                    }
                } else {
                    co.setState(Integer.parseInt(ActualState.EXCEPTION));
                }
                if (co.getNetWater() != null && co.getNetWater().compareTo(new BigDecimal(0.00)) < 0) {
                    co.setState(Integer.parseInt(ActualState.EXCEPTION));
                }
                if (co.getId() == null) {
                    co.setState(Integer.parseInt(ActualState.DISABLE));
                }
                co.put("stateName", stateMap.get(String.valueOf(co.getState())));
                if (co.getWriteTime() == null) {
                    co.setWriteTime(DateUtils.parseDate("0001-01-01 00:00:00"));
                }
                list.set(i, co);
            }
        }
    }

    /**
     * 实时数据接口
     */
    @RequiresPermissions(value = {"/statis/actual"})
    public void add() {
        Integer id = this.getParaToInt("id");
        String innerCode = "";
        if (id != null) {
            ActualData byId = ActualData.me.findById(id);
            this.setAttr("item", byId);
            innerCode = byId.getInnerCode();
        }
        this.setAttr("id", id);
        Map<String, String> nameList = Company.me.loadNameList();
        if (StringUtils.isNotEmpty(innerCode)) {
            for (String cName : nameList.keySet()) {
                if (innerCode.equals(nameList.get(cName))) {
                    this.setAttr("companyName", cName);
                }
            }
        }
        Set<String> names = nameList.keySet();
        this.setAttr("nameCodeMap", JSONUtils.toJSONString(nameList));
        this.setAttr("names", JSONUtils.toJSONString(names));
        render("actual_add.jsp");
    }

    @RequiresPermissions(value = {"/statis/actual"})
    public void save() {
        Long id = this.getParaToLong("id");
        String innerCode = this.getPara("innerCode");
        String meter_address = this.getPara("meterAddress");
        String alarm = this.getPara("alarm");
        BigDecimal netWater = new BigDecimal("0.00");
        if (StringUtils.isNotEmpty(this.getPara("netWater"))) {
            netWater = CodeNumUtil.getBigDecimal(this.getPara("netWater"), 2);
        }
        BigDecimal sumWater = new BigDecimal("0.00");
        if (StringUtils.isNotEmpty(this.getPara("sumWater"))) {
            sumWater = CodeNumUtil.getBigDecimal(this.getPara("sumWater"), 2);
        }
        Integer state = this.getParaToInt("state");
        String voltage = this.getPara("voltage");
        Date writeTime = null;
        try {
            writeTime = DateUtils.parseDate(this.getPara("writeTime"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        InvokeResult result = ActualData.me.save(id, innerCode, meter_address,
                alarm, netWater, sumWater, state, voltage, writeTime);
        this.renderJson(result);
    }

    @RequiresPermissions(value = {"/basic/actual"})
    public void delete() {
        String ids = this.getPara("ids");
        InvokeResult result = ActualData.me.deleteData(ids);
        this.renderJson(result);
    }
}
