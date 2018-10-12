package com.shangsc.platform.export;

import com.shangsc.platform.code.ReportTypeEnum;
import com.shangsc.platform.model.Company;
import org.apache.commons.lang3.ObjectUtils;
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
            fName = "单位" + reportTypeEnum.getNick() + "供水量";
        } else {
            fName = "单位" + reportTypeEnum.getNick() + "用水量";
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
            obj[0] = company.get("companyName");
            for (int i = 0; i < colKeys.size(); i++) {
                BigDecimal targetTotal = new BigDecimal("0");
                if (company.getBigDecimal(colKeys.get(i)) != null) {
                    obj[i + 1] = company.getBigDecimal(colKeys.get(i));
                } else {
                    obj[i + 1] = targetTotal;
                }
            }
            objects.add(obj);
        }

        Set<Integer> isNumTypeColSet = new HashSet<Integer>();
        for (int i = 2; i < colKeys.size() + 1; i++) {
            isNumTypeColSet.add(i);
        }

        super.logger.info("导出" + fName + "信息结束");
        return super.export(fName + "信息导出", listHeader, objects, isNumTypeColSet);
    }

    public String exportStreetStatis(List<Company> list, String type, Set<String> columnsTitle, List<String> columnsKey, ReportTypeEnum street) {
        String fName = "";
        if ("2".equals(type)) {
            fName = street.getNick() + "供水量";
        } else {
            fName = street.getNick() + "用水量";
        }
        super.logger.info("导出" + fName + "信息开始");
        List<String> colKeys = new ArrayList<>(columnsTitle);
        List<String> listHeader = new ArrayList<String>();
        listHeader.addAll(Arrays.asList(new String[]{
                "所属乡镇",
                "水源类型",
                "总用水量",
        }));
        listHeader.addAll(colKeys);

        logger.info("导出" + fName + "信息导出条数为:{}", list.size());
        List<Object[]> objects = new ArrayList<Object[]>();
        for (Company company : list) {
            Object[] obj = new Object[columnsKey.size() + 3];
            obj[0] = company.get("streetName");
            obj[1] = company.get("watersTypeName");
            obj[2] = company.get("watersUseTotal");
            for (int i = 0; i < columnsKey.size(); i++) {
                BigDecimal targetTotal = new BigDecimal("0");
                if (company.getBigDecimal(columnsKey.get(i)) != null) {
                    obj[i + 3] = company.getBigDecimal(columnsKey.get(i));
                } else {
                    obj[i + 3] = targetTotal;
                }
            }
            objects.add(obj);
        }
        Set<Integer> isNumTypeColSet = new HashSet<Integer>();
        for (int i = 4; i < colKeys.size() + 3; i++) {
            isNumTypeColSet.add(i);
        }
        super.logger.info("导出" + fName + "信息结束");


        Set<String> notRepeat = new HashSet<>();

        for (int i = 0; i < objects.size(); i++) {
            notRepeat.add(ObjectUtils.toString(objects.get(i)[0]));
        }

        int[] mergeIndex = new int[notRepeat.size() + 1];
        mergeIndex[0] = 0;

        for (int i = 1; i < notRepeat.size() + 1; i++) {
            mergeIndex[i] = i;
        }

        return super.export(fName + "信息导出", listHeader, objects, isNumTypeColSet);
    }

    public String exportCompanyStatis(List<Company> list, String type, Set<String> columnsTitle, List<String> columnsKey, ReportTypeEnum street) {
        String fName = "";
        if ("2".equals(type)) {
            fName = street.getNick() + "供水量";
        } else {
            fName = street.getNick() + "用水量";
        }
        super.logger.info("导出" + fName + "信息开始");
        List<String> colKeys = new ArrayList<>(columnsTitle);
        List<String> listHeader = new ArrayList<String>();
        listHeader.addAll(Arrays.asList(new String[]{
                "所属节水办",
                "用户编号",
                "用户名称",
                "水源类型",
                "总用水量",
        }));
        listHeader.addAll(colKeys);

        logger.info("导出" + fName + "信息导出条数为:{}", list.size());
        List<Object[]> objects = new ArrayList<Object[]>();
        for (Company company : list) {
            Object[] obj = new Object[columnsKey.size() + 5];
            obj[0] = company.getWaterUnit();
            obj[1] = company.getRealCode();
            obj[2] = company.getName();
            obj[3] = company.get("watersTypeName");
            obj[4] = company.get("watersUseTotal");
            for (int i = 0; i < columnsKey.size(); i++) {
                BigDecimal targetTotal = new BigDecimal("0");
                if (company.getBigDecimal(columnsKey.get(i)) != null) {
                    obj[i + 5] = company.getBigDecimal(columnsKey.get(i));
                } else {
                    obj[i + 5] = targetTotal;
                }
            }
            objects.add(obj);
        }
        Set<Integer> isNumTypeColSet = new HashSet<Integer>();
        for (int i = 6; i < colKeys.size() + 5; i++) {
            isNumTypeColSet.add(i);
        }
        super.logger.info("导出" + fName + "信息结束");


        Set<String> notRepeat = new HashSet<>();

        for (int i = 0; i < objects.size(); i++) {
            notRepeat.add(ObjectUtils.toString(objects.get(i)[0]));
        }

        int[] mergeIndex = new int[notRepeat.size() + 1];
        mergeIndex[0] = 0;

        for (int i = 1; i < notRepeat.size() + 1; i++) {
            mergeIndex[i] = i;
        }

        return super.export(fName + "信息导出", listHeader, objects, isNumTypeColSet);
    }
}
