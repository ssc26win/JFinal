package com.shangsc.front.wxapp.controller;

import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.code.ReadOrNo;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.Message;
import com.shangsc.platform.model.MsgReceiver;
import com.shangsc.platform.model.SysUser;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ssc
 * @Date 2018/8/16 11:11
 * @Desc 用途：
 */
@Clear(AuthorityInterceptor.class)
@RequiresPermissions(value = {"/"})
public class MsgController extends BaseController {

    /**
     * 站内消息列表
     */
    public void findRollList() {
        SysUser sysUser = findByWxAccount();
        if (sysUser == null) {
            this.renderJson(new ArrayList<>());
        }
        Integer limit = this.getParaToInt("limit", 10);
        List<Message> rollList = MsgReceiver.dao.getWxRollList(sysUser.getId(), limit);
        for (Message msgReceiver : rollList) {
            if (msgReceiver.getStatus() != null) {
                msgReceiver.put("statusName", ReadOrNo.getMap().get(String.valueOf(msgReceiver.getStatus())));
            }
        }
        this.renderJson(rollList);
    }

    /**
     * 站内消息详情
     */
    public void findById() {
        Long id = this.getParaToLong("id");
        Message byId = Message.dao.findById(id);
        this.renderJson(byId);
    }

    /**
     * 站内消息列表
     */
    public void findList() {
        SysUser sysUser = findByWxAccount();
        if (sysUser == null) {
            this.renderJson(new ArrayList<>());
        }
        Page<Message> pageInfo = MsgReceiver.dao.getWxPageList(this.getPage(), this.getRows(), sysUser.getId());
        List<Message> list = pageInfo.getList();
        for (Message msgReceiver : list) {
            if (msgReceiver.getStatus() != null) {
                msgReceiver.put("statusName", ReadOrNo.getMap().get(String.valueOf(msgReceiver.getStatus())));
            }
        }
        this.renderJson(pageInfo);
    }

    public void setRead() {
        String ids = this.getPara("ids");
        SysUser sysUser = findByWxAccount();
        InvokeResult resp = MsgReceiver.dao.setReading(ids, sysUser.getId());
        this.renderJson(resp);
    }

    public void delete() {
        String ids = this.getPara("ids");
        InvokeResult resp = MsgReceiver.dao.deleteData(ids);
        this.renderJson(resp);
    }

}
