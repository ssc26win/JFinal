package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.model.BaseModel;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * @Author ssc
 * @Date 2017/8/20 21:20
 * @Version 1.0.0
 * @Desc
 */
public class DataStatis extends BaseModel implements IBean {

    private Logger logger = Logger.getLogger(getClass());

    public Page<?> getReadnumStatis(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime, String ... keywords) {
        logger.info("orderbyStr : " + orderbyStr);
        String select="select * from";
        StringBuffer sqlExceptSelect=new StringBuffer("from company c");
        return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }
    public void exportReadNumData(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime, String ... keywords) {

    }

    public Page<?> getDailyStatis(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime, String ... keywords) {
        logger.info("orderbyStr : " + orderbyStr);
        String select="select * from";
        StringBuffer sqlExceptSelect=new StringBuffer("from company c");
        return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }
    public void exportDailyData(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime, String ... keywords) {

    }

    public Page<?> getMonthStatis(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime, String ... keywords) {
        logger.info("orderbyStr : " + orderbyStr);
        String select="select * from";
        StringBuffer sqlExceptSelect=new StringBuffer("from company c");
        return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }
    public void exportMonthData(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime, String ... keywords) {

    }

    public Page<?> getYearStatis(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime, String ... keywords) {
        logger.info("orderbyStr : " + orderbyStr);
        String select="select * from";
        StringBuffer sqlExceptSelect=new StringBuffer("from company c");
        return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }
    public void exportYearData(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime, String ... keywords) {

    }
}
