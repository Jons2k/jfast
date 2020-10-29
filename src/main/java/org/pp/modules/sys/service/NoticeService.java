package org.pp.modules.sys.service;

import java.util.Date;

import org.pp.core.BaseService;
import org.pp.modules.sys.model.Notice;
import org.pp.utils.SecurityUtil;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class NoticeService implements BaseService<Notice>{	
	public static Notice dao = new Notice().dao();
	private String error = "";

	/**
	 * 记录用户阅读公告
	 * @param id long 公告ID
	 * @param userId long  用户ID
	 * @return boolean
	 */
	public boolean read(long id, long userId) {
		Record r = Db.findFirst("SELECT * FROM sys_notice_read WHERE noticeId=? AND userId=?", id, userId);
		if(r == null) {
			r = new Record();
			r.set("noticeId", id);
			r.set("userId", userId);
			r.set("readTime", new Date());
			if(!Db.save("sys_notice_read", r)) {
				return false;
			}
		}
		return Db.update("UPDATE sys_notice SET readCount=readCount+1 WHERE id=?", id) > 0;
	}
	
	@Override
	public boolean insert(Notice bean) {
		bean.setAddTime(new Date());
		bean.setAddUserId(SecurityUtil.user().getId());
		bean.setReadCount(0l);
		return bean.save();
	}
	
	@Override
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	@Override
	public Notice getModel() {
		return dao;
	}
}
