package wx;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.bj.scb.conf.WxPayConfig;
import com.bj.scb.pay.OurWxPayConfig;
import com.bj.scb.pay.WXPay;
import com.bj.scb.pay.WXPayConstants;
import com.bj.scb.pay.WXPayUtil;
import com.bj.scb.utils.WeixinUtil;
import com.bj.scb.weixin.Util;

public class WXPayExample {

	public static void main(String[] args) throws Exception {
		// testXml();
		//testXml();
		int max=10000,min=1;
		int ran2 = (int) (Math.random()*(max-min)+min); 
		System.out.println("20181225" + ran2);
		
		testFile();
		
		
	}

	public static void testFile() throws IOException{
		//创建文件：
		//File file = new File("D:/1/1.txt");
		//System.out.println(file.createNewFile());//注意：文件所在路径(在这里的路径指：D:/1)必须存在  才能创建文件(1.txt)！！
		
		//创建文件夹：
		File file2 = new File("E:\\apache-tomcat-8.5.16-windows-x64\\apache-tomcat-8.5.16\\webapps\\imgdept\\");
		//file2.mkdir();//如果没有父路径，不会报错，但不会创建文件夹
		//file2.mkdirs();//如果父路径不存在，会自动先创建路径所需的文件夹
 
		if(!file2.exists()){
			file2.mkdirs();//如果父路径不存在，会自动先创建路径所需的文件夹
		}
		
		File file = new File(file2.getAbsolutePath() + "\\ssss.png");
		
		if(!file.isFile()) {
			file.createNewFile();
		}
		
		//判断文件是否存在以及文件类型
        //File file3 = new File("D:/1/新建文件夹");
        File file3 = new File("D:/1/1.txt");
        System.out.println(file3.exists());//判断路径D:/1下是否存在该文件1.txt 或  新建文件夹
        System.out.println(file3.isDirectory());//判断file3对象指向的路径是否是目录（在这里就是判断D:/1下的 新建文件夹 是否是文件夹  是就返回true）
        System.out.println(file3.isFile());//判断路径D:/1下的1.txt是否是文件类型
	}
	
	public static void testDemo() throws Exception {

		OurWxPayConfig config = new OurWxPayConfig();
		WXPay wxpay = new WXPay(config);

		Map<String, String> data = new HashMap<String, String>();
		data.put("body", "腾讯充值中心-QQ会员充值");
		data.put("out_trade_no", "2016090910595900000012");
		data.put("device_info", "");
		data.put("fee_type", "CNY");
		data.put("total_fee", "1");
		data.put("spbill_create_ip", "123.12.12.123");
		data.put("notify_url", "https://www.kjscb.com/wx/payment/webchat/notifyUrl");
		data.put("trade_type", "JSAPI"); // 此处指定为扫码支付
		data.put("product_id", "12");

		String xml = WXPayUtil.generateSignedXml(data, Util.KEY);
		boolean is = WXPayUtil.isSignatureValid(data, Util.KEY, WXPayConstants.SignType.MD5);
		System.out.println(xml);
		System.out.println(is);
		try {
			// Map<String, String> resp = wxpay.unifiedOrder(data);
			// System.out.println(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testXml() throws Exception {
		// 根据orderId 去获取金额和商品名称，这里我直接写死
		String body = "审车定金";// 商品名
		// ---------获取调用预支付相关参数
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appid", WxPayConfig.APPID);// appid
		paramMap.put("mch_id", WxPayConfig.MCHID);// 商户号
		paramMap.put("nonce_str", WeixinUtil.createNoncestr(32));// 随机字符串
		paramMap.put("body", body);// 商品描述
		// 商户订单号：需唯一 (一定要注意，看自己设计，支付id和订单id关联)
		// 注：我们这里用orderId,按道理是需要支付id，
		// 也就是每次按支付都要生成不一样的id,防止支付错误
		UUID uuid = UUID.randomUUID();
		System.out.println(uuid);
		paramMap.put("out_trade_no", uuid.toString());// orderId
		paramMap.put("total_fee", "1");// 金额 -- 实际金额需乘以 100
		paramMap.put("spbill_create_ip", "123.12.12.123");// 客户ip地址
		paramMap.put("notify_url", WxPayConfig.NOTIFY_URL);// 回调地址
		paramMap.put("trade_type", "JSAPI");// 交易类型
		paramMap.put("openid", "o0lMawW5y22d-DhE9Y2BFKn3HOFc");// 用户openid
		// paramMap.put("sign", WeixinUtil.getSign(paramMap));//签名 ，默认sign_type是MD5
		String xml = WXPayUtil.generateSignedXml(paramMap, Util.KEY);
		boolean is = WXPayUtil.isSignatureValid(paramMap, Util.KEY, WXPayConstants.SignType.MD5);
		System.out.println("WXPayUtil.generateSignedXml生成xml：" + xml);
		System.out.println(is);
	}

}
