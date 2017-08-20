package com.shangsc.platform.controller;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.model.ActualData;

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
        //String username=this.getPara("name");
        //String password=this.getPara("password");
        //String phone=this.getPara("phone");
        //String email=this.getPara("email");
        //Integer id=this.getParaToInt("id");
        //String des=this.getPara("des");
        //InvokeResult result=SysUser.me.save(id, username, password, des,phone, email);
        //this.renderJson(result);

    }

}
