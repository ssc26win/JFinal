package com.shangsc.platform.export;

import com.shangsc.platform.model.ActualData;

import java.util.*;

/**
 * @Author ssc
 * @Date 2017/8/28 17:43
 * @Desc 用途：
 */
public class CompanyMonthExportService extends ExportBaseService {

    /*
     "所属节水办",
                "单位编号",
                "单位名称",
                "日用水量",
                "单位地址",
     */
    public String export(List<ActualData> list, String type) {
        String fName = "";
        if ("2".equals(type)) {
            fName = "月供水量";
        } else {
            fName = "月用水量";
        }
        super.logger.info("导出" + fName + "信息开始");
        List<String> listHeader = new ArrayList<String>();
        listHeader.addAll(Arrays.asList(new String[]{
                "所属节水办",
                "单位编号",
                "单位名称",
                fName,
                "单位地址",
                "查询时间"
        }));

        logger.info("导出" + fName + "信息导出条数为:{}", list.size());
        List<Object[]> objects = new ArrayList<Object[]>();
        for (ActualData actualData : list) {
            if (actualData.get("monthTotal") == null) {
                actualData.put("monthTotal", 0);
            }
            Object[] obj = new Object[]{
                    actualData.get("water_unit"),
                    actualData.get("real_code"),
                    actualData.get("name"),
                    actualData.get("monthTotal"),
                    actualData.get("address"),
                    actualData.get("months")
            };
            objects.add(obj);
        }

        Set<Integer> isNumTypeColSet = new HashSet<Integer>();
        isNumTypeColSet.add(7);

        super.logger.info("导出" + fName + "信息结束");
        return super.export(fName + "信息导出", listHeader, objects, isNumTypeColSet);
    }
}