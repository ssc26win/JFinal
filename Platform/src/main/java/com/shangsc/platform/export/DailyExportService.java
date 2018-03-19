package com.shangsc.platform.export;

import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author ssc
 * @Date 2017/8/28 17:43
 * @Desc 用途：
 */
public class DailyExportService extends ExportBaseService{
    private static final String FILE_NAME = "日用水量信息导出";

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
    public String export(List<Record> list) {
        super.logger.info("导出日用水量信息开始");
        List<String> listHeader = new ArrayList<String>();
        listHeader.addAll(Arrays.asList(new String[]{
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
        }));

        logger.info("导出日用水量信息导出条数为:{}", list.size());
        List<Object[]> objects = new ArrayList<Object[]>();
        for (Record actualData : list) {
            if (actualData.get("dailyNum") == null) {
                actualData.set("dailyNum", 0);
            }
            Object[] obj = new Object[]{
                    actualData.get("water_unit"),
                    actualData.get("inner_code"),
                    actualData.get("name"),
                    actualData.get("line_num"),
                    actualData.get("meter_num"),
                    actualData.get("watersTypeName"),
                    actualData.get("dailyNum"),
                    actualData.get("meter_attr"),
                    actualData.get("meter_address"),
                    actualData.get("address")
            };
            objects.add(obj);
        }

        super.logger.info("导出日用水量信息结束");
        return super.export(FILE_NAME, listHeader, objects);
    }
}
