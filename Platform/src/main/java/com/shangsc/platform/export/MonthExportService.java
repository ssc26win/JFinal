package com.shangsc.platform.export;

import com.shangsc.platform.model.ActualData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author ssc
 * @Date 2017/8/28 17:43
 * @Desc 用途：
 */
public class MonthExportService extends ExportBaseService {
    private static final String FILE_NAME = "月用水量信息导出";

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
    public String export(List<ActualData> list, int month) {
        super.logger.info("导出月用水量信息开始");
        List<String> listHeader = new ArrayList<String>();
        listHeader.addAll(Arrays.asList(new String[]{
                "所属节水办",
                "单位编号",
                "单位名称",
                "路别",
                "水表编号",
                "水源类型",
                "月用水量",
                "水表属性",
                "表计地址",
                "计费周期",
                "单位地址",
                "查询时间"
        }));

        logger.info("导出月用水量信息导出条数为:{}", list.size());
        List<Object[]> objects = new ArrayList<Object[]>();
        for (ActualData actualData : list) {
            if (actualData.get("monthTotal") == null) {
                actualData.put("monthTotal", 0);
            }
            Object[] obj = new Object[]{
                    actualData.get("water_unit"),
                    actualData.getInnerCode(),
                    actualData.get("name"),
                    actualData.get("line_num"),
                    actualData.get("meter_num"),
                    actualData.get("watersTypeName"),
                    actualData.get("monthTotal"),
                    actualData.get("meter_attr"),
                    actualData.getMeterAddress(),
                    actualData.get("billing_cycle"),
                    actualData.get("address"),
                    actualData.get("months")
            };
            objects.add(obj);
        }

        super.logger.info("导出月用水量信息结束");
        return super.export(FILE_NAME, listHeader, objects);
    }
}