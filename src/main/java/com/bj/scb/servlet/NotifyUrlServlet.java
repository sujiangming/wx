package com.bj.scb.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.bj.scb.pay.WXPayUtil;
import com.bj.scb.weixin.Util;

/**
 * 支付回调 ClassName: NotifyUrlServlet date: 2017年10月24日 上午11:49:06
 *
 * @author sqq
 * @version
 * @since JDK 1.8
 */
@WebServlet("/payment/webchat/notifyUrl")
public class NotifyUrlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static org.apache.log4j.Logger log = Logger.getLogger(NotifyUrlServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletInputStream in = request.getInputStream();
		int len = -1;
		byte[] b = new byte[1024];
		String str = "";
		while ((len = in.read(b)) != -1)
			str += new String(b, 0, len);
		in.close();
		if (null != str && !"".equals(str)) {
			try {
				log.debug("付费参数：" + str);
				Map<String, String> map = WXPayUtil.xmlToMap(str);
				boolean isPay = WXPayUtil.isSignatureValid(map, Util.KEY);
				log.debug(isPay);
				if (isPay) {
					String returnCode = map.get("return_code");
					String resultCode = map.get("result_code");
					if ("SUCCESS".equals(resultCode) && "SUCCESS".equals(returnCode)) {
						String xml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
						log.debug("付费参数：xml:" + xml);
						response.getWriter().write(xml);
						request.getSession().setAttribute("resultCode", resultCode);
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

		}

	}

}
