package com.bj.scb.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.bj.scb.dao.base.BaseDaoImpl;
import com.bj.scb.pojo.MsgReply;
import com.bj.scb.utils.PageList;

@Repository
public class MsgReplyDaoImpl extends BaseDaoImpl {

	public List<MsgReply> getMsgList() {
		return this.findList("from " + MsgReply.class.getName());
	}

	public PageList<MsgReply> selectList(Map<String, Object> parameter) {
		String hql = " from " + MsgReply.class.getName();
		String where = " where 1=1";
		String whereCase = " a " + where;
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
		int showCount = Integer.parseInt(parameter.get("showCount").toString());
		String search = parameter.get("search") == null ? "" : parameter.get("search").toString();
		if (!search.equals("")) {
			whereCase += " and a.keyword like:username" + " ";
			map.put("username", "%" + search + "%");
		}
		hql += whereCase;
		return this.findPageList(hql, currentPage, showCount, map);
	}

	public MsgReply getMsgReplyById(String id) {
		return this.findObject(" from " + MsgReply.class.getName() + " where id='" + id + "'");
	}

	public void saveMsgReply(MsgReply checkMsg) {
		this.saveOrUpdateObject(checkMsg);
	}

	public void deleteMsgReply(String id) {
		String[] ids = id.split(",");
		Session session = this.getSession();
		for (int i = 0; i < ids.length; i++) {
			MsgReply checkMsg = this.getMsgReplyById(ids[i]);
			if (checkMsg != null) {
				session.delete(checkMsg);
			}
		}
	}

	public List<MsgReply> listAll() {
		String hql = " from " + MsgReply.class.getName();
		return this.findList(hql);
	}

	public MsgReply getMsgReplyByName(String name) {
		return this.findObject(" from " + MsgReply.class.getName() + " where keyword='" + name + "'");
	}
}
