package com.shangsc.platform.controller.statis;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.code.YesOrNo;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.DictData;
import com.shangsc.platform.util.CodeNumUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author ssc
 * @Date 2017/8/20 21:27
 * @Version 1.0.0
 * @Desc 实时数据
 */
public class ActualController extends BaseController {

    @RequiresPermissions(value={"/statis/actual"})
    public void index() {
        render("actual_data.jsp");
    }

    @RequiresPermissions(value={"/statis/actual"})
    public void getListData() {
        String keyword=this.getPara("name");
        Page<ActualData> pageInfo = ActualData.me.getActualDataPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        List<ActualData> list = pageInfo.getList();
        if (CommonUtils.isNotEmpty(list)) {
            Map<String, Object> mapWatersType = DictData.dao.getDictMap(0, DictData.WatersType);
            for (int i = 0; i < list.size(); i++) {
                ActualData co = list.get(i);
                co.put("watersTypeName", String.valueOf(mapWatersType.get(String.valueOf(co.getWatersType()))));
                co.put("alarm", YesOrNo.getYesOrNoMap().get(String.valueOf(co.getAlarm())));
                list.set(i, co);
            }
        }
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, list));
    }

    /**
     * 实时数据接口
     */
    @RequiresPermissions(value={"/statis/actual"})
    public void add() {
        Integer id = this.getParaToInt("id");
        if(id!=null){
            this.setAttr("item", ActualData.me.findById(id));
        }
        this.setAttr("id", id);
        render("actual_add.jsp");
    }

    @RequiresPermissions(value={"/statis/actual"})
    public void save(){
        Long id = this.getParaToLong("id");
        Long companyId = this.getParaToLong("companyId");
        String innerCode = this.getPara("innerCode");
        String lineNum = this.getPara("lineNum");
        String meterNum = this.getPara("meterNum");
        Integer watersType = this.getParaToInt("watersType");
        String alarm = this.getPara("alarm");
        BigDecimal netWater = CodeNumUtil.getBigDecimal(this.getPara("netWater"), 2);
        Integer state = this.getParaToInt("state");
        String voltage = this.getPara("voltage");
        InvokeResult result = ActualData.me.save(id, companyId, innerCode, lineNum, meterNum,
                watersType, alarm, netWater, state, voltage);
        this.renderJson(result);
    }

    @RequiresPermissions(value={"/basic/actual"})
    public void delete(){
        String ids = this.getPara("ids");
        InvokeResult result = ActualData.me.deleteData(ids);
        this.renderJson(result);
    }
}
