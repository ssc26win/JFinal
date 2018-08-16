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
package com.shangsc.platform.controller.sys;

import com.alibaba.druid.support.json.JSONUtils;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.util.IWebUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.SysRole;
import com.shangsc.platform.model.SysUser;
import com.shangsc.platform.vo.SysUserVo;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统用户管理.
 *
 * @author ssc
 */
public class UserController extends BaseController {

    @RequiresPermissions(value = {"/sys/user"})
    public void index() {
        render("user_index.jsp");
    }

    @RequiresPermissions(value = {"/sys/user"})
    public void getListData() {
        SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
        String keyword = this.getPara("name");
        Page<SysUser> pageInfo = SysUser.me.getSysUserPage(getPage(), this.getRows(), keyword, sysUser.getInnerCode(), this.getOrderbyStr());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value = {"/sys/user"})
    public void setVisible() {
        Integer visible = this.getParaToInt("visible");
        String ids = this.getPara("ids");
        InvokeResult result = SysUser.me.setVisible(ids, visible);
        this.renderJson(result);
    }

    @RequiresPermissions(value = {"/sys/user"})
    public void add() {
        Integer id = this.getParaToInt("id");
        String innerCode = "";
        if (id != null) {
            SysUser byId = SysUser.me.findById(id);
            this.setAttr("item", byId);
            innerCode = byId.getInnerCode();
        }
        List<SysRole> list = SysRole.me.getSysRoleNamelist();
        this.setAttr("roleList", list);
        this.setAttr("id", id);

        Map<String, String> nameList = Company.me.loadNameList();
        if (StringUtils.isNotEmpty(innerCode)) {
            for (String cName : nameList.keySet()) {
                if (innerCode.equals(nameList.get(cName))) {
                    this.setAttr("companyName", cName);
                }
            }
        }
        Set<String> names = nameList.keySet();
        this.setAttr("nameCodeMap", JSONUtils.toJSONString(nameList));
        this.setAttr("names", JSONUtils.toJSONString(names));

        render("user_add.jsp");
    }

    @RequiresPermissions(value = {"/sys/user"})
    public void save() {
        String username = this.getPara("name");
        String password = this.getPara("password");
        String phone = this.getPara("phone");
        String email = this.getPara("email");
        Integer id = this.getParaToInt("id");
        String des = this.getPara("des");
        String innerCode = this.getPara("innerCode");
        InvokeResult result = SysUser.me.save(id, username, password, des, phone, email, innerCode);
        this.renderJson(result);
    }


    @RequiresPermissions(value = {"/sys/user"})
    public void userRoleSetting() {
        Integer uid = this.getParaToInt("uid");
        this.setAttr("item", SysUser.me.findById(uid));
        InvokeResult result = SysRole.me.getRoleZtreeViewList(uid);
        this.setAttr("jsonTree", result);
        render("user_role_setting.jsp");
    }


    @RequiresPermissions(value = {"/sys/user"})
    @Before(Tx.class)
    public void saveUserRoles() {
        Integer uid = this.getParaToInt("id");
        String roleIds = this.getPara("roleIds");
        InvokeResult result = SysUser.me.changeUserRoles(uid, roleIds);
        this.renderJson(result);
    }

    @RequiresPermissions(value = {"/sys/user"})
    public void getMsgReceivers() {
        List<Record> list = SysUser.me.getSysUserList();
        List<SysUserVo> users = new ArrayList<>();
        for (Record record : list) {
            SysUserVo vo = new SysUserVo(record.getInt("id"), record.getStr("name"), record.getStr("companyName"));
            users.add(vo);
        }
        this.renderJson(list);
    }
}





