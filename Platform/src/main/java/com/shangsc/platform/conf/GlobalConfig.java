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
package com.shangsc.platform.conf;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.*;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.druid.IDruidStatViewAuth;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.shangsc.front.config.WxApiRoutes;
import com.shangsc.platform.actual.ActualTcpPlugin;
import com.shangsc.platform.actual.ActualUdpPlugin;
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.auth.interceptor.SysLogInterceptor;
import com.shangsc.platform.core.handler.ResourceHandler;
import com.shangsc.platform.core.util.IWebUtils;
import com.shangsc.platform.mail.MailPlugin;
import com.shangsc.platform.model.SysUser;
import com.shangsc.platform.model._MappingKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * API引导式配置
 */
public class GlobalConfig extends JFinalConfig implements Serializable {

    private static final long serialVersionUID = 853477578384441419L;

    public static final int EXPORT_SUM = 65535; //excel office 2003 支持最大行数

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    private void log(String msg) {
        logger.info(msg);
    }

    /**
     * 配置常量
     */
    @Override
    public void configConstant(Constants me) {
        // 加载少量必要配置，随后可用PropKit.get(...)获取值
        PropKit.use("config.properties");
        me.setDevMode(PropKit.getBoolean("devMode", false));
        me.setViewType(ViewType.JSP);
        me.setError404View("/page/404.jsp");
        me.setError500View("/page/500.jsp");
    }

    /**
     * 配置路由
     */
    @Override
    public void configRoute(Routes me) {
        me.add(new AdminRoutes());
        me.add(new FrontRoutes());
        me.add(new WxApiRoutes());
    }

    public static C3p0Plugin createC3p0Plugin() {
        return new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password"));
    }

    public static DruidPlugin createDruidPlugin() {
        DruidPlugin dp = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password"));
        dp.addFilter(new StatFilter());
        WallFilter wall = new WallFilter();
        wall.setDbType("mysql");
        dp.addFilter(wall);
        return dp;
    }

    /**
     * 配置插件
     */
    @Override
    public void configPlugin(Plugins me) {
        DruidPlugin druidPlugin = createDruidPlugin();
        me.add(druidPlugin);
        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        arp.setShowSql(PropKit.getBoolean("devMode", false));
        arp.setDevMode(PropKit.getBoolean("devMode", false));
        me.add(arp);
        me.add(new EhCachePlugin());
        // 所有配置在 MappingKit 中搞定
        _MappingKit.mapping(arp);

        //加载邮件配置
        initMail();

        //开启实时数据监听
        //initActualUdp();
        //initActualTcp();
    }

    /**
     * 配置全局拦截器
     */
    @Override
    public void configInterceptor(Interceptors me) {
        me.addGlobalActionInterceptor(new SysLogInterceptor());
        me.addGlobalActionInterceptor(new AuthorityInterceptor());
    }

    @Override
    public void configEngine(Engine arg0) {
        // TODO Auto-generated method stub
    }

    /**
     * 配置处理器
     */
    @Override
    public void configHandler(Handlers me) {
        DruidStatViewHandler dvh = new DruidStatViewHandler("/druid", new IDruidStatViewAuth() {
            @Override
            public boolean isPermitted(HttpServletRequest request) {
                SysUser sysUser = IWebUtils.getCurrentSysUser(request);
                return (sysUser != null && sysUser.getStr("name").equals("admin"));
            }
        });
        me.add(dvh);
        me.add(new ResourceHandler());
    }

    public void initMail() {
        new MailPlugin(PropKit.use("mail.properties").getProperties()).start();
    }


    public void initActualUdp() {
        try {
            new ActualUdpPlugin().start();
        } catch (Exception e) {
            e.printStackTrace();
            log("Udp server start failed");
        }
    }

    public void initActualTcp() {
        try {
            new ActualTcpPlugin().start();
        } catch (Exception e) {
            e.printStackTrace();
            log("Udp server start failed");
        }
    }
}
