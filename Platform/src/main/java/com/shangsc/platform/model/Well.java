package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.code.YesOrNo;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.base.BaseWell;
import com.shangsc.platform.util.CodeNumUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Well extends BaseWell<Well> {

	public static final Well me = new Well();

	public Page<Well> getWellPage(int page, int rows, String keyword, String orderbyStr) {
		String select = "select tw.*,tc.name as companyName,tc.real_code,tc.water_unit,tc.county,tc.street";
		StringBuffer sqlExceptSelect = new StringBuffer(" from t_well tw inner join " +
                " t_company tc on tw.inner_code=tc.inner_code ");
		sqlExceptSelect.append(" where 1=1 ");
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

    /**
     * 水表编号是否已存在
     * @param wellNum
     * @return
     */
    public boolean hasExistWellNum(Long id, String wellNum){
        StringBuffer sqlSelect = new StringBuffer("select count(1) as existCount from t_well where 1=1 ");
        sqlSelect.append(" and well_num='" + wellNum + "'");
        if (id != null) {
            sqlSelect.append(" and id<>" + id + "");
        }
        Record record = Db.findFirst(sqlSelect.toString());
        if (record != null) {
            return record.getLong("existCount") == 1L;
        } else {
            return false;
        }
    }

	public InvokeResult save(Long id, String innerCode,	String name, String wellNum,
							 String village, String address, BigDecimal wellDepth, BigDecimal groundDepth, String year, Integer oneselfWell,
							 BigDecimal innerDiameter, String material,	String application,	Integer electromechanics, Integer calculateWater,
							 String pumpModel, Integer calculateType, Integer aboveScale, Integer geomorphicType, Integer groundType,
							 String nameCode, String watersType, String useEfficiency, String method, Integer licence,
							 String licenceCode, BigDecimal waterWithdrawals) {
		if (!Company.me.hasExistCompany(innerCode)) {
			return InvokeResult.failure("保存失败, 公司编号不存在");
		}
        if (this.hasExistWellNum(id, wellNum)) {
            return InvokeResult.failure("保存失败, 水井编号已存在");
        }
		if (null != id && id > 0L) {
			Well well = this.findById(id);
			if (well == null) {
				return InvokeResult.failure("更新失败, 该水井不存在");
			}
			well = setProp(well, innerCode, name, wellNum, village, address, wellDepth, groundDepth, year, oneselfWell,
					innerDiameter, material, application, electromechanics, calculateWater, pumpModel, calculateType, aboveScale, geomorphicType,
					groundType,	nameCode, watersType, useEfficiency, method, licence, licenceCode, waterWithdrawals);
			well.update();
		} else {
            Well well = new Well();
            well = setProp(well, innerCode, name, wellNum, village, address, wellDepth, groundDepth, year, oneselfWell,
                    innerDiameter, material, application, electromechanics, calculateWater, pumpModel, calculateType, aboveScale, geomorphicType,
                    groundType,	nameCode, watersType, useEfficiency, method, licence, licenceCode, waterWithdrawals);
            well.save();
            Company.me.updateWellNum(innerCode, true);
		}
		return InvokeResult.success();
	}

	private Well setProp(Well well, String innerCode, String name, String wellNum,
			String village, String address, BigDecimal wellDepth, BigDecimal groundDepth, String year, Integer oneselfWell,
			BigDecimal innerDiameter, String material,	String application,	Integer electromechanics, Integer calculateWater,
			String pumpModel, Integer calculateType, Integer aboveScale, Integer geomorphicType,Integer groundType,
			String nameCode, String watersType, String useEfficiency, String method, Integer licence,
			String licenceCode, BigDecimal waterWithdrawals) {
		well.setInnerCode(innerCode);
		well.setName(name);
		well.setWellNum(wellNum);
		well.setVillage(village);
		well.setAddress(address);
		well.setWellDepth(wellDepth);
		well.setYear(year);
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
            Well well = Well.me.findById(ids.get(i));
            Company.me.updateWellNum(well.getInnerCode(), false);
			this.deleteById(ids.get(i));

		}
		return InvokeResult.success();
	}

    public static int[] saveBatch(List<Well> modelOrRecordList, int batchSize) {
        String sql = "insert into t_well(well_num,name,inner_code,village,address,year,well_depth,ground_depth," +
                "oneself_well,inner_diameter,material,application,electromechanics,calculate_water," +
                "pump_model,calculate_type,above_scale,geomorphic_type,ground_type,name_code," +
                "waters_type,use_efficiency,method,licence,licence_code,water_withdrawals) " +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        String columns = "well_num,name,inner_code,village,address,year,well_depth,ground_depth," +
                "oneself_well,inner_diameter,material,application,electromechanics,calculate_water," +
                "pump_model,calculate_type,above_scale,geomorphic_type,ground_type,name_code," +
                "waters_type,use_efficiency,method,licence,licence_code,water_withdrawals";

        int[] result = Db.batch(sql, columns, modelOrRecordList, batchSize);
        return result;
    }

    public void importData(List<Map<Integer, String>> maps) {
        List<Well> lists = new ArrayList<Well>();
        Map<String, Integer> dictCalculateType = DictData.dao.getDictNameMap(DictCode.CalculateType);
        Map<String, Integer> dictGeomorphicType = DictData.dao.getDictNameMap(DictCode.GeomorphicType);
        Map<String, Integer> dictGroundType = DictData.dao.getDictNameMap(DictCode.GroundType);
        for (int i = 0; i < maps.size(); i++) {
            Map<Integer, String> map = maps.get(i);
            Well well = new Well();
            String wellNum = StringUtils.trim(map.get(0));
            if (StringUtils.isEmpty(wellNum) || hasExistWellNum(null, wellNum)) {
                continue;
            }
            if (StringUtils.isNotEmpty(wellNum)) {
                well.setWellNum(wellNum);
            }
            if (StringUtils.isNotEmpty(map.get(1))) {
                well.setName(map.get(1));
            }
            if (StringUtils.isNotEmpty(map.get(2))) {
                well.setInnerCode(map.get(2));
                Company.me.updateWellNum(map.get(2), true);
            }
            if (StringUtils.isNotEmpty(map.get(7))) {
                well.setVillage(map.get(7));
            }
            if (StringUtils.isNotEmpty(map.get(8))) {
                well.setAddress(map.get(8));
            }
            if (StringUtils.isNotEmpty(map.get(9))) {
                well.setYear(map.get(9));
            }
    /*
      水井编号	水井名称	单位编号	单位名称	所属节水办	所属区县	乡	村	水井地址	成井时间（年）
      井深（米）	地下水埋深（米）	是否为单位自备井	井口井管内径(毫米)	井壁管材料	水源类型	应用状况	是否已配套机电设备
      是否已安装水量计量设施	水泵型号	水量计量设施类型	是否为规模以上地下水水源地的水井	所在地貌类型区	所取用地下水的类型
      所在水资源三级区名称及编码	水源类型	主要取水用途及效益	取水量确定方法	是否已办理取水许可证	取水许可证编号	年许可取水量(万立方米)
     */
            if (StringUtils.isNotEmpty(map.get(10))) {
                well.setWellDepth(CodeNumUtil.getBigDecimal(map.get(10), 2));
            }
            if (StringUtils.isNotEmpty(map.get(11))) {
                well.setGroundDepth(CodeNumUtil.getBigDecimal(map.get(11), 2));
            }
            if (StringUtils.isNotEmpty(map.get(12)) && YesOrNo.YesStr.equals(StringUtils.trim(map.get(12).toString()))) {
                well.setOneselfWell(Integer.parseInt(YesOrNo.Yes));
            } else {
                well.setOneselfWell(Integer.parseInt(YesOrNo.No));
            }
            if (StringUtils.isNotEmpty(map.get(13))) {
                well.setInnerDiameter(CodeNumUtil.getBigDecimal(map.get(13), 2));
            }
            if (StringUtils.isNotEmpty(map.get(14))) {
                well.setMaterial(map.get(14));
            }
            if (StringUtils.isNotEmpty(map.get(15))) {
                well.setWatersType(map.get(15));
            }
            if (StringUtils.isNotEmpty(map.get(16))) {
                well.setApplication(map.get(16));
            }
            if (StringUtils.isNotEmpty(map.get(17)) && YesOrNo.YesStr.equals(StringUtils.trim(map.get(17).toString()))) {
                well.setElectromechanics(Integer.parseInt(YesOrNo.Yes));
            } else{
                well.setElectromechanics(Integer.parseInt(YesOrNo.No));
            }
            if (StringUtils.isNotEmpty(map.get(18)) && YesOrNo.YesStr.equals(StringUtils.trim(map.get(18).toString()))) {
                well.setCalculateWater(Integer.parseInt(YesOrNo.Yes));
            } else{
                well.setCalculateWater(Integer.parseInt(YesOrNo.No));
            }
            if (map.get(19)!=null) {
                well.setPumpModel(map.get(19));
            }
            if (map.get(20)!=null) {
                well.setCalculateType(dictCalculateType.get(map.get(20)));
            }
            if (StringUtils.isNotEmpty(map.get(21)) && YesOrNo.YesStr.equals(StringUtils.trim(map.get(21).toString()))) {
                well.setAboveScale(Integer.parseInt(YesOrNo.Yes));
            } else{
                well.setAboveScale(Integer.parseInt(YesOrNo.No));
            }
            if (StringUtils.isNotEmpty(map.get(22))) {
                well.setGeomorphicType(dictGeomorphicType.get(map.get(22)));
            }
            if (StringUtils.isNotEmpty(map.get(23))) {
                well.setGroundType(dictGroundType.get(map.get(23)));
            }
            if (StringUtils.isNotEmpty(map.get(24))) {
                well.setNameCode(map.get(24));
            }
            if (StringUtils.isNotEmpty(map.get(25))) {
                well.setUseEfficiency(map.get(25));
            }
            if (StringUtils.isNotEmpty(map.get(26))) {
                well.setMethod(map.get(26));
            }
            if (StringUtils.isNotEmpty(map.get(27)) && YesOrNo.YesStr.equals(StringUtils.trim(map.get(27).toString()))) {
                well.setLicence(Integer.parseInt(YesOrNo.Yes));
            } else{
                well.setLicence(Integer.parseInt(YesOrNo.No));
            }
            if (StringUtils.isNotEmpty(map.get(28))) {
                well.setLicenceCode(map.get(28));
            }
            if (StringUtils.isNotEmpty(map.get(29))) {
                well.setWaterWithdrawals(CodeNumUtil.getBigDecimal(map.get(29), 2));
            } else {
                well.setWaterWithdrawals(CodeNumUtil.getBigDecimal("0.00", 2));
            }
            lists.add(well);
        }
        saveBatch(lists, lists.size());
    }
}
