package wx;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 工作日计算工具类<br/>
 * 目前仅支持2017,2018年
 * 
 * @author Corvey
 * @Date 2018年11月9日16:53:52
 */
public class WorkdayUtils {

	/** 预设工作日数据的开始年份 */
	private static final int START_YEAR = 2017;

	/** 预设工作日数据的结束年份 */
	private static final int END_YEAR = 2018;

	/** 起始日期处理策略 */
	private static final BoundaryDateHandlingStrategy START_DATE_HANDLING_STRATEGY = date -> {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY) < 12; // 如果开始时间在中午12点前，则当天也算作一天，否则不算
	};

	/** 结束日期处理策略 */
	private static final BoundaryDateHandlingStrategy END_DATE_HANDLING_STRATEGY = date -> {
		return true; // 结束时间无论几点，都算作1天
	};

	/** 工作日map，true为补休，false为放假 */
	private static final Map<Integer, Boolean> WORKDAY_MAP = new HashMap<>();

	private static final SegmentTree SEGMENT_TREE;

	static {
		initWorkday(); // 初始化工作日map

		// 计算从START_YEAR到END_YEAR一共有多少天
		int totalDays = 0;
		for (int year = START_YEAR; year <= END_YEAR; ++year) {
			totalDays += getDaysOfYear(year);
		}
		int[] workdayArray = new int[totalDays]; // 将工作日的数据存入到数组
		Calendar calendar = new GregorianCalendar(START_YEAR, 0, 1);
		for (int i = 0; i < totalDays; ++i) {
			// 将日期转为yyyyMMdd格式的int
			int datestamp = calendar.get(Calendar.YEAR) * 10000 + (calendar.get(Calendar.MONTH) + 1) * 100
					+ calendar.get(Calendar.DAY_OF_MONTH);
			Boolean isWorkDay = WORKDAY_MAP.get(datestamp);
			if (isWorkDay != null) { // 如果在工作日map里有记录，则按此判断工作日
				workdayArray[i] = isWorkDay ? 1 : 0;
			} else { // 如果在工作日map里没记录，则按是否为周末判断工作日
				int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
				workdayArray[i] = (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) ? 1 : 0;
			}
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		SEGMENT_TREE = new SegmentTree(workdayArray); // 生成线段树
	}

	/**
	 * 计算两个日期之间有多少个工作日<br/>
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int howManyWorkday(Date startDate, Date endDate) {
		if (startDate.after(endDate)) {
			return howManyWorkday(endDate, startDate);
		}

		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		int startDays = getDaysAfterStartYear(startCalendar) - 1; // 第一天从0开始

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		int endDays = getDaysAfterStartYear(endCalendar) - 1; // 第一天从0开始

		if (startDays == endDays) { // 如果开始日期和结束日期在同一天的话
			return isWorkDay(startDate) ? 1 : 0; // 当天为工作日则返回1天，否则0天
		}

		if (!START_DATE_HANDLING_STRATEGY.ifCountAsOneDay(startDate)) { // 根据处理策略，如果开始日期不算一天的话
			++startDays; // 起始日期向后移一天
		}

		if (!END_DATE_HANDLING_STRATEGY.ifCountAsOneDay(endDate)) { // 根据处理策略，如果结束日期不算一天的话
			--endDays; // 结束日期向前移一天
		}
		return SEGMENT_TREE.query(startDays, endDays);
	}

	/**
	 * 是否为工作日
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isWorkDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int days = getDaysAfterStartYear(calendar) - 1;
		return SEGMENT_TREE.query(days, days) == 1;
	}

	/**
	 * 计算从开始年份到这个日期有多少天
	 * 
	 * @param calendar
	 * @return
	 */
	private static int getDaysAfterStartYear(Calendar calendar) {
		int year = calendar.get(Calendar.YEAR);
		if (year < START_YEAR || year > END_YEAR) {
			throw new IllegalArgumentException(
					String.format("系统目前仅支持计算%d年至%d年之间的工作日，无法计算%d年！", START_YEAR, END_YEAR, year));
		}
		int days = 0;
		for (int i = START_YEAR; i < year; ++i) {
			days += getDaysOfYear(i);
		}
		days += calendar.get(Calendar.DAY_OF_YEAR);
		return days;
	}

	/**
	 * 计算该年有几天，闰年返回366，平年返回365
	 * 
	 * @param year
	 * @return
	 */
	private static int getDaysOfYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0 ? 366 : 365;
	}

	/**
	 * 初始化工作日Map<br/>
	 * 日期格式必须为yyyyMMdd，true为补休，false为放假，如果本来就是周末的节假日则不需再设置
	 */
	private static void initWorkday() {
		// ---------------2017------------------
		WORKDAY_MAP.put(20170102, false);
		WORKDAY_MAP.put(20170122, true);
		WORKDAY_MAP.put(20170127, false);
		WORKDAY_MAP.put(20170130, false);
		WORKDAY_MAP.put(20170131, false);
		WORKDAY_MAP.put(20170201, false);
		WORKDAY_MAP.put(20170202, false);
		WORKDAY_MAP.put(20170204, true);
		WORKDAY_MAP.put(20170401, true);
		WORKDAY_MAP.put(20170403, false);
		WORKDAY_MAP.put(20170404, false);
		WORKDAY_MAP.put(20170501, false);
		WORKDAY_MAP.put(20170527, true);
		WORKDAY_MAP.put(20170529, false);
		WORKDAY_MAP.put(20170530, false);
		WORKDAY_MAP.put(20170930, true);
		WORKDAY_MAP.put(20171002, false);
		// ------------------2018----------------
		WORKDAY_MAP.put(20180101, false);
		WORKDAY_MAP.put(20180211, true);
		WORKDAY_MAP.put(20180215, false);
		WORKDAY_MAP.put(20180216, false);
		WORKDAY_MAP.put(20180219, false);
		WORKDAY_MAP.put(20180220, false);
		WORKDAY_MAP.put(20180221, false);
		WORKDAY_MAP.put(20180224, true);
		WORKDAY_MAP.put(20180405, false);
		WORKDAY_MAP.put(20180406, false);
		WORKDAY_MAP.put(20180408, true);
		WORKDAY_MAP.put(20180428, true);
		WORKDAY_MAP.put(20180430, false);
		WORKDAY_MAP.put(20180501, false);
		WORKDAY_MAP.put(20180618, false);
		WORKDAY_MAP.put(20180924, false);
		WORKDAY_MAP.put(20180929, true);
		WORKDAY_MAP.put(20180930, true);
		WORKDAY_MAP.put(20181001, false);
		WORKDAY_MAP.put(20181002, false);
		WORKDAY_MAP.put(20181003, false);
		WORKDAY_MAP.put(20181004, false);
		WORKDAY_MAP.put(20181005, false);
	}

	/**
	 * 边界日期处理策略<br/>
	 * 在计算两个日期之间有多少个工作日时，有的特殊需求是如果开始/结束的日期在某个时间之前/后（如中午十二点前），则不把当天算作一天<br/>
	 * 因此特将此逻辑分离出来，各自按照不同需求实现该接口即可
	 * 
	 * @author Corvey
	 * @Date 2018年11月12日15:38:16
	 */
	private interface BoundaryDateHandlingStrategy {
		/** 是否把这个日期算作一天 */
		boolean ifCountAsOneDay(Date date);
	}

	/**
	 * zkw线段树
	 * 
	 * @author Corvey
	 */
	private static class SegmentTree {

		private int[] data; // 线段树数据
		private int numOfLeaf; // 叶子结点个数

		public SegmentTree(int[] srcData) {
			for (numOfLeaf = 1; numOfLeaf < srcData.length; numOfLeaf <<= 1)
				;
			data = new int[numOfLeaf << 1];
			for (int i = 0; i < srcData.length; ++i) {
				data[i + numOfLeaf] = srcData[i];
			}
			for (int i = numOfLeaf - 1; i > 0; --i) {
				data[i] = data[i << 1] + data[i << 1 | 1];
			}
		}

		/** [left, right]区间求和，left从0开始 */
		public int query(int left, int right) {
			if (left > right) {
				return query(right, left);
			}
			left = left + numOfLeaf - 1;
			right = right + numOfLeaf + 1;
			int sum = 0;
			for (; (left ^ right ^ 1) != 0; left >>= 1, right >>= 1) {
				if ((~left & 1) == 1)
					sum += data[left ^ 1];
				if ((right & 1) == 1)
					sum += data[right ^ 1];
			}
			return sum;
		}
	}

	public static void main(String[] args) throws ParseException {
		System.out.println("测试开始：-------------------");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH");
		Scanner cin = new Scanner(System.in);
		while (cin.hasNext()) {
			String l = cin.next();
			Date start = df.parse(l);
			String r = cin.next();
			Date end = df.parse(r);
			System.out.println(String.format("%s 到 %s, 有%d个工作日！", df.format(start), df.format(end), howManyWorkday(start, end)));
		}
		cin.close();
	}
}
