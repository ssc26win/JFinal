package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.base.BaseCompany;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Company extends BaseCompany<Company> {
	public static final Company me = new Company();
    private static final long serialVersionUID = -1982696969221258167L;

    /**
     * 公司编号是否已存在
     * @param inner_code
     * @return
     */
    public boolean hasExistCompany(String inner_code){
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("inner_code", Operators.EQ, inner_code));
        long num = Company.me.getCount(conditions);
        return num>0?true:false;
    }

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

    public InvokeResult deleteData(String idStrs) {
        List<Long> ids = CommonUtils.getLongListByStrs(idStrs);
        for (int i = 0; i < ids.size(); i++) {
            this.deleteById(ids.get(i));
        }
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
        String select="select c.*, (select count(net_water) from t_actual_data tad where c.inner_code = tad.inner_code) as waterUseNum";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_company c ");
        sqlExceptSelect.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (c.name like '%" + StringUtils.trim(keyword) + "%' or c.inner_code='" + StringUtils.trim(keyword)
                    + "' or contact='" + StringUtils.trim(keyword) + "') ");
        }
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
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

    public int hasActual() {
        String sqlExceptSelect = " select count(c.inner_code) as normalNum from t_company c " +
                "INNER JOIN (select DISTINCT inner_code from t_actual_data) ta on ta.inner_code=c.inner_code";
        List<Record> lists = Db.find(sqlExceptSelect);
        if (CollectionUtils.isNotEmpty(lists)) {
            Object obj = lists.get(0).get("normalNum");
            if (obj != null) {
                return Integer.parseInt(obj.toString());
            } else {
                return 0;
            }
        }
        return 0;
    }

    public int totalCount() {
        String sqlExceptSelect = " select count(*) as totalCompany from (select DISTINCT inner_code from t_company) c ";
        List<Record> lists = Db.find(sqlExceptSelect);
        if (CollectionUtils.isNotEmpty(lists)) {
            Object obj = lists.get(0).get("totalCompany");
            if (obj != null) {
                return Integer.parseInt(obj.toString());
            } else {
                return 0;
            }
        }
        return 0;
    }

    public int warnCount() {
        String innerCodes = getWarnInnerCodes();
        String sqlExceptSelect = " select count(*) as warnCompany from t_company" +
                " where inner_code in (" + innerCodes + ")";
        List<Record> lists = Db.find(sqlExceptSelect);
        if (CollectionUtils.isNotEmpty(lists)) {
            Object obj = lists.get(0).get("warnCompany");
            if (obj != null) {
                return Integer.parseInt(obj.toString());
            } else {
                return 0;
            }
        }
        return 0;
    }

    public Page<Company> getWarnCompanyPage(int page, int rows, String keyword, String orderbyStr) {
        String innerCodes = getWarnInnerCodes();
        String select="select c.*";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_company c ");
        sqlExceptSelect.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (c.name like '%" + StringUtils.trim(keyword) + "%' or c.inner_code='" + StringUtils.trim(keyword)
                    + "' or contact='" + StringUtils.trim(keyword) + "') ");
        }
        if (StringUtils.isNotEmpty(innerCodes)) {
            sqlExceptSelect.append(" and c.inner_code in (" + innerCodes+ ")");
        }
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        return this.paginate(page, rows, select, sqlExceptSelect.toString());
    }


    public Page<Company> getNormalCompanyPage(int page, int rows, String keyword, String orderbyStr) {
        String select="select c.*";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_company c ");
        sqlExceptSelect.append(" where 1=1 and c.inner_code in (select DISTINCT inner_code from t_actual_data tad )");
        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (c.name like '%" + StringUtils.trim(keyword) + "%' or c.inner_code='" + StringUtils.trim(keyword)
                    + "' or contact='" + StringUtils.trim(keyword) + "') ");
        }
        String warnInnercode = getWarnInnerCodes();
        if (StringUtils.isNotEmpty(warnInnercode)) {
            sqlExceptSelect.append(" and c.inner_code not in (" + warnInnercode+ ")");
        }
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        return this.paginate(page, rows, select, sqlExceptSelect.toString());
    }

    public Page<Company> getOtherCompanyPage(int page, int rows, String keyword, String orderbyStr) {
        String select="select c.*";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_company c");
        sqlExceptSelect.append(" where 1=1 and c.inner_code not in (select DISTINCT inner_code from t_actual_data tad )");
        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (c.name like '%" + StringUtils.trim(keyword) + "%' or c.inner_code='" + StringUtils.trim(keyword)
                    + "' or contact='" + StringUtils.trim(keyword) + "') ");
        }
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        return this.paginate(page, rows, select, sqlExceptSelect.toString());
    }

    private Set<String> warnInnerCodes = new HashSet<>();
    // 预警閥值
    private static final BigDecimal THRESHOLD = new BigDecimal("2");

    public String getWarnInnerCodes() {
        //用水量告警单位数量
        //取得用水指标数据
        List<WaterIndex> waterIndices = WaterIndex.me.getAllList();
        for (WaterIndex index : waterIndices) {
            index.getWaterIndex();//年
            WaterMeter waterMeter = WaterMeter.me.findByInnerCode(index.getInnerCode());

            if (null != waterMeter) {
                Record records1 = ActualData.me.getYearActual(index.getInnerCode());
                if (null != records1) {
                    comp((BigDecimal) records1.get("yearTotal"), (BigDecimal) index.getWaterIndex(), index.getInnerCode());

                    List<Record> records = ActualData.me.getMonthActualDataPage(index.getInnerCode());
                    for (int i = 0; i < records.size(); i++) {
                        Record record = records.get(i);
                        BigDecimal monthActTotal = new BigDecimal(record.get("total").toString());
                        switch ((record.get("time").toString())) {
                            case "01":
                                comp(monthActTotal, index.getJanuary(), index.getInnerCode());
                                break;
                            case "02":
                                comp(monthActTotal, index.getFebruary(), index.getInnerCode());
                                break;
                            case "03":
                                comp(monthActTotal, index.getMarch(), index.getInnerCode());
                                break;
                            case "04":
                                comp(monthActTotal, index.getApril(), index.getInnerCode());
                                break;
                            case "05":
                                comp(monthActTotal, index.getMay(), index.getInnerCode());
                                break;
                            case "06":
                                comp(monthActTotal, index.getJune(), index.getInnerCode());
                                break;
                            case "07":
                                comp(monthActTotal, index.getJuly(), index.getInnerCode());
                                break;
                            case "08":
                                comp(monthActTotal, index.getAugust(), index.getInnerCode());
                                break;
                            case "09":
                                comp(monthActTotal, index.getSeptember(), index.getInnerCode());
                                break;
                            case "10":
                                comp(monthActTotal, index.getOctober(), index.getInnerCode());
                                break;
                            case "11":
                                comp(monthActTotal, index.getNovember(), index.getInnerCode());
                                break;
                            case "12":
                                comp(monthActTotal, index.getDecember(), index.getInnerCode());
                                break;
                        }
                    }
                }
            }

        }
        String innerCodes = "";
        StringBuilder sb= new StringBuilder();
        for (String innerCode:warnInnerCodes) {
            sb.append("'").append(innerCode).append("'").append(",");
        }
        if (StringUtils.isNotEmpty(sb.toString())) {
            innerCodes = sb.toString().substring(0, sb.toString().length() - 1);
        }
        return innerCodes;
    }

    private void comp(BigDecimal monthActTotal, BigDecimal moth, String innerCode) {
        if (null == moth) {
            moth = new BigDecimal(0);
        }
        if (moth.add(this.THRESHOLD).compareTo(monthActTotal) < 0) {
            warnInnerCodes.add(innerCode);
        }
    }
}
