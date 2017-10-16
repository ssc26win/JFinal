package com.shangsc.platform.controller.statis;

import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.model.ActualLog;

import java.util.List;

/**
 * @Author ssc
 * @Date 2017/10/16 14:38
 * @Desc 用途：
 */
public class ActuallogController extends BaseController {

    @RequiresPermissions(value={"/statis/actuallog"})
    public void index() {
        render("actual_log.jsp");
    }

    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value={"/statis/actuallog"})
    public void getListData() {
        String keyword = this.getPara("name");
        String srcType = this.getPara("srcType");
        Page<ActualLog> pageInfo = ActualLog.dao.getLogPage(getPage(), this.getRows(), keyword, srcType, this.getOrderbyStr());
        List<ActualLog> logs = pageInfo.getList();
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo, logs)  );
    }
    @Clear(AuthorityInterceptor.class)
    @RequiresPermissions(value={"/statis/actuallog"})
    public void showContent() {
        Integer id = this.getParaToInt("id");
        if(id!=null){
            this.setAttr("item", ActualLog.dao.findById(id));
        }
        this.setAttr("id", id);
        render("actual_log_content.jsp");
    }

}
