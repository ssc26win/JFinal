package com.shangsc.platform.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.upload.UploadFile;
import com.shangsc.platform.core.util.FileUtils;
import com.shangsc.platform.core.util.IWebUtils;
import com.shangsc.platform.core.util.RandomUtils;
import com.shangsc.platform.core.util.VerifyCodeUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.Image;
import com.shangsc.platform.model.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 生成验证码接口
 *
 * @author luxiansheng
 */

public class ImageController extends Controller {

    public static final int BUFFER_SIZE = 1024 * 1024;

    public void getCode() throws IOException {
        HttpServletResponse response = this.getResponse();
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        //生成随机字串  
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        //存入会话session  
        this.setSessionAttr("imageCode", verifyCode.toLowerCase());
        //生成图片  
        int w = 200, h = 80;
        VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
        this.renderNull();
    }

    /**
     * initialPreview: [
     * "http://lorempixel.com/800/460/people/1",
     * "http://lorempixel.com/800/460/people/2"
     * ],
     * initialPreviewConfig: [
     * {caption: "People-1.jpg", size: 576237, width: "120px", url: "/site/file-delete", key: 1},
     * {caption: "People-2.jpg", size: 932882, width: "120px", url: "/site/file-delete", key: 2},
     * ],
     */
    public void uploadImgs() {
        this.setAttr("relaTable", this.getPara("relaTable"));
        Long relaId = this.getParaToLong("relaId");
        List<Image> images = Image.dao.find("select * from t_image where rela_id=?", relaId);
        if (CollectionUtils.isNotEmpty(images)) {
            JSONArray initialPreview = new JSONArray();
            JSONArray initialPreviewConfig = new JSONArray();
            String contextPath = this.getRequest().getServletContext().getContextPath();
            String context_path = contextPath.equals("/") ? "" : contextPath;
            for (Image image : images) {
                initialPreview.add(image.getImgUrl());
                JSONObject p = new JSONObject();
                p.put("caption", image.getOriginName());
                p.put("size", image.getSize() * 1024);
                p.put("width", image.getWidth() + "px");
                p.put("height", image.getHeight() + "px");
                p.put("url", context_path + "/image/deleteImg?relaId=" + relaId + "&imgId=" + image.getId());
                p.put("key", image.getId());
                initialPreviewConfig.add(p);
            }
            this.setAttr("initialPreview", initialPreview);
            this.setAttr("initialPreviewConfig", initialPreviewConfig);
        } else {
            this.setAttr("initialPreview", "notConfig");
            this.setAttr("initialPreviewConfig", "notConfig");
        }
        Long maxFileCount = 0L;
        if (StringUtils.isNotEmpty(this.getPara("relaTable"))) {
            if ("t_ad".equals(this.getPara("relaTable"))) {
                Image first = Image.dao.findFirst("select count(1) as ImageCount from t_image where rela_type='t_ad' and rela_id=?", relaId);
                maxFileCount = 1L;
                if (first != null) {
                    Long imageCount = first.get("ImageCount");
                    if (imageCount > 0L) {
                        maxFileCount = 0L;
                    }
                }
            } else if ("t_law_record".equals(this.getPara("relaTable"))) {
                Image first = Image.dao.findFirst("select count(1) as ImageCount from t_image where rela_type='t_law_record' and rela_id=?", relaId);
                maxFileCount = 3L;
                if (first != null) {
                    Long imageCount = first.get("ImageCount");
                    if (imageCount > 0L) {
                        maxFileCount = maxFileCount >= imageCount ? (maxFileCount - imageCount) : 0L;
                    }
                }
            }
        }
        this.setAttr("maxFileCount", this.getParaToInt("maxFileCount"));
        this.setAttr("relaId", relaId);
        render("add_images.jsp");
    }

    public void uploadData() {
        SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
        Integer userId = 0;
        String userName = "";
        if (sysUser != null) {
            userId = sysUser.getId();
            userName = sysUser.getName();
        }
        String memo = this.getPara("memo");
        List<UploadFile> flist = this.getFiles("/temp", 1024 * 1024 * 100);
        String targetName = RandomUtils.getRandomDigit(10);
        Map<String, Object> data = Maps.newHashMap();
        if (flist.size() > 0) {
            UploadFile uf = flist.get(0);
            String originFileName = uf.getFileName();
            String endStr = originFileName.substring(originFileName.lastIndexOf("."), originFileName.length());
            String finalFileName = targetName + endStr;
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
            String status_url = PropKit.get("static_url");
            String fileUrl = "common/img/" + System.currentTimeMillis() + "/" + finalFileName;
            //String fileUrl = "common/img/" + System.currentTimeMillis() + "/" + originFileName;
            String newFile = PropKit.get("uploadImgsPath") + fileUrl;
            FileUtils.mkdir(newFile, false);
            FileUtils.copy(uf.getFile(), new File(newFile), BUFFER_SIZE);
            uf.getFile().delete();
            String imgUrl = status_url + fileUrl;
            data.put("staticUrl", imgUrl);
            data.put("fileUrl", fileUrl);
            String relaTable = this.getPara("relaTable");
            Long relaId = this.getParaToLong("relaId", 0L);
            InvokeResult save = Image.dao.save(null, originFileName, originFileName, width, height, size,
                    memo, imgUrl, relaId, relaTable, userId, userName);
            Object imageId = save.getData();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uploadImgId", imageId);
            jsonObject.put("uploadImgFileName", originFileName);
            renderJson(jsonObject);
        }
    }

    public void deleteImg() {
        Long relaId = this.getParaToLong("relaId");
        Long id = this.getParaToLong("imgId");
        InvokeResult result = Image.dao.deleteDataByIdAndRelaId(relaId, id);
        renderJson(result);
    }
}
