package com.bj.scb.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @Title: 转型、判空、格式化操作
 * @Description:
 * @Author:fengjianshe
 * @Since:2013年8月26日
 * @Version:1.1.0
 */
public class StringUtil {

	private static Map<String, Object> dataMap = new HashMap<String, Object>();

	public static void initMap(Map<String, Object> paramMap) {
		dataMap = paramMap;
	}

	/**
	 * 判断是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(Object obj) {

		if (null == obj || "".equals(obj.toString().trim()) || obj.toString().toUpperCase().equals("NULL")) {
			return true;
		}
		return false;

	}

	public static String getPercent(double d) {

		return getDouble1(d * 100) + "%";
	}

	public static byte getByte(Object obj) {
		int i = getInt(obj);
		if (-128 < i && i < 127) {
			return (byte) i;
		}
		return (byte) 0;
	}

	/**
	 * 获取整数
	 * 
	 * @param obj
	 * @return
	 */
	public static float getFloat(Object obj) {

		if (null == obj || "".equals(obj.toString().trim()) || obj.toString().toUpperCase().equals("NULL")) {
			return 0;
		}
		try {
			return ((int) (Float.valueOf(obj.toString()) * 100)) / (float) 100;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 获取整数
	 * 
	 * @param obj
	 * @return
	 */
	public static float getFloatM(Object obj) {

		if (null == obj || "".equals(obj.toString().trim()) || obj.toString().toUpperCase().equals("NULL")) {
			return 0;
		}
		try {
			if (dataMap == null) {
				throw new NullPointerException("dataMap is null");
			}
			return getFloat(dataMap.get(obj));
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 获取浮点数: 例子: 104 / 0.7 普通计算结果为:72.7995898 ,而通过本方法处理后会得出精确的值:72.8
	 * 
	 * @param f
	 * @return
	 */
	public static float getFloat2(float f) {

		return new Float(new DecimalFormat("#.##").format(f)).floatValue();

	}

	/**
	 * 获取浮点数: 例子: 104 / 0.7 普通计算结果为:72.7495898 ,而通过本方法处理后会得出精确的值:72.8
	 * 
	 * @param f
	 * @return
	 */
	public static float getFloat1(float f) {

		return new Float(new DecimalFormat("#.#").format(f)).floatValue();

	}

	public static double getDouble1(Object obj) {

		if (obj != null && obj.toString().trim() != "" && obj.toString().trim().toUpperCase() != "NULL") {
			return getDouble(Double.valueOf(obj.toString()));
		} else {
			return 0.0;
		}
	}

	/**
	 * 获取Double
	 * 
	 * @param obj
	 * @return
	 */
	public static double getDoubleM(Object obj) {

		if (obj != null && obj.toString().trim() != "" && obj.toString().trim().toUpperCase() != "NULL") {
			try {
				if (dataMap == null) {
					throw new NullPointerException("dataMap is null");
				}
				return getDouble1(dataMap.get(obj));
			} catch (Exception e) {
				return 0;
			}
		} else {
			return 0.0;
		}
	}

	/**
	 * 获取浮点数: 例子: 104 / 0.7 普通计算结果为:72.7495898 ,而通过本方法处理后会得出精确的值:72.8
	 * 
	 * @param f
	 * @return
	 */
	public static double getDouble(double f) {

		return (new Double(new DecimalFormat("#.##").format(f))).doubleValue();

	}

	/**
	 * 获取整数
	 * 
	 * @param obj
	 * @return
	 */
	public static int getInt(Object obj) {

		if (null == obj || "".equals(obj.toString().trim()) || obj.toString().toUpperCase().equals("NULL")) {
			return 0;
		}
		try {
			Double d = new Double(obj.toString());
			return d.intValue();
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 获取整数
	 * 
	 * @param obj
	 * @return
	 */
	public static int getIntM(Object obj) {

		if (null == obj || "".equals(obj.toString().trim()) || obj.toString().toUpperCase().equals("NULL")) {
			return 0;
		}
		try {
			if (dataMap == null) {
				throw new NullPointerException("dataMap is null");
			}
			return getInt(dataMap.get(obj));
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 获取整数
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean getBoolean(Object obj) {
		if (null == obj || "".equals(obj.toString().trim()) || obj.toString().toUpperCase().equals("NULL")) {
			return false;
		}
		try {
			if (getInt(obj) == 1 || obj.toString().equalsIgnoreCase("true")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获取整数
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean getBooleanM(Object obj) {
		if (null == obj || "".equals(obj.toString().trim()) || obj.toString().toUpperCase().equals("NULL")) {
			return false;
		}
		try {
			if (dataMap == null) {
				throw new NullPointerException("dataMap is null");
			}
			return getBoolean(dataMap.get(obj));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获取字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String getString(Object obj) {
		if (null == obj || "".equals(obj.toString().trim()) || obj.toString().toUpperCase().equals("NULL")) {
			return "";
		}
		try {
			return obj.toString().trim();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获取长整数
	 * 
	 * @param obj
	 * @return
	 */
	public static long getLong(Object obj) {

		if (null == obj || "".equals(obj.toString().trim()) || obj.toString().toUpperCase().equals("NULL")) {
			return 0;
		}
		try {
			return Long.valueOf(obj.toString());
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 获取字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String getStringM(Object obj) {
		if (null == obj || "".equals(obj.toString().trim()) || obj.toString().toUpperCase().equals("NULL")) {
			return "";
		}
		try {
			if (dataMap == null) {
				throw new NullPointerException("dataMap is null");
			}
			return getString(dataMap.get(obj));
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获取utf8编码字符串
	 * 
	 * @param s
	 * @return
	 */
	public static String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Utf8URL解码
	 * 
	 * @param text
	 * @return
	 */
	public static String Utf8URLdecode(String text) {
		String result = "";
		int p = 0;
		if (text != null && text.length() > 0) {
			text = text.toLowerCase();
			p = text.indexOf("%e");
			if (p == -1)
				return text;
			while (p != -1) {
				result += text.substring(0, p);
				text = text.substring(p, text.length());
				if (text == "" || text.length() < 9)
					return result;
				result += CodeToWord(text.substring(0, 9));
				text = text.substring(9, text.length());
				p = text.indexOf("%e");
			}
		}
		return result + text;
	}

	/**
	 * 编码是否有效
	 * 
	 * @param text
	 * @return
	 */
	private static boolean Utf8codeCheck(String text) {
		String sign = "";
		if (text.startsWith("%e"))
			for (int i = 0, p = 0; p != -1; i++) {
				p = text.indexOf("%", p);
				if (p != -1)
					p++;
				sign += p;
			}
		return sign.equals("147-1");
	}

	/**
	 * 是否Utf8Url编码
	 * 
	 * @param text
	 * @return
	 */
	public boolean isUtf8Url(String text) {
		text = text.toLowerCase();
		int p = text.indexOf("%");
		if (p != -1 && text.length() - p > 9) {
			text = text.substring(p, p + 9);
		}
		return Utf8codeCheck(text);
	}

	/**
	 * utf8URL编码转字符
	 * 
	 * @param text
	 * @return
	 */
	private static String CodeToWord(String text) {
		String result;
		if (Utf8codeCheck(text)) {
			byte[] code = new byte[3];
			code[0] = (byte) (Integer.parseInt(text.substring(1, 3), 16) - 256);
			code[1] = (byte) (Integer.parseInt(text.substring(4, 6), 16) - 256);
			code[2] = (byte) (Integer.parseInt(text.substring(7, 9), 16) - 256);
			try {
				result = new String(code, "UTF-8");
			} catch (UnsupportedEncodingException ex) {
				result = null;
			}
		} else {
			result = text;
		}
		return result;
	}

	public static String format(String message, Object[] words) {
		if (words != null && words.length > 0 && message instanceof String) {
			message = MessageFormat.format((String) message, words);
		}
		return message;
	}

	/**
	 * servlet
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String toStringUTF8(String str) throws UnsupportedEncodingException {
		return new String(str.getBytes("ISO-8859-1"), "UTF-8");
	}

	/**
	 * 时间格式转换 <> 取得当前系统时间
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = sdf.format(date);
		return str;
	}

	/**
	 * 判断是否是手机号码
	 * 
	 * @param mobile
	 * @return
	 * @Description:
	 */
	public static boolean isMobileNo(String mobile) {
		boolean isTrue = false;
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[6-8])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobile);
		isTrue = m.matches();
		return isTrue;
	}

	/**
	 * 截取后几位的字符串
	 * 
	 * @param str
	 * @param lenth
	 * @return
	 * @Description:
	 */
	public static String cutBehindStr(String str, int lenth) {
		return str.substring(str.length() - lenth);
	}

	/**
	 * 生成指定位数数字随机数
	 * 
	 * @return
	 * @Description:
	 */
	public static String getRandomDigitStr(int count) {
		Random rd = new Random();
		String n = "";
		int getNum;
		do {
			getNum = Math.abs(rd.nextInt()) % 10 + 48;// 产生数字0-9的随机数
			// getNum = Math.abs(rd.nextInt())%26 + 97;//产生字母a-z的随机数
			char num1 = (char) getNum;
			String dn = Character.toString(num1);
			n += dn;
		} while (n.length() < count);
		return n;
	}

	/**
	 * (单位：米)转算成经纬度 纬度1度 = 大约111km 纬度1分 = 大约1.85km 纬度1秒 = 大约30.9m 经度xpoint =
	 * 116.34(单位：°) 纬度ypoint = 24.52(单位：°)
	 * 
	 * @return
	 * @Description:
	 */
	public static double ConvertXYpoint(Object obj) {
		if (null == obj || "".equals(obj.toString().trim()) || obj.toString().toUpperCase().equals("NULL")) {

			return 0;

		} else {
			Double d = new Double(obj.toString());
			return d / 111 / 1000;
		}

	}

	/**
	 * 判断文件是否为图片<br>
	 * <br>
	 * 
	 * @param pInput    文件名<br>
	 * @param pImgeFlag 判断具体文件类型<br>
	 * @return 检查后的结果<br>
	 * @throws Exception
	 */
	public static boolean isPicture(String pInput) throws Exception {
		// 文件名称为空的场合
		if (isNullOrEmpty(pInput)) {
			// 返回不和合法
			return false;
		}
		// 获得文件后缀名
		String tmpName = pInput.substring(pInput.lastIndexOf(".") + 1, pInput.length());
		// 声明图片后缀名数组
		String imgeArray[] = { "bmp", "gif", "jpe", "jpg", "jpeg", "png" };

		for (int i = 0; i < imgeArray.length; i++) {
			if (tmpName.equals(imgeArray[i])) {
				return true;
			}
		}

		return false;
	}

	public static String removeBlankAndEnter(String content) {
		String dest = "";
		if (content != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(content);
			dest = m.replaceAll("");
		}
		return dest;
	}

	public static String sub(String str, int bi, int ei) {

		return str.substring(bi, ei);
	}

	/**
	 * 验证邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 验证邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkAccount(String account) {
		boolean flag = false;
		try {
			String check = "^[A-Za-z0-9]{6,20}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(account);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	public static String mul(String value1, String value2) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		NumberFormat integer = NumberFormat.getIntegerInstance();
		String str = integer.format(b1.multiply(b2).doubleValue());// 如果带小数会四舍五入到整数12,343,172
		int index=str.indexOf(",");
		if (index >0) {
			return str.replace(",", "");
		}
		return str;
	}
}
