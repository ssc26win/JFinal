package com.shangsc.platform.export;

import com.shangsc.platform.model.Company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用水单位信息导出类
 *
 * @author ssk www.hnapay.com Inc.All rights reserved
 * @create 2017-08-21-下午 2:30
 */

public class CompanyExportService extends ExportBaseService {

    private static final String FILE_NAME = "用水单位信息导出";


    public String export(List<Company> companies) {
        super.logger.info("导出用水单位信息开始");
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
                "单位编号",
                "单位名称",
                "所属乡镇或街道",
                "单位地址",
                "用户类型",
                "取水用途",
                "联系人",
                "联系电话",
                "邮政编码",
                "管水部门",
                "水井数量",
                "一级表数量",
                "远传表数量",
                "节水型单位类型",
                "创建时间"
        }));

        logger.info("导出用水单位信息导出条数为:{}", companies.size());
        List<Object[]> objects = new ArrayList<Object[]>();
        for (Company company : companies) {
            Object[] obj = new Object[]{
                    company.getInnerCode(),
                    company.getName(),
                    company.getStreet(),
                    company.getAddress(),
                    company.get("customerTypeName"),
                    company.get("waterUseTypeName"),
                    company.getContact(),
                    company.getPhone(),
                    company.getPostalCode(),
                    company.getDepartment(),
                    company.getWellCount(),
                    company.getFirstWatermeterCount(),
                    company.getRemotemeterCount(),
                    company.get("unitTypeName"),
                    company.getCreateTime()

            };
            objects.add(obj);
        }

        super.logger.info("导出用水单位信息结束");
        return super.export(FILE_NAME, listHeader, objects);
    }
}
