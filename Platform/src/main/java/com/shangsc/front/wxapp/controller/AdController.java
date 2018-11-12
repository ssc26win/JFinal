package com.shangsc.front.wxapp.controller;

import com.jfinal.aop.Clear;
import com.shangsc.front.util.JsonUtil;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.Ad;
import com.shangsc.platform.model.Image;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * @Author ssc
 * @Date 2018/9/29 11:42
 * @Desc 用途：
 */
@Clear(AuthorityInterceptor.class)
@RequiresPermissions(value = {"/"})
public class AdController extends BaseController {

    @Clear(AuthorityInterceptor.class)
    public void findById() {
        Long id = this.getParaToLong("id");
        Ad ad = Ad.dao.findById(id);
        Image image = Image.dao.findFirst("select * from t_image where rela_id=? and rela_type=? order by id desc limit 1", ad.getId(), "t_ad");
        ad.setImgUrl(image.getImgUrl());
        if (ad != null) {
            this.renderJson(ad);
        } else {
            this.renderJson(InvokeResult.failure("暂未发布公告"));
        }
    }

    @Clear(AuthorityInterceptor.class)
    public void findList() {
        List<Ad> ads = Ad.dao.find("select ta.*,ti.img_url as imgUrl from t_ad ta inner join " +
                "(select id as imgId,rela_id,img_url from t_image where rela_type='t_ad') ti on ti.rela_id=ta.id " +
                "where status=1 order by ti.imgId desc limit 1");
        if (CollectionUtils.isNotEmpty(ads)) {
            this.renderJson(ads);
        } else {
            this.renderJson(InvokeResult.failure("暂未发布公告"));
        }
    }
}
