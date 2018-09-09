package com.shangsc.platform.controller;


import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.upload.UploadFile;
import com.shangsc.platform.core.util.FileUtils;
import com.shangsc.platform.core.util.RandomUtils;
import com.shangsc.platform.core.util.VerifyCodeUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.Image;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
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

    public void uploadImgs() {
        this.setAttr("relaTable", this.getPara("relaTable"));
        Long relaId = this.getParaToLong("relaId");
        List<Image> images = Image.dao.find("select * from t_image where rela_id=", relaId);


        render("add_images.jsp");
    }

    public void uploadData() {
        List<UploadFile> flist = this.getFiles("/temp", 1024 * 1024 * 50);
        String targetName = RandomUtils.getRandomDigit(10);

        Map<String, Object> data = Maps.newHashMap();
        if (flist.size() > 0) {
            UploadFile uf = flist.get(0);
            String originFileName = uf.getFileName();
            String endStr = originFileName.substring(originFileName.lastIndexOf("."), originFileName.length());
            String finalFileName = targetName + endStr;
            String status_url = PropKit.get("static_url");
            String fileUrl = "common/img/" + System.currentTimeMillis() + "/" + finalFileName;
            String newFile = PropKit.get("uploadImgsPath") + fileUrl;
            FileUtils.mkdir(newFile, false);
            FileUtils.copy(uf.getFile(), new File(newFile), BUFFER_SIZE);
            uf.getFile().delete();
            String imgUrl = status_url + fileUrl;
            data.put("staticUrl", imgUrl);
            data.put("fileUrl", fileUrl);
            String relaTable = this.getPara("relaTable");
            InvokeResult save = Image.dao.save(null, originFileName, imgUrl, null, relaTable);
            Object imageId = save.getData();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uploadImgId", imageId);
            jsonObject.put("uploadImgFileName", originFileName);
            renderJson(jsonObject);
        }
    }
}
