package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.shangsc.platform.core.model.Condition;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.base.BaseLawRecord;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class LawRecord extends BaseLawRecord<LawRecord> {
    public static final LawRecord dao = new LawRecord();
    public String globalInnerCode;

    public void setGlobalInnerCode(String globalInnerCode) {
        this.globalInnerCode = globalInnerCode;
    }

    public InvokeResult save(Long id, String title, String content, Integer status, Integer uId, String innerCode, String userName) {
        if (null != id && id > 0L) {
            LawRecord lawRecord = this.findById(id);
            lawRecord = setProp(lawRecord, title, content, status, uId, innerCode, null, null, "");
            lawRecord.setUpdateTime(new Date());
            lawRecord.update();
        } else {
            LawRecord lawRecord = new LawRecord();
            lawRecord = setProp(lawRecord, title, content, status, uId, innerCode, null, null, "");
            lawRecord.setCreateUser(userName);
            lawRecord.setCreateTime(new Date());
            lawRecord.save();
        }
        return InvokeResult.success();
    }

    private void offPublishOther() {
        Db.update("update t_law_record set status=0 where 1=1");
    }

    private LawRecord setProp(LawRecord lawRecord, String title, String content, Integer status, Integer uId, String innerCode,
                              BigDecimal longitude, BigDecimal latitude, String memo) {
        lawRecord.setTitle(title);
        lawRecord.setContent(content);
        lawRecord.setStatus(status);
        lawRecord.setLongitude(longitude);
        lawRecord.setLatitude(latitude);
        lawRecord.setMemo(memo);
        lawRecord.setUserId(uId);
        lawRecord.setInnerCode(innerCode);
        return lawRecord;
    }

    public InvokeResult deleteData(String idStrs) {
        List<Long> ids = CommonUtils.getLongListByStrs(idStrs);
        for (int i = 0; i < ids.size(); i++) {
            this.deleteById(ids.get(i));
        }
        return InvokeResult.success();
    }

    public Page<LawRecord> getPageList(int page, int rows, String keyword, String orderbyStr, SysUser sysUser) {
        String select = " select tlr.* ";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_law_record tlr where 1=1 ");

        if (!sysUser.isAdmin()) {
            sqlExceptSelect.append(" and tlr.user_id=" + sysUser.getId());
        }

        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (tlr.title like '%" + StringUtils.trim(keyword) + "%' or tlr.content like '%" + keyword + "%')");
        }

        //if (StringUtils.isNotEmpty(globalInnerCode)) {
        //    sqlExceptSelect.append(" and tlr.inner_code='" + StringUtils.trim(globalInnerCode) + "' ");
        //}
        if (StringUtils.isNotEmpty(orderbyStr)) {
            sqlExceptSelect.append(orderbyStr);
        }
        return this.paginate(page, rows, select, sqlExceptSelect.toString());
    }

    /**********************************
     * WxApp use
     ***************************************/

    public InvokeResult saveWx(Long id, String title, String content, Integer status, Integer uId,
                               BigDecimal longitude, BigDecimal latitude,
                               String innerCode, String userName, String memo) {
        if (null != id && id > 0L) {
            LawRecord lawRecord = this.findById(id);
            lawRecord = setProp(lawRecord, title, content, status, uId, innerCode, longitude, latitude, memo);
            lawRecord.setUpdateTime(new Date());
            lawRecord.update();
        } else {
            LawRecord lawRecord = new LawRecord();
            lawRecord = setProp(lawRecord, title, content, status, uId, innerCode, longitude, latitude, memo);
            lawRecord.setCreateUser(userName);
            lawRecord.setCreateTime(new Date());
            lawRecord.save();
            id = lawRecord.getLong("id");
        }
        return InvokeResult.success(id);
    }

    public Page<LawRecord> findWxList(int page, int rows, String keyword, SysUser sysUser) {
        String select = " select tlr.* ";
        StringBuffer sqlExceptSelect = new StringBuffer(" from t_law_record tlr where 1=1 ");

        if (StringUtils.isNotEmpty(keyword)) {
            sqlExceptSelect.append(" and (tlr.title like '%" + StringUtils.trim(keyword) + "%' or tlr.content like '%" + keyword + "%')");
        }
        if (!sysUser.isAdmin()) {
            sqlExceptSelect.append(" and tlr.user_id=" + sysUser.getId());
        }

        return this.paginate(page, rows, select, sqlExceptSelect.toString());
    }
}
