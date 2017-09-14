package com.shangsc.platform.controller.basic;

import com.google.common.collect.Maps;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.code.YesOrNo;
import com.shangsc.platform.conf.GlobalConfig;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.util.FileUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.export.WellExportService;
import com.shangsc.platform.model.DictData;
import com.shangsc.platform.model.Well;
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
public class WellController extends BaseController {

    @RequiresPermissions(value = {"/basic/well"})
    public void index() {
        render("well_index.jsp");
    }

    @RequiresPermissions(value={"/basic/well"})
    public void getListData() {
        String keyword = this.getPara("name");
        Page<Well> pageInfo = Well.me.getWellPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        List<Well> list = pageInfo.getList();
        setVoProp(list);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, list));
    }

    @RequiresPermissions(value={"/basic/well"})
    public void add() {
        Integer id = this.getParaToInt("id");
        if (id!=null) {
            this.setAttr("item", Well.me.findById(id));
        }
        this.setAttr("id", id);
        render("well_add.jsp");
    }

    @RequiresPermissions(value={"/basic/well"})
    public void save(){
        Long id = this.getParaToLong("id");
        String name = this.getPara("name");
        String wellNum = this.getPara("wellNum");
        String innerCode = this.getPara("innerCode");

        String village = this.getPara("village");
        String address = this.getPara("address");
        BigDecimal wellDepth = CodeNumUtil.getBigDecimal(this.getPara("wellDepth"), 2);
        BigDecimal groundDepth = CodeNumUtil.getBigDecimal(this.getPara("groundDepth"), 2);

        String year = this.getPara("year");

        Integer oneselfWell = this.getParaToInt("oneselfWell");
        BigDecimal innerDiameter = CodeNumUtil.getBigDecimal(this.getPara("innerDiameter"), 2);

        String material = this.getPara("material");
        String application = this.getPara("application");
        Integer electromechanics = this.getParaToInt("electromechanics");
        Integer calculateWater = this.getParaToInt("calculateWater");
        String pumpModel = this.getPara("pumpModel");

        Integer calculateType = this.getParaToInt("calculateType");
        Integer aboveScale = this.getParaToInt("aboveScale");
        Integer geomorphicType = this.getParaToInt("geomorphicType");
        Integer groundType = this.getParaToInt("groundType");
        String nameCode = this.getPara("nameCode");

        String watersType = this.getPara("watersType");

        String useEfficiency = this.getPara("useEfficiency");

        String method = this.getPara("method");

        Integer licence = this.getParaToInt("licence");
        String licenceCode = this.getPara("licenceCode");

        BigDecimal waterWithdrawals = CodeNumUtil.getBigDecimal(this.getPara("waterWithdrawals"), 2);

        InvokeResult result = Well.me.save(id, innerCode, name, wellNum, village, address, wellDepth, groundDepth, year,oneselfWell,
                innerDiameter, material, application, electromechanics, calculateWater, pumpModel, calculateType, aboveScale, geomorphicType,
                groundType,	nameCode, watersType, useEfficiency, method, licence, licenceCode, waterWithdrawals);
        this.renderJson(result);
    }

    @RequiresPermissions(value={"/basic/well"})
    public void delete(){
        String ids = this.getPara("ids");
        InvokeResult result = Well.me.deleteData(ids);
        this.renderJson(result);
    }

    @RequiresPermissions(value = {"/basic/well"})
    public void export() {
        String keyword = this.getPara("name");
        Page<Well> pageInfo = Well.me.getWellPage(getPage(), GlobalConfig.EXPORT_SUM, keyword, this.getOrderbyStr());
        List<Well> list = pageInfo.getList();
        setVoProp(list);
        WellExportService service = new WellExportService();
        String path = service.export(list);
        renderFile(new File(path));
    }

    private void setVoProp(List<Well> list) {
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapCalculateType = DictData.dao.getDictMap(0, DictCode.CalculateType);
            Map<String, Object> mapGeomorphicType = DictData.dao.getDictMap(0, DictCode.GeomorphicType);
            Map<String, Object> mapGroundType = DictData.dao.getDictMap(0, DictCode.GroundType);
            Map<String, Object> mapStreetType = DictData.dao.getDictMap(0, DictCode.Street);
            for (int i = 0; i < list.size(); i++) {
                Well co = list.get(i);
                co.put("streetName", String.valueOf(mapStreetType.get(String.valueOf(co.get("street")))));
                co.put("aboveScaleName",  YesOrNo.getYesOrNoMap().get(String.valueOf(co.getAboveScale())));
                co.put("oneselfWellName", YesOrNo.getYesOrNoMap().get(String.valueOf(co.getOneselfWell())));
                co.put("electromechanicsName", YesOrNo.getYesOrNoMap().get(String.valueOf(co.getElectromechanics())));
                co.put("calculateWaterName", YesOrNo.getYesOrNoMap().get(String.valueOf(co.getCalculateType())));
                co.put("licenceName", YesOrNo.getYesOrNoMap().get(String.valueOf(co.getLicence())));
                co.put("calculateTypeName", String.valueOf(mapCalculateType.get(String.valueOf(co.getCalculateType()))));
                co.put("geomorphicTypeName", String.valueOf(mapGeomorphicType.get(String.valueOf(co.getGeomorphicType()))));
                co.put("groundTypeName", String.valueOf(mapGroundType.get(String.valueOf(co.getGroundType()))));
                list.set(i, co);
            }
        }
    }

    @RequiresPermissions(value={"/basic/well"})
    public void importPage() {
        this.setAttr("uploadUrl", "well");
        render("import_data.jsp");
    }

    @RequiresPermissions(value = {"/basic/well"})
    public void downloadDemo() {
        renderFile(new File(PropKit.get("import.water.well.demo.path")));
    }

    @RequiresPermissions(value = {"/basic/well"})
    public void uploadImportData() {
        String dataStr= DateUtils.format(new Date(), "yyyyMMddHHmm");
        List<UploadFile> flist = this.getFiles("/temp", 1024 * 1024 * 50);
        Map<String,Object> data= Maps.newHashMap();
        if(flist.size()>0){
            UploadFile uf=flist.get(0);
            String status_url= PropKit.get("uploadWellPath");
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

    @RequiresPermissions(value = {"/basic/well"})
    public void importData() {
        String newFilePath = this.getPara("importUrl");
        WellExportService service = new WellExportService();
        try {
            List<Map<Integer, String>> maps = service.importExcel(newFilePath);
            Well.me.importData(maps);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.renderJson(InvokeResult.success());
    }

}

