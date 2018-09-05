package com.shangsc.platform.export;

import com.shangsc.platform.model.ActualData;

import java.util.*;

/**
 * @Author ssc
 * @Date 2017/8/28 17:43
 * @Desc 用途：
 */
public class DailyExportService extends ExportBaseService{
    /*
         "所属节水办",
                "单位编号",
                "单位名称",
                "路别",
                "水表编号",
                "水源类型",
                "日用水量",
                "水表属性",
                "表计地址",
                "单位地址",
     */
    public String export(List<ActualData> list, String type) {
        String fName = "";
        if ("2".equals(type)) {
            fName = "日供水量";
        } else {
            fName = "日用水量";
        }
        super.logger.info("导出" + fName + "信息开始");
        List<String> listHeader = new ArrayList<String>();
        listHeader.addAll(Arrays.asList(new String[]{
                "所属节水办",
                "单位编号",
                "单位名称",
                "路别",
                "水表编号",
                "水源类型",
                fName,
                "水表属性",
                "表计地址",
                "单位地址",
                "查询时间"
        }));

        logger.info("导出"+ fName +"信息导出条数为:{}", list.size());
        List<Object[]> objects = new ArrayList<Object[]>();
        for (ActualData actualData : list) {
            if (actualData.get("net_water") == null) {
                actualData.set("net_water", 0);
            }
            Object[] obj = new Object[]{
                    actualData.get("water_unit"),
                    actualData.get("real_code"),
                    actualData.get("name"),
                    actualData.get("line_num"),
                    actualData.get("meter_num"),
                    actualData.get("watersTypeName"),
                    actualData.get("net_water"),
                    actualData.get("meter_attr"),
                    actualData.get("meter_address"),
                    actualData.get("address"),
                    actualData.get("todays")
            };
            objects.add(obj);
        }

        Set<Integer> isNumTypeColSet = new HashSet<Integer>();
        isNumTypeColSet.add(7);

        super.logger.info("导出" + fName + "信息结束");
        return super.export(fName + "信息导出", listHeader, objects, isNumTypeColSet);
    }
}
