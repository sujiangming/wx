package com.bj.scb.utils;

import java.math.BigDecimal;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.bj.scb.conf.WxPayConfig;
import com.bj.scb.weixin.Util;

public class TestURL {
	/**
	 * 方法名：main</br>
	 * 详述：生成URL编码 </br>
	 * 开发人员：souvc </br>
	 * 创建时间：2016-1-4 </br>
	 * 
	 * @param args 说明返回值含义
	 * @throws 说明发生此异常的条件
	 */
	public static void main(String[] args) {
		// String source="http://www.kjscb.com/wx/oauthServlet";
		// System.out.println(CommonUtil.urlEncodeUTF8(source));
		// System.out.println(StringUtil.getRandomDigitStr(20));
		// String is=mul("1", "100");
		// System.out.println("----==="+is);
		// testSign();
		// String url =
		// "https://www.kjscb.com/wx/weixinweb/map/nav_guide.jsp?106.64433,26.390623;毕节市韩元机动车检车站";
		// String string = Util.getJSSignature(url);
		// System.out.println(string);
		getSignature();
	}

	public static String mul(String value1, String value2) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		NumberFormat integer = NumberFormat.getIntegerInstance();
		String str = integer.format(b1.multiply(b2).doubleValue());// 如果带小数会四舍五入到整数12,343,172
		int index = str.indexOf(",");
		if (index > 0) {
			return str.replace(",", "");
		}
		return str;
	}

	public static void testSign() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appid", WxPayConfig.APPID);// appid
		paramMap.put("mch_id", WxPayConfig.MCHID);// 商户号
		paramMap.put("nonce_str", WeixinUtil.createNoncestr(32));// 随机字符串
		paramMap.put("body", "审车定金");// 商品描述
		// 商户订单号：需唯一 (一定要注意，看自己设计，支付id和订单id关联)
		// 注：我们这里用orderId,按道理是需要支付id，
		// 也就是每次按支付都要生成不一样的id,防止支付错误
		paramMap.put("out_trade_no", "201812250110");// orderId
		paramMap.put("total_fee", "1");// 金额 -- 实际金额需乘以 100
		paramMap.put("spbill_create_ip", "58.16.97.180");// 客户ip地址
		paramMap.put("notify_url", WxPayConfig.NOTIFY_URL);// 回调地址
		paramMap.put("trade_type", "JSAPI");// 交易类型
		paramMap.put("openid", "o0lMawa68kwTqqVMWyHFTrVfNags");// 用户openid
		paramMap.put("sign", WeixinUtil.getSign(paramMap));// 签名 ，默认sign_type是MD5
		System.out.println(WeixinUtil.arrayToXml(paramMap));
	}

	public static String getSignature() {
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("jsapi_ticket",
				"kgt8ON7yVITDhtdwci0qedTZwvJdt8iUso_L3EWQgzPf2Gozkv1iYdy29hf0-wdmPPVjQKzLP1lQYWI6SwtnrQ");
		maps.put("noncestr", "Sxu0Rfh1oBf88N8KEfoQAb0zpO5rO65o");
		maps.put("timestamp", "1554967110");
		maps.put("url", "https://www.kjscb.com/wx/weixinweb/map/nav_guide.jsp?106.64433,26.390623;1");
		try {
			String str = SHA1(maps);
			maps.put("signature", str.toLowerCase());
			System.out.println(TestURL.class.getName() +"---->"+ str.toLowerCase());
		} catch (DigestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		maps.put("appId", Util.APPID);
		return JSON.toJSONString(maps);
	}

	public static String SHA1(Map<String, Object> maps) throws DigestException {
		// 获取信息摘要 - 参数字典排序后字符串
		String decrypt = getOrderByLexicographic(maps);
		try {
			// 指定sha1算法
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(decrypt.getBytes());
			// 获取字节数组
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString().toUpperCase();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new DigestException("签名错误！");
		}
	}

	private static String getOrderByLexicographic(Map<String, Object> maps) {
		System.out.println(splitParams(lexicographicOrder(getParamsName(maps)), maps));
		// jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg&noncestr=Wm3WZYTPz0wzccnW&timestamp=1414587457&url=http://mp.weixin.qq.com?params=value
		// jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg&noncestr=Wm3WZYTPz0wzccnW&timestamp=1414587457&url=http://mp.weixin.qq.com?params=value
		return splitParams(lexicographicOrder(getParamsName(maps)), maps);
	}

	private static List<String> getParamsName(Map<String, Object> maps) {
		List<String> paramNames = new ArrayList<String>();
		for (Map.Entry<String, Object> entry : maps.entrySet()) {
			paramNames.add(entry.getKey());
		}
		return paramNames;
	}

	private static List<String> lexicographicOrder(List<String> paramNames) {
		Collections.sort(paramNames);
		return paramNames;
	}

	/**
	 * 拼接排序好的参数名称和参数值 
	 * 
	 * @param paramNames 排序后的参数名称集合  maps 参数key-value map集合  * @return String
	 *                   拼接后的字符串 
	 */
	private static String splitParams(List<String> paramNames, Map<String, Object> maps) {
		StringBuilder paramStr = new StringBuilder();
		for (String paramName : paramNames) {
			paramStr.append(paramName);
			for (Map.Entry<String, Object> entry : maps.entrySet()) {
				if (paramName.equals(entry.getKey())) {
					paramStr.append("=" + String.valueOf(entry.getValue()) + "&");
				}
			}
		}
		paramStr.deleteCharAt(paramStr.length() - 1);
		return paramStr.toString();
	}

}
