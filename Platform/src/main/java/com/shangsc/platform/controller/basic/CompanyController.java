package com.shangsc.platform.controller.basic;

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
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.util.FileUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.export.CompanyExportService;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.DictData;
import com.shangsc.platform.util.CodeNumUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/basic/company"})
    public void position() {
        this.setAttr("address", this.getPara("address"));
        this.setAttr("position", this.getPara("position"));
        render("company_map.jsp");
    }
    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/basic/company"})
    public void normal() {
        this.setAttr("flag", "Normal");
        render("company_index.jsp");
    }
    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/basic/company"})
    public void warn() {
        this.setAttr("flag", "Warn");
        render("company_index.jsp");
    }
    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/basic/company"})
    public void other() {
        this.setAttr("flag", "Other");
        render("company_index.jsp");
    }
    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value = {"/basic/company"})
    public void supply() {
        this.setAttr("flag", "Supply");
        render("company_index.jsp");
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value={"/basic/company"})
    public void getListData() {
        String keyword=this.getPara("name");
        String companyType=this.getPara("companyType");
        Page<Company> pageInfo = Company.me.getCompanyPage(getPage(), this.getRows(), keyword, this.getOrderbyStr(), companyType);
        List<Company> companies = pageInfo.getList();
        setVoProp(companies);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, companies)  );
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value={"/basic/company"})
    public void getNormalListData() {
        String keyword=this.getPara("name");
        Page<Company> pageInfo = Company.me.getNormalCompanyPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        List<Company> companies = pageInfo.getList();
        setVoProp(companies);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, companies)  );
    }

    @RequiresPermissions(value={"/basic/company"})
    public void getWarnListData() {
        String keyword=this.getPara("name");
        Page<Company> pageInfo = Company.me.getWarnCompanyPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        List<Company> companies = pageInfo.getList();
        setVoProp(companies);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, companies)  );
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value={"/basic/company"})
    public void getOtherListData() {
        String keyword=this.getPara("name");
        Page<Company> pageInfo = Company.me.getOtherCompanyPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        List<Company> companies = pageInfo.getList();
        setVoProp(companies);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, companies)  );
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value={"/basic/company"})
    public void getSupplyListData() {
        String keyword=this.getPara("name");
        Page<Company> pageInfo = Company.me.getSupplyCompanyPage(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        List<Company> companies = pageInfo.getList();
        setVoProp(companies);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, companies)  );
    }

    @RequiresPermissions(value={"/basic/company"})
    public void add() {
        Integer id = this.getParaToInt("id");
        if(id!=null){
            Company company = Company.me.findById(id);
            this.setAttr("item", company);
            if(company.getLongitude() != null && company.getLatitude() != null) {
                this.setAttr("position", company.getLongitude() + "," + company.getLatitude());
            }
        }
        this.setAttr("id", id);
        render("company_add.jsp");
    }

    @RequiresPermissions(value={"/basic/company"})
    public void save(){
        Long id = this.getParaToLong("id");
        String innerCode = StringUtils.trim(this.getPara("innerCode"));
        String name = this.getPara("name");
        String waterUnit = this.getPara("waterUnit");
        String county = this.getPara("county");
        String streetSrc = this.getPara("streetSrc");
        Integer street = this.getParaToInt("street");
        String address = this.getPara("address");
        Integer customerType = this.getParaToInt("customerType");
        Integer company_type = this.getParaToInt("company_type");
        String gbIndustry = this.getPara("gb_industry");
        String mainIndustry = this.getPara("main_industry");
        Integer waterUseType = this.getParaToInt("waterUseType");
        String contact = this.getPara("contact");
        String phone = this.getPara("phone");
        String postalCode = this.getPara("postalCode");
        String department = this.getPara("department");
        Integer wellCount = this.getParaToInt("wellCount");
        Integer firstWatermeterCount = this.getParaToInt("firstWatermeterCount");
        Integer remotemeterCount = this.getParaToInt("remotemeterCount");
        Integer unitType = this.getParaToInt("unitType");
        String position = this.getPara("position");

        Date createTime = null;
        if (StringUtils.isNotEmpty(this.getPara("createTime"))) {
            createTime = DateUtils.parseDate(this.getPara("createTime"));
        }

        BigDecimal longitude = null;
        BigDecimal latitude = null;
        if (StringUtils.isNotEmpty(position)) {
            longitude = CodeNumUtil.getBigDecimal(position.split(",")[0], 6);
            latitude = CodeNumUtil.getBigDecimal(position.split(",")[1], 6);
        }
        BigDecimal self_well_price = null;
        if (StringUtils.isNotEmpty(this.getPara("self_well_price"))) {
            self_well_price = CodeNumUtil.getBigDecimal(this.getPara("self_well_price"), 2);
        }
        BigDecimal surface_price = null;
        if (StringUtils.isNotEmpty(this.getPara("surface_price"))) {
            surface_price = CodeNumUtil.getBigDecimal(this.getPara("surface_price"), 2);
        }
        BigDecimal self_free_price = null;
        if (StringUtils.isNotEmpty(this.getPara("self_free_price"))) {
            self_free_price = CodeNumUtil.getBigDecimal(this.getPara("self_free_price"), 2);
        }
        InvokeResult result = Company.me.save(id, name, innerCode, waterUnit, county, street, streetSrc, address,
                customerType, waterUseType, gbIndustry, mainIndustry, contact, phone, postalCode, department,
                wellCount, firstWatermeterCount, remotemeterCount, unitType, longitude, latitude, createTime,
                self_well_price, surface_price, self_free_price, company_type);
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
        String companyType = this.getPara("companyType");
        String flagType = this.getPara("flagType");
        Page<Company> pageInfo = new Page<>();
        if (ExportType.COMPANY_NORMAL.equals(flagType)) {
            pageInfo = Company.me.getNormalCompanyPage(getPage(), GlobalConfig.EXPORT_SUM, keyword, this.getOrderbyStr());
        } else if (ExportType.COMPANY_SUPPLY.equals(flagType)) {
            pageInfo = Company.me.getSupplyCompanyPage(getPage(), GlobalConfig.EXPORT_SUM, keyword, this.getOrderbyStr());
        } else if (ExportType.COMPANY_WARN.equals(flagType)) {
            pageInfo = Company.me.getWarnCompanyPage(getPage(), GlobalConfig.EXPORT_SUM, keyword, this.getOrderbyStr());
        } else {
            pageInfo = Company.me.getCompanyPage(getPage(), GlobalConfig.EXPORT_SUM, keyword, this.getOrderbyStr(), companyType);
        }
        List<Company> companies = pageInfo.getList();
        if (CommonUtils.isNotEmpty(companies)) {
            Map<String, Object> mapUserType = DictData.dao.getDictMap(0, DictCode.UserType);
            Map<String, Object> mapWaterUseType = DictData.dao.getDictMap(0, DictCode.WaterUseType);
            Map<String, Object> mapUintType = DictData.dao.getDictMap(0, DictCode.UnitType);
            Map<String, Object> mapStreetType = DictData.dao.getDictMap(0, DictCode.Street);
            for (int i = 0; i < companies.size(); i++) {
                Company co = companies.get(i);
                if (co.getCustomerType() != null) {
                    co.put("customerTypeName", String.valueOf(mapUserType.get(String.valueOf(co.getCustomerType()))));
                }
                if (co.getWaterUseType() != null) {
                    co.put("waterUseTypeName", String.valueOf(mapWaterUseType.get(String.valueOf(co.getWaterUseType()))));
                }
                if (co.getUnitType() != null) {
                    co.put("unitTypeName", String.valueOf(mapUintType.get(String.valueOf(co.getUnitType()))));
                }
                if (co.getStreet() != null) {
                    co.put("streetName", String.valueOf(mapStreetType.get(String.valueOf(co.getStreet()))));
                }
                companies.set(i, co);
            }
        }
        String path = service.export(companies);
        renderFile(new File(path));
    }

    private void setVoProp(List<Company> companies){
        if (CommonUtils.isNotEmpty(companies)) {
            Map<String, Object> mapUserType = DictData.dao.getDictMap(0, DictCode.UserType);
            Map<String, Object> mapWaterUseType = DictData.dao.getDictMap(0, DictCode.WaterUseType);
            Map<String, Object> mapUintType = DictData.dao.getDictMap(0, DictCode.UnitType);
            Map<String, Object> mapStreetType = DictData.dao.getDictMap(0, DictCode.Street);
            for (int i = 0; i < companies.size(); i++) {
                Company co = companies.get(i);
                if (co.getCustomerType() != null) {
                    co.put("customerTypeName", String.valueOf(mapUserType.get(String.valueOf(co.getCustomerType()))));
                }
                if (co.getWaterUseType() != null) {
                    co.put("waterUseTypeName", String.valueOf(mapWaterUseType.get(String.valueOf(co.getWaterUseType()))));
                }
                if (co.getUnitType() != null) {
                    co.put("unitTypeName", String.valueOf(mapUintType.get(String.valueOf(co.getUnitType()))));
                }
                if (co.getStreet() != null) {
                    co.put("streetName", String.valueOf(mapStreetType.get(String.valueOf(co.getStreet()))));
                }
                if (co.getAddress() != null) {
                    co.setAddress("<a href='#' title='点击查看导航地图' style='cursor: pointer' onclick=\"openMap('"
                            + co.get("inner_code") + "')\">" + co.getAddress() + "</a>");
                }
                companies.set(i, co);
            }
        }
    }

    @RequiresPermissions(value={"/basic/company"})
    public void importPage() {
        this.setAttr("uploadUrl", "company");
        render("import_data.jsp");
    }

    @RequiresPermissions(value = {"/basic/company"})
    public void downloadDemo() {
        renderFile(new File(PropKit.get("import.water.company.demo.path")));
    }

    @RequiresPermissions(value = {"/basic/company"})
    public void uploadImportData() {
        String dataStr= DateUtils.format(new Date(), "yyyyMMddHHmm");
        List<UploadFile> flist = this.getFiles("/temp", 1024 * 1024 * 50);
        Map<String,Object> data= Maps.newHashMap();
        if(flist.size()>0){
            UploadFile uf=flist.get(0);
            String status_url= PropKit.get("uploadCompanyPath");
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

    @RequiresPermissions(value = {"/basic/company"})
    public void importData() {
        String newFilePath = this.getPara("importUrl");
        CompanyExportService service = new CompanyExportService();
        try {
            List<Map<Integer, String>> maps = service.importExcel(newFilePath);
            Company.me.importData(maps);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.renderJson(InvokeResult.success());
    }

}
