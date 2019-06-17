package com.bj.scb.weixin;

import org.springframework.stereotype.Component;

/**
 * @category 用于存储token
 * @author JDRY-SJM
 *
 */
@Component
public class AccessToken {
	private String accessToken;
	private long expireTime;
	private volatile static AccessToken instance;

	private AccessToken() {

	}

	public static AccessToken getInstance() {
		if (instance == null) {
			synchronized (AccessToken.class) {
				if (instance == null) {
					instance = new AccessToken();
				}
			}
		}
		return instance;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = System.currentTimeMillis() + expireTime * 1000;
	}

	/**
	 * @category 判断token是否过期
	 * @return
	 */
	public boolean isExpired() {
		return System.currentTimeMillis() > expireTime;
	}

}
