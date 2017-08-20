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
package com.shangsc.platform.controller.sys;

import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.auth.anno.RequiresPermissions;
import com.shangsc.platform.core.auth.interceptor.SysLogInterceptor;
import com.shangsc.platform.core.controller.JCBaseController;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.model.SysLog;

import java.util.HashSet;
import java.util.Set;

/**
 * 日志管理.
 *
 * @author ssc
 */
@Clear(SysLogInterceptor.class)
public class LogController extends JCBaseController {

	@RequiresPermissions
	public void index() {
		render("log_index.jsp");
	}
	
	public void getListData() {
		String title=this.getPara("title");
		Set<Condition> conditions=new HashSet<Condition>();
		if(CommonUtils.isNotEmpty(title)){
			conditions.add(new Condition("class_name", Operators.LIKE,title));
		}
		Page<SysLog> pageInfo = SysLog.dao.getPage(getPage(), this.getRows(),conditions,this.getOrderby());
		this.renderJson(JqGridModelUtils.toJqGridView(pageInfo)); 
	}
}





