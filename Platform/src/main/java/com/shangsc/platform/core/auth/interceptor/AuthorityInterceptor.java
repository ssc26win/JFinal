package com.shangsc.platform.core.auth.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.common.Logical;
import com.shangsc.platform.core.util.IWebUtils;
import com.shangsc.platform.model.SysUser;

/**
 * 权限拦截器
 */
public class AuthorityInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        RequiresPermissions requiresPermissions = inv.getMethod().getAnnotation(RequiresPermissions.class);
        boolean hasPermissions = true;
        if (requiresPermissions == null) {
            requiresPermissions = inv.getController().getClass().getAnnotation(RequiresPermissions.class);
        }
        if (requiresPermissions != null) {
            try {
                SysUser sysUser = IWebUtils.getCurrentSysUser(inv.getController().getRequest());
                if (sysUser == null) {//没有登陆
                    hasPermissions = false;
                    if (StrKit.notBlank(inv.getActionKey()) && !inv.getActionKey().equals("/")) {
                        inv.getController().redirect("/login?url=" + inv.getActionKey() + "&login_time_out=1");
                    } else {
                        inv.getController().redirect("/login");
                    }
                } else {
                    //判断是否有该资源的访问权限
                    String[] values = requiresPermissions.value();
                    if (values.length == 1) {
                        if (StrKit.isBlank(values[0])) {
                            values[0] = inv.getActionKey();
                        }
                        if (!sysUser.getPermissionSets().contains(values[0]) && !sysUser.isAdmin()) {
                            hasPermissions = false;
                            inv.getController().redirect("/login?noAuth=1");
                        }
                    } else {
                        if (requiresPermissions.logical().equals(Logical.AND)) {
                            for (String value : values) {
                                if (!sysUser.getPermissionSets().contains(value)) {
                                    hasPermissions = false;
                                    break;
                                }
                            }
                            if (!hasPermissions) {
                                if (sysUser.isAdmin()) {
                                    hasPermissions = true;
                                }
                            }
                        } else {
                            hasPermissions = false;
                            for (String value : values) {
                                if (sysUser.getPermissionSets().contains(value)) {
                                    hasPermissions = true;
                                    break;
                                }
                            }
                        }
                        if (!hasPermissions) {
                            if (sysUser.isAdmin()) {
                                hasPermissions = true;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (hasPermissions) {
            inv.invoke();
        }
    }
}
