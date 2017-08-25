package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.model.base.BaseMonthNum;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * @Author ssc
 * @Date 2017/8/23 0:59
 * @Version 1.0.0
 * @Desc
 */
public class MonthNum extends BaseMonthNum<MonthNum> {

    private Logger logger = Logger.getLogger(getClass());

    public Page<MonthNum> getMonthStatis(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime, String ... keywords) {
        logger.info("orderbyStr : " + orderbyStr);
        String select="select * ";
        StringBuffer sqlExceptSelect=new StringBuffer(" company c ");
        return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }
    public void exportMonthData(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime, String ... keywords) {

    }
}
