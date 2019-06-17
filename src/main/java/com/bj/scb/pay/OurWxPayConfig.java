package com.bj.scb.pay;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.bj.scb.weixin.Util;

public class OurWxPayConfig extends WXPayConfig {

	private byte[] certData;

	public OurWxPayConfig() throws Exception {
        InputStream certStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("cert/wxpay/apiclient_cert.p12");
        this.certData = IOUtils.toByteArray(certStream);
        certStream.close();
    }
	
	public String getDomain() {
		return Util.DOMAIN;
	}

	public String getAppID() {
		return Util.APPID;
	}

	public String getMchID() {
		return Util.MCH_NO;
	}

	public String getKey() {
		return Util.KEY;
	}

	public InputStream getCertStream() {
		ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
		return certBis;
	}

	public int getHttpConnectTimeoutMs() {
		return 8000;
	}

	public int getHttpReadTimeoutMs() {
		return 10000;
	}

	public IWXPayDomain getWXPayDomain() {
		MyWXPayDomain myWXPayDomain = new MyWXPayDomain();
		return myWXPayDomain;
	}

}
