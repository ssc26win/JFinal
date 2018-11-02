package com.shangsc.front.wxapp.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.kit.PropKit;
import com.jfinal.template.ext.directive.Str;
import com.shangsc.front.util.HttpUtils;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.util.IWebUtils;
import com.shangsc.platform.core.util.MD5Utils;
import com.shangsc.platform.core.util.MyDigestUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author ssc
 * @Date 2018/8/16 10:53
 * @Desc 用途：
 */
@Clear(AuthorityInterceptor.class)
@RequiresPermissions(value = {"/"})
public class LoginController extends BaseController {

    @Clear(AuthorityInterceptor.class)
    public void roleResList() {
        SysUser byWxAccount = findByWxAccount();
        List<SysRes> wxResList = SysRes.me.findWxResList(byWxAccount.getId());
        if (CollectionUtils.isNotEmpty(wxResList)) {
            Map<String, String> map = new HashMap<>();
            for (SysRes sysRes : wxResList) {
                map.put(sysRes.getSeq().toString(), sysRes.getName());
            }
            this.renderJson(InvokeResult.success(map, "授权资源列表"));
        } else {
            this.renderJson(InvokeResult.failure("未获取绑定微信账号授权资源"));
        }
    }

    @Clear(AuthorityInterceptor.class)
    public void regist() {
        String username = this.getPara("username");
        String password = this.getPara("password");
        String phone = this.getPara("phone");
        String email = this.getPara("email");
        String wxAccount = this.getPara("wxAccount");
        if (StringUtils.isEmpty(username)) {
            this.renderJson(InvokeResult.failure("用户名不能为空"));
            return;
        }
        if (StringUtils.isEmpty(password)) {
            this.renderJson(InvokeResult.failure("密码不能为空"));
            return;
        }
        InvokeResult result = SysUser.me.registWx(username, password, phone, email, wxAccount);
        this.renderJson(result);
    }

    @Clear(AuthorityInterceptor.class)
    public void binding() {
        SysUser byWxAccount = findByWxAccount();
        InvokeResult result = InvokeResult.success(Boolean.FALSE, "未绑定微信账号");
        if (byWxAccount != null && byWxAccount.getStatus() == 1) {
            result = InvokeResult.success(Boolean.TRUE, "已绑定微信账号");
        }
        if (byWxAccount != null && byWxAccount.getStatus() == 0) {
            result = InvokeResult.success(Boolean.FALSE, "该账号已停用");
        }
        this.renderJson(result);
    }

    @Clear(AuthorityInterceptor.class)
    public void cancelBinding() {
        SysUser byWxAccount = findByNameToCancel();
        if (byWxAccount != null) {
            byWxAccount.setWxAccount("");
            byWxAccount.update();
        }
        this.renderJson(InvokeResult.success(this.getPara("userName"), "解绑成功"));
    }

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
        } else {
            List<SysRes> wxResList = SysRes.me.findWxResList(sysUser.getId());
            if ("Wexin_regist".equals(sysUser.getWxMemo()) && CollectionUtils.isEmpty(wxResList)) {
                this.renderJson(InvokeResult.failure("微信账号未授权，请联系管理员"));
                return;
            }
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
    public void loginWxServer() {
        AppVersion appVersion = AppVersion.dao.findwxLastAppVersion();
        if (appVersion != null) {
            String appid = appVersion.getWxAppId();
            String secret = appVersion.getWxAppSecret();
            String code = this.getPara("code");
            if (StringUtils.isNotEmpty(code) && StringUtils.isNotEmpty(appid) && StringUtils.isNotEmpty(secret)) {
                String apiUrl = "https://api.weixin.qq.com/sns/jscode2session?grant_type=authorization_code";
                apiUrl = apiUrl + "&appid=" + appid;
                apiUrl = apiUrl + "&secret=" + secret;
                apiUrl = apiUrl + "&js_code=" + code;
                byte[] bytes = HttpUtils.doGet(apiUrl);
                String result = new String(bytes);
                Object parse = JSONObject.parse(result);
                this.renderJson(parse);
            } else {
                this.renderJson(InvokeResult.failure("未获取微信授权code"));
            }
        } else {
            this.renderJson(InvokeResult.failure("未获取微信appid，secret"));
        }
    }

    @Clear(AuthorityInterceptor.class)
    public void findMemberInfo() {
        SysUser sysUser = findByWxAccount();
        if (sysUser != null) {
            SysUserRole sysUserRole = SysUserRole.dao.findFirst("select * from sys_user_role where user_id=" + sysUser.getId());
            if (sysUserRole != null) {
                Integer roleId = sysUserRole.getRoleId();
                SysRole byId = SysRole.me.findById(roleId);
                if (byId != null) {
                    sysUser.put("roleInfo", byId);
                }
            }
            Company byInnerCode = Company.me.findByInnerCode(sysUser.getInnerCode());
            if (byInnerCode != null) {
                sysUser.put("company", byInnerCode);
            }
        }
        this.renderJson(sysUser);
    }
}
