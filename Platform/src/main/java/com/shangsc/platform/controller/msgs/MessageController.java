package com.shangsc.platform.controller.msgs;

import com.google.common.collect.Maps;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.shangsc.platform.code.YesOrNo;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.*;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.Message;
import com.shangsc.platform.model.SysUser;

import java.io.File;
import java.util.*;

/**
 * @Author ssc
 * @Date 2018/8/16 10:42
 * @Desc 用途：
 */

public class MessageController extends BaseController {

    @RequiresPermissions(value = {"/basic/msg"})
    public void index() {
        render("message_index.jsp");
    }

    @RequiresPermissions(value = {"/basic/msg"})
    public void getListData() {
        String keyword = this.getPara("innerCode");
        Set<Condition> conditions = new HashSet<Condition>();
        if (CommonUtils.isNotEmpty(keyword)) {
            conditions.add(new Condition("inner_code", Operators.LIKE, keyword));
        }
        Page<Message> pageInfo = Message.dao.getPage(getPage(), this.getRows(), conditions, this.getOrderby());
        List<Message> list = pageInfo.getList();
        for (Message message : list) {
            if (message.getStatus() != null) {
                message.put("statusName", YesOrNo.getYesOrNoMap().get(String.valueOf(message.getStatus())));
            }
        }
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value = {"/basic/msg"})
    public void add() {
        Integer id = this.getParaToInt("id");
        if (id != null) {
            this.setAttr("item", Message.dao.findById(id));
        }
        this.setAttr("id", id);
        String action = "add";
        if (id != null) {
            action = "edit";
        }
        this.setAttr("action", action);
        render("message_add.jsp");
    }

    @RequiresPermissions(value = {"/basic/msg"})
    public void save() {
        SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
        Long id = this.getParaToLong("id");
        String title = this.getPara("title");
        String content = this.getPara("content");
        String imgUrl = this.getPara("imgUrl");
        Integer status = this.getParaToInt("status");
        InvokeResult result = Message.dao.save(id, title, content, imgUrl, status, sysUser.getName(), new ArrayList<Long>());
        this.renderJson(result);
    }

    @RequiresPermissions(value = {"/basic/msg"})
    public void delete() {
        String ids = this.getPara("ids");
        InvokeResult result = Message.dao.deleteData(ids);
        this.renderJson(result);
    }

    @RequiresPermissions(value = {"/basic/msg"})
    public void publish() {
        Long id = this.getParaToLong("id");
        InvokeResult result = Message.dao.publish(id);
        this.renderJson(result);
    }

    @RequiresPermissions(value = {"/basic/msg"})
    public void uploadImg() {
        String dataStr = DateUtils.format(new Date(), "yyyyMMddHHmm");
        List<UploadFile> flist = this.getFiles("/temp", 1024 * 1024 * 50);
        Map<String, Object> data = Maps.newHashMap();
        if (flist.size() > 0) {
            UploadFile uf = flist.get(0);
            String status_url = PropKit.get("static_url");
            String fileUrl = dataStr + "/" + uf.getFileName();
            String newFile = PropKit.get("uploadAdImgPath") + fileUrl;
            FileUtils.mkdir(newFile, false);
            FileUtils.copy(uf.getFile(), new File(newFile), BUFFER_SIZE);
            uf.getFile().delete();
            data.put("staticUrl", status_url);
            data.put("fileUrl", newFile);
            renderJson(data);
        }
    }
}
