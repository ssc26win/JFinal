package com.shangsc.platform.controller.basic;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.WaterMeter;

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

    @RequiresPermissions(value={"/basic/meter"})
    public void getListData() {
        String keyword=this.getPara("name");
        Page<WaterMeter> pageInfo = WaterMeter.me.getWaterMeterPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value={"/basic/meter"})
    public void add() {
        Integer id = this.getParaToInt("id");
        if(id!=null){
            this.setAttr("item", WaterMeter.me.findById(id));
        }
        this.setAttr("id", id);
        render("meter_add.jsp");
    }

    @RequiresPermissions(value={"/basic/meter"})
    public void save(){
        Long id = this.getParaToLong("id");
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
        String street = this.getPara("street");
        String address = this.getPara("address");
        Integer customerType = this.getParaToInt("customerType");
        Integer waterUseType = this.getParaToInt("waterUseType");
        String contact = this.getPara("contact");
        String phone = this.getPara("phone");
        String postalCode = this.getPara("postalCode");
        String department = this.getPara("department");
        Integer wellCount = this.getParaToInt("wellCount");
        Integer firstWatermeterCount = this.getParaToInt("firstWatermeterCount");
        Integer remotemeterCount = this.getParaToInt("remotemeterCount");
        Integer unitType = this.getParaToInt("unitType");
        InvokeResult result = Company.me.save(id, name, innerCode, street, address, customerType, waterUseType,
                contact, phone, postalCode, department, wellCount, firstWatermeterCount, remotemeterCount, unitType);
        this.renderJson(result);
    }

    @RequiresPermissions(value={"/basic/meter"})
    public void delete(){
        Long id = this.getParaToLong("id");
        InvokeResult result = WaterMeter.me.deleteData(id);
        this.renderJson(result);
    }
}
