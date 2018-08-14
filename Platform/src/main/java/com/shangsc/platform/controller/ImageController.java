package com.shangsc.platform.controller;


import com.jfinal.core.Controller;
import com.shangsc.platform.core.util.VerifyCodeUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 生成验证码接口
 *
 * @author luxiansheng
 */
public class ImageController extends Controller {

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
}
