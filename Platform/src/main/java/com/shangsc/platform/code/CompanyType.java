package com.shangsc.platform.code;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author ssc
 * @Date 2017/9/12 12:36
 * @Version 1.0.0
 * @Desc
 */
public class CompanyType {

    public static Integer getCompanyType(String name, Map<Integer, String> companies) {
        if (StringUtils.isNotEmpty(name)) {
            for (Integer type : companies.keySet()) {
                if (name.indexOf(companies.get(type)) > 0) {
                    return type;
                }
            }
        }
        return null;
    }


    //导入区分单位类型
    public static Set<String> likeType = new HashSet<String>();

    static {
        likeType.add("供水");
        likeType.add("水厂");
        likeType.add("村委会");
    }

    public static boolean notCompany(String str) {
        if (StringUtils.isNotEmpty(str)) {
            for (String type : likeType) {
                if (str.indexOf(type) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static final Integer COMPANY = 1;
    public static final Integer SUPPLY = 2;
    public static final Integer Village = 3;

    public static final String COMPANY_STR = "用水单位";
    public static final String SUPPLY_STR = "供水单位";

    public static Map<Integer, String> getMap() {
        Map<Integer, String> addType = new HashMap<Integer, String>();
        addType.put(COMPANY, COMPANY_STR);
        addType.put(SUPPLY, SUPPLY_STR);
        return addType;
    }

    public static boolean isCompany(String str) {
        return COMPANY.equals(str);
    }
}
