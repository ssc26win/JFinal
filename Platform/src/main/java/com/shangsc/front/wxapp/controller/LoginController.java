package com.shangsc.front.wxapp.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.kit.PropKit;
import com.shangsc.front.util.JsonUtil;
import com.shangsc.front.validate.bean.CommonDes;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.util.IWebUtils;
import com.shangsc.platform.core.util.MD5Utils;
import com.shangsc.platform.core.util.MyDigestUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.SysLoginRecord;
import com.shangsc.platform.model.SysUser;
import org.apache.commons.codec.binary.Base64;

import java.util.Date;

/**
 * @Author ssc
 * @Date 2018/8/16 10:53
 * @Desc 用途：
 */
@Clear(AuthorityInterceptor.class)
@RequiresPermissions(value = {"/"})
public class LoginController extends BaseController {

    @Clear(AuthorityInterceptor.class)
    public void doLoginWxApp() {
        SysUser sysUser = SysUser.me.getByName(this.getPara("username"));
        if (sysUser == null) {
            sysUser = SysUser.me.getByEmail(this.getPara("username"));
            if (sysUser == null) {
                sysUser = SysUser.me.getByPhone(this.getPara("username"));
            }
        }
        if (sysUser == null) {
            this.renderJson(InvokeResult.failure("用户不存在"));
            return;
        }
        if (SysLoginRecord.dao.wxhasOverLoginErrTimes(sysUser.getId())) {
            this.renderJson(InvokeResult.failure("今天连续输入密码错误次数超过10次"));
            return;
        }
        if (!sysUser.getPwd().equals(MyDigestUtils.shaDigestForPasswrod(this.getPara("password")))) {
            SysLoginRecord.dao.saveSysLoginRecord(sysUser.getId(), 0);
            this.renderJson(InvokeResult.failure("用户密码输入有误"));
            return;
        }
        if (sysUser.getStatus() == 2) {
            this.renderJson(InvokeResult.failure("用户被冻结，请联系管理员"));
            return;
        }
        long expireTime = DateUtils.addDay(new Date(), 365).getTime();
        String userInfo = MD5Utils.GetMD5Code(sysUser.getName() + sysUser.getPwd() + expireTime + PropKit.get("app_key"));
        String token = Base64.encodeBase64String(new String(sysUser.getName() + ":" + expireTime + ":" + userInfo).getBytes());
        sysUser.setToken(token);
        String wxAccount = this.getPara("wxAccount");
        sysUser.setWxAccount(wxAccount);
        SysUser.me.updateByWxLogin(sysUser.getId(), wxAccount, token);
        SysLoginRecord.dao.saveSysLoginRecord(sysUser.getId(), 1);
        JSONObject data = new JSONObject();
        Integer roleType = 0;
        if ("admin".equals(sysUser.getName())) {
            roleType = 1;
        }
        data.put("roleType", roleType);
        this.renderJson(InvokeResult.success(data, "登录成功"));
    }

    @Clear(AuthorityInterceptor.class)
    public void findMemberInfo() {
        SysUser sysUser = findByWxAccount();
        if (sysUser != null) {
            Company byInnerCode = Company.me.findByInnerCode(sysUser.getInnerCode());
            if (byInnerCode != null) {
                sysUser.put("companyName", byInnerCode.getName());
            }
        }
        this.renderJson(sysUser);
    }
}
