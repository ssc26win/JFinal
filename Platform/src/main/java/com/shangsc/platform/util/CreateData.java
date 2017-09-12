package com.shangsc.platform.util;

import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.model.Company;
import com.shangsc.platform.model.WaterIndex;
import com.shangsc.platform.model.WaterMeter;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author ssc
 * @Date 2017/9/13 0:07
 * @Version 1.0.0
 * @Desc
 */
public class CreateData {

    public static void createCompany() {
        List<Company> companies = new ArrayList<>();
        //100 公司
        for (int i = 1; i <= 100; i++) {
            Company company = new Company();
            company.setName("公司-Test-" + i);
            company.setInnerCode(CodeNumUtil.genInnerCode(i));
            company.setWaterUnit("通州区节水办");
            company.setCounty("通州区");
            company.setStreet(2);
            company.setStreetSrc("通州卫星城");
            company.setAddress("北京市通州区后南仓" + i + "号");
            company.setCustomerType(2);
            company.setWaterUseType(2);
            company.setGbIndustry("卫生" + i);
            company.setMainIndustry("医院" + i);
            company.setContact("Tester" + i);
            company.setPhone("1356323445" + i);
            company.setPostalCode("100010");
            company.setDepartment("计量室");
            company.setWellCount(1);
            company.setFirstWatermeterCount(1);
            company.setRemotemeterCount(1);
            company.setUnitType(2);
            company.setCreateTime(new Date());
            company.setSelfWellPrice(new BigDecimal(String.valueOf(i + 1)));
            company.setSurfacePrice(new BigDecimal(String.valueOf(i + 2)));
            company.setSelfFreePrice(new BigDecimal(String.valueOf(i + 4)));
            companies.add(company);
        }
        Company.saveBatch(companies, companies.size());
    }

    public static void createMeter() {
        //100 公司
        Date today = new Date();
        for (int i = 1; i <= 100; i++) {
            WaterMeter meter = new WaterMeter();
            meter.setInnerCode(CodeNumUtil.genInnerCode(i));
            meter.setMeterNum("Test0010" + i);
            meter.setMeterAddress(CodeNumUtil.genInnerCode2(i));
            meter.setMeterAttr("属性" + i);
            meter.setChargeType(2);
            meter.setWatersType(2);
            Calendar c = Calendar.getInstance();
            c.setTime(today);
            c.add(Calendar.DAY_OF_MONTH, i);
            meter.setRegistDate(c.getTime());
            meter.save();
        }
    }

    public static void createWaterIndex() {
        //100 公司
        for (int i = 1; i <= 100; i++) {
            WaterIndex index = new WaterIndex();
            index.setInnerCode(CodeNumUtil.genInnerCode(i));
            index.setWaterIndex(new BigDecimal(200));
            index.setWatersType(2);
            index.setJanuary(new BigDecimal(new Random().nextInt(10)+1));
            index.setFebruary(new BigDecimal(new Random().nextInt(10)+1));
            index.setMarch(new BigDecimal(new Random().nextInt(10)+1));
            index.setApril(new BigDecimal(new Random().nextInt(10)+1));
            index.setMay(new BigDecimal(new Random().nextInt(10)+1));
            index.setJune(new BigDecimal(new Random().nextInt(10)+1));
            index.setJuly(new BigDecimal(new Random().nextInt(10)+1));
            index.setAugust(new BigDecimal(new Random().nextInt(10)+1));
            index.setSeptember(new BigDecimal(new Random().nextInt(10)+1));
            index.setOctober(new BigDecimal(new Random().nextInt(10)+1));
            index.setNovember(new BigDecimal(new Random().nextInt(10)+1));
            index.setDecember(new BigDecimal(new Random().nextInt(10)+1));
            index.save();
        }
    }

    public static void createActual() {
        //100 公司
        Date today = new Date();
       // for (int i = 1; i <= 20; i++) {
            for (int j = 0; j < 100; j++) {
                ActualData data = new ActualData();
                data.setInnerCode(CodeNumUtil.genInnerCode(j));
                data.setMeterAddress(CodeNumUtil.genInnerCode2(j));
                data.setNetWater(new BigDecimal(new Random().nextInt(2)+1));
                data.setState(1);
                data.setWriteTime(new Date());
                data.save();
            }

       // }
    }
}
