package com.shangsc.platform.controller.basic;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.Company;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author ssc
 * @Date 2017/8/20 16:38
 * @Version 1.0.0
 * @Desc
 */
public class CompanyController extends BaseController {

    @RequiresPermissions(value={"/basic/company"})
    public void index() {
        render("company_index.jsp");
    }

    @RequiresPermissions(value={"/basic/company"})
    public void getListData() {
        String keyword=this.getPara("name");
        Set<Condition> conditions=new HashSet<Condition>();
        if(CommonUtils.isNotEmpty(keyword)){
            conditions.add(new Condition("name", Operators.LIKE, keyword));
        }
        Page<Company> pageInfo = Company.me.getPage(getPage(), this.getRows(),conditions,this.getOrderby());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value={"/basic/company"})
    public void add() {
        Integer id = this.getParaToInt("id");
        if(id!=null){
            this.setAttr("item", Company.me.findById(id));
        }
        this.setAttr("id", id);
        render("company_add.jsp");
    }

    @RequiresPermissions(value={"/basic/company"})
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

    @RequiresPermissions(value={"/basic/company"})
    public void delete(){
        Long id = this.getParaToLong("id");
        InvokeResult result = Company.me.deleteData(id);
        this.renderJson(result);
    }
}
