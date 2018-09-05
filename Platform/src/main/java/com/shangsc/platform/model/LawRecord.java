package com.shangsc.platform.model;

import com.jfinal.plugin.activerecord.Db;
import com.shangsc.platform.code.YesOrNo;
import com.shangsc.platform.core.util.CommonUtils;
import com.shangsc.platform.core.view.InvokeResult;
import com.shangsc.platform.model.base.BaseLawRecord;

import java.util.Date;
import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class LawRecord extends BaseLawRecord<LawRecord> {
	public static final LawRecord dao = new LawRecord();

	public InvokeResult save(Long id, String title, String content, Integer status, String userName) {
		if (null != id && id > 0L) {
			LawRecord lawRecord = this.findById(id);
			if (lawRecord == null) {
				return InvokeResult.failure("更新消息失败, 该消息不存在");
			}
			lawRecord = setProp(lawRecord, title, content, status, userName);
			if (YesOrNo.isYes(String.valueOf(status))) {
				offPublishOther();
			}
			lawRecord.update();
		} else {
			LawRecord lawRecord = new LawRecord();
			lawRecord = setProp(lawRecord, title, content, status, userName);
			if (YesOrNo.isYes(String.valueOf(status))) {
				offPublishOther();
			}
			lawRecord.save();
		}
		return InvokeResult.success();
	}

	private void offPublishOther() {
		Db.update("update t_law_record set status=0 where 1=1");
	}

	private LawRecord setProp(LawRecord lawRecord, String title, String content, Integer status, String userName) {
		lawRecord.setTitle(title);
		lawRecord.setContent(content);
		lawRecord.setStatus(status);
		lawRecord.setMemo("");
		lawRecord.setCreateUser(userName);
		lawRecord.setCreateTime(new Date());
		return lawRecord;
	}

	public InvokeResult deleteData(String idStrs) {
		List<Long> ids = CommonUtils.getLongListByStrs(idStrs);
		for (int i = 0; i < ids.size(); i++) {
			this.deleteById(ids.get(i));
		}
		return InvokeResult.success();
	}
}