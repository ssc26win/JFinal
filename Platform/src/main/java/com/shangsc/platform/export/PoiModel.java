package com.shangsc.platform.export;

/**
 * @Author ssc
 * @Date 2018/9/28 10:40
 * @Desc 用途：
 */
public class PoiModel {

    private String content;

    private String oldContent;

    private int rowIndex;

    private int cellIndex;

    public String getOldContent() {
        return oldContent;
    }

    public void setOldContent(String oldContent) {
        this.oldContent = oldContent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }
}
