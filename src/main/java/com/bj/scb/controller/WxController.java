package com.bj.scb.controller;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bj.scb.pojo.CheckStation;
import com.bj.scb.pojo.CustomerInfo;
import com.bj.scb.pojo.InnerUser;
import com.bj.scb.pojo.MsgReply;
import com.bj.scb.pojo.Order;
import com.bj.scb.pojo.OrderNickName;
import com.bj.scb.service.CheckStationService;
import com.bj.scb.service.CustomerInfoService;
import com.bj.scb.service.MsgService;
import com.bj.scb.service.OrderNickNameService;
import com.bj.scb.service.OrderService;
import com.bj.scb.service.TimeOrderCountService;
import com.bj.scb.weixin.AuthUtil;
import com.bj.scb.weixin.BaseMessage;
import com.bj.scb.weixin.PictureMessage;
import com.bj.scb.weixin.PictureMessage.ImageMessage;
import com.sun.xml.internal.ws.developer.StreamingAttachment;
import com.bj.scb.weixin.TemplateMessageManager;
import com.bj.scb.weixin.TextMessage;
import com.bj.scb.weixin.Util;
import com.bj.scb.weixin.WxService;

import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;

@Controller
public class WxController {

	@Resource
	private WxService wxService;
	@Resource
	private OrderService orderService;
	@Resource
	private TimeOrderCountService timeOrderCountService;
	@Resource
	private CheckStationService checkStationService;
	@Resource
	CustomerInfoService customerInfoService;
	@Resource
	OrderNickNameService orderNickNameService;
	@Resource
	MsgService msgService;

