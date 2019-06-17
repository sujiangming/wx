package com.bj.scb.weixin;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("xml")
public class TextMessage extends BaseMessage{
	@XStreamAlias("Content")
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public TextMessage(Map<String, String> requestMap,String content) {
		super(requestMap);
		this.setMsgType("text");
		this.content=content;
	}


	
	
}
