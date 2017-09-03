package com.shangsc.platform.export;

import com.shangsc.platform.model.ActualData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author ssc
 * @Date 2017/8/28 17:42
 * @Desc 用途：
 */
public class ReadNumExportService extends ExportBaseService{
    private static final String FILE_NAME = "读数查询信息导出";

    public String export(List<ActualData> list) {
        super.logger.info("导出读数查询信息开始");
        List<String> listHeader = new ArrayList<String>();
        listHeader.addAll(Arrays.asList(new String[]{
                "单位名称",
                "单位编号",
                "路别",
                "水表表号",
                "水源类型",
                "水表属性",
                "查询时间",
                "水表读数",
                "单位地址"
        }));

        logger.info("读数查询导出条数为:{}", list.size());
        List<Object[]> objects = new ArrayList<Object[]>();
        for (ActualData actualData : list) {
            Object[] obj = new Object[]{
                    actualData.get("name"),
                    actualData.getInnerCode(),
                    actualData.getLineNum(),
                    actualData.getMeterNum(),
                    actualData.get("watersTypeName"),
                    actualData.get("meter_attr"),
                    actualData.getWriteTime(),
                    actualData.getNetWater(),
                    actualData.get("address")

            };
            objects.add(obj);
        }

        super.logger.info("导出读数查询信息结束");
        return super.export(FILE_NAME, listHeader, objects);
    }
}
