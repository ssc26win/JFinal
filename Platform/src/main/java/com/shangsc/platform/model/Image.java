package com.shangsc.platform.model;

import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.base.BaseImage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class Image extends BaseImage<Image> {
    public static final Image dao = new Image();

    public InvokeResult save(Long id, String name, String originName, Integer width, Integer height,
                             Integer size, String memo, String imgUrl, Long relaId, String relaTable,
                             Integer userId, String userName) {
        if (null != id && id > 0L) {
            Image image = this.findById(id);
            if (image == null) {
                return InvokeResult.failure("更新消息失败, 该消息不存在");
            }
            image = setProp(image, name, originName, width, height, size, memo, imgUrl, relaId, relaTable, userId, userName);
            image.update();
        } else {
            Image image = new Image();
            image = setProp(image, name, originName, width, height, size, memo, imgUrl, relaId, relaTable, userId, userName);
            image.save();
            id = image.getLong("id");
        }
        return InvokeResult.success(id);
    }

    private Image setProp(Image image, String name, String originName, Integer width, Integer height,
                          Integer size, String memo, String imgUrl, Long relaId, String relaTable,
                          Integer userId, String userName) {
        image.setName(name);
        image.setOriginName(originName);
        image.setWidth(width);
        image.setHeight(height);
        image.setSize(size);
        image.setMemo(memo);
        image.setImgUrl(imgUrl);
        image.setRelaId(relaId);
        image.setRelaType(relaTable);
        image.setCreateTime(new Date());
        image.setUserId(userId);
        image.setCreateUser(userName);
        return image;
    }

    public InvokeResult deleteData(String idStrs) {
        List<Long> ids = CommonUtils.getLongListByStrs(idStrs);
        for (int i = 0; i < ids.size(); i++) {
            this.deleteById(ids.get(i));
        }
        return InvokeResult.success();
    }

    public InvokeResult deleteDataByIdAndRelaId(Long relaId, Long id) {
        int delete = this.delete(CommonUtils.getConditions(new Condition("rela_id", Operators.EQ, relaId),
                new Condition("id", Operators.EQ, id)));
        return InvokeResult.success();
    }

    public Map<Long, List<String>> findImgsByLawIds(List<Long> relaIds, String relaType) {
        List<Image> images = this.find("select rela_id,origin_name from t_image " +
                "where rela_id in (" + StringUtils.join(relaIds, ",") + ") and rela_type='" + relaType + "' order by rela_id asc, create_time asc ");
        Map<Long, List<String>> msgReceiversMap = new LinkedHashMap<>();
        for (Long relaId : relaIds) {
            List<String> names = new ArrayList<>();
            for (Image m : images) {
                if (m.getRelaId() == relaId.longValue()) {
                    names.add(m.getOriginName());
                }
            }
            if (CollectionUtils.isNotEmpty(names)) {
                msgReceiversMap.put(relaId, names);
            }
        }
        return msgReceiversMap;
    }
}
