package com.bj.scb.service;

import java.util.Map;

import com.bj.scb.pojo.Comment;
import com.bj.scb.utils.PageList;

public interface CommentService {

	public PageList<Comment> selectList(Map<String, Object> parameter);
	
	// 查询ID
	//public Comment getCommentById(String id);
	Comment getCommentById(String id);
	
	void  deleteComment(String id);
	//
	 void save(Comment comment);

	public Comment selectOne(String order);
}
