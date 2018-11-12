package com.shangsc.platform.controller.msgs;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.code.ReadOrNo;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.IWebUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.MsgReceiver;
import com.shangsc.platform.model.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ssc
 * @Date 2018/8/24 10:49
 * @Desc 用途：
 */
public class MsgReceiverController extends BaseController {

    @Clear(AuthorityInterceptor.class)
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

    @Clear(AuthorityInterceptor.class)
    public void msgCount() {
        SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
        Long unReadCount = MsgReceiver.dao.findUnReadCount(sysUser.getId());
        JSONObject result = new JSONObject();
        result.put("msgCount", unReadCount);
        renderJson(result);
    }

    @Clear(AuthorityInterceptor.class)
    public void myMsgList() {
        render("message_list.jsp");
    }

    @Clear(AuthorityInterceptor.class)
    public void getUnReadListData() {
        SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
        String keyword = this.getPara("name");
        Integer status = this.getParaToInt("status");
        Page<MsgReceiver> pageInfo = MsgReceiver.dao.getPageInfo(getPage(), this.getRows(), sysUser, keyword, status, getOrderbyStr());
        List<MsgReceiver> list = pageInfo.getList();
        for (MsgReceiver msgReceiver : list) {
            if (msgReceiver.getStatus() != null) {
                msgReceiver.put("statusName", ReadOrNo.getColorMap().get(String.valueOf(msgReceiver.getStatus())));
            }
        }
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @Clear(AuthorityInterceptor.class)
    public void delete() {
        String ids = this.getPara("ids");
        InvokeResult resp = MsgReceiver.dao.deleteData(ids);
        this.renderJson(resp);
    }

    @Clear(AuthorityInterceptor.class)
    public void setReading() {
        SysUser user = IWebUtils.getCurrentSysUser(getRequest());
        String ids = this.getPara("ids");
        InvokeResult resp = MsgReceiver.dao.setReading(ids, user.getId());
        this.renderJson(resp);
    }
}