	@RequestMapping(value = "/wxOnline", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public void wxline(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (request.getMethod().toLowerCase().equals("get")) {
			String signature = request.getParameter("signature");
			String timestamp = request.getParameter("timestamp");
			String nonce = request.getParameter("nonce");
			String echostr = request.getParameter("echostr");
			if (Util.check(timestamp, nonce, signature)) {
				PrintWriter out = response.getWriter();
				out.print(echostr);
				out.flush();
				out.close();
			}
		} else {
			request.setCharacterEncoding("utf8");
			response.setCharacterEncoding("utf8");
			Map<String, String> requestMap = Util.parseRequest(request);
			String respXml = getResponse(requestMap);
			PrintWriter out = response.getWriter();
			out.print(respXml);
			out.flush();
			out.close();
		}
	}

	/**
	 * @category 用于处理所有的事件和消息的回复 返回的是xml数据包
	 * @param requestMap
	 * @return
	 */
	private String getResponse(Map<String, String> requestMap) {
		String msgType = requestMap.get("MsgType");
		String msgContent = requestMap.get("Content");// 用户发来的内容
		BaseMessage msg = null;
		String result = null;
		switch (msgType) {
		case "text":
			MsgReply msgReply = msgService.getMsgReplyByName(msgContent);// 根据关键字从数据库获取内容
			if (msgReply != null) {
				if (msgReply.getMediaId() != null 
						&& !"".equals(msgReply.getMediaId()) 
						&& msgReply.getClickUrl() == null || "".equals(msgReply.getClickUrl())) {//被动回复图片消息
					result = generatePicMsgReplyXml(requestMap, msgReply.getMediaId());
				} else if(msgReply.getClickUrl() != null && !"".equals(msgReply.getClickUrl())) {//被动回复图文消息
					result = generateTextPicMsgXml(requestMap, msgReply);
				}else { //被动回复文本消息
					result = generateTextXml(requestMap, msgReply);
				}
			} else {
				msg = delTextMessage(requestMap, msgContent);
				result = Util.beanToXml(msg, TextMessage.class);
			}
			break;
		case "event":
			msg = dealEvent(requestMap);
			result = Util.beanToXml(msg, TextMessage.class);
			break;
		default:
			msg = delTextMessage(requestMap, msgContent);
			result = Util.beanToXml(msg, TextMessage.class);
			break;
		}
		// 把消息对象处理为xml数据包
		if (result != null) {
			 //System.out.println("=====result=====" + result);
			return result;
		}
		return null;
	}

	// 处理文本消息
	public BaseMessage delTextMessage(Map<String, String> requestMap, String msg) {
		// 需要返回的封装对象
		TextMessage text = null;
		// 原先的回复规则
		if (msg.equals("1")) {
			text = new TextMessage(requestMap,
					"审车流程：" + "１、关注审车帮公众号。\r\n" + "２、填写相关信息并预约。\r\n" + "3、支付定金。\r\n" + "4、预约成功。\r\n"
							+ "5、检测站发送客服人员联系方式。\r\n" + "6、将车在预约时段驶到检测站。\r\n" + "7、联系客服将车交给客服并支付余款，等待检测。\r\n"
							+ "8、检测完结客服将车及合格证交给车主。\r\n" + "9、车主对此次服务进行评价。\r\n" + "");
		} else if (msg.equals("2")) {
			text = new TextMessage(requestMap,
					"审车注意事项：\r\n" + "为了您的爱车能顺利通过检测请注意以下事项：\r\n" + "１、私家车必须携带行车证和车主身份证；单位车辆必须携带行车证和单位委托书。\r\n"
							+ "２、所审车辆交强险必须在有效期内，如上一年交强险保期不足10天，必须携带下一年交强险保单副本。\r\n"
							+ "3、所审车辆必须和行车证上记载信息一致，无改装，特别是轮胎型号，一定要与机动车登记本上的信息一致。\r\n" + "");
		} else {
			text = new TextMessage(requestMap,
					"欢迎关注毕节快捷审车帮！审车帮是由毕节市飞弘网络科技有限公司运营的Ｏ2Ｏ微信预约审车平台，通过本平台预约审车是车主与检测站客服直接联系对接，没有二拐，没有中间商，审车费用就是检测站的上线检测费，没有其它费用，同时预约享受预约通道，免排队，在预约时段随到随检，为车主节约时间、减省费用。审车请点预约审车，关心您，更关心您的车生活。审车流程：回复 1 ，审车注意事项：回复 2");
		}
		return text;
	}

	/**
	 * @category 根据被动回复文本消息数据格式组装成xml返回给用户
	 * @param requestMap
	 * @param msgReply
	 * @return
	 */
	public String generateTextXml(Map<String, String> requestMap,MsgReply msgReply) {
		
		String xml="<xml>\r\n" + 
				"<ToUserName><![CDATA["+requestMap.get("FromUserName")+"]]></ToUserName>\r\n" + 
				"<FromUserName><![CDATA["+requestMap.get("ToUserName")+"]]></FromUserName>\r\n" + 
				"<CreateTime>"+requestMap.get("CreateTime")+"</CreateTime>\r\n" + 
				"<MsgType><![CDATA[text]]></MsgType>\r\n" + 
				"<Content><![CDATA["+msgReply.getContent()+"]]></Content>\r\n" + 
				"</xml>";
		return xml;
	}
	
	/**
	 * @category 按照被动回复图片信息数据格式组装成xml
	 * @param requestMap
	 * @param mediaId
	 * @return
	 */
	public String generatePicMsgReplyXml(Map<String, String> requestMap, String mediaId) {
		 String xml = "<xml>\r\n" + 
		 		"<ToUserName><![CDATA["+requestMap.get("FromUserName")+"]]></ToUserName>\r\n" + 
		 		"<FromUserName><![CDATA["+requestMap.get("ToUserName")+"]]></FromUserName>\r\n" + 
		 		"<CreateTime>"+requestMap.get("CreateTime")+"</CreateTime>\r\n" + 
		 		"<MsgType><![CDATA[image]]></MsgType>\r\n" + 
		 		"<Image>\r\n" + 
		 		"<MediaId><![CDATA["+mediaId+"]]></MediaId>\r\n" + 
		 		"</Image>\r\n" + 
		 		"</xml>";
         return xml;
	}
	
	/**
	 * @category 根据图文消息数据格式组装成xml返回给用户
	 * @param requestMap
	 * @param msgReply
	 * @return
	 */
	public String  generateTextPicMsgXml(Map<String, String> requestMap,MsgReply msgReply) {
		String xml="<xml>\r\n" + 
				"<ToUserName><![CDATA["+requestMap.get("FromUserName")+"]]></ToUserName>\r\n" + 
				"<FromUserName><![CDATA["+requestMap.get("ToUserName")+"]]></FromUserName>\r\n" + 
				"<CreateTime>"+requestMap.get("CreateTime")+"</CreateTime>\r\n" + 
				"<MsgType><![CDATA[news]]></MsgType>\r\n" + 
				"<ArticleCount>1</ArticleCount>\r\n" + 
				"<Articles>\r\n" + 
				"<item>\r\n" + 
				"<Title><![CDATA["+msgReply.getTitle()+"]]></Title>\r\n" + 
				"<Description><![CDATA["+msgReply.getDescription()+"]]></Description>\r\n" + 
				"<PicUrl><![CDATA["+msgReply.getPicUrl()+"]]></PicUrl>\r\n" + 
				"<Url><![CDATA["+msgReply.getClickUrl()+"]]></Url>\r\n" + 
				"</item>\r\n" + 
				"</Articles>\r\n" + 
				"</xml>";
		return xml;
	}

	// 处理用户关注事件
	private BaseMessage dealEvent(Map<String, String> requestMap) {
		String event = requestMap.get("Event");
		TextMessage textMessage = null;
		switch (event) {
		case "subscribe":
			wxService.getUserInfo(requestMap.get("FromUserName"));
			textMessage = new TextMessage(requestMap,
					"欢迎关注毕节快捷审车帮！审车帮是由毕节市飞弘网络科技有限公司运营的Ｏ2Ｏ微信预约审车平台，通过本平台预约审车是车主与检测站客服直接联系对接，没有二拐，没有中间商，审车费用就是检测站的上线检测费，没有其它费用，同时预约享受预约通道，免排队，在预约时段随到随检，为车主节约时间、减省费用。审车请点预约审车，关心您，更关心您的车生活。审车流程：回复 1 ，审车注意事项：回复 2");
			break;
		case "unsubscribe":
			textMessage = new TextMessage(requestMap, "我会在这永远等着你回来！谢谢");
			break;
		case "CLICK":
			textMessage = new TextMessage(requestMap, "该功能正在开发中~");
			break;
		default:
			break;
		}
		return textMessage;
	}

	/**
	 * 用户授权事件
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/yuYueShouQuan", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String shouQuan(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> parameter) throws Exception {

		String backUrl = "https://www.kjscb.com/wx/callBack";
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Util.APPID + "&redirect_uri="
				+ URLEncoder.encode(backUrl, "utf-8") + "&response_type=code" + "&scope=snsapi_base"
				+ "&state=1#wechat_redirect";

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg", "success");
		jsonObject.put("url", url);

		return jsonObject.toString();
	}

	@RequestMapping(value = "/callBack")
	public String Callback(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String code = request.getParameter("code");
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Util.APPID + "&secret="
				+ Util.APPSECRET + "&code=" + code + "&grant_type=authorization_code";
		try {
			JSONObject json = AuthUtil.doGetJson(url);
			if (json != null && json.containsKey("openid")) {
				String orderUserId = json.getString("openid");
				session.setAttribute("oppind", orderUserId);
			}
			return "weixinweb/index";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 
	 * 预约功能
	 */
	@RequestMapping(value = "/saveOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String saveOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> parameter) throws Exception {

		// 预约人的微信号
		String orderUserId = parameter.get("openid");
		// 图片路径
		String carLicence = parameter.get("imagePath");

		String carOwnerName = URLDecoder.decode(parameter.get("carOwnerName"), "UTF-8");
		String carOwnerMobile = URLDecoder.decode(parameter.get("carOwnerMobile"), "UTF-8");
		String carCode = URLDecoder.decode(parameter.get("carCode"), "UTF-8");
		String orderProject = URLDecoder.decode(parameter.get("carType"), "UTF-8");
		String checkStationName = URLDecoder.decode(parameter.get("checkStationName"), "UTF-8");
		String orderDate = parameter.get("orderDate");
		String orderTime = URLDecoder.decode(parameter.get("orderTime"), "UTF-8");
		// 不改写之前的表结构，将日期和时间拼接成表之前所存储的数据格式
		String orderDateTime = orderDate + ";" + orderTime;
		// String[] orderTD = orderDateTime.split(";");
		// String orderTime = orderTD[1];
		// String orderDate = orderTD[0];
		String carInsurance = URLDecoder.decode(parameter.get("carInsurance"), "UTF-8");

		Order order = new Order();

		order.setCarLicence(carLicence);
		order.setCarOwnerMobile(carOwnerMobile);
		order.setCarOwnerName(carOwnerName);
		order.setCarCode(carCode);
		order.setOrderProject(orderProject);
		order.setCheckStationName(checkStationName);
		order.setOrderTime(orderDateTime);
		order.setCarInsurance(carInsurance);
		order.setIsRefund("否");
		order.setTypeState("已预约");
		order.setInsertTime((new Date()).getTime());
		order.setOrderUserId(orderUserId);

		// 保存订单信息
		orderService.saveOrder(order);
		// 更新时间段及人数表的次数
		timeOrderCountService.updateOrderCount(orderTime, orderDate, checkStationName);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg", "success");

		// 根据检查站名称查询出检查站，目的是获取检查站的经纬度
		CheckStation checkStation = checkStationService.getCheckStationByName(checkStationName);

		// 发送给预约人
		TemplateMessageManager.sendTemplateMessage(orderUserId, carOwnerName, orderDateTime,
				checkStation.getCheckStationX() + "," + checkStation.getCheckStationY(), checkStationName, "审车");

		// 发送给区域管理员
		InnerUser innerUser = orderService.selectAdminByStationName(checkStationName);

		if (null != innerUser) {
			TemplateMessageManager.sendAdminTemplateMessage(innerUser.getInnerUserId(), carOwnerName, checkStationName);
		}

		// 保存客户预约记录及客户昵称
		savenickname(orderUserId, checkStationName, carOwnerName, carOwnerMobile);

		return jsonObject.toString();

	}

	// 保存客户预约记录及客户昵称
	public void savenickname(String orderUserId, String checkStationName, String carOwnerName, String carOwnerMobile) {
		CustomerInfo cInfo = customerInfoService.selectNickName(orderUserId);
		if (cInfo != null) {
			OrderNickName oNickName = new OrderNickName();
			oNickName.setOrderUserId(orderUserId);
			oNickName.setOrderUserNickName(cInfo.getCustomerNickName());
			oNickName.setCustomerPic(cInfo.getCustomerPic());
			oNickName.setCarOwnerMobile(carOwnerMobile);
			oNickName.setCarOwnerName(carOwnerName);
			oNickName.setCheckStationName(checkStationName);
			oNickName.setInsertTime((new Date()).getTime());
			orderNickNameService.saveObj(oNickName);
		}
	}

	/**
	 * @category 上传图片接口
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/upimg", produces = "application/json; charset=utf-8")
	public @ResponseBody String uploadImg2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		File imageFileParentPath = getFile(request);
		long currentTime = System.currentTimeMillis();
		File imageFile = new File(imageFileParentPath.getAbsolutePath() + "\\" + currentTime + ".png");
		String imgUrl = Util.UPLOAD_URL + currentTime + ".png";
		String resultStr = request.getParameter("image").toString();// 前端传来的是压缩后的并用base64编码后的字符串
		resultStr = resultStr.substring(resultStr.indexOf(",") + 1);// 需要去掉头部信息，这很重要
		FileOutputStream outputStream = new FileOutputStream(imageFile);// 创建输出流
		BASE64Decoder base64Decoder = new BASE64Decoder();// 获得一个图片文件流，我这里是从flex中传过来的
		byte[] result = base64Decoder.decodeBuffer(resultStr);// 解码
		for (int i = 0; i < result.length; ++i) {
			if (result[i] < 0) {// 调整异常数据
				result[i] += 256;
			}
		}
		outputStream.write(result);
		outputStream.flush();
		outputStream.close();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("mse", "success");
		jsonObject.put("path", imgUrl);
		
		return jsonObject.toString();
	}

	/**
	 * @category 获取上传图片的路径及文件对象，图片统一放在imgdept
	 * @param request
	 * @return
	 */
	public File getFile(HttpServletRequest request) {
		String rootPath = request.getSession().getServletContext().getRealPath("");
		String contextPath = request.getContextPath();
		String contextPathReplace = contextPath.replaceAll("/", "");
		String rootPathNew = rootPath.replace("\\" + contextPathReplace, "");

		File imageFileParentPath = new File(rootPathNew + "imgdept\\");
		if (!imageFileParentPath.exists()) {
			imageFileParentPath.mkdirs();
		}
		return imageFileParentPath;
	}
	
	/**
	 * @category 上传图片到微信服务器接口：
	 * 			  1、被动回复消息-需要上传图片到微信服务器
	 * 			  2、另外上传的图片保存到本地	
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/uploadPicToWx", produces = "application/json; charset=utf-8")
	public @ResponseBody String uploadPicToWx(HttpServletRequest request, HttpServletResponse response) throws Exception {
		File imageFileParentPath = getFile(request);
		long currentTime = System.currentTimeMillis();
		File imageFile = new File(imageFileParentPath.getAbsolutePath() + "\\" + currentTime + ".png");
		String imgUrl = Util.UPLOAD_URL + currentTime + ".png";
		String resultStr = request.getParameter("image").toString();// 前端传来的是压缩后的并用base64编码后的字符串
		resultStr = resultStr.substring(resultStr.indexOf(",") + 1);// 需要去掉头部信息，这很重要
		FileOutputStream outputStream = new FileOutputStream(imageFile);
		BASE64Decoder base64Decoder = new BASE64Decoder();
		byte[] result = base64Decoder.decodeBuffer(resultStr);// 解码
		for (int i = 0; i < result.length; ++i) {
			if (result[i] < 0) {// 调整异常数据
				result[i] += 256;
			}
		}
		outputStream.write(result);
		outputStream.flush();
		outputStream.close();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("mse", "success");
		jsonObject.put("path", imgUrl);

		// fix me 上传到微信服务器
		String uploadUrl = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=" + Util.getAccessToken()
				+ "&type=image";
		String mediaId = null;
		try {
			mediaId = upload(uploadUrl, imageFile, null, null);
			jsonObject.put("mediaId", mediaId);
			jsonObject.put("status", "1");
			jsonObject.put("message", "获取mediaId成功");
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("status", "0");
			jsonObject.put("message", "获取mediaId失败");
		}

		return jsonObject.toString();
	}
	
	/**
	 * @category 上传图片到微信服务器，调用素材临时接口
	 * @param url
	 * @param filePath
	 * @param accessToken
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public String upload(String url, File file, String accessToken, String type) throws Exception {
		URL urlObj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);

		// 设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");

		// 设置边界
		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");

		byte[] head = sb.toString().getBytes("utf-8");

		// 获得输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());
		// 输出表头
		out.write(head);

		// 文件正文部分
		// 把文件已流文件的方式 推入到url中
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();

		// 结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线

		out.write(foot);
		out.flush();
		out.close();

		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		try {
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		JSONObject jsonObj = JSONObject.fromObject(result);
		return jsonObj.getString("media_id");
	}
}
