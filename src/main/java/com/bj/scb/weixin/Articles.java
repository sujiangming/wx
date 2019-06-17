package com.bj.scb.weixin;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class Articles extends BaseMessage{

	@XStreamAlias("Item")
	private Item item; 
	
	public Articles(Map<String, String> requestMap,Item item) {
		super(requestMap);
		this.setMsgType("news");
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	@XStreamAlias("xml")
	public static class Item{
		@XStreamAlias("Title")
		private String title;
		@XStreamAlias("Description")
		private String description;
		@XStreamAlias("PicUrl")
		private String picUrl;
		@XStreamAlias("Url")
		private String url;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getPicUrl() {
			return picUrl;
		}
		public void setPicUrl(String picUrl) {
			this.picUrl = picUrl;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	}
}
