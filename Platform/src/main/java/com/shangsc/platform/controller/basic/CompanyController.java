package com.shangsc.platform.controller.basic;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.SysUser;

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
        String username=this.getPara("name");
        String password=this.getPara("password");
        String phone=this.getPara("phone");
        String email=this.getPara("email");
        Integer id=this.getParaToInt("id");
        String des=this.getPara("des");
        InvokeResult result=SysUser.me.save(id, username, password, des,phone, email);
        this.renderJson(result);
    }

    @RequiresPermissions(value={"/basic/company"})
    public void delete(){
        String username=this.getPara("name");
        String password=this.getPara("password");
        String phone=this.getPara("phone");
        String email=this.getPara("email");
        Integer id=this.getParaToInt("id");
        String des=this.getPara("des");
        InvokeResult result=SysUser.me.save(id, username, password, des,phone, email);
        this.renderJson(result);
    }
}
