package com.shangsc.platform.export;

import com.google.common.collect.Lists;
import com.shangsc.platform.util.ToolPoi;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 通用导出类
 *
 * @author ssk
 * @create 2017-08-21-下午 1:53
 */
public abstract class ExportBaseService {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());


    public String export(String filename, List<String> listHeader, List<Object[]> objects, Set<Integer> isNumTypeColSet) {
        SXSSFWorkbook wb = new SXSSFWorkbook();
        Sheet sheet = wb.createSheet(filename);

        // 表头
        Row rowHead = sheet.createRow(0);
        for (int i = 0; i < listHeader.size(); i++) {
            Cell cell = rowHead.createCell(i);
            cell.setCellValue(listHeader.get(i));
            sheet.setColumnWidth(i, 30 * 256);
        }
        // 表头样式
        ToolPoi.setHeadStyle(wb, rowHead.cellIterator());

        // 基本样式
        XSSFColor color = new XSSFColor(new java.awt.Color(255, 255, 255));
        Font font = ToolPoi.createFont(wb, Font.BOLDWEIGHT_NORMAL, Font.COLOR_NORMAL, (short) 10);
        CellStyle style = ToolPoi.createBorderCellStyle(wb, HSSFColor.WHITE.index, color, CellStyle.ALIGN_CENTER, font);

        // 样式换行
        CellStyle wrap = wb.createCellStyle();
        wrap.cloneStyleFrom(style);
        wrap.setWrapText(true);

        // 绿色
        XSSFCellStyle blue = (XSSFCellStyle) wb.createCellStyle();
        blue.cloneStyleFrom(style);
        blue.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 164, 137)));

        // 红色
        XSSFCellStyle red = (XSSFCellStyle) wb.createCellStyle();
        red.cloneStyleFrom(style);
        red.setFillForegroundColor(new XSSFColor(new java.awt.Color(243, 123, 83)));

        // 处理数据
        for (int i = 0; i < objects.size(); i++) {
            Object[] obj = objects.get(i);
            // 创建行
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < obj.length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellStyle(style);
                if (CollectionUtils.isNotEmpty(isNumTypeColSet) && isNumTypeColSet.contains(j + 1)) {
                    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    String colVal = ObjectUtils.toString(obj[j]);
                    if (StringUtils.isNotEmpty(colVal)) {
                        cell.setCellValue(Double.parseDouble(colVal));
                    } else {
                        cell.setCellValue(Double.parseDouble("0"));
                        logger.info("column value empty!");
                    }
                } else {
                    cell.setCellValue(ObjectUtils.toString(obj[j]));
                }
            }

        }

        String path = ToolPoi.writeExcel(wb, filename);

        return path;
    }

    public String export(String filename, List<String> listHeader, List<Object[]> objects, Set<Integer> isNumTypeColSet, int[] mergeIndex, boolean isMerge) {
        SXSSFWorkbook wb = new SXSSFWorkbook();
        Sheet sheet = wb.createSheet(filename);

        // 表头
        Row rowHead = sheet.createRow(0);
        for (int i = 0; i < listHeader.size(); i++) {
            Cell cell = rowHead.createCell(i);
            cell.setCellValue(listHeader.get(i));
            sheet.setColumnWidth(i, 30 * 256);
        }
        // 表头样式
        ToolPoi.setHeadStyle(wb, rowHead.cellIterator());

        // 基本样式
        XSSFColor color = new XSSFColor(new java.awt.Color(255, 255, 255));
        Font font = ToolPoi.createFont(wb, Font.BOLDWEIGHT_NORMAL, Font.COLOR_NORMAL, (short) 10);
        CellStyle style = ToolPoi.createBorderCellStyle(wb, HSSFColor.WHITE.index, color, CellStyle.ALIGN_CENTER, font);

        // 样式换行
        CellStyle wrap = wb.createCellStyle();
        wrap.cloneStyleFrom(style);
        wrap.setWrapText(true);

        // 绿色
        XSSFCellStyle blue = (XSSFCellStyle) wb.createCellStyle();
        blue.cloneStyleFrom(style);
        blue.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 164, 137)));

        // 红色
        XSSFCellStyle red = (XSSFCellStyle) wb.createCellStyle();
        red.cloneStyleFrom(style);
        red.setFillForegroundColor(new XSSFColor(new java.awt.Color(243, 123, 83)));

        List<PoiModel> poiModels = Lists.newArrayList();
        // 处理数据
        for (int a = 0; a < objects.size(); a++) {
            Object[] obj = objects.get(a);
            // 创建行
            Row row = sheet.createRow(a + 1);
            int index = 1;
            for (int b = 0; b < obj.length; b++) {
                /*循环需要合并的列*/
                for (int j = 0; j < mergeIndex.length; j++) {
                    if (index == 1) {
                        /*记录第一行的开始行和开始列*/
                        PoiModel poiModel = new PoiModel();
                        poiModel.setOldContent(ObjectUtils.toString(obj[b]));
                        poiModel.setContent(ObjectUtils.toString(obj[b]));
                        poiModel.setRowIndex(1);
                        poiModel.setCellIndex(b);
                        poiModels.add(poiModel);
                        break;
                    } else if (b > 0 && mergeIndex[j] == b) {
                        /*这边i>0也是因为第一列已经是最前一列了，只能从第二列开始*/
                        /*当前同一列的内容与上一行同一列不同时，把那以上的合并, 或者在当前元素一样的情况下，前一列的元素并不一样，这种情况也合并*/
                        /*如果不需要考虑当前行与上一行内容相同，但是它们的前一列内容不一样则不合并的情况，把下面条件中||poiModels.get(i).getContent().equals(ObjectUtils.toString(obj[b])) && !poiModels.get(i - 1).getOldContent().equals(map.get(title[i-1]))去掉就行*/
                        if (!poiModels.get(b).getContent().equals(ObjectUtils.toString(obj[b])) ||
                                poiModels.get(b).getContent().equals(ObjectUtils.toString(obj[b]))
                                        && !poiModels.get(b - 1).getOldContent().equals(ObjectUtils.toString(obj[b - 1]))) {
                            /*当前行的当前列与上一行的当前列的内容不一致时，则把当前行以上的合并*/
                            CellRangeAddress cra = new CellRangeAddress(poiModels.get(b).getRowIndex()/*从第二行开始*/, index - 1/*到第几行*/,
                                    poiModels.get(b).getCellIndex()/*从某一列开始*/, poiModels.get(b).getCellIndex()/*到第几列*/);
                            //在sheet里增加合并单元格
                            sheet.addMergedRegion(cra);
                            /*重新记录该列的内容为当前内容，行标记改为当前行标记，列标记则为当前列*/
                            poiModels.get(b).setContent(ObjectUtils.toString(obj[b]));
                            poiModels.get(b).setRowIndex(index);
                            poiModels.get(b).setCellIndex(b);
                        }
                    }
                    /*处理第一列的情况*/
                    if (mergeIndex[j] == b && b == 0 && !poiModels.get(b).getContent().equals(ObjectUtils.toString(obj[b]))) {
                        /*当前行的当前列与上一行的当前列的内容不一致时，则把当前行以上的合并*/
                        CellRangeAddress cra = new CellRangeAddress(poiModels.get(b).getRowIndex()/*从第二行开始*/, index - 1/*到第几行*/,
                                poiModels.get(b).getCellIndex()/*从某一列开始*/, poiModels.get(b).getCellIndex()/*到第几列*/);
                        //在sheet里增加合并单元格
                        sheet.addMergedRegion(cra);
                        /*重新记录该列的内容为当前内容，行标记改为当前行标记*/
                        poiModels.get(b).setContent(ObjectUtils.toString(obj[b]));
                        poiModels.get(b).setRowIndex(index);
                        poiModels.get(b).setCellIndex(b);
                    }

                    /*最后一行没有后续的行与之比较，所有当到最后一行时则直接合并对应列的相同内容*/
                    if (mergeIndex[j] == b && index == objects.size()) {
                        CellRangeAddress cra = new CellRangeAddress(poiModels.get(b).getRowIndex()/*从第二行开始*/, index/*到第几行*/,
                                poiModels.get(b).getCellIndex()/*从某一列开始*/, poiModels.get(b).getCellIndex()/*到第几列*/);
                        //在sheet里增加合并单元格
                        sheet.addMergedRegion(cra);
                    }
                }

                Cell cell = row.createCell(b);
                cell.setCellStyle(style);
                if (CollectionUtils.isNotEmpty(isNumTypeColSet) && isNumTypeColSet.contains(b + 1)) {
                    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    String colVal = ObjectUtils.toString(obj[b]);
                    if (StringUtils.isNotEmpty(colVal)) {
                        cell.setCellValue(Double.parseDouble(colVal));
                    } else {
                        cell.setCellValue(Double.parseDouble("0"));
                        logger.info("column value empty!");
                    }
                } else {
                    cell.setCellValue(ObjectUtils.toString(obj[b]));
                }
                index++;
            }

        }


        String path = ToolPoi.writeExcel(wb, filename);

        return path;
    }

    public List<Map<Integer, String>> importExcel(String fileAbsPath) throws Exception {
        List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
        Workbook wb = WorkbookFactory.create(new File(fileAbsPath));
        Sheet sheet = wb.getSheetAt(0); // 获取到第一个sheet中数据
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {// 第二行开始取值，第一行为标题行
            Row row = sheet.getRow(i);// 获取到第i列的行数据(表格行)
            Map<Integer, String> map = new HashMap<Integer, String>();
            if (row != null) {
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);// 获取到第j行的数据(单元格)
                    if (cell != null) {
                        if ("yyyy/mm;@".equals(cell.getCellStyle().getDataFormatString()) || "m/d/yy".equals(cell.getCellStyle().getDataFormatString())
                                || "yy/m/d".equals(cell.getCellStyle().getDataFormatString()) || "mm/dd/yy".equals(cell.getCellStyle().getDataFormatString())
                                || "dd-mmm-yy".equals(cell.getCellStyle().getDataFormatString()) || "yyyy/m/d".equals(cell.getCellStyle().getDataFormatString())) {
                            if (cell.getDateCellValue() != null) {
                                map.put(j, new SimpleDateFormat("yyyy/MM/dd").format(cell.getDateCellValue()));
                            } else {
                                map.put(j, new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
                            }
                        } else {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            map.put(j, cell.getStringCellValue());
                        }
                    } else {
                        break;
                    }
                }
                list.add(map);
            }
        }
        return list;
    }
}
