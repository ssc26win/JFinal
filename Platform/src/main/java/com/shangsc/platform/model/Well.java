package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.base.BaseWell;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Well extends BaseWell<Well> {

	public static final Well me = new Well();

	public Page<Well> getWellPage(int page, int rows, String keyword, String orderbyStr) {
		String select = "select tw.*,tc.name as companyName";
		StringBuffer sqlExceptSelect = new StringBuffer(" from t_well tw, t_company tc");
		sqlExceptSelect.append(" where 1=1 and tw.inner_code=tc.inner_code ");
		if (StringUtils.isNotEmpty(keyword)) {
			keyword = StringUtils.trim(keyword);
			if (StringUtils.isNotEmpty(keyword)) {
				sqlExceptSelect.append(" and (tw.name like '%" + keyword + "%' " +" or tc.name like '%" + keyword + "%'"
						+ " or tw.inner_code='" + keyword + "' or tw.well_num='" + keyword + "') ");
			}
		}
		if (StringUtils.isNotEmpty(orderbyStr)) {
			sqlExceptSelect.append(orderbyStr);
		}
		this.paginate(page, rows, select, sqlExceptSelect.toString());
		return this.paginate(page, rows, select, sqlExceptSelect.toString());
	}
	/**
	 * 水表编号是否已存在
	 * @param wellNum
	 * @return
	 */
	public boolean hasExist(String wellNum){
		Set<Condition> conditions = new HashSet<Condition>();
		conditions.add(new Condition("well_num", Operators.EQ, wellNum));
		long num = this.getCount(conditions);
		return num>0?true:false;
	}

	public InvokeResult save(Long id, Long companyId, String innerCode,	String name, String wellNum, String township,
							 String village, String address, BigDecimal wellDepth, BigDecimal groundDepth, Date startDate, Integer oneselfWell,
							 BigDecimal innerDiameter, String material,	String application,	Integer electromechanics, Integer calculateWater,
							 Integer pumpModel, Integer calculateType, Integer aboveScale, Integer geomorphicType, Integer groundType,
							 String nameCode, Integer watersType, String useEfficiency, String method, Integer licence,
							 String licenceCode, BigDecimal waterWithdrawals) {
		if (!Company.me.hasExistCompany(innerCode)) {
			return InvokeResult.failure("公司编号不存在");
		}
		if (null != id && id > 0l) {
			Well well = this.findById(id);
			if (well == null) {
				return InvokeResult.failure("更新失败, 该水井不存在");
			}
			well = setProp(well, companyId, innerCode, name, wellNum, township, village, address, wellDepth, groundDepth, startDate, oneselfWell,
					innerDiameter, material, application, electromechanics, calculateWater, pumpModel, calculateType, aboveScale, geomorphicType,
					groundType,	nameCode, watersType, useEfficiency, method, licence, licenceCode, waterWithdrawals);
			well.update();
		} else {
			if (this.hasExist(wellNum)) {
				return InvokeResult.failure("水井编号已存在");
			} else {
				Well well = new Well();
				well = setProp(well, companyId, innerCode, name, wellNum, township, village, address, wellDepth, groundDepth, startDate, oneselfWell,
						innerDiameter, material, application, electromechanics, calculateWater, pumpModel, calculateType, aboveScale, geomorphicType,
						groundType,	nameCode, watersType, useEfficiency, method, licence, licenceCode, waterWithdrawals);
				well.save();
			}
		}
		return InvokeResult.success();
	}

	private Well setProp(Well well, Long companyId, String innerCode, String name, String wellNum, String township,
			String village, String address, BigDecimal wellDepth, BigDecimal groundDepth, Date startDate, Integer oneselfWell,
			BigDecimal innerDiameter, String material,	String application,	Integer electromechanics, Integer calculateWater,
			Integer pumpModel, Integer calculateType, Integer aboveScale, Integer geomorphicType,Integer groundType,
			String nameCode, Integer watersType, String useEfficiency, String method, Integer licence,
			String licenceCode, BigDecimal waterWithdrawals) {
		well.setCompanyId(companyId);
		well.setInnerCode(innerCode);
		well.setName(name);
		well.setWellNum(wellNum);
		well.setTownship(township);
		well.setVillage(village);
		well.setAddress(address);
		well.setWellDepth(wellDepth);
		well.setStartDate(startDate);
		well.setGroundDepth(groundDepth);
		well.setOneselfWell(oneselfWell);
		well.setInnerDiameter(innerDiameter);
		well.setMaterial(material);
		well.setApplication(application);
		well.setElectromechanics(electromechanics);
		well.setCalculateWater(calculateWater);
		well.setPumpModel(pumpModel);
		well.setCalculateType(calculateType);
		well.setAboveScale(aboveScale);
		well.setGeomorphicType(geomorphicType);
		well.setGroundType(groundType);
		well.setNameCode(nameCode);
		well.setWatersType(watersType);
		well.setUseEfficiency(useEfficiency);
		well.setMethod(method);
		well.setLicence(licence);
		well.setLicenceCode(licenceCode);
		well.setWaterWithdrawals(waterWithdrawals);
		return well;
	}

	public InvokeResult deleteData(String idStrs) {
		List<Long> ids = CommonUtils.getLongListByStrs(idStrs);
		for (int i = 0; i < ids.size(); i++) {
			this.deleteById(ids.get(i));
		}
		return InvokeResult.success();
	}

}
