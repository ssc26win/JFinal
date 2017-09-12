package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.shangsc.platform.code.CompanyType;
import com.shangsc.platform.code.DictCode;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.model.Operators;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.util.DateUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.base.BaseCompany;
import com.shangsc.platform.util.CodeNumUtil;
import com.shangsc.platform.util.ToolDateTime;
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

    public Company findByInnerCode(String inner_code) {
        String sql = "select * from t_company where inner_code='" + inner_code + "'";
        List<Company> companies = this.find(sql);
        if (CollectionUtils.isNotEmpty(companies)) {
            return companies.get(0);
        }
        return null;
    }

    public void updateMeterNum(String innerCode, boolean flag) {
        Company company = Company.me.findByInnerCode(innerCode);
        if (company != null) {
            Integer num = company.getRemotemeterCount();
            if (num == null) {
                num = 0;
            }
            if (flag) {
                company.setRemotemeterCount(num + 1);
            } else {
                if (num > 0) {
                    company.setRemotemeterCount(num - 1);
                }
            }
            company.update();
        }
    }

    public void updateWellNum(String innerCode, boolean flag) {
        Company company = Company.me.findByInnerCode(innerCode);
        if (company != null) {
            Integer num = company.getWellCount();
            if (num == null) {
                num = 0;
            }
            if (flag) {
                company.setWellCount(num + 1);
            } else {
                if (num > 0) {
                    company.setWellCount(num - 1);
                }
            }
            company.update();
        }
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

    public InvokeResult save(Long id, String name, String innerCode, String waterUnit, String county, Integer street, String streetSrc,
                             String address, Integer customerType, Integer waterUseType, String gbIndustry, String mainIndustry,
                             String contact, String phone, String postalCode, String department, Integer wellCount, Integer firstWatermeterCount,
                             Integer remotemeterCount,Integer unitType, BigDecimal longitude, BigDecimal latitude, Date createDate,
                             BigDecimal self_well_price, BigDecimal surface_price, BigDecimal self_free_price) {
        if (null != id && id > 0l) {
            Company company = this.findById(id);
            if (company == null) {
                return InvokeResult.failure("更新失败单位, 该单位不存在");
            }
            company = setProp(company, name, innerCode, waterUnit, county, street, streetSrc, address, customerType, waterUseType,
                    gbIndustry, mainIndustry, contact, phone, postalCode, department, wellCount, firstWatermeterCount,
                    remotemeterCount, unitType, longitude, latitude, createDate, self_well_price, surface_price, self_free_price);
            company.update();
        } else {
            if(this.hasExist(name, innerCode)){
                return InvokeResult.failure("单位名称已存在");
            } else {
                Company company = new Company();
                company = setProp(company, name, innerCode, waterUnit, county, street, streetSrc, address, customerType, waterUseType,
                        gbIndustry, mainIndustry, contact, phone, postalCode, department, wellCount, firstWatermeterCount,
                        remotemeterCount, unitType, longitude, latitude, createDate, self_well_price, surface_price, self_free_price);
                company.save();
            }
        }
        return InvokeResult.success();
    }

    private Company setProp(Company company, String name, String innerCode, String waterUnit, String county, Integer street, String streetSrc,
                            String address, Integer customerType, Integer waterUseType, String gbIndustry, String mainIndustry,
                            String contact, String phone, String postalCode, String department, Integer wellCount, Integer firstWatermeterCount,
                            Integer remotemeterCount,Integer unitType, BigDecimal longitude, BigDecimal latitude, Date createDate,
                            BigDecimal self_well_price, BigDecimal surface_price, BigDecimal self_free_price) {
        company.setName(name);
        company.setInnerCode(innerCode);
        company.setWaterUnit(waterUnit);
        company.setCounty(county);
        company.setStreet(street);
        company.setStreetSrc(streetSrc);
        company.setAddress(address);
        company.setCustomerType(customerType);
        company.setWaterUseType(waterUseType);
        company.setGbIndustry(gbIndustry);
        company.setMainIndustry(mainIndustry);
        company.setContact(contact);
        company.setPhone(phone);
        company.setPostalCode(postalCode);
        company.setDepartment(department);
        company.setWellCount(wellCount);
        company.setFirstWatermeterCount(firstWatermeterCount);
        company.setRemotemeterCount(remotemeterCount);
        company.setUnitType(unitType);
        company.setLongitude(longitude);
        company.setLatitude(latitude);
        if (createDate == null) {
            company.setCreateTime(new Date());
        } else {
            company.setCreateTime(createDate);
        }
        company.setSelfWellPrice(self_well_price);
        company.setSurfacePrice(surface_price);
        company.setSelfFreePrice(self_free_price);
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

    public List<Record> getCompanyAll(String innerCode) {
        String select="select * from t_company c left join (select sum(net_water) as waterUseNum,inner_code as innerCode from t_actual_data GROUP BY inner_code) tad" +
                " on c.inner_code=tad.innerCode";
        if (StringUtils.isNotEmpty(innerCode)) {
            select = select + " where c.inner_code='" + innerCode + "'";
        }
        return Db.find(select);
    }

    public List<Record> getCompanyByType(String type) {
        String select="select * from t_company c left join (select sum(net_water) as waterUseNum,inner_code as innerCode from t_actual_data GROUP BY inner_code) tad" +
                " on c.inner_code=tad.innerCode";
        if ("1".equals(type)) {
            String innerCodes = getWarnInnerCodes();
            select = select + " where c.inner_code not in (" + innerCodes + ")";
        } else if ("2".equals(type)) {
            String innerCodes = getWarnInnerCodes();
            select = select + " where c.inner_code in (" + innerCodes + ")";
        } else {
            select = select + " where 1=1 ";
        }
        return Db.find(select);
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
        String select="select c.*";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_company c ");
        sqlExceptSelect.append(" where company_type=1 ");
        sqlExceptSelect.append(" and inner_code in (" + getWarnExceptionInnerCodeSql(new Date()) + ")");
        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (c.name like '%" + StringUtils.trim(keyword) + "%' or c.inner_code='" + StringUtils.trim(keyword)
                    + "' or contact='" + StringUtils.trim(keyword) + "') ");
        }
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        return this.paginate(page, rows, select, sqlExceptSelect.toString());
    }


    public Page<Company> getNormalCompanyPage(int page, int rows, String keyword, String orderbyStr) {
        String select="select c.*";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_company c ");
        sqlExceptSelect.append(" where company_type=1");
        sqlExceptSelect.append(" and inner_code not in (" + getWarnExceptionInnerCodeSql(new Date()) + ")");
        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (c.name like '%" + StringUtils.trim(keyword) + "%' or c.inner_code='" + StringUtils.trim(keyword)
                    + "' or contact='" + StringUtils.trim(keyword) + "') ");
        }
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        return this.paginate(page, rows, select, sqlExceptSelect.toString());
    }

    public Page<Company> getOtherCompanyPage(int page, int rows, String keyword, String orderbyStr) {
        String select="select c.*";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_company c");
        sqlExceptSelect.append(" where company_type=1 and c.inner_code not in (select DISTINCT inner_code from t_actual_data tad )");
        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (c.name like '%" + StringUtils.trim(keyword) + "%' or c.inner_code='" + StringUtils.trim(keyword)
                    + "' or contact='" + StringUtils.trim(keyword) + "') ");
        }
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        return this.paginate(page, rows, select, sqlExceptSelect.toString());
    }

    public Page<Company> getSupplyCompanyPage(int page, int rows, String keyword, String orderbyStr) {
        String select="select c.*";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_company c");
        sqlExceptSelect.append(" where company_type=2 ");
        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (c.name like '%" + StringUtils.trim(keyword) + "%' or c.inner_code='" + StringUtils.trim(keyword)
                    + "' or contact='" + StringUtils.trim(keyword) + "') ");
        }
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        return this.paginate(page, rows, select, sqlExceptSelect.toString());
    }

    public Integer getSupplyCompanyCount() {
       return Integer.parseInt(this.find("select count(*) as sumCount from t_company where company_type=2").get(0).get("sumCount").toString());
    }


    private Set<String> warnInnerCodes = new HashSet<>();
    // 预警閥值
    private static final BigDecimal THRESHOLD = new BigDecimal("2");

    @Deprecated
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

    public String getWarnExceptionInnerCodeSql(Date date) {
        Map<String, String> monthDateBetween = ToolDateTime.get2MonthDateBetween(date);
        String start = monthDateBetween.get("start");
        String end = monthDateBetween.get("end");
        String month_str = monthDateBetween.get("month_str");
        Integer month = Integer.parseInt(monthDateBetween.get("month"));
        String sql = "select t.inner_code from (select allad.*,sum(allad.net_water) as sumWater from (select tad.*,twm.waters_type from t_actual_data tad inner join (select waters_type,meter_address from t_water_meter) twm" +
                " on twm.meter_address=tad.meter_address) allad where allad.write_time >='"+start+"' and allad.write_time <'"+end+"' group by allad.meter_address) t" +
                " INNER join t_water_index twi on twi.inner_code=t.inner_code where t.sumWater>twi." + month_str + " and t.waters_type=twi.waters_type";
        return sql;
    }

    public String getWarnExceptionSql(Date date) {
        Map<String, String> monthDateBetween = ToolDateTime.get2MonthDateBetween(date);
        String start = monthDateBetween.get("start");
        String end = monthDateBetween.get("end");
        String month_str = monthDateBetween.get("month_str");
        Integer month = Integer.parseInt(monthDateBetween.get("month"));
        String sql = "select * from (select allad.*,sum(allad.net_water) as sumWater from (select tad.*,twm.waters_type from t_actual_data tad inner join (select waters_type,meter_address from t_water_meter) twm" +
                " on twm.meter_address=tad.meter_address) allad where allad.write_time >='"+start+"' and allad.write_time <'"+end+"' group by allad.meter_address) t" +
                " INNER join t_water_index twi on twi.inner_code=t.inner_code where t.sumWater>twi." + month_str + " and t.waters_type=twi.waters_type";
        return sql;
    }

    public Set<String> getWarnExceptionInnerCode(Date date) {
        Map<String, String> monthDateBetween = ToolDateTime.get2MonthDateBetween(date);
        String start = monthDateBetween.get("start");
        String end = monthDateBetween.get("end");
        String month_str = monthDateBetween.get("month_str");
        Integer month = Integer.parseInt(monthDateBetween.get("month"));
        String sql = "select * from (select allad.*,sum(allad.net_water) as sumWater from (select tad.*,twm.waters_type from t_actual_data tad inner join (select waters_type,meter_address from t_water_meter) twm" +
                " on twm.meter_address=tad.meter_address) allad where allad.write_time >='"+start+"' and allad.write_time <'"+end+"' group by allad.meter_address) t" +
                " INNER join t_water_index twi on twi.inner_code=t.inner_code where t.sumWater>twi." + month_str + " and t.waters_type=twi.waters_type";
        List<Record> records = Db.find(sql);
        Set<String> set = new HashSet<>();
        for (Record record:records) {
            set.add(record.get("inner_code").toString());
        }
        return set;
    }

    private void comp(BigDecimal monthActTotal, BigDecimal moth, String innerCode) {
        if (null == moth) {
            moth = new BigDecimal(0);
        }
        if (moth.add(this.THRESHOLD).compareTo(monthActTotal) < 0) {
            warnInnerCodes.add(innerCode);
        }
    }

    public static int[] saveBatch(List<Company> modelOrRecordList, int batchSize) {
        String sql = "insert into t_company(inner_code,name,water_unit,county,street,street_src,address,customer_type,gb_industry," +
                "main_industry,water_use_type,contact,phone,postal_code,department,well_count,first_watermeter_count," +
                "remotemeter_count,unit_type,longitude,latitude,self_well_price,surface_price,self_free_price,create_time)" +
                " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String columns = "inner_code,name,water_unit,county,street,street_src,address,customer_type,gb_industry," +
                "main_industry,water_use_type,contact,phone,postal_code,department,well_count,first_watermeter_count," +
                "remotemeter_count,unit_type,longitude,latitude,self_well_price,surface_price,self_free_price,create_time";
        int[] result = Db.batch(sql, columns, modelOrRecordList, batchSize);
        return result;
    }

    public void importData(List<Map<Integer, String>> maps) {
        List<Company> lists = new ArrayList<Company>();
        Map<String, Integer> dictUserType = DictData.dao.getDictNameMap(DictCode.UserType);
        Map<String, Integer> dictStreet = DictData.dao.getDictNameMap(DictCode.Street);
        Map<String, Integer> dictWaterUseType = DictData.dao.getDictNameMap(DictCode.WaterUseType);
        for (int i = 0; i < maps.size(); i++) {
            Company company = new Company();
            company.setCompanyType(CompanyType.COMPANY);
            Map<Integer, String> map = maps.get(i);
            String innerCode = null;
            if (map.get(0) != null) {
                innerCode = map.get(0).toString();
            }
            if (StringUtils.isEmpty(innerCode) || hasExistCompany(innerCode)) {
                continue;
            }
            String name = null;
            if (map.get(1) != null) {
                name = map.get(1).toString();
                if (CompanyType.notCompany(name)) {
                    company.setCompanyType(CompanyType.SUPPLY);
                }
            }
            String waterUnit = null;
            if (map.get(2) != null) {
                waterUnit = map.get(2).toString();
            }
            String county = null;
            if (map.get(3) != null) {
                county = map.get(3).toString();
            }
            Integer street = null;
            if (map.get(4) != null) {
                street = dictStreet.get(map.get(4).toString());
            }
            String streetSrc = null;
            if (map.get(5) != null) {
                streetSrc = map.get(5).toString();
            }
            Integer customerType = null;
            if (map.get(6) != null) {
                customerType = dictUserType.get(map.get(6).toString());
                if (CompanyType.notCompany(name)) {
                    company.setCompanyType(CompanyType.SUPPLY);
                }
            }
            String gbIndustry = null;
            if (map.get(7) != null) {
                gbIndustry = map.get(7).toString();
            }
            String mainIndustry = null;
            if (map.get(8) != null) {
                mainIndustry = map.get(8).toString();
            }
            Integer waterUseType = null;
            if (map.get(9) != null) {
                waterUseType = dictWaterUseType.get(map.get(9).toString());
            }
            String contact = null;
            if (map.get(10) != null) {
                contact = map.get(10).toString();
            }
            String phone = null;
            if (map.get(11) != null) {
                phone = map.get(11).toString();
            }
            String address = null;
            if (map.get(12) != null) {
                address = map.get(12).toString();
            }
            String postalCode = null;
            if (map.get(13) != null) {
                postalCode = map.get(13).toString();
            }
            String department = null;
            if (map.get(14) != null) {
                department = map.get(14).toString();
            }

            Integer wellCount = 0;
            Integer firstWatermeterCount = 0;
            Integer remotemeterCount = 0;
            Integer unitType = 1;

            BigDecimal self_well_price = null;
            if (map.get(15) != null) {
                self_well_price = CodeNumUtil.getBigDecimal(map.get(15).toString(), 2);
            }

            BigDecimal surface_price = null;
            if (map.get(16) != null) {
                surface_price = CodeNumUtil.getBigDecimal(map.get(16).toString(), 2);
            }
            BigDecimal self_free_price = null;
            if (map.get(17) != null) {
                self_free_price = CodeNumUtil.getBigDecimal(map.get(17).toString(), 2);
            }

            Date createDate = null;
            Object createDateObj = map.get(18);
            if (createDateObj != null) {
                String createDateStr = createDateObj.toString();
                String date = "";
                if (createDateStr.indexOf("-") > 0) {
                    date = ToolDateTime.format(StringUtils.trim(createDateStr) + " 00:00:00", ToolDateTime.pattern_ymd, ToolDateTime.pattern_ymd_hms);
                } else if (createDateStr.indexOf("/") > 0) {
                    date = ToolDateTime.format(StringUtils.trim(createDateStr) + " 00:00:00", ToolDateTime.pattern_ymd2, ToolDateTime.pattern_ymd_hms);
                } else {
                    date = ToolDateTime.format(new Date(), ToolDateTime.pattern_ymd_hms);
                }
                createDate = DateUtils.getDate(date, ToolDateTime.pattern_ymd);
            }
            setProp(company, name, innerCode, waterUnit, county, street, streetSrc, address, customerType, waterUseType,
                    gbIndustry, mainIndustry, contact, phone, postalCode, department, wellCount, firstWatermeterCount,
                    remotemeterCount, unitType, null, null, createDate, self_well_price, surface_price, self_free_price);
            lists.add(company);
        }
        Company.me.saveBatch(lists, lists.size());
    }
}
