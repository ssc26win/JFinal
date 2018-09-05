package com.shangsc.platform.controller.basic;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jfinal.aop.Clear;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.code.ExportType;
import com.shangsc.platform.conf.GlobalConfig;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.util.FileUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.export.WaterMeterExportService;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.DictData;
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

    @RequiresPermissions(value = {"/basic/meter"})
    public void index() {
        render("meter_index.jsp");
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/basic/meter"})
    public void normal() {
        this.setAttr("flag", "Normal");
        render("meter_index.jsp");
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/basic/meter"})
    public void exption() {
        this.setAttr("flag", "Exception");
        render("meter_index.jsp");
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/basic/meter"})
    public void stop() {
        this.setAttr("flag", "Stop");
        render("meter_index.jsp");
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/basic/meter"})
    public void disable() {
        this.setAttr("flag", "Disable");
        render("meter_index.jsp");
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/basic/meter"})
    public void getListData() {
        String keyword = this.getPara("name");
        Page<WaterMeter> pageInfo = WaterMeter.me.getWaterMeterPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        List<WaterMeter> list = pageInfo.getList();
        setVoProp(list);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, list));
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/basic/meter"})
    public void getExceptionListData() {
        String keyword = this.getPara("name");
        Page<WaterMeter> pageInfo = WaterMeter.me.getExceptionWaterMeterPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        List<WaterMeter> list = pageInfo.getList();
        setVoProp(list);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, list));
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/basic/meter"})
    public void getStopListData() {
        String keyword = this.getPara("name");
        Page<WaterMeter> pageInfo = WaterMeter.me.getStopMeterPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        List<WaterMeter> list = pageInfo.getList();
        setVoProp(list);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, list));
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/basic/meter"})
    public void getDisableListData() {
        String keyword = this.getPara("name");
        Page<WaterMeter> pageInfo = WaterMeter.me.getDisableWaterMeterPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        List<WaterMeter> list = pageInfo.getList();
        setVoProp(list);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, list));
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/basic/meter"})
    public void getNormalListData() {
        String keyword = this.getPara("name");
        Page<WaterMeter> pageInfo = WaterMeter.me.getNormalMeterPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        List<WaterMeter> list = pageInfo.getList();
        setVoProp(list);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, list));
    }

    @RequiresPermissions(value = {"/basic/meter"})
    public void add() {
        Integer id = this.getParaToInt("id");
        String innerCode = "";
        if (id != null) {
            WaterMeter byId = WaterMeter.me.findById(id);
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
        render("meter_add.jsp");
    }

    @RequiresPermissions(value = {"/basic/meter"})
    public void save() {
        Long id = this.getParaToLong("id");
        String innerCode = this.getPara("innerCode");
        String lineNum = this.getPara("lineNum");
        String meterNum = StringUtils.trim(this.getPara("meterNum"));
        String meterAddress = StringUtils.trim(this.getPara("meterAddress"));
        BigDecimal times = new BigDecimal("1");
        if (StringUtils.isNotEmpty(this.getPara("times"))) {
            times = new BigDecimal(StringUtils.trim(this.getPara("times")));
        }
        Integer watersType = this.getParaToInt("watersType");
        String meterAttr = this.getPara("meterAttr");
        Integer chargeType = this.getParaToInt("chargeType");
        String billingCycle = this.getPara("billingCycle");
        Date registDate = null;
        if (StringUtils.isNotEmpty(this.getPara("registDate"))) {
            registDate = DateUtils.parseDate(this.getPara("registDate"));
        }
        String vender = this.getPara("vender");
        String memo = this.getPara("memo");
        InvokeResult result = WaterMeter.me.save(id, innerCode, lineNum, meterNum, meterAddress, times,
                watersType, meterAttr, chargeType, billingCycle, registDate, vender, memo);
        this.renderJson(result);
    }

    @RequiresPermissions(value = {"/basic/meter"})
    public void delete() {
        String ids = this.getPara("ids");
        InvokeResult result = WaterMeter.me.deleteData(ids);
        this.renderJson(result);
    }

    @RequiresPermissions(value = {"/basic/meter"})
    public void export() {
        String keyword = this.getPara("name");
        String flagType = this.getPara("flagType");
        Set<Condition> conditions = new HashSet<Condition>();
        if (CommonUtils.isNotEmpty(keyword)) {
            conditions.add(new Condition("name", Operators.LIKE, keyword));
        }
        Page<WaterMeter> pageInfo = new Page<>();
        if (ExportType.METER_NORMAL.equals(flagType)) {
            pageInfo = WaterMeter.me.getNormalMeterPage(getPage(), GlobalConfig.EXPORT_SUM, keyword, this.getOrderbyStr());
        } else if (ExportType.METER_STOP.equals(flagType)) {
            pageInfo = WaterMeter.me.getStopMeterPage(getPage(), GlobalConfig.EXPORT_SUM, keyword, this.getOrderbyStr());
        } else if (ExportType.METER_EXCEPTION.equals(flagType)) {
            pageInfo = WaterMeter.me.getExceptionWaterMeterPage(getPage(), GlobalConfig.EXPORT_SUM, keyword, this.getOrderbyStr());
        } else if (ExportType.METER_DISABLE.equals(flagType)) {
            pageInfo = WaterMeter.me.getDisableWaterMeterPage(getPage(), GlobalConfig.EXPORT_SUM, keyword, this.getOrderbyStr());
        } else {
            pageInfo = WaterMeter.me.getWaterMeterPage(getPage(), GlobalConfig.EXPORT_SUM, keyword, this.getOrderbyStr());
        }
        List<WaterMeter> list = pageInfo.getList();
        setVoProp(list);
        WaterMeterExportService service = new WaterMeterExportService();
        String path = service.export(list);
        renderFile(new File(path));
    }

    private void setVoProp(List<WaterMeter> list) {
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictCode.WatersType);
            Map<String, Object> mapChargeType = DictData.dao.getDictMap(0, DictCode.ChargeType);
            Map<String, Object> mapWaterUseType = DictData.dao.getDictMap(0, DictCode.WaterUseType);
            for (int i = 0; i < list.size(); i++) {
                WaterMeter co = list.get(i);
                if (co.get("water_use_type") != null) {
                    co.put("waterUseTypeName", String.valueOf(mapWaterUseType.get(String.valueOf(co.get("water_use_type")))));
                }
                if (co.getWatersType() != null) {
                    co.put("watersTypeName", String.valueOf(mapWatersType.get(String.valueOf(co.getWatersType()))));

                }
                if (co.getChargeType() != null) {
                   co.put("chargeTypeName", String.valueOf(mapChargeType.get(String.valueOf(co.getChargeType()))));
                }
                list.set(i, co);
            }
        }
    }

    @RequiresPermissions(value={"/basic/meter"})
    public void importPage() {
        this.setAttr("uploadUrl", "meter");
        render("import_data.jsp");
    }

    @RequiresPermissions(value = {"/basic/meter"})
    public void downloadDemo() {
        renderFile(new File(PropKit.get("import.water.meter.demo.path")));
    }

    @RequiresPermissions(value = {"/basic/meter"})
    public void uploadImportData() {
        String dataStr= DateUtils.format(new Date(), "yyyyMMddHHmm");
        List<UploadFile> flist = this.getFiles("/temp", 1024 * 1024 * 50);
        Map<String,Object> data= Maps.newHashMap();
        if(flist.size()>0){
            UploadFile uf=flist.get(0);
            String status_url= PropKit.get("uploadMeterPath");
            String fileUrl=dataStr+"/"+uf.getFileName();
            String newFile=status_url+fileUrl;
            FileUtils.mkdir(newFile, false);
            FileUtils.copy(uf.getFile(), new File(newFile), BUFFER_SIZE);
            data.put("fileName", uf.getFileName());
            data.put("fileUrl", newFile);
            uf.getFile().delete();
            renderJson(data);
        }
    }

    @RequiresPermissions(value = {"/basic/meter"})
    public void importData() {
        String newFilePath = this.getPara("importUrl");
        WaterMeterExportService service = new WaterMeterExportService();
        try {
            List<Map<Integer, String>> maps = service.importExcel(newFilePath);
            WaterMeter.me.importData(maps);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.renderJson(InvokeResult.success());
    }

    @RequiresPermissions(value = {"/basic/meter"})
    public void findAddress() {
        String innerCode = this.getPara("innerCode");
        JSONObject result = new JSONObject();
        result.put("addressList", WaterMeter.me.findAddressByInnerCode(innerCode));
        this.renderJson(result);
    }

}
