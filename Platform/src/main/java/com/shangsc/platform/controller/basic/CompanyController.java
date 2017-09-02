package com.shangsc.platform.controller.basic;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.conf.GlobalConfig;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.export.CompanyExportService;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.DictData;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
        Page<Company> pageInfo = Company.me.getCompanyPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        List<Company> companies = pageInfo.getList();
        if (CommonUtils.isNotEmpty(companies)) {
            Map<String, Object> mapUserType = DictData.dao.getDictMap(0, DictData.UserType);
            Map<String, Object> mapWaterUseType = DictData.dao.getDictMap(0, DictData.WaterUseType);
            Map<String, Object> mapUintType = DictData.dao.getDictMap(0, DictData.UnitType);
            for (int i = 0; i < companies.size(); i++) {
                Company co = companies.get(i);
                co.put("customerTypeName", String.valueOf(mapUserType.get(String.valueOf(co.getCustomerType()))));
                co.put("waterUseTypeName", String.valueOf(mapWaterUseType.get(String.valueOf(co.getWaterUseType()))));
                co.put("unitTypeName", String.valueOf(mapUintType.get(String.valueOf(co.getUnitType()))));
                co.setAddress("<a href='#' title='点击查看导航地图' style='cursor: pointer' onclick=\"openMap('" + co.getName() + "', '"
                        + co.getAddress() + "', '" + co.get("waterUseNum") + "'" + ")\">" + co.getAddress() + "</a>");
                companies.set(i, co);
            }
        }
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, companies)  );
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
        String ids = this.getPara("ids");
        InvokeResult result = Company.me.deleteData(ids);
        this.renderJson(result);
    }

    @RequiresPermissions(value = {"/basic/company"})
    public void export() {
        CompanyExportService service = new CompanyExportService();
        String keyword = this.getPara("name");
        Set<Condition> conditions=new HashSet<Condition>();
        if(CommonUtils.isNotEmpty(keyword)){
            conditions.add(new Condition("name", Operators.LIKE, keyword));
        }
        Page<Company> pageInfo = Company.me.getCompanyPage(getPage(), GlobalConfig.EXPORT_SUM, keyword, this.getOrderbyStr());
                Company.me.getPage(getPage(), GlobalConfig.EXPORT_SUM, conditions, this.getOrderby());
        List<Company> companies = pageInfo.getList();
        if (CommonUtils.isNotEmpty(companies)) {
            Map<String, Object> mapUserType = DictData.dao.getDictMap(0, DictData.UserType);
            Map<String, Object> mapWaterUseType = DictData.dao.getDictMap(0, DictData.WaterUseType);
            Map<String, Object> mapUintType = DictData.dao.getDictMap(0, DictData.UnitType);
            for (int i = 0; i < companies.size(); i++) {
                Company co = companies.get(i);
                co.put("customerTypeName", String.valueOf(mapUserType.get(String.valueOf(co.getCustomerType()))));
                co.put("waterUseTypeName", String.valueOf(mapWaterUseType.get(String.valueOf(co.getWaterUseType()))));
                co.put("unitTypeName", String.valueOf(mapUintType.get(String.valueOf(co.getUnitType()))));
                companies.set(i, co);
            }
        }
        String path = service.export(companies);
        renderFile(new File(path));
    }
}
