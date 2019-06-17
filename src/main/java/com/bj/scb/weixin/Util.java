package com.bj.scb.weixin;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSON;
import com.bj.scb.pay.WXPayUtil;
import com.thoughtworks.xstream.XStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Util {

	public static final String TOKEN = "22_B5Q2P4JEEdKF8gZkc_AXD3oe60UDLmDwPFy4JUw0UJiZ7SRVPrhwc3LP3rK5gwvYNorxN0vP7xCqRl1okPqjxPMURJgsH4RWaX0SXdV9h8aA41VrEYFYQbk4gcgyjHFbaMr90Dko6dJ-eLnpFYHfAHAATC";
	private static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	public static final String APPID = "wx5f5fe40470be5d0b";//测试账号 "wx87bdcdb38c818077"
	public static final String APPSECRET = "708934964f2369b6a76fe896e3985789";//"58f48c12064de09bc1012740f00366c5";
	public static final String MCH_NO = "1435583802";
	public static final String KEY = "qWWKwJDiQMqakBXYeAadae6DrRWUp79i";//商户支付密钥Key。审核通过后，在微信发送的邮件中查看，需改
	public static final String DOMAIN = "www.kjscb.com";//"subenjiang.ngrok.xiaomiqiu.cn" ;
	public static Menu menu = new Menu();
	public static final String UPLOAD_URL = "https://www.kjscb.com/imgdept/";
	
	/**
	 * 获取微信自定义菜单
	 */
	public static boolean initWxMenu() {
		// 调用微信自定义菜单接口
		boolean isExistMenu = false;
		JSONObject jsonObject = queryWxMenu();
		if (jsonObject != null) {
			Object object = jsonObject.get("menu");
			if (null != object) {
				JSONArray jsonArray = jsonObject.getJSONObject("menu").getJSONArray("button");
				List<FirstMenu> list = jsonArray.toList(jsonArray, FirstMenu.class);
				Util.menu.setButton(list);
				isExistMenu = true;
			}
		}
		return isExistMenu;
	}

	/**
	 * 自定义菜单查询接口
	 * 
	 * @return
	 */
	public static JSONObject queryWxMenu() {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
		url = url.replace("ACCESS_TOKEN", (Util.getAccessToken()));
		String ret = Util.post(url, null);// 发送请求
		JSONObject jsonObject = JSONObject.fromObject(ret);
		return jsonObject;
	}

	// 向指定的地址上发送post请求
	public static String post(String url, String data) {
		String line = "";
		try {
			URL urlObj = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
			// 要发送数据，必须设置为可发送数据状态
			connection.setDoOutput(true);
			// 获取输出流
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			// 写出数据
			if (data != null && !"".equals(data)) {
				out.write(data.toString().getBytes("utf-8"));
			}
			out.close();
			// 获取输入流
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String tmp = "";
			while ((tmp = reader.readLine()) != null) {
				line = line + tmp;
			}
			reader.close();
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return line;
	}

	// 向指定的地址发送get请求
	public static String get(String url) {
		URL url1;
		try {
			url1 = new URL(url);
			HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
			// 将返回的输入流转换成字符串
			InputStream inputStream = urlConnection.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader in = new BufferedReader(inputStreamReader);
			String jsonUserStr = in.readLine().toString();
			// 释放资源
			inputStream.close();
			inputStream = null;
			urlConnection.disconnect();

			return jsonUserStr;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * @category get请求-获取token
	 */
	private static void getToken() {
		String url = GET_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		String tokenStr = Util.get(url);
		JSONObject jsonObject = JSONObject.fromObject(tokenStr);
		String token = jsonObject.getString("access_token");
		String expireIn = jsonObject.getString("expires_in");
		// 创建token对象
		AccessToken.getInstance().setAccessToken(token);// (token, expireIn);
		AccessToken.getInstance().setExpireTime(Long.valueOf(expireIn));
	}

	/**
	 * @category 向外提供的获取token的方法
	 * @return
	 */
	public static String getAccessToken() {
		if (AccessToken.getInstance() == null || AccessToken.getInstance().isExpired()) {
			getToken();
		}
		return AccessToken.getInstance().getAccessToken();
	}

	/**
	 * @category 获取随机数
	 * @return
	 */
	public static String getRadomStr() {
		int max = 50, min = 1;
		int ran2 = (int) (Math.random() * (max - min) + min);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String dateString = sdf.format(new Date());
		return "Key" + dateString + ran2;
	}

	/**
	 * @category 验证签名
	 * @param timestamp
	 * @param nonce
	 * @param signature
	 * @return
	 */
	public static boolean check(String timestamp, String nonce, String signature) {
		// 1）将token、timestamp、nonce三个参数进行字典序排序
		String[] strs = new String[] { TOKEN, timestamp, nonce };
		Arrays.sort(strs);
		// 2）将三个参数字符串拼接成一个字符串进行sha1加密
		String str = strs[0] + strs[1] + strs[2];
		String mysig = sha1(str);
		// 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
		return mysig.equalsIgnoreCase(signature);
	}

	// 进行sha1加密
	public static String sha1(String src) {

		try {
			// 获取一个加密对象
			MessageDigest md = MessageDigest.getInstance("sha1");
			// 加密
			byte[] digest = md.digest(src.getBytes());
			char[] chars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
			StringBuilder sb = new StringBuilder();
			// 处理加密结果
			for (byte b : digest) {
				sb.append(chars[(b >> 4) & 15]);
				sb.append(chars[b & 15]);
			}
			return sb.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 解析xml数据包
	 */

	public static Map<String, String> parseRequest(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		SAXReader saxReader = new SAXReader();
		InputStream is;
		try {
			is = request.getInputStream();
			// 读取输入流，获取文档对象
			Document document = saxReader.read(is);
			// 根据文档对象获取根节点
			Element root = document.getRootElement();
			// 根据根节点获取所有子节点
			List<Element> elements = root.elements();
			for (Element e : elements) {
				map.put(e.getName(), e.getStringValue());
			}
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	// 把消息对象处理为xml数据包
	public static String beanToXml(BaseMessage msg,Class clzss) {
		XStream stream = new XStream();
		// 设置需要处理@XStreamAlias("xml")注解的类
		stream.processAnnotations(clzss);//TextMessage.class
		String xml = stream.toXML(msg);
		return xml;
	}

	/**
	 * @category 测试时的accuss_tocken
	 * @return
	 */
	/*
	 * public static String textacctocken() { String acctoc =
	 * get("https://www.kjscb.com/wx/acctocken"); return acctoc; }
	 */

	/**
	 * @param currentUrl（当前网页的URL，不包含#及其后面部分）
	 * @category 获取JS-SDK签名
	 * @return
	 */
	public static String getJSSignature(String currentUrl) {
		// 1、获取jsapi_ticket
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
		url = url.replace("ACCESS_TOKEN", (Util.getAccessToken()));
		String ret = Util.get(url);// 发送请求
		JSONObject jsonObject = JSONObject.fromObject(ret);
		if (jsonObject == null || "".equals(jsonObject.getString("ticket"))) {
			return null;
		}
		// 2、如果过去jsapi_ticket成功，则需要开始拼接字符串
		String ticket = jsonObject.getString("ticket");
		String noncestr = WXPayUtil.generateNonceStr();// 随机字符串
		long timestamp = WXPayUtil.getCurrentTimestamp();// 时间戳

		// 3、将字符串进行sha1加密
		String signature = createSignature(ticket, noncestr, timestamp, currentUrl);

		// 4、将时间戳、随机数、签名、appId传到前端页面
		jsonObject.put("noncestr", noncestr);
		jsonObject.put("timestamp", timestamp);
		jsonObject.put("signature", signature);
		jsonObject.put("appId", APPID);
		return jsonObject.toString();
	}

	/**
	 * @category 生成签名，并返回
	 * @param ticket
	 * @param noncestr
	 * @param timestamp
	 * @param url
	 * @return
	 */
	public static String createSignature(String ticket, String noncestr, Long timestamp, String url) {
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("jsapi_ticket", ticket);
		maps.put("noncestr", noncestr);
		maps.put("timestamp", timestamp);
		maps.put("url", url);
		String sha1Str = null;
		try {
			sha1Str = SHA1(maps);
			System.out.println("JS-SDK：" + sha1Str);
		} catch (DigestException e) {
			e.printStackTrace();
		}
		return sha1Str;
	}

	/**
	 * @category SHA1算法生成签名，即signature = SHA1(string1)
	 * @param maps
	 * @return
	 * @throws DigestException
	 */
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

	/**
	 * @category 将拼接好的字符串进行字典顺序排序
	 * @param maps
	 * @return
	 */
	private static String getOrderByLexicographic(Map<String, Object> maps) {
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
	 * @category拼接排序好的参数名称和参数值 
	 * 
	 * @param paramNames 排序后的参数名称集合  maps 参数key-value map集合 
	 * @return String 拼接后的字符串 
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
