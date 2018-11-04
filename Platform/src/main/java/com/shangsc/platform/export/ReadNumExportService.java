package com.shangsc.platform.export;

import com.shangsc.platform.model.ActualData;

import java.util.*;

/**
 * @Author ssc
 * @Date 2017/8/28 17:42
 * @Desc 用途：
 */
public class ReadNumExportService extends ExportBaseService{
    private static final String FILE_NAME = "读数查询信息导出";

    /**
     *  "所属节水办",
     "单位编号",
     "单位名称",
     "路别",
     "水表编号",
     "水源类型",
     "水表读数",
     "水表属性",
     "表计地址",
     "单位地址",
     * @param list
     * @return
     */
    public String export(List<ActualData> list) {
        super.logger.info("导出读数查询信息开始");
        List<String> listHeader = new ArrayList<String>();
        listHeader.addAll(Arrays.asList(new String[]{
                "所属节水办",
                "单位编号",
                "单位名称",
                "路别",
                "水表编号",
                "水源类型",
                "净用水量",
                "水表读数",
                "水表属性",
                "表计地址",
                "查询时间",
                "单位地址"
        }));

        logger.info("读数查询导出条数为:{}", list.size());
        List<Object[]> objects = new ArrayList<Object[]>();
        for (ActualData actualData : list) {
            Object[] obj = new Object[]{
                    actualData.get("water_unit"),
                    actualData.get("real_code"),
                    actualData.get("name"),
                    actualData.get("line_num"),
                    actualData.get("meter_num"),
                    actualData.get("watersTypeName"),
                    actualData.getNetWater(),
                    actualData.getSumWater(),
                    actualData.get("meter_attr"),
                    actualData.getMeterAddress(),
                    actualData.getWriteTime(),
                    actualData.get("address")

            };
            objects.add(obj);
        }

        Set<Integer> isNumTypeColSet = new HashSet<Integer>();
        isNumTypeColSet.add(7);

        super.logger.info("导出读数查询信息结束");
        return super.export(FILE_NAME, listHeader, objects, isNumTypeColSet);
    }
}
