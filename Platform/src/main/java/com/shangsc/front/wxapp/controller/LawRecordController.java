package com.shangsc.front.wxapp.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jfinal.aop.Clear;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.shangsc.platform.code.YesOrNo;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.FileUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.Image;
import com.shangsc.platform.model.LawRecord;
import com.shangsc.platform.model.SysUser;
import com.shangsc.platform.util.CodeNumUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author ssc
 * @Date 2018/8/16 11:09
 * @Desc 用途：
 */
@Clear(AuthorityInterceptor.class)
@RequiresPermissions(value = {"/"})
public class LawRecordController extends BaseController {

    @Clear(AuthorityInterceptor.class)
    public void findList() {
        SysUser sysUser = findByWxAccount();
        String keyword = getPara("keyword");
        Page<LawRecord> pageInfo = LawRecord.dao.findWxList(getPage(), this.getRows(), keyword, sysUser.getInnerCode(), sysUser.getId());
        List<LawRecord> list = pageInfo.getList();
        if (CollectionUtils.isNotEmpty(list)) {
            Set<Long> ids = new LinkedHashSet<>();
            for (LawRecord lawRecord : list) {
                ids.add(lawRecord.getId());
            }
            for (LawRecord lawRecord : list) {
                if (lawRecord.getStatus() != null) {
                    lawRecord.put("statusName", YesOrNo.getYesOrNoMap().get(String.valueOf(lawRecord.getStatus())));
                }
            }
        }
        this.renderJson(pageInfo);
    }

    @Clear(AuthorityInterceptor.class)
    public void findById() {
        SysUser sysUser = findByWxAccount();
        if (StringUtils.isEmpty(this.getPara("id"))) {
            this.renderJson(InvokeResult.failure("id不存在"));
        }
        LawRecord lawRecord = LawRecord.dao.findById(this.getParaToLong("id"));
        List<Image> images = Image.dao.find("select * from t_image where rela_id=? and rela_type=?", lawRecord.getId(), "t_law_record");
        lawRecord.put("images", images);
        this.renderJson(lawRecord);
    }


    @Clear(AuthorityInterceptor.class)
    public void save() {
        SysUser sysUser = findByWxAccount();
        Integer userId = 0;
        String userName = "";
        if (sysUser != null) {
            userId = sysUser.getId();
            userName = sysUser.getName();
        }
        Long id = this.getParaToLong("id");
        String title = this.getPara("title");
        String content = this.getPara("content");
        BigDecimal longitude = StringUtils.isEmpty(getPara("longitude")) ? null : CodeNumUtil.getBigDecimal(getPara("longitude"), 6);
        BigDecimal latitude = StringUtils.isEmpty(getPara("latitude")) ? null : CodeNumUtil.getBigDecimal(getPara("latitude"), 6);
        InvokeResult result = LawRecord.dao.saveWx(id, title, content, null, longitude, latitude, sysUser.getInnerCode(), sysUser.getName());
        Long relaId = Long.parseLong(result.getData().toString());

        List<UploadFile> flist = this.getFiles("/temp", 1024 * 1024 * 100);
        Map<String, Object> data = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(flist)) {
            for (int i = 0; i < flist.size(); i++) {
                UploadFile uf = flist.get(i);
                File picture = uf.getFile();
                Integer size = 0;
                Integer width = 0;
                Integer height = 0;
                try {
                    BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
                    width = sourceImg.getWidth(); // 源图宽度
                    height = sourceImg.getHeight(); // 源图高度
                    size = new Long(picture.length() / 1024).intValue();// 源图大小
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String originFileName = uf.getFileName();
                String status_url = PropKit.get("static_url");
                String fileUrl = "common/img/" + System.currentTimeMillis() + "/" + originFileName;
                String newFile = PropKit.get("uploadImgsPath") + fileUrl;
                FileUtils.mkdir(newFile, false);
                FileUtils.copy(uf.getFile(), new File(newFile), BUFFER_SIZE);
                uf.getFile().delete();
                String imgUrl = status_url + fileUrl;
                data.put("staticUrl", imgUrl);
                data.put("fileUrl", fileUrl);
                Image.dao.save(null, originFileName, originFileName, width, height, size,
                        null, imgUrl, relaId, "t_law_record", userId, userName);
            }
        }
        renderJson(InvokeResult.success(null, "上传成功"));
    }

    @Clear(AuthorityInterceptor.class)
    public void deleteImg() {
        Long relaId = this.getParaToLong("id");
        Long id = this.getParaToLong("imgId");
        InvokeResult result = Image.dao.deleteDataByIdAndRelaId(relaId, id);
        renderJson(result);
    }

    @Clear(AuthorityInterceptor.class)
    public void deleteLaw() {
        Long relaId = this.getParaToLong("id");
        boolean b = LawRecord.dao.deleteById(relaId);
        if (b) {
            Image.dao.deleteByRelaIdWx(relaId);
        }
        renderJson(InvokeResult.success());
    }

}
