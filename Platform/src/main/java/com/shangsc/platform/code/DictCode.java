package com.shangsc.platform.code;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ssc
 * @Date 2017/9/7 10:55
 * @Desc 用途：
 */
public class DictCode {

    public static final String UserType = "UserType";
    public static final String WaterUseType = "WaterUseType";
    public static final String UnitType = "UnitType";
    public static final String WatersType = "WatersType";
    public static final String ChargeType = "ChargeType";

    public static final String Street = "Street";
    public static final String PumpModel = "PumpModel";
    public static final String CalculateType = "CalculateType";
    public static final String GeomorphicType = "GeomorphicType";
    public static final String GroundType = "GroundType";

    public static final String MeterAttr = "MeterAttr";
    public static final String Term = "Term";

    public static final String ACTUAL_EXCEPTION_TIME_OUT = "ActualExceptionTimeOut";

    public static String getCompanyUse() {
        List<String> list = new ArrayList<>();
        list.add("'" + Street + "'");
        list.add("'" + UnitType + "'");
        list.add("'" + UserType + "'");
        list.add("'" + WaterUseType + "'");
        list.add("'" + Term + "'");
        return StringUtils.join(list, ",");
    }

    public String getMeterUse() {
        List<String> list = new ArrayList<>();
        list.add("'" + WatersType + "'");
        list.add("'" + WaterUseType + "'");
        list.add("'" + ChargeType + "'");
        list.add("'" + MeterAttr + "'");
        list.add("'" + Term + "'");
        return StringUtils.join(list, ",");
    }

    public String getWellUse() {
        List<String> list = new ArrayList<>();
        list.add("'" + CalculateType + "'");
        list.add("'" + GeomorphicType + "'");
        list.add("'" + GroundType + "'");
        return StringUtils.join(list, ",");
    }

    public String getStatisUse() {
        List<String> list = new ArrayList<>();
        list.add("'" + WatersType + "'");
        list.add("'" + MeterAttr + "'");
        list.add("'" + Street + "'");
        return StringUtils.join(list, ",");
    }


    public static void main(String[] args) {
        System.out.println(getCompanyUse());
    }

}
