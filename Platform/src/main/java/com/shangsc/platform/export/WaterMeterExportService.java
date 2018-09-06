package com.shangsc.platform.export;

import com.shangsc.platform.model.WaterMeter;
import com.shangsc.platform.util.ToolDateTime;

import java.util.*;

/**
 * 水表信息导出
 *
 * @author ssk www.hnapay.com Inc.All rights reserved
 * @create 2017-08-22-下午 3:17
 */
public class WaterMeterExportService extends ExportBaseService {

    private static final String FILE_NAME = "水表信息导出";


    public String export(List<WaterMeter> waterMeters) {
        super.logger.info("导出水表信息开始");
        List<String> listHeader = new ArrayList<String>();
        listHeader.addAll(Arrays.asList(new String[]{
                "单位编号",
                "单位名称",
                "所属节水办",
                "所属区县",
                "路别",
                "表计地址",
                "最小单位",
                "水表编号",
                "水源类型",
                "国标行业",
                "主要行业",
                "取水用途",
                "水表属性",
                "收费类型",
                "注册日期",
                "计费周期",
                "备注",
                "生产厂家"
        }));

        /**
         * 单位编号	单位名称	所属节水办	所属区县	路别	表计地址	最小单位	表号	水源类型	国标行业	主要行业	取水用途	水表属性	收费类型	注册日期
         */
        logger.info("水表信息导出条数为:{}", waterMeters.size());
        List<Object[]> objects = new ArrayList<Object[]>();
        for (WaterMeter waterMeter : waterMeters) {
            Object[] obj = new Object[]{
                waterMeter.get("real_code"),
                waterMeter.get("companyName"),
                waterMeter.get("water_unit"),
                waterMeter.get("county"),
                waterMeter.getLineNum(),
                waterMeter.getMeterAddress(),
                waterMeter.getTimes(),
                waterMeter.getMeterNum(),
                waterMeter.get("watersTypeName"),
                waterMeter.get("gb_industry"),
                waterMeter.get("main_industry"),
                waterMeter.get("waterUseTypeName"),
                waterMeter.get("meterAttrName"),
                waterMeter.get("chargeTypeName"),
                ToolDateTime.format(waterMeter.getRegistDate(), ToolDateTime.pattern_ymd),
                waterMeter.getBillingCycle(),
                waterMeter.getMemo(),
                waterMeter.getVender()
            };
            objects.add(obj);
        }

        Set<Integer> isNumTypeColSet = new HashSet<Integer>();


        super.logger.info("导出水表信息结束");
        return super.export(FILE_NAME, listHeader, objects, isNumTypeColSet);
    }
}
