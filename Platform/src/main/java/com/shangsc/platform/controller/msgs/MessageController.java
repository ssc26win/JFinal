package com.shangsc.platform.controller.msgs;

import com.alibaba.druid.support.json.JSONUtils;
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
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.Message;
import com.shangsc.platform.model.MsgReceiver;
import com.shangsc.platform.model.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

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
        String keyword = this.getPara("name");
        Set<Condition> conditions = new HashSet<Condition>();
        if (CommonUtils.isNotEmpty(keyword)) {
            conditions.add(new Condition("title", Operators.LIKE, keyword));
        }
        Page<Message> pageInfo = Message.dao.getPage(getPage(), this.getRows(), conditions, this.getOrderby());
        List<Message> list = pageInfo.getList();
        if (CollectionUtils.isNotEmpty(list)) {
            Set<Long> msgIds = new HashSet<>();
            for (Message message : list) {
                msgIds.add(message.getId());
            }
            Map<Long, List<String>> byMsgIds = MsgReceiver.dao.findReceiversByMsgIds(new ArrayList<Long>(msgIds));
            for (Message message : list) {
                if (message.getStatus() != null) {
                    message.put("statusName", YesOrNo.getYesOrNoMap().get(String.valueOf(message.getStatus())));
                }
                List<String> receiverNames = byMsgIds.get(message.getId());
                if (CollectionUtils.isNotEmpty(receiverNames)) {
                    message.put("MsgReceiverNames", StringUtils.join(receiverNames, ","));
                }
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
    public void setReceiver() {
        Integer id = this.getParaToInt("mid");
        InvokeResult result = SysUser.me.getUserZtreeViewList(id, "");
        Map<String, String> nameList = Company.me.loadNameList();
        Set<String> names = nameList.keySet();
        this.setAttr("nameCodeMap", JSONUtils.toJSONString(nameList));
        this.setAttr("names", JSONUtils.toJSONString(names));

        this.setAttr("mid", id);
        this.setAttr("jsonTree", result);
        render("receivers_add.jsp");
    }

    @RequiresPermissions(value = {"/basic/msg"})
    public void findReceiverByCode() {
        Integer id = this.getParaToInt("mid");
        String innerCode = this.getPara("innerCode");
        InvokeResult result = SysUser.me.getUserZtreeViewList(id, innerCode);
        renderJson(result);
    }

    @RequiresPermissions(value = {"/basic/msg"})
    public void save() {
        SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
        Long id = this.getParaToLong("id");
        String title = this.getPara("title");
        String content = this.getPara("content");
        String imgUrl = this.getPara("imgUrl");
        Integer status = this.getParaToInt("status", 0);
        InvokeResult result = Message.dao.save(id, title, content, imgUrl, status, sysUser.getInnerCode(),
                sysUser.getId(), sysUser.getName(), new ArrayList<Long>());
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
}
