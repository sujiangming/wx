package com.bj.scb.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bj.scb.dao.impl.CommentDaoImpl;
import com.bj.scb.pojo.Comment;
import com.bj.scb.service.CommentService;
import com.bj.scb.utils.PageList;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

	@Resource
	CommentDaoImpl commentDaoImpl;

	@Override
	public PageList<Comment> selectList(Map<String, Object> parameter) {
		return commentDaoImpl.selectList(parameter);
	}

	@Override
	public Comment getCommentById(String id) {
		return commentDaoImpl.getCommentById(id);
	}

	@Override
	public void deleteComment(String id) {
		commentDaoImpl.deleteComment(id);

	}

	@Override
	public void save(Comment comment) {
		commentDaoImpl.save(comment);
	}

	@Override
	public Comment selectOne(String order) {
		return commentDaoImpl.selectOne(order);
	}

}
