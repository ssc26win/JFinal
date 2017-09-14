package com.shangsc.platform.export;

import com.shangsc.platform.model.WaterMeter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 水表信息导出
 *
 * @author ssk www.hnapay.com Inc.All rights reserved
 * @create 2017-08-22-下午 3:17
 */
public class WaterMeterExportService extends ExportBaseService {

    private static final String FILE_NAME = "水表信息导出";


    public String export(List<WaterMeter> waterMeters) {

        List<String> listHeader = new ArrayList<String>();
        listHeader.addAll(Arrays.asList(new String[] {
                "单位编号",
                "单位名称",
                "所属节水办",
                "所属区县",
                "路别",
                "表计地址",
                "最小单位",
                "水源类型",
                "国标行业",
                "主要行业",
                "取水用途",
                "水表属性",
                "收费类型",
                "计费周期",
                "注册日期"
        }));

        /**
         *  `company_id` bigint(20) DEFAULT NULL COMMENT '单位id',
         `inner_code` varchar(50) DEFAULT NULL COMMENT '单位编号',
         `line_num` varchar(50) DEFAULT NULL COMMENT '路别',
         `meter_num` varchar(50) DEFAULT NULL COMMENT '水表表号',
         `waters_type` tinyint(4) DEFAULT NULL COMMENT '水源类型',
         `water_use_type` tinyint(4) DEFAULT NULL COMMENT '取水用途',
         `meter_attr` varchar(50) DEFAULT NULL COMMENT '水表属性',
         `charge_type` tinyint(4) DEFAULT NULL COMMENT '收费类型',
         `billing_cycle` varchar(50) DEFAULT NULL COMMENT '计费周期',
         `regist_date` datetime DEFAULT NULL COMMENT '注册日期',
         */
        logger.info("水表信息导出条数为:{}", waterMeters.size());
        List<Object[]> objects = new ArrayList<Object[]>();
        for (WaterMeter waterMeter : waterMeters) {
            Object[] obj = new Object[] {
                    waterMeter.getInnerCode(),
                    waterMeter.get("companyName"),
                    waterMeter.get("water_unit"),
                    waterMeter.get("county"),
                    waterMeter.getLineNum(),
                    waterMeter.getMeterAddress(),
                    waterMeter.getTimes(),
                    waterMeter.get("watersTypeName"),
                    waterMeter.get("gb_industry"),
                    waterMeter.get("main_industry"),
                    waterMeter.get("waterUseTypeName"),
                    waterMeter.getMeterAttr(),
                    waterMeter.get("chargeTypeName"),
                    waterMeter.getBillingCycle(),
                    waterMeter.getRegistDate(),

            };
            objects.add(obj);
        }
        return super.export(FILE_NAME, listHeader, objects);
    }
}
