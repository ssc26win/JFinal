package com.shangsc.platform.controller.basic;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.Ad;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author ssc
 * @Date 2017/8/21 15:45
 * @Desc 用途：
 */
public class AdController extends BaseController {

    @RequiresPermissions(value={"/basic/ad"})
    public void index() {
        render("ad_index.jsp");
    }

    @RequiresPermissions(value={"/basic/ad"})
    public void getListData() {
        String keyword=this.getPara("innerCode");
        Set<Condition> conditions=new HashSet<Condition>();
        if(CommonUtils.isNotEmpty(keyword)){
            conditions.add(new Condition("inner_code", Operators.LIKE, keyword));
        }
        Page<Ad> pageInfo = Ad.dao.getPage(getPage(), this.getRows(),conditions,this.getOrderby());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value={"/basic/ad"})
    public void add() {
        Integer id = this.getParaToInt("id");
        if(id!=null){
            this.setAttr("item", Ad.dao.findById(id));
        }
        this.setAttr("id", id);
        render("ad_add.jsp");
    }

    @RequiresPermissions(value={"/basic/ad"})
    public void save(){
        Long id = this.getParaToLong("id");
        String title = this.getPara("title");
        String content = this.getPara("content");
        String imgUrl = this.getPara("imgUrl");
        Integer status = this.getParaToInt("status");
        InvokeResult result = Ad.dao.save(id, title, content, imgUrl, status);
        this.renderJson(result);
    }

    @RequiresPermissions(value={"/basic/ad"})
    public void delete(){
        Long id = this.getParaToLong("id");
        InvokeResult result = Ad.dao.deleteData(id);
        this.renderJson(result);
    }
}
