package com.shangsc.platform.controller.basic;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.conf.GlobalConfig;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.export.WaterIndexExportService;
import com.shangsc.platform.model.DictData;
import com.shangsc.platform.model.WaterIndex;
import com.shangsc.platform.util.CodeNumUtil;

import java.io.File;
import java.math.BigDecimal;
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
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWaterUseType = DictData.dao.getDictMap(0, DictData.WaterUseType);
            for (int i = 0; i < list.size(); i++) {
                WaterIndex co = list.get(i);
                co.put("waterUseTypeName", String.valueOf(mapWaterUseType.get(String.valueOf(co.getWaterUseType()))));
                list.set(i, co);
            }
        }
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
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
        Long companyId = this.getParaToLong("companyId");
        String innerCode = this.getPara("innerCode");
        String waterUseType = this.getPara("waterUseType");
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

        InvokeResult result = WaterIndex.me.save(id, companyId, innerCode, waterUseType, waterIndex,
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
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWaterUseType = DictData.dao.getDictMap(0, DictData.WaterUseType);
            for (int i = 0; i < list.size(); i++) {
                WaterIndex co = list.get(i);
                co.put("waterUseTypeName", String.valueOf(mapWaterUseType.get(String.valueOf(co.getWaterUseType()))));
                list.set(i, co);
            }
        }
        WaterIndexExportService service = new WaterIndexExportService();
        String path = service.export(list);
        renderFile(new File(path));
    }
}
