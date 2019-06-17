package com.bj.scb.weixin;

import java.net.URLEncoder;
import java.util.Date;
import com.alibaba.fastjson.JSONObject;
import com.bj.scb.utils.JdryTime;

public class TemplateMessageManager {

	// 模板消息字体颜色
	private static final String COLOR = "#173177";

	// 设置行业
	public void set() {
		String at = Util.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";
		url = url.replace("ACCESS_TOKEN", at);
		String data = "{\n" + "          \"industry_id1\":\"25\",\n" + "          \"industry_id2\":\"41\"\n"
				+ "       }";
		Util.post(url, data);
	}

	// 获取行业
	public void get() {
		String at = Util.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=ACCESS_TOKEN";
		url = url.replace("ACCESS_TOKEN", at);
		Util.get(url);
	}

	/**
	 * @category 应急救援-预约成功后，发送消息给预约人
	 * @param openid
	 * @param name
	 * @param time
	 * @throws Exception
	 */
	public static void sendTemplateMessageToCustomer(String openid, String userName) throws Exception {
		String at = Util.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
		url = url.replace("ACCESS_TOKEN", at);
		JSONObject jsonObject = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("first", getJSONObject("您好，预约结果：", COLOR));
		data.put("keyword1", getJSONObject(userName, COLOR));
		data.put("keyword2", getJSONObject("应急救援", COLOR));
		data.put("keyword3", getJSONObject(JdryTime.getFullTimeBySec(new Date().getTime()), COLOR));
		data.put("keyword4", getJSONObject("客服正在确认", COLOR));
		data.put("remark", getJSONObject("请您稍等片刻，小二马上就好", COLOR));
		jsonObject.put("touser", openid);
		jsonObject.put("template_id", "E_kar7ymOXmetNjfPcDn-CC8-Ac387esshbXJr4zMtA");
		jsonObject.put("data", data);
		Util.post(url, jsonObject.toJSONString());
	}

	/**
	 * @category 应急救援-预约成功后，发送消息给管理员的消息模板
	 * @param openid
	 * @param name
	 * @throws Exception
	 */
	public static void sendYJAdminTemplateMessage(String openid, String name, String checkStationName) throws Exception {
		String at = Util.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
		url = url.replace("ACCESS_TOKEN", at);
		JSONObject jsonObject = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("first", getJSONObject("您有一条待办审批提醒：", COLOR));
		data.put("keyword1", getJSONObject("应急救援", COLOR));
		data.put("keyword2", getJSONObject("派单", COLOR));
		data.put("keyword3", getJSONObject("需要对该订单进行审批派工", COLOR));
		data.put("keyword4", getJSONObject(name, COLOR));
		data.put("keyword5", getJSONObject("管理员审批", COLOR));
		data.put("remark", getJSONObject("请及时办理，点击详情查看", COLOR));
		jsonObject.put("touser", openid);
		jsonObject.put("template_id", "bfHrzf5oqxV45bcGOiC-329cSbm4jJ_n3Bv2VYN6F5s");
		String para = URLEncoder.encode(checkStationName, "utf-8");// (checkStationName)
		jsonObject.put("url", "https://www.kjscb.com/wx/yjpg/index.jsp?" + para + ";" + "1");
		jsonObject.put("data", data);
		Util.post(url, jsonObject.toJSONString());
	}
	
	/**
	 * @category 预约审车-预约成功后，发送消息给预约人，预约人需要导航URL
	 * @param openid
	 * @param name
	 * @param time
	 * @param xy
	 * @param chechStationName
	 * @param typeName
	 * @throws Exception
	 */
	public static void sendTemplateMessage(String openid, String name, String time, String xy, String chechStationName,
			String typeName) throws Exception {
		String at = Util.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
		url = url.replace("ACCESS_TOKEN", at);
		JSONObject jsonObject = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("first", getJSONObject("您好，预约结果：", COLOR));
		data.put("keyword1", getJSONObject(name, COLOR));
		data.put("keyword2", getJSONObject(typeName, COLOR));
		data.put("keyword3", getJSONObject(time, COLOR));
		data.put("keyword4", getJSONObject("客服已确认", COLOR));
		data.put("remark", getJSONObject("导航请点详情", COLOR));
		jsonObject.put("touser", openid);
		jsonObject.put("template_id", "E_kar7ymOXmetNjfPcDn-CC8-Ac387esshbXJr4zMtA");
		String position = URLEncoder.encode(xy, "utf-8");
		String stationName = URLEncoder.encode(chechStationName, "utf-8");
		jsonObject.put("url", "https://www.kjscb.com/wx/weixinweb/map/nav_guide.jsp?" + position + ";" + stationName);
		jsonObject.put("data", data);
		Util.post(url, jsonObject.toJSONString());
	}

