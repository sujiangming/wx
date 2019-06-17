/**
 * File Name:WeixinUtil.java
 * Date:2017年10月24日下午2:05:05
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.bj.scb.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.bj.scb.conf.WxPayConfig;


/**
 * 微信相关工具类
 * ClassName:WeixinUtil 
 * Date:     2017年10月24日 下午2:05:05 
 * @author   sqq 
 * @since    JDK 1.8 
 */
public class WeixinUtil {
	
	/** = */
	public static final String QSTRING_EQUAL = "=";

	/** & */
	public static final String QSTRING_SPLIT = "&";
	
	/**
	 * 	作用：产生随机字符串，不长于32位
	 */
	public static String createNoncestr(int length){
		
		String chars = "abcdefghijklmnopqrstuvwxyz0123456789";  
		String str ="";
		Random rand = new Random();
		for (int i = 0; i < length; i++) {
			int index = rand.nextInt(chars.length());
			str += chars.substring(index, index + 1);
		} 
		return str;
	}
		
	/**
	 * 	把请求要素按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param para 请求要素
     * @param sort 是否需要根据key值作升序排列
     * @param encode 是否需要URL编码
     * @return 拼接成的字符串
	 */
	public static String formatBizQueryParaMap(Map<String,String> para,boolean sort, boolean encode)
	{
		List<String> keys = new ArrayList<String>(para.keySet());
        
        if (sort)
        	Collections.sort(keys);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = para.get(key);
            
            if (encode) {
				try {
					value = URLEncoder.encode(value, WxPayConfig.ENCODE);
				} catch (UnsupportedEncodingException e) {
				}
            }
            
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                sb.append(key).append(QSTRING_EQUAL).append(value);
            } else {
                sb.append(key).append(QSTRING_EQUAL).append(value).append(QSTRING_SPLIT);
            }
        }
        return sb.toString();
	}
	
	/**
	 * 	作用：生成签名
	 */
	public static String getSign(Map<String,String> paramMap)
	{
		for (String key : paramMap.keySet()) {//
			//值为空不参加签名
			if(StringUtil.isNullOrEmpty(paramMap.get(key))){
				paramMap.remove(key);
			}
		}
		String params = formatBizQueryParaMap(paramMap, true, false);
		//签名步骤二：在string后加入KEY
		params = params + "&key=" + WxPayConfig.KEY;//
		//签名步骤三：MD5加密并转大写
		params = MD5Util.MD5(params);
		return params;
	}
	
	/**
	 * 	作用：map转xml
	 */
	public static String arrayToXml(Map<String,String> paramMap){
        String xml = "<xml>";
        for (String key : paramMap.keySet()) {
			//值是否只有字母和数字
			if(paramMap.get(key).matches("^[\\da-zA-Z]*$")){
				xml += "<" + key + ">" + paramMap.get(key) + "</" + key + ">"; 
			}else{
				xml += "<" + key + "><![CDATA[" + paramMap.get(key) + "]]></" + key + ">";
			}
		}
        xml += "</xml>";
        return xml;
    }
	
	/**
	 * xml 转  map
	 * @param xml
	 * @return
	 */
	public static Map<String,String> xmltoMap(String xml) {  
        try {  
            Map<String,String> map = new HashMap<String,String>();  
            Document document = DocumentHelper.parseText(xml);  
            Element nodeElement = document.getRootElement();  
            List node = nodeElement.elements();  
            for (Iterator it = node.iterator(); it.hasNext();) {
                Element elm = (Element) it.next();  
                String val = elm.getText();
                val = val.replace("<![CDATA[", "");
                val = val.replace("]]>", "");
                map.put(elm.getName(), val);  
                elm = null;  
            }  
            node = null;
            nodeElement = null;  
            document = null;  
            return map;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }
	
	/**
	 * jsonStrToMap:(json转map). 
	 * @author sqq
	 * @param jsonStr
	 * @return
	 * @since JDK 1.8
	 */
	public static Map<String, Object> jsonStrToMap(String jsonStr){  
        Map<String, Object> map = new HashMap<String, Object>();  
        //最外层解析  
        JSONObject json = JSONObject.fromObject(jsonStr); 
        for(Object k : json.keySet()){  
              Object v = json.get(k);   
              //如果内层还是数组的话，继续解析  
              if(v instanceof JSONArray){  
                    List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();  
                    Iterator<JSONObject> it = ((JSONArray)v).iterator();  
                    while(it.hasNext()){  
                      JSONObject json2 = it.next();  
                      list.add(jsonStrToMap(json2.toString()));  
                    }  
                    map.put(k.toString(), list);  
              } else {  
                  map.put(k.toString(), v);  
              }  
        }
        return map;  
    }
	/**
	 * 获取ip地址
	 * getRemoteHost
	 * @author sqq
	 * @param request
	 * @return
	 * @since JDK 1.8
	 */
	public static String getRemoteHost(javax.servlet.http.HttpServletRequest request){
	    String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr();
	    }
	    return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}
	
	/** 
	 * 获取精确到秒的时间戳 
	 * @param date 
	 * @return 
	 */  
	public static int getTimestamp(Date date){  
	    if (null == date) {  
	        return 0;  
	    }  
	    String timestamp = String.valueOf(date.getTime()/1000);  
	    return Integer.valueOf(timestamp);  
	}
	
	/**
	 * 授权统一路径拼凑
	 * @param dqurl 当前访问的路径
	 * @param str 路径返回的参数
	 * @return
	 * @throws Exception
	 */
	public String doWeixinRedirectUrl(HttpServletRequest request) throws Exception{
		//特别注意：params分享会出现code过期参数  应去掉后作为回调地址
	//	String server_url_name = PropertiesUtil.getValue("SERVER_URL_NAME");//服务器地址
		String returl = request.getScheme()+"://"+ request.getServerName() + request.getRequestURI(); 
		Map<String,String[]> paramMap = request.getParameterMap();
		String params = "";
		int next = 0;
		//过滤code、state
		for (String key : paramMap.keySet()) {
			String[] strs = paramMap.get(key);
			if(!key.equals("code") && !key.equals("state")){
				if(next == 0){
					params += "?";
				}else{
					params += "&";
				}
				params += key + "=" + strs[0];
				next ++;
			}
			
		}
		String dqurl = returl + params;
		dqurl = URLEncoder.encode(dqurl, "utf-8");
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+ WxPayConfig.APPID +"&redirect_uri="+ dqurl +"&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
		return url;
	}
}

