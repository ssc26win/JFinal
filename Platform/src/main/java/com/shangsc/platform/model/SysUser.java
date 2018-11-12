/**
 * Copyright (c) 2011-2016, Eason Pan(pylxyhome@vip.qq.com).
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shangsc.platform.model;

import com.google.common.collect.Lists;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.core.cache.CacheClearUtils;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.MyDigestUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.core.view.ZtreeView;
import com.shangsc.platform.model.base.BaseSysUser;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author ssc
 *         系统用户
 */
public class SysUser extends BaseSysUser<SysUser> {
    /**
     *
     */
    private static final long serialVersionUID = -1982696969221258167L;
    public static SysUser me = new SysUser();

    /**
     * 权限集
     */
    public Set<String> getPermissionSets() {
        return SysRes.me.getSysUserAllResUrl(this.getId());
    }

    /**
     * 是否有执法管理员权限
     */
    public boolean isLawAdmin() {
        long count = Db.queryLong("select count(*) from sys_user_role where role_id=? and user_id=?", 65, this.getId());
        return count > 0 ? true : false;
    }

    /**
     * 是否有管理员权限
     */
    public boolean isAdmin() {
        long count = Db.queryLong("select count(*) from sys_user_role where role_id=? and user_id=?", 1, this.getId());
        return count > 0 ? true : false;
    }


