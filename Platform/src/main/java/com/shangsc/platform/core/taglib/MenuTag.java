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
package com.shangsc.platform.core.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.shangsc.platform.core.util.IWebUtils;
import com.shangsc.platform.model.SysRes;
import com.shangsc.platform.model.SysUser;

/**
 * 系统菜单
 * @author ssc
 * 2014年8月9日
 */
public class MenuTag extends TagSupport {

    private static final long serialVersionUID = 1L;
    private Integer type = 1;

    @Override
    public int doStartTag() throws JspException {
        try {
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            SysUser sysUser = IWebUtils.getCurrentSysUser(request);
            if (sysUser != null) {
                int uid = sysUser.getInt("id");
                String menuView = SysRes.me.getSysUserMenuView(uid, "/");
                pageContext.getOut().print(menuView);
            } else {
                pageContext.getOut().print("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
