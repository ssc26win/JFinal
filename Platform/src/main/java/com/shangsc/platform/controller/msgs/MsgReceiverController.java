package com.shangsc.platform.controller.msgs;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.code.ReadOrNo;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.IWebUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.Message;
import com.shangsc.platform.model.MsgReceiver;
import com.shangsc.platform.model.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author ssc
 * @Date 2018/8/24 10:49
 * @Desc 用途：
 */
public class MsgReceiverController extends BaseController {

    @RequiresPermissions(value = {"/basic/msgreceiver"})
    public void save() {
        Long mid = this.getParaToLong("mid");
        String para = this.getPara("receiverIds");

        List<Long> receiverIds = new ArrayList<>();

        if (StringUtils.isNotEmpty(para)) {
            String[] split = para.split(",");
            for (String rId : split) {
                if (NumberUtils.isNumber(rId)) {
                    receiverIds.add(Long.valueOf(rId));
                }
            }
        }
        InvokeResult result = MsgReceiver.dao.saveList(mid, receiverIds);
        this.renderJson(result);
    }

    @RequiresPermissions(value = {"/basic/msgreceiver"})
    public void msgCount() {
        SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
        Long unReadCount = MsgReceiver.dao.findUnReadCount(sysUser.getId());
        JSONObject result = new JSONObject();
        result.put("msgCount", unReadCount);
        renderJson(result);
    }

    @RequiresPermissions(value = {"/basic/msgreceiver"})
    public void myMsgList() {
        render("message_list.jsp");
    }

    @RequiresPermissions(value = {"/basic/msgreceiver"})
    public void getUnReadListData() {
        SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
        String keyword = this.getPara("name");
        Set<Condition> conditions = new HashSet<Condition>();
        if (CommonUtils.isNotEmpty(keyword)) {
            conditions.add(new Condition("title", Operators.LIKE, keyword));
        }

        conditions.add(new Condition("receiver_id", Operators.EQ, sysUser.getId()));
        Page<MsgReceiver> pageInfo = MsgReceiver.dao.getPage(getPage(), this.getRows(), conditions, this.getOrderby());
        List<MsgReceiver> list = pageInfo.getList();
        List<Long> msgIds = new ArrayList<>();
        for (MsgReceiver msgReceiver : list) {
            msgIds.add(msgReceiver.getMsgId());
        }
        List<Message> msgs = Message.dao.find("select * from t_message where id in (?)", StringUtils.join(msgIds, ","));
        for (MsgReceiver msgReceiver : list) {
            if (msgReceiver.getStatus() != null) {
                msgReceiver.put("statusName", ReadOrNo.getMap().get(String.valueOf(msgReceiver.getStatus())));
            }
            if (CollectionUtils.isNotEmpty(msgs)) {
                for (Message m : msgs) {
                    if (m.getId() == msgReceiver.getMsgId().longValue()) {
                        msgReceiver.put("title", m.getTitle());
                        msgReceiver.put("content", m.getContent());
                    }
                }
            }
        }
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value = {"/basic/msgreceiver"})
    public void delete() {
        String ids = this.getPara("ids");
        InvokeResult resp = MsgReceiver.dao.deleteData(ids);
        this.renderJson(resp);
    }

    @RequiresPermissions(value = {"/basic/msgreceiver"})
    public void setReading() {
        String ids = this.getPara("ids");
        InvokeResult resp = MsgReceiver.dao.setReading(ids);
        this.renderJson(resp);
    }
}