	/**
	 * @category 发送给检车站管理员的消息模板
	 * 
	 * @param openid
	 * @param name
	 * @throws Exception
	 */
	public static void sendAdminTemplateMessage(String openid, String name, String checkStationName) throws Exception {
		String at = Util.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
		url = url.replace("ACCESS_TOKEN", at);
		JSONObject jsonObject = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("first", getJSONObject("您有一条待办审批提醒：", COLOR));
		data.put("keyword1", getJSONObject("审车", COLOR));
		data.put("keyword2", getJSONObject("派单", COLOR));
		data.put("keyword3", getJSONObject("需要对该订单进行审批派工", COLOR));
		data.put("keyword4", getJSONObject(name, COLOR));
		data.put("keyword5", getJSONObject("管理员审批", COLOR));
		data.put("remark", getJSONObject("请及时办理，点击详情查看", COLOR));
		jsonObject.put("touser", openid);
		jsonObject.put("template_id", "bfHrzf5oqxV45bcGOiC-329cSbm4jJ_n3Bv2VYN6F5s");
		String para = URLEncoder.encode(checkStationName, "utf-8");// (checkStationName)
		jsonObject.put("url", "https://www.kjscb.com/wx/dispatch/index.jsp?" + para + ";" + "1");
		jsonObject.put("data", data);
		Util.post(url, jsonObject.toJSONString());
	}

	/**
	 * @category 发送给预约人接单成功的消息模板
	 * 
	 * @param openid  预约人的微信号
	 * @param name    客服人员的名称
	 * @param checkStationName    客服人员所属站点
	 * @param mobile    客服人员的手机号
	 * @throws Exception
	 */
	public static void sendYuYueMessage(String openid, String name, String checkStationName, String mobile)
			throws Exception {
		String at = Util.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
		url = url.replace("ACCESS_TOKEN", at);
		JSONObject jsonObject = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("first", getJSONObject("您的订单已受理，客服人员如下：", COLOR));
		data.put("keyword1", getJSONObject(name, COLOR));
		data.put("keyword2", getJSONObject(checkStationName + "客服人员", COLOR));
		data.put("keyword3", getJSONObject(mobile, COLOR));
		data.put("remark", getJSONObject("如有问题，请联系客服人员", COLOR));
		jsonObject.put("touser", openid);
		jsonObject.put("template_id", "cqUAPNRTkVcLSdIMn4uIgBY-pH2VRqJObRPROAhkvz0");
		jsonObject.put("data", data);
		jsonObject.put("url", "https://www.kjscb.com/wx/weixinweb/phone.jsp?" + mobile + ";" + "1");
		jsonObject.put("data", data);
		Util.post(url, jsonObject.toJSONString());
	}

