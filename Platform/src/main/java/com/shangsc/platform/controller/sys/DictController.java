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
import com.shangsc.platform.core.auth.interceptor.AuthorityInterceptor;
import com.shangsc.platform.core.controller.BaseController;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.JqGridModelUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.DictData;
import com.shangsc.platform.model.DictType;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class DictController extends BaseController {
	

	@RequiresPermissions(value={"/dict"})
	public void index() {
		render("type_index.jsp");
	}
	
	@RequiresPermissions(value={"/dict"})
	public void add_type() {
		Integer id=this.getParaToInt("id");
		if(id!=null){
			this.setAttr("item", DictType.dao.findById(id));
		}
		render("type_add.jsp");
	}
	@RequiresPermissions(value={"/dict"})
	public void add_data() {
		Integer id=this.getParaToInt("id");
		if(id!=null){
			this.setAttr("item", DictData.dao.findById(id));
		}
		this.setAttr("typeId", this.getParaToInt("typeId"));
		render("data_add.jsp");
	}
	@RequiresPermissions(value={"/dict"})
	public void data_index() {
		Integer typeId=this.getParaToInt("typeId");
		this.setAttr("typeId", typeId);
		render("data_index.jsp");
	}
	
	@RequiresPermissions(value={"/dict"})
	public void saveType(){
		Integer id=this.getParaToInt("id");
		String name=this.getPara("name");
		String remark=this.getPara("remark"); 
		InvokeResult invokeResult=DictType.dao.saveDictType(id,name,remark);
		this.renderJson(invokeResult);
	}
	
	@RequiresPermissions(value={"/dict"})
	public void saveData(){
		Integer id=this.getParaToInt("id");
		Integer seq=this.getParaToInt("seq");
		String value=this.getPara("value");
		Integer typeId=this.getParaToInt("typeId");
		String name=this.getPara("name");
		String remark=this.getPara("remark"); 
		InvokeResult invokeResult=DictData.dao.saveDictData(id,name,remark,seq,value,typeId);
		this.renderJson(invokeResult);
	}
	
	@RequiresPermissions(value={"/dict"})
	public void deleteData(){
		Integer id=this.getParaToInt("id");
		InvokeResult invokeResult=DictData.dao.deleteData(id);
		this.renderJson(invokeResult);
	}
	
	@RequiresPermissions(value={"/dict"})
	public void getTypeListData() {
		String keyword=this.getPara("keyword");
		Set<Condition> conditions=new HashSet<Condition>();
		if(CommonUtils.isNotEmpty(keyword)){
			conditions.add(new Condition("name", Operators.LIKE,keyword));
		}
		Page<DictType> pageInfo=DictType.dao.getPage(getPage(), this.getRows(),conditions,this.getOrderby());
		this.renderJson(JqGridModelUtils.toJqGridView(pageInfo)); 
	}
	
	@RequiresPermissions(value={"/dict"})
	public void getListData() {
		Integer typeId=this.getParaToInt("typeId");
		String keyword=this.getPara("keyword");
		Set<Condition> conditions=new HashSet<Condition>();
		if(CommonUtils.isNotEmpty(keyword)){
			conditions.add(new Condition("name",Operators.LIKE,keyword));
		}
		conditions.add(new Condition("dict_type_id",Operators.EQ,typeId));
		Page<DictData> pageInfo=DictData.dao.getPage(getPage(), this.getRows(),conditions,this.getOrderby());
		this.renderJson(JqGridModelUtils.toJqGridView(pageInfo)); 
	}

    @Clear(AuthorityInterceptor.class)
    public void getByType() {
        Integer typeId = this.getParaToInt("typeId");
        String typeName = this.getPara("typeName");

        Map<String, List<Map<String, Object>>> allData = new HashMap<String, List<Map<String,Object>>>();

        if ((typeId == null || typeId == 0) && StringUtils.isEmpty(typeName)) {

            List<Map<String, Object>> resultUserType = DictData.dao.getDictMapList(0, DictData.UserType);

            allData.put(DictData.UserType, resultUserType);

			List<Map<String, Object>> resultWaterUseType = DictData.dao.getDictMapList(0, DictData.WaterUseType);

            allData.put(DictData.WaterUseType, resultWaterUseType);

			List<Map<String, Object>> resultUnitType = DictData.dao.getDictMapList(0, DictData.UnitType);

            allData.put(DictData.UnitType, resultUnitType);

			List<Map<String, Object>> resultWatersType = DictData.dao.getDictMapList(0, DictData.WatersType);

            allData.put(DictData.WatersType, resultWatersType);

			List<Map<String, Object>> resultChargeType = DictData.dao.getDictMapList(0, DictData.ChargeType);

            allData.put(DictData.ChargeType, resultChargeType);

        } else {
			List<Map<String, Object>> resultType = DictData.dao.getDictMapList(typeId, typeName);
            allData.put(typeName, resultType);
        }
        this.renderJson(allData);
    }
}





