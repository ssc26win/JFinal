/**
 * Copyright (c) 2011-2016, Eason Pan(pylxyhome@vip.qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shangsc.platform.conf;

import com.jfinal.config.Routes;
import com.shangsc.platform.controller.ChartController;
import com.shangsc.platform.controller.ImageController;
import com.shangsc.platform.controller.IndexController;
import com.shangsc.platform.controller.app.AppVersionController;
import com.shangsc.platform.controller.basic.*;
import com.shangsc.platform.controller.statis.*;
import com.shangsc.platform.controller.sys.*;

/**
 * 后台管理Routes配置
 * @author ssc
 *
 */
public class AdminRoutes extends Routes{

	@Override
	public void config() {
		add("/", IndexController.class,"/WEB-INF/view");
		add("/sys/log", LogController.class,"/WEB-INF/view/sys");
		add("/sys/res", ResController.class,"/WEB-INF/view/sys");
		add("/sys/user", UserController.class,"/WEB-INF/view/sys");
		add("/sys/role", RoleController.class,"/WEB-INF/view/sys");
		add("/dict", DictController.class,"/WEB-INF/view/sys/dict");
		add("/app", AppVersionController.class,"/WEB-INF/view/app");
		add("/image", ImageController.class,"/WEB-INF/view/image");

        add("/basic/company", CompanyController.class, "/WEB-INF/view/basic");
        add("/basic/meter", MeterController.class, "/WEB-INF/view/basic");
        add("/basic/well", WellController.class, "/WEB-INF/view/basic");
        add("/basic/waterindex", WaterIndexController.class, "/WEB-INF/view/basic");

        add("/baidumap", MapController.class, "/WEB-INF/view");

		add("/basic/ad", AdController.class, "/WEB-INF/view/basic");

		add("/statis/actual", ActualController.class, "/WEB-INF/view/statis");

        add("/statis/readnum", ReadnumStatisController.class, "/WEB-INF/view/statis");
        add("/statis/daily", DailyStatisController.class, "/WEB-INF/view/statis");
        add("/statis/month", MonthStatisController.class, "/WEB-INF/view/statis");
        add("/statis/year", YearStatisController.class, "/WEB-INF/view/statis");
		add("/chart", ChartController.class, "/WEB-INF/view");
	}

}