	/**
	 * @category 发送给预约人进行评价的模板消息
	 * 
	 * @param openid  预约人的微信号
	 * @param name    预约人的名称
	 * @param checkStationName    预约的站点名称
	 * @param flag     1 代表预约审车，2代表应急救援
	 * @throws Exception
	 */
	public static void sendCommentMessage(String openid, String name, String checkStationName, String order,int flag)
			throws Exception {
		String flagStr = "";
		if(flag == 1) {
			flagStr = "审车";
		}else {
			flagStr = "应急救援";
		}
		String at = Util.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
		url = url.replace("ACCESS_TOKEN", at);
		JSONObject jsonObject = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("first", getJSONObject("您有一条服务完成提醒：", COLOR));
		data.put("keyword1", getJSONObject(flagStr, COLOR));
		data.put("keyword2", getJSONObject(JdryTime.getFullTimeBySec((new Date()).getTime()), COLOR));
		data.put("remark", getJSONObject("请点击详情评价", COLOR));
		jsonObject.put("touser", openid);
		jsonObject.put("template_id", "wTe8GfLTuKaLD9LWdv3fILtLjj2QdWV8uN7DmZIgGzQ");
		String open = URLEncoder.encode(openid, "utf-8");
		String nam = URLEncoder.encode(name, "utf-8");
		String carname = URLEncoder.encode(checkStationName, "utf-8");
		String orde = URLEncoder.encode(order, "utf-8");
		jsonObject.put("url", "https://www.kjscb.com/wx/weixinweb/pj.jsp?" + open + ";" + nam + ";" + carname + ";"
				+ orde + ";" + "1");
		jsonObject.put("data", data);
		Util.post(url, jsonObject.toJSONString());
	}

	/**
	 * @category 发送给检车站客服人员的消息模板
	 * @param openid
	 * @param name
	 * @throws Exception
	 */
	public static void sendAdminTemplateMessageKehu(String openid, String name, String checkStationName, String kefuid)
			throws Exception {
		String at = Util.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
		url = url.replace("ACCESS_TOKEN", at);
		JSONObject jsonObject = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("first", getJSONObject("您有一条待办审批提醒：", COLOR));
		data.put("keyword1", getJSONObject("审车", COLOR));
		data.put("keyword2", getJSONObject("服务", COLOR));
		data.put("keyword3", getJSONObject("需要对该订单进行服务处理", COLOR));
		data.put("keyword4", getJSONObject(name, COLOR));
		data.put("keyword5", getJSONObject("客服服务", COLOR));
		data.put("remark", getJSONObject("请及时办理，点击详情查看", COLOR));
		jsonObject.put("touser", openid);
		jsonObject.put("template_id", "bfHrzf5oqxV45bcGOiC-329cSbm4jJ_n3Bv2VYN6F5s");
		String para = URLEncoder.encode(checkStationName, "utf-8");// (checkStationName)
		String kfid = URLEncoder.encode(kefuid, "utf-8");
		jsonObject.put("url", "https://www.kjscb.com/wx/Service/index.jsp?" + para + ";" + kfid + ";" + "1");
		jsonObject.put("data", data);
		Util.post(url, jsonObject.toJSONString());
	}
	
	/**
	 * @category 发送给应急救援点的客服人员完成服务的消息模板
	 * @param openid 客服的微信号
	 * @param name   客服的名称
	 * @param checkStationName   客服所属站点的名称
	 * @param staffId   客服的Id
	 * @throws Exception
	 */
	public static void sendYJStaffMessage(String openid, String name, String checkStationName, String staffId)
			throws Exception {
		String at = Util.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
		url = url.replace("ACCESS_TOKEN", at);
		JSONObject jsonObject = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("first", getJSONObject("您有一条待办审批提醒：", COLOR));
		data.put("keyword1", getJSONObject("应急救援", COLOR));
		data.put("keyword2", getJSONObject("服务", COLOR));
		data.put("keyword3", getJSONObject("需要对该订单进行服务处理", COLOR));
		data.put("keyword4", getJSONObject(name, COLOR));
		data.put("keyword5", getJSONObject("客服服务", COLOR));
		data.put("remark", getJSONObject("请及时办理，点击详情查看", COLOR));
		jsonObject.put("touser", openid);
		jsonObject.put("template_id", "bfHrzf5oqxV45bcGOiC-329cSbm4jJ_n3Bv2VYN6F5s");
		jsonObject.put("url", "https://www.kjscb.com/wx/yjwc/index.jsp?" + staffId + ";" + "1");
		jsonObject.put("data", data);
		Util.post(url, jsonObject.toJSONString());
	}

	public static JSONObject getJSONObject(String value, String color) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("value", value);
		jsonObject.put("color", color);
		return jsonObject;
	}
}
