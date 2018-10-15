package com.shangsc.platform.export;

import com.shangsc.platform.model.ActualData;
import com.shangsc.platform.util.ToolDateTime;

import java.util.*;

/**
 * @Author ssc
 * @Date 2018/10/15 10:24
 * @Desc 用途：
 */
public class ActualExportService extends ExportBaseService {

    private static final String FILE_NAME = "实时数据信息导出";


    public String export(List<ActualData> actualDatas) {
        super.logger.info("导出实时数据信息开始");
        /**
         *   { label: '单位名称', name: 'companyName', width: 120, sortable:false},
         { label: '单位编号', name: 'inner_code', width: 80, sortable:false},
         { label: '路别', name: 'line_num', width: 100, sortable:false},
         { label: '表计地址', name: 'meter_address', width: 100,sortable:false},
         { label: '水源类型', name: 'watersTypeName', width: 45, sortable:false},

         { label: '净用水量（立方米）', name: 'net_water', width: 100, sortable:true},
         { label: '累计用水量（立方米）', name: 'sum_water', width: 100, sortable:true},
         { label: '当前状态', name: 'stateName', width: 50, sortable:false},
         { label: '抄表时间', name: 'write_time', width: 100, sortable:true},
         { label: '电池电压（伏特v）', name: 'voltage', width: 100, sortable:true}
         */
        List<String> listHeader = new ArrayList<String>();
        listHeader.addAll(Arrays.asList(new String[]{
                "单位编号",
                "单位名称",
                "路别",
                "表计地址",
                "水源类型",
                "净用水量（立方米）",
                "累计用水量（立方米）",
                "抄表时间",
                "电池电压（伏特v）",
        }));

        logger.info("导出实时数据信息导出条数为:{}", actualDatas.size());
        List<Object[]> objects = new ArrayList<Object[]>();
        for (ActualData actualData : actualDatas) {
            Object[] obj = new Object[]{
                    actualData.get("real_code"),
                    actualData.get("companyName"),
                    actualData.get("line_num"),
                    actualData.getMeterAddress(),
                    actualData.get("watersTypeName"),
                    actualData.getNetWater(),
                    actualData.getSumWater(),
                    ToolDateTime.format(actualData.getWriteTime(), ToolDateTime.pattern_ymd),
                    actualData.getVoltage()
            };
            objects.add(obj);
        }

        Set<Integer> isNumTypeColSet = new HashSet<Integer>();
        isNumTypeColSet.add(6);
        isNumTypeColSet.add(7);

        super.logger.info("导出实时数据信息结束");
        return super.export(FILE_NAME, listHeader, objects, isNumTypeColSet);
    }
}
