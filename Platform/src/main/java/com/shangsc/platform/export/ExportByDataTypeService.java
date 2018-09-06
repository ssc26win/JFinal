package com.shangsc.platform.export;

import com.shangsc.platform.code.ReportTypeEnum;
import com.shangsc.platform.model.Company;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author ssc
 * @Date 2018/9/6 22:29
 * @Version 1.0.0
 * @Desc
 */
public class ExportByDataTypeService extends ExportBaseService {

    public String exportCompanyStatis(List<Company> list, String type, Map<String, String> columns, ReportTypeEnum reportTypeEnum) {
        String fName = "";
        if ("2".equals(type)) {
            fName = "单位" + reportTypeEnum.getNick() + " 供水量";
        } else {
            fName = "单位" + reportTypeEnum.getNick() + " 用水量";
        }
        super.logger.info("导出" + fName + "信息开始");
        List<String> colKeys = new ArrayList<>(columns.keySet());
        List<String> listHeader = new ArrayList<String>();
        listHeader.addAll(Arrays.asList(new String[]{
                "单位名称",
        }));
        listHeader.addAll(colKeys);
        logger.info("导出" + fName + "信息导出条数为:{}", list.size());
        List<Object[]> objects = new ArrayList<Object[]>();
        for (Company company : list) {
            Object[] obj = new Object[colKeys.size() + 1];
            obj[0] = company.getName();
            for (int i = 0; i < colKeys.size(); i++) {
                BigDecimal targetTotal = new BigDecimal("0.0");
                if (StringUtils.isNotEmpty(company.getStr(colKeys.get(i)))) {
                    obj[i + 1] = company.getBigDecimal(colKeys.get(i));
                } else {
                    obj[i + 1] = targetTotal;
                }
            }
            objects.add(obj);
        }

        Set<Integer> isNumTypeColSet = new HashSet<Integer>();
        for (int i = 1; i < colKeys.size() + 1; i++) {
            isNumTypeColSet.add(i);
        }

        super.logger.info("导出" + fName + "信息结束");
        return super.export(fName + "信息导出", listHeader, objects, isNumTypeColSet);
    }

    public String exportStreetStatis(List<Company> list, String type, Set<String> columns, ReportTypeEnum street) {
        String fName = "";
        if ("2".equals(type)) {
            fName = street.getNick() + " 供水量";
        } else {
            fName = street.getNick() + " 用水量";
        }
        super.logger.info("导出" + fName + "信息开始");
        List<String> colKeys = new ArrayList<>(columns);
        List<String> listHeader = new ArrayList<String>();
        listHeader.addAll(Arrays.asList(new String[]{
                "所属乡镇",
                "水源类型",
        }));
        listHeader.addAll(colKeys);

        logger.info("导出" + fName + "信息导出条数为:{}", list.size());
        List<Object[]> objects = new ArrayList<Object[]>();
        for (Company company : list) {
            Object[] obj = new Object[colKeys.size() + 1];
            obj[0] = company.get("streetName");
            obj[1] = company.get("watersTypeName");
            for (int i = 0; i < colKeys.size(); i++) {
                BigDecimal targetTotal = new BigDecimal("0.0");
                if (StringUtils.isNotEmpty(company.getStr(colKeys.get(i)))) {
                    obj[i + 2] = company.getBigDecimal(colKeys.get(i));
                } else {
                    obj[i + 2] = targetTotal;
                }
            }
            objects.add(obj);
        }
        Set<Integer> isNumTypeColSet = new HashSet<Integer>();
        for (int i = 1; i < colKeys.size() + 1; i++) {
            isNumTypeColSet.add(i);
        }
        super.logger.info("导出" + fName + "信息结束");
        return super.export(fName + "信息导出", listHeader, objects, isNumTypeColSet);
    }
}
