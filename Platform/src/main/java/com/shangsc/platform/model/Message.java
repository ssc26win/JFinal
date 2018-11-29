package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.Db;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.base.BaseMessage;
import org.apache.commons.collections.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Message extends BaseMessage<Message> {
    public static final Message dao = new Message();

    public InvokeResult save(Long id, String title, String content, String memo, Integer status,
                             String innerCode, Integer userId, String userName, List<Long> userIds) {
        if (null != id && id > 0L) {
            Message message = this.findById(id);
            if (message == null) {
                return InvokeResult.failure("更新消息失败, 该消息不存在");
            }
            message = setProp(message, title, content, memo, status, innerCode, userId, userName, userIds);
            message.setUpdateTime(new Date());
            message.update();
        } else {
            Message message = new Message();
            message = setProp(message, title, content, memo, status, innerCode, userId, userName, userIds);
            message.setCreateTime(new Date());
            message.save();
        }
        return InvokeResult.success();
    }

    private void offPublishOther() {
        Db.update("update t_message set status=0 where 1=1");
    }

    private Message setProp(Message message, String title, String content, String memo, Integer status,
                            String innerCode, Integer userId, String userName, List<Long> userIds) {
        message.setTitle(title);
        message.setContent(content);
        message.setMemo(memo);
        //message.setImgUrl(imgUrl);
        message.setStatus(status);
        //message.setInnerCode(innerCode);
        message.setUserId(userId);
        message.setCreateUser(userName);
        return message;
    }

    public InvokeResult deleteData(String idStrs) {
        List<Long> ids = CommonUtils.getLongListByStrs(idStrs);
        if (ids.contains(1L)) {
            return InvokeResult.failure("删除失败，默认系统消息不可删除！");
        }
        for (int i = 0; i < ids.size(); i++) {
            this.deleteById(ids.get(i));
        }
        return InvokeResult.success();
    }

    public InvokeResult publish(Long id) {
        List<MsgReceiver> msgReceivers = MsgReceiver.dao.find("select * from t_msg_receiver where msg_id=" + id);
        if (CollectionUtils.isNotEmpty(msgReceivers)) {
            Message message = this.findById(id);
            if (message == null) {
                return InvokeResult.failure("发布消息失败, 该消息不存在");
            }
            message.setStatus(1);
            message.update();
            return InvokeResult.success();
        } else {
            return InvokeResult.failure("发布消息失败, 请先添加接收人");
        }
    }
}
