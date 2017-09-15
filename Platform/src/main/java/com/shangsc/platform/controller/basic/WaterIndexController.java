package com.shangsc.platform.controller.basic;

import com.google.common.collect.Maps;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.conf.GlobalConfig;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.util.FileUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.export.WaterIndexExportService;
import com.shangsc.platform.model.DictData;
import com.shangsc.platform.model.WaterIndex;
import com.shangsc.platform.util.CodeNumUtil;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author ssc
 * @Date 2017/8/20 16:39
 * @Version 1.0.0
 * @Desc
 */
public class WaterIndexController extends BaseController {

    @RequiresPermissions(value = {"/basic/waterindex"})
    public void index() {
        render("water_index.jsp");
    }

    @RequiresPermissions(value={"/basic/waterindex"})
    public void getListData() {
        String keyword=this.getPara("name");
        Page<WaterIndex> pageInfo = WaterIndex.me.getWaterIndexPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        List<WaterIndex> list = pageInfo.getList();
        setVoProp(list);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, list));
    }

    @RequiresPermissions(value={"/basic/waterindex"})
    public void add() {
        Integer id = this.getParaToInt("id");
        if(id!=null){
            this.setAttr("item", WaterIndex.me.findById(id));
        }
        this.setAttr("id", id);
        render("water_index_add.jsp");
    }

    @RequiresPermissions(value={"/basic/waterindex"})
    public void save(){
        Long id = this.getParaToLong("id");
        String innerCode = this.getPara("innerCode");
        Integer watersType = this.getParaToInt("watersType");
        BigDecimal waterIndex =  CodeNumUtil.getBigDecimal(this.getPara("waterIndex"), 2);
        BigDecimal january = CodeNumUtil.getBigDecimal(this.getPara("january"), 2);
        BigDecimal february = CodeNumUtil.getBigDecimal(this.getPara("february"), 2);
        BigDecimal march = CodeNumUtil.getBigDecimal(this.getPara("march"), 2);
        BigDecimal april = CodeNumUtil.getBigDecimal(this.getPara("april"), 2);
        BigDecimal may = CodeNumUtil.getBigDecimal(this.getPara("may"), 2);
        BigDecimal june = CodeNumUtil.getBigDecimal(this.getPara("june"), 2);
        BigDecimal july = CodeNumUtil.getBigDecimal(this.getPara("july"), 2);
        BigDecimal august = CodeNumUtil.getBigDecimal(this.getPara("august"), 2);
        BigDecimal september = CodeNumUtil.getBigDecimal(this.getPara("september"), 2);
        BigDecimal october = CodeNumUtil.getBigDecimal(this.getPara("october"), 2);
        BigDecimal november = CodeNumUtil.getBigDecimal(this.getPara("november"), 2);
        BigDecimal december = CodeNumUtil.getBigDecimal(this.getPara("december"), 2);
        BigDecimal totalIndex = new BigDecimal(0.00);
        totalIndex = totalIndex.add(january);
        totalIndex = totalIndex.add(february);
        totalIndex = totalIndex.add(march);
        totalIndex = totalIndex.add(april);
        totalIndex = totalIndex.add(may);
        totalIndex = totalIndex.add(june);
        totalIndex = totalIndex.add(july);
        totalIndex = totalIndex.add(august);
        totalIndex = totalIndex.add(september);
        totalIndex = totalIndex.add(october);
        totalIndex = totalIndex.add(november);
        totalIndex = totalIndex.add(december);
        if (waterIndex.compareTo(totalIndex) != 0) {
            this.renderJson(InvokeResult.failure("年用水指标不等于各月指标之和"));
            return;
        }
        InvokeResult result = WaterIndex.me.save(id, innerCode, watersType, waterIndex,
                january, february, march, april, may, june, july, august, september, october, november, december);
        this.renderJson(result);
    }

    @RequiresPermissions(value={"/basic/waterindex"})
    public void delete(){
        String ids = this.getPara("ids");
        InvokeResult result = WaterIndex.me.deleteData(ids);
        this.renderJson(result);
    }

    @RequiresPermissions(value = {"/basic/waterindex"})
    public void export() {
        String keyword=this.getPara("name");
        Page<WaterIndex> pageInfo = WaterIndex.me.getWaterIndexPage(getPage(), GlobalConfig.EXPORT_SUM, keyword, this.getOrderbyStr());
        List<WaterIndex> list = pageInfo.getList();
        setVoProp(list);
        WaterIndexExportService service = new WaterIndexExportService();
        String path = service.export(list);
        renderFile(new File(path));
    }

    @RequiresPermissions(value={"/basic/waterindex"})
    public void importPage() {
        this.setAttr("uploadUrl", "waterindex");
        render("import_data.jsp");
    }

    @RequiresPermissions(value = {"/basic/waterindex"})
    public void downloadDemo() {
        renderFile(new File(PropKit.get("import.water.index.demo.path")));
    }

    @RequiresPermissions(value = {"/basic/waterindex"})
    public void uploadImportData() {
        String dataStr= DateUtils.format(new Date(), "yyyyMMddHHmm");
        List<UploadFile> flist = this.getFiles("/temp", 1024 * 1024 * 50);
        Map<String,Object> data= Maps.newHashMap();
        if(flist.size()>0){
            UploadFile uf=flist.get(0);
            String status_url= PropKit.get("uploadWaterIndexPath");
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

    @RequiresPermissions(value = {"/basic/waterindex"})
    public void importData() {
        String newFilePath = this.getPara("importUrl");
        WaterIndexExportService service = new WaterIndexExportService();
        try {
            List<Map<Integer, String>> maps = service.importExcel(newFilePath);
            WaterIndex.me.importData(maps);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.renderJson(InvokeResult.success());
    }

    private void setVoProp(List<WaterIndex> list) {
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWaterUseType = DictData.dao.getDictMap(0, DictCode.WatersType);
            for (int i = 0; i < list.size(); i++) {
                WaterIndex co = list.get(i);
                if (co.getWatersType() != null) {
                    co.put("watersTypeName", String.valueOf(mapWaterUseType.get(String.valueOf(co.getWatersType()))));
                }
                list.set(i, co);
            }
        }
    }
}
