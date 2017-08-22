package com.shangsc.platform.controller.statis;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.model.ReadNum;

import java.io.File;
import java.util.Date;

/**
 * @Author ssc
 * @Date 2017/8/20 19:12
 * @Version 1.0.0
 * @Desc
 */
public class ReadnumStatisController extends BaseController {

    private ReadNum dataStatis = new ReadNum();

    @RequiresPermissions(value={"/statis/readnum"})
    public void index() {
        render("read_num.jsp");
    }

    @RequiresPermissions(value={"/statis/readnum"})
    public void getListData() {
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
        Date startTime = this.getParaToDate("startTime");
        Date endTime = this.getParaToDate("endTime");

        Page<?> pageInfo = dataStatis.getReadnumStatis(getPage(), getRows(), getOrderbyStr(),
                startTime, endTime, name, innerCode);

        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value={"/statis/readnum"})
    public void exportData() {
        String name = this.getPara("name");
        String innerCode = this.getPara("innerCode");
        Date startTime = this.getParaToDate("startTime");
        Date endTime = this.getParaToDate("endTime");

        dataStatis.exportReadNumData(getPage(), getRows(), getOrderbyStr(),
                startTime, endTime, name, innerCode);
        File file = new File("");
        this.renderFile(file);
    }
}
