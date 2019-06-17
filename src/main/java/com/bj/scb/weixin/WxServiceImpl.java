package com.bj.scb.weixin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bj.scb.dao.impl.CustomerInfoDaoImpl;
import com.bj.scb.pojo.CustomerInfo;
import com.bj.scb.utils.CommonUtil;

import net.sf.json.JSONObject;


@Service
@Transactional
public class WxServiceImpl implements WxService{
	
	@Resource
	CustomerInfoDaoImpl customerInfoDaoImpl;

	// 获取用户的基本信息
	public void getUserInfo(String openid){
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		url = url.replace("ACCESS_TOKEN", Util.getAccessToken()).replace("OPENID", openid);
		String result = Util.get(url);
		JSONObject jObject=JSONObject.fromObject(result);
		CustomerInfo cInfo=new CustomerInfo();
		try {
			cInfo.setCustomerId(URLDecoder.decode(jObject.get("openid").toString(), "UTF-8"));
			cInfo.setCustomerNickName(URLDecoder.decode(jObject.get("nickname").toString(), "UTF-8"));
			cInfo.setCustomerPic(URLDecoder.decode(jObject.get("headimgurl").toString(), "UTF-8"));
			if(jObject.get("sex").toString().equals("1")) {
				cInfo.setCustomerGender("男");
			}else {
				cInfo.setCustomerGender("女");
			}
			
			cInfo.setCustomerAddress(URLDecoder.decode(jObject.get("city").toString(), "UTF-8"));
			cInfo.setIsInnerPeople("否");
			customerInfoDaoImpl.insertUser(cInfo);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		
	}
}
