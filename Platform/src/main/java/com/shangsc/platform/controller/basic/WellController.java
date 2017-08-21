package com.shangsc.platform.controller.basic;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.Well;
import com.shangsc.platform.util.CodeNumUtil;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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
        String keyword=this.getPara("name");
        Set<Condition> conditions=new HashSet<Condition>();
        if(CommonUtils.isNotEmpty(keyword)){
            conditions.add(new Condition("name", Operators.LIKE, keyword));
        }
        Page<Well> pageInfo = Well.me.getPage(getPage(), this.getRows(),conditions,this.getOrderby());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value={"/basic/well"})
    public void add() {
        Integer id = this.getParaToInt("id");
        if(id!=null){
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
        Long companyId = this.getParaToLong("companyId");
        String innerCode = this.getPara("innerCode");

        String township = this.getPara("township");
        String village = this.getPara("village");
        String address = this.getPara("address");
        BigDecimal wellDepth = CodeNumUtil.getBigDecimal(this.getPara("wellDepth"), 2);
        BigDecimal groundDepth = CodeNumUtil.getBigDecimal(this.getPara("groundDepth"), 2);

        //start_date

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

        Integer watersType = this.getParaToInt("watersType");

        String useEfficiency = this.getPara("useEfficiency");

        String method = this.getPara("method");

        Integer licence = this.getParaToInt("licence");
        String licenceCode = this.getPara("licenceCode");

        BigDecimal waterWithdrawals = CodeNumUtil.getBigDecimal(this.getPara("waterWithdrawals"), 2);

        InvokeResult result = Well.me.save(id, companyId, innerCode, name, wellNum, township, village, address, wellDepth, groundDepth, oneselfWell,
                innerDiameter, material, application, electromechanics, calculateWater, pumpModel, calculateType, aboveScale, geomorphicType,
                groundType,	nameCode, watersType, useEfficiency, method, licence, licenceCode, waterWithdrawals);
        this.renderJson(result);
    }

    @RequiresPermissions(value={"/basic/well"})
    public void delete(){
        Long id = this.getParaToLong("id");
        InvokeResult result = Well.me.deleteData(id);
        this.renderJson(result);
    }
}