    /**
     * 用户登陆
     *
     * @param username
     * @param pwd
     * @return
     * @throws java.io.UnsupportedEncodingException
     * @author ssc
     */
    public InvokeResult login(String username, String pwd, HttpServletResponse response, HttpSession session, String url) {
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("name", Operators.EQ, username));
        conditions.add(new Condition("pwd", Operators.EQ, MyDigestUtils.shaDigestForPasswrod(pwd)));
        SysUser sysUser = this.get(conditions);
        if (sysUser == null) {
            return InvokeResult.failure("用户名或密码不对");
        }
        if (sysUser.getInt("status") == 2) {
            return InvokeResult.failure("用户被冻结，请联系管理员");
        }
        //IWebUtils.setCurrentLoginSysUser(response,session,sysUser);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("url", url);
        return InvokeResult.success(data, "");
    }

    /**
     * 获取用户拥有的角色列表，最多查20个
     *
     * @param uid
     * @return
     * @author ssc
     */
    public List<SysUser> getSysUserList(int uid) {
        return this.paginate(1, 20, "select *", "from sys_user ", uid).getList();
    }

    public List<SysUser> getSysUserIdList(int uid) {
        return this.paginate(1, 20, "select id", "from sys_user ", uid).getList();
    }

    public InvokeResult setVisible(String bids, Integer visible) {
        List<Integer> ids = new ArrayList<Integer>();
        if (bids.contains(",")) {
            for (String aid : bids.split(",")) {
                if (StrKit.notBlank(aid)) {
                    ids.add(Integer.valueOf(aid));
                }
            }
        } else {
            if (StrKit.notBlank(bids)) {
                ids.add(Integer.valueOf(bids));
            }
        }
        if (bids.length() > 0) {
            bids = bids.subSequence(0, bids.length() - 1).toString();
        }
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("id", Operators.IN, ids));
        Map<String, Object> newValues = new HashMap<String, Object>();
        newValues.put("status", visible);
        this.update(conditions, newValues);
        return InvokeResult.success();
    }

    /**
     * 用户名是否已存在
     *
     * @param name
     * @return
     */
    public boolean hasExist(String name) {
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("name", Operators.EQ, name));
        long num = this.getCount(conditions);
        return num > 0 ? true : false;
    }

    /**
     * 手机号是否已存在
     *
     * @param phone
     * @return
     */
    public boolean hasExistPhone(String phone) {
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("phone", Operators.EQ, phone));
        long num = this.getCount(conditions);
        return num > 0 ? true : false;
    }

    /**
     * 邮箱是否已存在
     *
     * @param email
     * @return
     */
    public boolean hasExistEmail(String email) {
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("email", Operators.EQ, email));
        long num = this.getCount(conditions);
        return num > 0 ? true : false;
    }

    public SysUser getByName(String name) {
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("name", Operators.EQ, name));
        return this.get(conditions);
    }

    public SysUser getByPhone(String phone) {
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("phone", Operators.EQ, phone));
        return this.get(conditions);
    }

    public SysUser getByEmail(String email) {
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("email", Operators.EQ, email));
        return this.get(conditions);
    }

    public SysUser getByEmailAndName(String email, String username) {
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("email", Operators.EQ, email));
        conditions.add(new Condition("name", Operators.EQ, username));
        return this.get(conditions);
    }

    public InvokeResult save(Integer id, String username, String password, String des, String phone, String email, String innerCode, String wxAccount) {
        if (null != id) {
            SysUser sysUser = this.findById(id);
            sysUser.set("des", des).set("phone", phone).set("email", email).set("inner_code", innerCode).set("wx_account", wxAccount).update();
        } else {
            if (StringUtils.isNotEmpty(innerCode) && !Company.me.hasExistInnerCode(null, innerCode)) {
                return InvokeResult.failure("公司编码不存在");
            }
            if (this.hasExist(username)) {
                return InvokeResult.failure("用户名已存在");
            }
            if (this.hasExistPhone(phone)) {
                return InvokeResult.failure("手机号已存在");
            }
            if (this.hasExistEmail(email)) {
                return InvokeResult.failure("邮箱已存在");
            }
            if (StrKit.isBlank(password)) {
                password = "123456";
            }
            SysUser sysUser = new SysUser();
            sysUser.set("name", username).set("pwd", MyDigestUtils.shaDigestForPasswrod(password)).set("createdate", new Date())
                    .set("des", des).set("phone", phone).set("email", email).save();
        }
        return InvokeResult.success();
    }

    /**
     * 修改用户角色
     *
     * @param uid
     * @param roleIds
     * @return
     */
    public InvokeResult changeUserRoles(Integer uid, String roleIds) {
        Db.update("delete from sys_user_role where user_id = ?", uid);
        SysUser byId = SysUser.me.findById(uid);
        if (byId != null && "Wexin_regist".equals(byId.getWxMemo())) {
            if ("64".equals(roleIds)) {
                byId.setWxMemo("Weixin_regist");
            } else {
                byId.setWxMemo("");
            }
            byId.update();
        }
        List<String> sqlList = Lists.newArrayList();
        for (String roleId : roleIds.split(",")) {
            if (CommonUtils.isNotEmpty(roleId)) {
                sqlList.add("insert into sys_user_role (user_id,role_id) values (" + uid + "," + Integer.valueOf(roleId) + ")");
            }
        }
        Db.batch(sqlList, 5);
        CacheClearUtils.clearUserMenuCache();
        return InvokeResult.success();
    }

    /**
     * 密码修改
     *
     * @param uid
     * @param newPwd
     * @return
     */
    public InvokeResult savePwdUpdate(Integer uid, String newPwd) {
        SysUser sysUser = SysUser.me.findById(uid);
        if (sysUser != null) {
            Db.update("update sys_login_record set login_err_times=0 where sys_uid=" + uid);
            sysUser.set("pwd", newPwd).update();
            return InvokeResult.success();
        } else {
            return InvokeResult.failure(-2, "修改失败");
        }
    }

    public Page<SysUser> getSysUserPage(int page, int rows, String keyword, String innerCode, String orderbyStr) {
        String select = "select tc.name as companyName,su.*, (select group_concat(name) as roleNames from sys_role " +
                " where id in (select role_id from sys_user_role where user_id=su.id)) as roleNames ";
        StringBuffer sqlExceptSelect = new StringBuffer(" from sys_user su left join t_company tc on tc.inner_code=su.inner_code where 1=1 ");
        if (StringUtils.isNotEmpty(innerCode)) {
            sqlExceptSelect.append(" and su.inner_code='" + innerCode + "'");
        }
        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (su.name like '%" + keyword + "%' or tc.name like '% " + keyword + "%') ");
        }
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        return this.paginate(page, rows, select, sqlExceptSelect.toString());
    }

    public InvokeResult regist(String username, String password, String phone, String email, String innerCode) {
        if (StringUtils.isNotEmpty(innerCode) && !Company.me.hasExistInnerCode(null, innerCode)) {
            return InvokeResult.failure("公司编码不存在");
        }
        if (this.hasExist(username)) {
            return InvokeResult.failure("用户名已存在");
        } else {
            if (StrKit.isBlank(password)) {
                password = "123456";
            }
            SysUser sysUser = new SysUser();
            sysUser.set("name", username).set("pwd", MyDigestUtils.shaDigestForPasswrod(password)).set("createdate", new Date())
                    .set("phone", phone).set("email", email).set("inner_code", innerCode).save();
            List<String> sqlList = Lists.newArrayList();
            for (String roleId : "63".split(",")) {
                if (CommonUtils.isNotEmpty(roleId)) {
                    sqlList.add("insert into sys_user_role (user_id,role_id) values (" + sysUser.getId() + "," + Integer.valueOf(roleId) + ")");
                }
            }
            Db.batch(sqlList, 5);
            CacheClearUtils.clearUserMenuCache();
        }
        return InvokeResult.success();
    }

    public List<Record> getSysUserList() {
        List<Record> list = Db.find("select id,name,tc.name as companyName from sys_user susr left join t_company tc on tc.inner_code = susr.inner_code");
        return list;
    }

    public InvokeResult getUserZtreeViewList(Integer msgId, String innerCode) {
        List<Record> receivers = Db.find("select receiver_id from t_msg_receiver where msg_id=" + msgId);
        List<Long> receiversIds = new ArrayList<>();
        for (Record record : receivers) {
            receiversIds.add(record.getLong("receiver_id"));
        }
        String sql = "select susr.id as id,susr.name as name,tc.name as companyName from sys_user susr " +
                "left join t_company tc on tc.inner_code = susr.inner_code where 1=1 ";
        if (StringUtils.isNotEmpty(innerCode)) {
            sql = sql + " and susr.inner_code='" + innerCode + "'";
        }
        List<Record> list = Db.find(sql);
        List<ZtreeView> ztreeViews = new ArrayList<ZtreeView>();
        ztreeViews.add(new ZtreeView(10000, null, "用户列表", true));
        for (Record record : list) {
            ZtreeView ztreeView = new ZtreeView();
            ztreeView.setId(record.getInt("id"));
            if (StringUtils.isNotEmpty(record.getStr("companyName"))) {
                ztreeView.setName(record.getStr("name") + "(" + record.getStr("companyName") + ")");
            } else {
                ztreeView.setName(record.getStr("name"));
            }
            ztreeView.setOpen(true);
            ztreeView.setpId(10000);
            if (receiversIds.contains(Long.parseLong(record.getInt("id") + ""))) {
                ztreeView.setChecked(true);
            } else {
                ztreeView.setChecked(false);
            }
            ztreeViews.add(ztreeView);
        }
        return InvokeResult.success(JsonKit.toJson(ztreeViews));
    }

    /********************************** WxApp use ***************************************/

    /**
     * @param userId
     * @param wxAccount
     * @param token
     */
    public void updateByWxLogin(Integer userId, String wxAccount, String token) {
        Db.update("update sys_user set wx_account='" + wxAccount + "',token='" + token + "' where id=" + userId);
    }

    public InvokeResult registWx(String username, String password, String phone, String email, String wxAccount) {
        if (this.hasExist(username)) {
            return InvokeResult.failure("用户名已存在");
        } else {
            if (StrKit.isBlank(password)) {
                password = "123456";
            }
            SysUser sysUser = new SysUser();
            sysUser.set("name", username).set("pwd", MyDigestUtils.shaDigestForPasswrod(password)).set("createdate", new Date())
                    .set("phone", phone).set("email", email).set("wx_account", wxAccount).set("wx_memo", "Wexin_regist").save();
        }
        return InvokeResult.success(Boolean.TRUE, "注册成功");
    }
}