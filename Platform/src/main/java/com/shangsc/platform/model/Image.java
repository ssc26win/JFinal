package com.shangsc.platform.model;

import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.base.BaseImage;

import java.util.List;

public class Image extends BaseImage<Image> {
    public static final Image dao = new Image();

    public InvokeResult save(Long id, String memo, String imgUrl, Long relaId, String relaTable) {
        if (null != id && id > 0L) {
            Image image = this.findById(id);
            if (image == null) {
                return InvokeResult.failure("更新消息失败, 该消息不存在");
            }
            image = setProp(image, memo, imgUrl, relaId, relaTable);
            image.update();
        } else {
            Image image = new Image();
            image = setProp(image, memo, imgUrl, relaId, relaTable);
            boolean save = image.save();
            id = image.getLong("id");
        }
        return InvokeResult.success(id);
    }

    private Image setProp(Image image, String memo, String imgUrl, Long relaId, String relaTable) {
        image.setMemo(memo);
        image.setImgUrl(imgUrl);
        image.setRelaId(relaId);
        image.setRelaType(relaTable);
        return image;
    }

    public InvokeResult deleteData(String idStrs) {
        List<Long> ids = CommonUtils.getLongListByStrs(idStrs);
        for (int i = 0; i < ids.size(); i++) {
            this.deleteById(ids.get(i));
        }
        return InvokeResult.success();
    }
}
