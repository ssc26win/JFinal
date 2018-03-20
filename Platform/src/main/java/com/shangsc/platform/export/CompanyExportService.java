package com.shangsc.platform.export;

import com.shangsc.platform.model.Company;
import com.shangsc.platform.util.ToolDateTime;

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

    private static final String FILE_NAME = "单位信息导出";


    public String export(List<Company> companies) {
        super.logger.info("导出单位信息开始");
        /**
         * 单位编号	单位名称	所属节水办	所属区县	所属乡镇	原乡镇或街道	用户类型	国标行业	主要行业
         * 取水用途	联系人	联系电话	单位地址	邮政编码	管水部门	自备井基本水价	地表水基本水价	自来水基本水价	注册日期
         */
        List<String> listHeader = new ArrayList<String>();
        listHeader.addAll(Arrays.asList(new String[]{
                "单位编号",
                "单位名称",
                "所属节水办",
                "所属区县",
                "所属乡镇",
                "原乡镇或街道",
                "用户类型",
                "国标行业",
                "主要行业",
                "取水用途",
                "联系人",
                "联系电话",
                "单位地址",
                "邮政编码",
                "管水部门",
                "自备井基本水价",
                "地表水基本水价",
                "自来水基本水价",
                "注册日期",
                "节水型单位类型",
                "水井数量",
                "一级表数量",
                "远传表数量",
        }));

        logger.info("导出单位信息导出条数为:{}", companies.size());
        List<Object[]> objects = new ArrayList<Object[]>();
        for (Company company : companies) {
            Object[] obj = new Object[]{
                    company.getInnerCode(),
                    company.getName(),
                    company.getWaterUnit(),
                    company.getCounty(),
                    company.get("streetName"),
                    company.getStreetSrc(),
                    company.get("customerTypeName"),
                    company.getGbIndustry(),
                    company.getMainIndustry(),
                    company.get("waterUseTypeName"),
                    company.getContact(),
                    company.getPhone(),
                    company.getAddress(),
                    company.getPostalCode(),
                    company.getDepartment(),
                    company.getSelfWellPrice(),
                    company.getSurfacePrice(),
                    company.getSelfFreePrice(),
                    ToolDateTime.format(company.getCreateTime(), ToolDateTime.pattern_ymd),
                    company.get("unitTypeName"),
                    company.getWellCount(),
                    company.getFirstWatermeterCount(),
                    company.getRemotemeterCount(),
            };
            objects.add(obj);
        }

        super.logger.info("导出单位信息结束");
        return super.export(FILE_NAME, listHeader, objects);
    }
}
