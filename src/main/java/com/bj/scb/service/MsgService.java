package com.bj.scb.service;

import java.util.List;
import java.util.Map;

import com.bj.scb.pojo.MsgReply;
import com.bj.scb.utils.PageList;

public interface MsgService {
	
	List<MsgReply> getMsgList();

	PageList<MsgReply> selectList(Map<String, Object> parameter);

	MsgReply getMsgReplyById(String id);
	
	MsgReply getMsgReplyByName(String name);

	void saveMsgReply(MsgReply checkMsg);

	void deleteMsgReply(String id);

	List<MsgReply> listAll();
}
