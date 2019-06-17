package com.bj.scb.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.bj.scb.conf.WxPayConfig;
import com.bj.scb.pay.WXPayUtil;
import com.bj.scb.utils.HttpInvoker;
import com.bj.scb.utils.StringUtil;
import com.bj.scb.utils.WeixinUtil;
import com.bj.scb.weixin.Util;

import net.sf.json.JSONObject;

/**
 * 统一下单 (预支付) ClassName: UnifiedOrderServlet date: 2017年10月24日 上午11:47:14
 *
 * @author sqq
 * @version
 * @since JDK 1.8
 */
@WebServlet("/unifiedOrder")
public class UnifiedOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static org.apache.log4j.Logger log = Logger.getLogger(NotifyUrlServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String openId = request.getParameter("openId");
		String refund = request.getParameter("refund");

		log.debug(UnifiedOrderServlet.class.getName() + ",openId:" + openId + ",refund：" + refund);
		
		
		Map<String, Object> rsMap = new HashMap<String, Object>();
		PrintWriter out = response.getWriter();
		try {
			rsMap.put("status", 0);// 默认出错
			// 必要参数请自行判断
			if (StringUtil.isNullOrEmpty(openId)) {
				throw new Exception("openId为空");
			}
			// 根据orderId 去获取金额和商品名称，这里我直接写死
			String body = "预约定金";// 商品名
			// ---------获取调用预支付相关参数
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("appid", WxPayConfig.APPID);// appid
			paramMap.put("mch_id", WxPayConfig.MCHID);// 商户号
			paramMap.put("nonce_str", WeixinUtil.createNoncestr(32));// 随机字符串
			paramMap.put("body", body);// 商品描述
			// 商户订单号：需唯一 (一定要注意，看自己设计，支付id和订单id关联)
			// 注：我们这里用orderId,按道理是需要支付id，
			// 也就是每次按支付都要生成不一样的id,防止支付错误
			int max = 1000, min = 1;
			int ran2 = (int) (Math.random() * (max - min) + min);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String dateString = sdf.format(new Date());
			paramMap.put("out_trade_no", dateString + ran2);// orderId
			// Integer.toString(Integer.parseInt(refund)*100)
			paramMap.put("total_fee", StringUtil.mul(refund, "100"));// 金额 -- 实际金额需乘以 100
			paramMap.put("spbill_create_ip", WeixinUtil.getRemoteHost(request));// 客户ip地址
			paramMap.put("notify_url", WxPayConfig.NOTIFY_URL);// 回调地址
			paramMap.put("trade_type", "JSAPI");// 交易类型
			paramMap.put("openid", openId);// 用户openid
			String xml = WXPayUtil.generateSignedXml(paramMap, Util.KEY);// 签名 ，默认sign_type是MD5

			log.debug(UnifiedOrderServlet.class.getName() + ",签名xml:" + xml);
			
			String unifiedStr = HttpInvoker.sendPostRequest(WxPayConfig.UNIFIED_ORDER, xml);
			Map<String, String> preMap = WXPayUtil.xmlToMap(unifiedStr);
			String prepayId = preMap.get("prepay_id");

			if (StringUtil.isNullOrEmpty(prepayId)) {
				throw new Exception("获取prepay_id错误:" + preMap.get("return_msg"));
			}
			// 返回参数
			Map<String, String> jsApiMap = new HashMap<String, String>();
			jsApiMap.put("appId", WxPayConfig.APPID);
			jsApiMap.put("timeStamp", WeixinUtil.getTimestamp(new Date()) + "");
			jsApiMap.put("nonceStr", WeixinUtil.createNoncestr(32));
			jsApiMap.put("package", "prepay_id=" + prepayId);
			jsApiMap.put("signType", "MD5");
			jsApiMap.put("paySign", WXPayUtil.generateSignature(jsApiMap, Util.KEY));// hhhhhhhhhh
			rsMap.put("status", 1);// 成功
			rsMap.put("param", JSONObject.fromObject(jsApiMap));
			String json = JSONObject.fromObject(rsMap).toString();
			
			log.debug(UnifiedOrderServlet.class.getName() + ",返回参数json:" + json);
			
			out.print(json);// 发送给前端
		} catch (Exception e) {
			e.getStackTrace();
			rsMap.put("msg", e);
			out.print(rsMap);
		} finally {
			out.close();
			out = null;
		}
	}

}
