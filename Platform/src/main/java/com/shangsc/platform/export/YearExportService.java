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
public class YearExportService extends ExportBaseService {
    private static final String FILE_NAME = "年用水量信息导出";

    /*
     { label: '单位名称', name: 'companyName', width: 120, sortable:false},
    { label: '单位编号', name: 'inner_code', width: 80, sortable:false},
    { label: '路别', name: 'line_num', width: 100, sortable:false},
    { label: '水表表号', name: 'meter_num', width: 100,sortable:false},
    { label: '水源类型', name: 'watersTypeName', width: 45, sortable:false},
    { label: '水表属性', name: 'alarm', width: 45, sortable:false},
    { label: '查询时间', name: 'find_time', width: 100, sortable:true},
    { label: '用水量', name: 'net_water', width: 80, sortable:false},
    { label: '单位地址', name: 'addressMap', width: 100,sortable:false}
     */
    public String export(List<ActualData> list,int year) {
        super.logger.info("导出月用水量信息开始");
        List<String> listHeader = new ArrayList<String>();
        listHeader.addAll(Arrays.asList(new String[]{
                "单位名称",
                "单位编号",
                "路别",
                "水表表号",
                "水源类型",
                "水表属性",
                "查询时间",
                "年用水量",
                "单位地址"
        }));

        logger.info("导出年用水量信息导出条数为:{}", list.size());
        List<Object[]> objects = new ArrayList<Object[]>();
        for (ActualData actualData : list) {
            Object[] obj = new Object[]{
                    actualData.get("name"),
                    actualData.getInnerCode(),
                    actualData.getLineNum(),
                    actualData.getMeterNum(),
                    actualData.get("watersTypeName"),
                    actualData.get("meter_attr"),
                    actualData.get("yearStr"),
                    actualData.get("netWaterNum"),
                    actualData.get("address")
            };
            objects.add(obj);
        }

        super.logger.info("导出年用水量信息结束");
        return super.export(FILE_NAME, listHeader, objects);
    }
}