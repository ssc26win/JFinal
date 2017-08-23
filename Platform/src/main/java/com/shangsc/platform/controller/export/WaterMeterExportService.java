package com.shangsc.platform.controller.export;

import com.jfinal.plugin.activerecord.Page;
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


    public String export(Page<WaterMeter> page) {

        List<String> listHeader = new ArrayList<String>();
        listHeader.addAll(Arrays.asList(new String[] {
                "单位id",
                "单位编号",
                "路别",
                "水表表号",
                "水源类型",
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
        List<WaterMeter> waterMeters = page.getList();
        logger.info("水表信息导出条数为:{}", waterMeters.size());
        List<Object[]> objects = new ArrayList<Object[]>();
        for (WaterMeter waterMeter : waterMeters) {
            Object[] obj = new Object[] {
                    waterMeter.getCompanyId(),
                    waterMeter.getInnerCode(),
                    waterMeter.getLineNum(),
                    waterMeter.getMeterNum(),
                    waterMeter.getWatersType(),
                    waterMeter.getWaterUseType(),
                    waterMeter.getMeterAttr(),
                    waterMeter.getChargeType(),
                    waterMeter.getBillingCycle(),
                    waterMeter.getRegistDate()

            };
        }
        return super.export(FILE_NAME, listHeader, objects);
    }
}
