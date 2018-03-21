package com.shangsc.platform.export;

import com.shangsc.platform.model.WaterIndex;

import java.util.*;

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
         所属节水办	用户编号	用户名称	水源类型	小计	01月	02月	03月	04月	05月	06月	07月	08月	09月	10月	11月	12月
         */
        List<String> listHeader = new ArrayList<String>();
        listHeader.addAll(Arrays.asList(new String[]{
                "所属节水办",
                "单位编号",
                "单位名称",
                "水源类型",
                "小计",
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
                    waterIndex.get("watersTypeName"),
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

        Set<Integer> isNumTypeColSet = new HashSet<Integer>();
        isNumTypeColSet.add(5);
        isNumTypeColSet.add(6);
        isNumTypeColSet.add(7);
        isNumTypeColSet.add(8);
        isNumTypeColSet.add(9);
        isNumTypeColSet.add(10);
        isNumTypeColSet.add(11);
        isNumTypeColSet.add(12);
        isNumTypeColSet.add(13);
        isNumTypeColSet.add(14);
        isNumTypeColSet.add(15);
        isNumTypeColSet.add(16);
        isNumTypeColSet.add(17);

        super.logger.info("导出单位用水指标信息结束");
        return super.export(FILE_NAME, listHeader, objects, isNumTypeColSet);
    }
}
