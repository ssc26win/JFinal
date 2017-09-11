package com.shangsc.platform.export;

import com.shangsc.platform.model.WaterIndex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * WaterIndex
 *
 * @author ssk www.hnapay.com Inc.All rights reserved
 * @create 2017-08-22-下午 3:41
 */
public class WaterIndexExportService extends ExportBaseService{
    private static final String FILE_NAME = "单位用水指标信息导出";


    public String export(List<WaterIndex> list) {
        super.logger.info("导出单位用水指标信息开始");
        /**
         *  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
         `inner_code` varchar(50) DEFAULT NULL COMMENT '单位编号',
         `name` varchar(255) DEFAULT NULL COMMENT '单位名称',
         `street` varchar(255) DEFAULT NULL COMMENT '所属乡镇或街道',
         `address` varchar(255) DEFAULT NULL COMMENT '单位地址',
         `customer_type` tinyint(2) DEFAULT NULL COMMENT '用户类型',
         `water_use_type` tinyint(2) DEFAULT NULL COMMENT '取水用途',
         `contact` varchar(100) DEFAULT NULL COMMENT '联系人',
         `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
         `postal_code` varchar(50) DEFAULT NULL COMMENT '邮政编码',
         `department` varchar(255) DEFAULT NULL COMMENT '管水部门',
         `well_count` int(11) DEFAULT 0 COMMENT '水井数量',
         `first_watermeter_count` int(11) DEFAULT 0 COMMENT '一级表数量',
         `remotemeter_count` int(11) DEFAULT 0 COMMENT '远传表数量',
         `unit_type` tinyint(4) DEFAULT NULL COMMENT '节水型单位类型',
         `create_time` datetime DEFAULT NULL COMMENT '创建时间',
         */
        List<String> listHeader = new ArrayList<String>();
        listHeader.addAll(Arrays.asList(new String[]{
                "所属节水办",
                "单位编号",
                "单位名称",
                "水源类型",
                "年用水指标",
                "一月",
                "二月",
                "三月",
                "四月",
                "五月",
                "六月",
                "七月",
                "八月",
                "九月",
                "十月",
                "十一月",
                "十二月"
        }));

        logger.info("导出单位用水指标导出条数为:{}", list.size());
        List<Object[]> objects = new ArrayList<Object[]>();
        for (WaterIndex waterIndex : list) {
            Object[] obj = new Object[]{
                    waterIndex.get("water_unit"),
                    waterIndex.getInnerCode(),
                    waterIndex.get("companyName"),
                    waterIndex.get("watersName"),
                    waterIndex.getWaterIndex(),
                    waterIndex.getJanuary(),
                    waterIndex.getFebruary(),
                    waterIndex.getMarch(),
                    waterIndex.getApril(),
                    waterIndex.getApril(),
                    waterIndex.getJune(),
                    waterIndex.getJuly(),
                    waterIndex.getAugust(),
                    waterIndex.getSeptember(),
                    waterIndex.getOctober(),
                    waterIndex.getDecember(),
                    waterIndex.getNovember()
            };
            objects.add(obj);
        }

        super.logger.info("导出单位用水指标信息结束");
        return super.export(FILE_NAME, listHeader, objects);
    }
}
