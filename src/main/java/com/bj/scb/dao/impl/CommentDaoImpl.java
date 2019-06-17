package com.bj.scb.dao.impl;

import java.util.Map;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.bj.scb.dao.base.BaseDaoImpl;
import com.bj.scb.pojo.Comment;
import com.bj.scb.utils.PageList;

@Repository
public class CommentDaoImpl extends BaseDaoImpl {

	public PageList<Comment> selectList(Map<String, Object> parameter) {
		String checkStationName = parameter.get("checkStationName") == null ? ""
				: parameter.get("checkStationName").toString();
		String hql = "from " + Comment.class.getName() + " where commentUserContent='" + checkStationName
				+ "' order by commentTime desc";
		int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
		int showCount = Integer.parseInt(parameter.get("showCount").toString());
		return this.findPageList(hql, currentPage, showCount);
	}

	public Comment getCommentById(String id) {
		return this.findObject("from " + Comment.class.getName() + " where id='" + id + "'");
	}

	public void deleteComment(String id) {
		String[] ids = id.split(",");
		Session session = this.getSession();
		for (int i = 0; i < ids.length; i++) {
			Comment comment = this.getCommentById(id);
			if (comment != null) {
				session.delete(comment);
			}
		}
	}

	public void save(Comment comment) {
		this.saveOrUpdateObject(comment);
	}

	public Comment selectOne(String order) {
		String hql = " from " + Comment.class.getName() + " where commentOrder='" + order + "' and commentStatus='1'";
		return this.findObject(hql);
	}

}
