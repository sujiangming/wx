package com.bj.scb.pay;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bj.scb.utils.WeixinUtil;

@Controller
public class OurWxPayController {
	
	@RequestMapping("/pay")
	public Map<String, String> wxPayFunction(HttpServletRequest request,HttpServletResponse response) throws Exception {
		OurWxpayParam wxpayParam = new OurWxpayParam();
		String notifyUrl = "https://www.kjscb.com/wx/payment/webchat/notifyUrl"; // 我这里的回调地址是随便写的，到时候需要换成处理业务的回调接口
		OurWxPayConfig ourWxPayConfig = new OurWxPayConfig();
		WXPay wxPay = new WXPay(ourWxPayConfig);
		// 根据微信支付api来设置
		Map<String, String> data = new HashMap<>();
		data.put("appid", ourWxPayConfig.getAppID());
		data.put("mch_id", ourWxPayConfig.getMchID()); // 商户号
		data.put("trade_type", "JSAPI"); // 支付场景 APP 微信app支付 JSAPI 公众号支付 NATIVE 扫码支付
		data.put("notify_url", notifyUrl); // 回调地址
		data.put("spbill_create_ip", WeixinUtil.getRemoteHost(request)); // 终端ip
		data.put("total_fee", wxpayParam.getTotalFee()); // 订单总金额
		data.put("fee_type", "CNY"); // 默认人民币
		data.put("out_trade_no", wxpayParam.getOutTradeNo()); // 交易号
		data.put("body", wxpayParam.getBody());
		data.put("nonce_str", "bfrhncjkfdkfd"); // 随机字符串小于32位
		String s = WXPayUtil.generateSignature(data, ourWxPayConfig.getKey()); // 签名
		data.put("sign", s);

		/** wxPay.unifiedOrder 这个方法中调用微信统一下单接口 */
		Map<String, String> respData = wxPay.unifiedOrder(data);
		if (respData.get("return_code").equals("SUCCESS")) {
			// 返回给APP端的参数，APP端再调起支付接口
			Map<String, String> repData = new HashMap<>();
			repData.put("appid", ourWxPayConfig.getAppID());
			repData.put("mch_id", ourWxPayConfig.getMchID());
			repData.put("prepayid", respData.get("prepay_id"));
			repData.put("package", "WXPay");
			repData.put("noncestr", respData.get("nonce_str"));
			repData.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
			String sign = WXPayUtil.generateSignature(repData, ourWxPayConfig.getKey()); // 签名
			respData.put("sign", sign);
			respData.put("timestamp", repData.get("timestamp"));
			respData.put("package", "WXPay");
			return respData;
		}
		throw new Exception(respData.get("return_msg"));
	}
}
