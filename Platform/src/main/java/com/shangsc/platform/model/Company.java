package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.base.BaseCompany;

import java.util.*;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Company extends BaseCompany<Company> {
	public static final Company me = new Company();
    private static final long serialVersionUID = -1982696969221258167L;

    /**
     * 单位名是否已存在
     * @param name
     * @return
     */
    public boolean hasExist(String name, String innerCode){
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("name", Operators.EQ, name));
        conditions.add(new Condition("inner_code", Operators.EQ, innerCode));
        long num = this.getCount(conditions);
        return num>0?true:false;
    }

    public InvokeResult deleteData(Long id) {
        this.deleteById(id);
        return InvokeResult.success();
    }

    public InvokeResult save(Long id, String name, String innerCode, String street, String address, Integer customerType, Integer waterUseType,
                             String contact, String phone, String postalCode, String department, Integer wellCount, Integer firstWatermeterCount,
                             Integer remotemeterCount,Integer unitType) {
        if (null != id && id > 0l) {
            Company company = this.findById(id);
            if (company == null) {
                return InvokeResult.failure("更新失败单位, 该单位不存在");
            }
            company = setProp(company, name, innerCode, street, address, customerType, waterUseType,
                    contact, phone, postalCode, department, wellCount, firstWatermeterCount, remotemeterCount, unitType);
            company.update();
        } else {
            if(this.hasExist(name, innerCode)){
                return InvokeResult.failure("单位名称已存在");
            } else {
                Company company = new Company();
                company = setProp(company, name, innerCode, street, address, customerType, waterUseType,
                        contact, phone, postalCode, department, wellCount, firstWatermeterCount, remotemeterCount, unitType);
                company.save();
            }
        }
        return InvokeResult.success();
    }

    private Company setProp(Company company, String name, String innerCode, String street, String address, Integer customerType,
                            Integer waterUseType, String contact, String phone, String postalCode, String department, Integer wellCount,
                            Integer firstWatermeterCount, Integer remotemeterCount,Integer unitType) {
        company.setName(name);
        company.setInnerCode(innerCode);
        company.setStreet(street);
        company.setAddress(address);
        company.setCustomerType(customerType);
        company.setWaterUseType(waterUseType);
        company.setContact(contact);
        company.setPhone(phone);
        company.setPostalCode(postalCode);
        company.setDepartment(department);
        company.setWellCount(wellCount);
        company.setFirstWatermeterCount(firstWatermeterCount);
        company.setRemotemeterCount(remotemeterCount);
        company.setUnitType(unitType);
        company.setCreateTime(new Date());
        return company;
    }

    public Page<Company> getCompanyPage(int page, int rows, String keyword, String orderbyStr) {
        Set<Condition> conditions = new HashSet<Condition>();
        if (CommonUtils.isNotEmpty(keyword)) {
            conditions.add(new Condition("name", Operators.LIKE, keyword));
        }
        String select="select c.* from";
        StringBuffer sqlExceptSelect = new StringBuffer("t_company c");
        return this.paginate(page, rows, select, sqlExceptSelect.toString());
    }

    public Map<String, Object> getCompanyMap() {
        List<Company> allList = this.getAllList();
        Map<String , Object> map = new HashMap<String, Object>();
        for (Company company : allList) {
            map.put(company.getInnerCode(), company.getName());
        }
        return map;
    }

    public Page<Company> getReadnumStatis(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime, String ... keywords) {
        String select=" select * ";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_company c ");
        return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }

    public Page<Company> getDailyStatis(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime, String ... keywords) {
        String select=" select c.*, (select meter_num,line_num from t_water_meter twm where twm.inner_code=c.inner_code) ";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_company c ");
        return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }

    public Page<Company> getMonthStatis(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime, String ... keywords) {
        String select=" select * ";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_company c ");
        return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }

    public Page<Company> getYearStatis(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime, String ... keywords) {
        String select=" select * ";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_company c ");
        return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }
}
