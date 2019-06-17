package com.bj.scb.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bj.scb.dao.impl.MsgReplyDaoImpl;
import com.bj.scb.pojo.MsgReply;
import com.bj.scb.service.MsgService;
import com.bj.scb.utils.PageList;

@Service
@Transactional
public class MsgServiceImpl implements MsgService {

	@Resource
	MsgReplyDaoImpl msgReplyDaoImpl;

	public List<MsgReply> getMsgList() {
		return msgReplyDaoImpl.getMsgList();
	}

	@Override
	public PageList<MsgReply> selectList(Map<String, Object> parameter) {
		return msgReplyDaoImpl.selectList(parameter);
	}

	@Override
	public MsgReply getMsgReplyById(String id) {
		return msgReplyDaoImpl.getMsgReplyById(id);
	}

	@Override
	public void saveMsgReply(MsgReply checkMsg) {
		msgReplyDaoImpl.saveMsgReply(checkMsg);
	}

	@Override
	public void deleteMsgReply(String id) {
		msgReplyDaoImpl.deleteMsgReply(id);
	}

	@Override
	public List<MsgReply> listAll() {
		return msgReplyDaoImpl.listAll();
	}

	@Override
	public MsgReply getMsgReplyByName(String name) {
		return msgReplyDaoImpl.getMsgReplyByName(name);
	}
}
