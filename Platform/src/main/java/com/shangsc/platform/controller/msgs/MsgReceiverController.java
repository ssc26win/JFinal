package com.shangsc.platform.controller.msgs;

import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.MsgReceiver;
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

    @RequiresPermissions(value = {"/basic/setReceiver"})
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
}
