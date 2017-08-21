package com.shangsc.platform.controller.statis;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.WaterMeter;
import com.shangsc.platform.util.CodeNumUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author ssc
 * @Date 2017/8/20 21:27
 * @Version 1.0.0
 * @Desc 实时数据
 */
public class ActualController extends BaseController {

    @RequiresPermissions(value={"/basic/actual"})
    public void index() {
        render("actual_data.jsp");
    }

    @RequiresPermissions(value={"/basic/actual"})
    public void getListData() {
        String keyword=this.getPara("name");
        Set<Condition> conditions=new HashSet<Condition>();
        if(CommonUtils.isNotEmpty(keyword)){
            conditions.add(new Condition("name", Operators.LIKE, keyword));
        }
        Page<ActualData> pageInfo = ActualData.me.getPage(getPage(), this.getRows(),conditions,this.getOrderby());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    /**
     * 实时数据接口
     */
    @RequiresPermissions(value={"/basic/actual"})
    public void add() {
        Integer id = this.getParaToInt("id");
        if(id!=null){
            this.setAttr("item", ActualData.me.findById(id));
        }
        this.setAttr("id", id);
        render("actual_add.jsp");
    }

    @RequiresPermissions(value={"/basic/actual"})
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
        Long id = this.getParaToLong("id");
        InvokeResult result = ActualData.me.deleteData(id);
        this.renderJson(result);
    }
}
