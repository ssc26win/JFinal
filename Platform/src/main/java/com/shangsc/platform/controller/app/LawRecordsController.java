package com.shangsc.platform.controller.app;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.code.YesOrNo;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.IWebUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.Image;
import com.shangsc.platform.model.LawRecord;
import com.shangsc.platform.model.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @Author ssc
 * @Date 2018/8/16 11:37
 * @Desc 用途：
 */
public class LawRecordsController extends BaseController {

    @RequiresPermissions(value = {"/app/law"})
    public void index() {
        render("law_index.jsp");
    }

    @RequiresPermissions(value = {"/app/law"})
    public void getListData() {
        LawRecord.dao.setGlobalInnerCode(getInnerCode());
        String keyword = this.getPara("name");
        Page<LawRecord> pageInfo = LawRecord.dao.getPageList(getPage(), this.getRows(), keyword, this.getOrderbyStr());
        List<LawRecord> list = pageInfo.getList();
        if (CollectionUtils.isNotEmpty(list)) {
            Set<Long> ids = new LinkedHashSet<>();
            for (LawRecord lawRecord : list) {
                ids.add(lawRecord.getId());
            }
            Map<Long, List<String>> map = Image.dao.findImgsByLawIds(new ArrayList<Long>(ids), "t_law_record");
            for (LawRecord lawRecord : list) {
                if (lawRecord.getStatus() != null) {
                    lawRecord.put("statusName", YesOrNo.getYesOrNoMap().get(String.valueOf(lawRecord.getStatus())));
                }
                if (map.containsKey(lawRecord.getId())) {
                    lawRecord.put("imgNames", StringUtils.join(map.get(lawRecord.getId()), ","));
                }
            }
        }
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value = {"/app/law"})
    public void add() {
        Long id = this.getParaToLong("id");
        if (id != null) {
            this.setAttr("item", LawRecord.dao.findById(id));
        }
        this.setAttr("id", id);
        String action = "add";
        if (id != null) {
            action = "edit";
        }
        this.setAttr("action", action);
        render("law_add.jsp");
    }

    @RequiresPermissions(value = {"/app/law"})
    public void save() {
        SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
        Long id = this.getParaToLong("id");
        String title = this.getPara("title");
        String content = this.getPara("content");
        Integer status = this.getParaToInt("status");
        InvokeResult result = LawRecord.dao.save(id, title, content, status, sysUser.getId(), sysUser.getInnerCode(), sysUser.getName());
        this.renderJson(result);
    }

    @RequiresPermissions(value = {"/app/law"})
    public void delete() {
        String ids = this.getPara("ids");
        InvokeResult result = LawRecord.dao.deleteData(ids);
        this.renderJson(result);
    }
}
