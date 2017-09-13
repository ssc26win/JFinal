package com.shangsc.platform.model;

import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.base.BaseActualLog;

import java.util.Date;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class ActualLog extends BaseActualLog<ActualLog> {
    public static final ActualLog dao = new ActualLog();

    public InvokeResult save(Long id, String srcType, Integer port, String ip, String content, Date addTime) {
        if (null != id && id > 0l) {
            ActualLog actualLog = this.findById(id);
            if (actualLog == null) {
                return InvokeResult.failure("更新失败, 该记录不存在");
            }
            setProp(actualLog, srcType, port, ip, content, addTime);
            actualLog.update();
        } else {
            ActualLog actualLog = new ActualLog();
            setProp(actualLog, srcType, port, ip, content, addTime);
            actualLog.save();
        }
        return InvokeResult.success();
    }

    private void setProp(ActualLog actualLog, String srcType, Integer port, String ip, String content, Date addTime) {
        actualLog.setSrcType(srcType);
        actualLog.setPort(port);
        actualLog.setIp(ip);
        actualLog.setContent(content);
        if (addTime == null) {
            actualLog.setAddTime(new Date());
        } else {
            actualLog.setAddTime(addTime);
        }
    }
    //{content:123, port:10002, src_type:tcp, add_time:Wed Sep 13 11:25:48 CST 2017, ip:172.17.227.92}
}