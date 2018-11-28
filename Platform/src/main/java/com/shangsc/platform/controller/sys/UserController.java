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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 系统用户管理.
 *
 * @author ssc
 */
public class UserController extends BaseController {

    private final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    @RequiresPermissions(value = {"/sys/user"})
    public void index() {
        render("user_index.jsp");
    }

    @RequiresPermissions(value = {"/sys/user"})
    public void getListData() {
        SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
        if ("admin".equals(sysUser.getName())) {

        }
        String keyword = this.getPara("name");
        Page<SysUser> pageInfo = SysUser.me.getSysUserPage(getPage(), this.getRows(), keyword, "", this.getOrderbyStr());
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
        String wxAccount = this.getPara("wxAccount");
        InvokeResult result = SysUser.me.save(id, username, password, des, phone, email, innerCode, wxAccount);
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


    @RequiresPermissions(value = {"/sys/user"})
    public void setCompanies() {
        Integer uId = this.getParaToInt("uId");
        SysUser byId = SysUser.me.findById(uId);
        String innerCodes = byId.getInnerCode();
        String innerCodesResult = "";
        if (StringUtils.isNotEmpty(innerCodes)) {
            String[] split = innerCodes.split(",");
            for (int i = 0; i < split.length; i++) {
                split[i] = "'" + split[i] + "'";
            }
            innerCodesResult = StringUtils.join(split, ",");
        }
        InvokeResult result = Company.me.getUserZtreeViewList(innerCodesResult, "");
        this.setAttr("jsonTree", result);
        Map<String, String> nameList = Company.me.loadNameList();
        Set<String> names = nameList.keySet();
        this.setAttr("nameCodeMap", JSONUtils.toJSONString(nameList));
        this.setAttr("names", JSONUtils.toJSONString(names));
        this.setAttr("uId", this.getParaToInt("uId"));
        render("user_companies.jsp");
    }

    @RequiresPermissions(value = {"/sys/user"})
    public void saveCompanies() {
        final Integer uId = this.getParaToInt("uId");
        String companiesIds = this.getPara("companiesIds");
        String[] split = StringUtils.split(companiesIds, ",");
        List<Integer> cIdsList = new ArrayList<>();
        for (String cid : split) {
            Integer targetId = Integer.valueOf(cid);
            if (targetId > 0 && targetId < 100000000) {
                cIdsList.add(targetId);
            }
        }
        final List<Integer> cIdsListFinal = cIdsList;
        if (CollectionUtils.isNotEmpty(cIdsList)) {
            singleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    List<Company> companies = Company.me.find("select inner_code from t_company where id in (" + StringUtils.join(cIdsListFinal, ",") + ")");
                    String innerCodes = "";
                    if (CollectionUtils.isNotEmpty(companies)) {
                        List<String> innerCodesList = new ArrayList<>();
                        for (Company c : companies) {
                            innerCodesList.add(c.getInnerCode());
                        }
                        if (CollectionUtils.isNotEmpty(innerCodesList)) {
                            innerCodes = StringUtils.join(innerCodesList, ",");
                        }
                    }
                    logger.info("【分配管辖范围】 uId={} ==> {}", uId, innerCodes);
                    SysUser byId = SysUser.me.findById(uId);
                    byId.setInnerCode(innerCodes);
                    byId.update();
                }
            });
        } else {
            logger.info("【分配管辖范围】 uId={} ==> {}", uId, "");
            SysUser byId = SysUser.me.findById(uId);
            byId.setInnerCode("");
            byId.update();
        }
        renderJson(InvokeResult.success());
    }

    @RequiresPermissions(value = {"/sys/user"})
    public void delete() {
        String ids = this.getPara("ids");
        InvokeResult result = SysUser.me.deleteData(ids);
        this.renderJson(result);
    }

}





