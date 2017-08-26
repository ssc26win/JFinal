package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.model.base.BaseReadnum;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * @Author ssc
 * @Date 2017/8/23 0:58
 * @Version 1.0.0
 * @Desc
 */
public class ReadNum extends BaseReadnum<ReadNum> {
    private Logger logger = Logger.getLogger(getClass());

    public Page<ReadNum> getReadnumStatis(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime, String ... keywords) {
        logger.info("orderbyStr : " + orderbyStr);
        String select="select * ";
        StringBuffer sqlExceptSelect=new StringBuffer(" company c");
        return this.paginate(pageNo, pageSize, select, sqlExceptSelect.toString());
    }

    public void exportReadNumData(int pageNo, int pageSize, String orderbyStr, Date startTime, Date endTime, String ... keywords) {

    }
}
