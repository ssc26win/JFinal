package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * @Author ssc
 * @Date 2017/8/20 21:20
 * @Version 1.0.0
 * @Desc
 */
public class DataStatis {
    private static final Company company = new Company();
    private static final ActualData actualData = new ActualData();

    private static Page<Record> getReadnumStatis(int page, int rows, String keywords, String orderBy) {


        Page<Record> recordPage = new Page<Record>();
        String select = "";
        return null;
    }

}
