package com.bj.scb.weixin;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @category 图文消息
 * @author JDRY-SJM
 *
 */
@XStreamAlias("xml")
public class NewsMessage extends BaseMessage{
	@XStreamAlias("ArticleCount")
	private String articleCount;
	
	@XStreamAlias("Articles")
	private Articles articles;
	
	public NewsMessage(Map<String, String> requestMap,Articles articles) {
		super(requestMap);
		this.setMsgType("news");
		this.articleCount = requestMap.get("articleCount");
		this.articles = articles;
	}

	public String getArticleCount() {
		return articleCount;
	}

	public void setArticleCount(String articleCount) {
		this.articleCount = articleCount;
	}

	public Articles getArticles() {
		return articles;
	}

	public void setArticles(Articles articles) {
		this.articles = articles;
	}
}
