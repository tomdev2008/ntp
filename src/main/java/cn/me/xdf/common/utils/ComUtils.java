package cn.me.xdf.common.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

/**
 * 共用辅助方法
 * 
 * 包含一些零散的便捷方法。
 * 
 * @author xiaobin
 * 
 */
public class ComUtils {

	public static final String JSESSION_COOKIE = "JSESSIONID";
	public static final String JSESSION_URL = "jsessionid";

	/**
	 * 获得当前时间。
	 * 
	 * @return
	 */
	public static java.sql.Timestamp now() {
		return new java.sql.Timestamp(System.currentTimeMillis());
	}

	/**
	 * 格式化日期。yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String dateFormat(Date date) {
		return format.format(date);
	}

	/**
	 * 格式化日期。yyyy-MM-dd hh-mm-ss
	 * 
	 * @param date
	 * @return
	 */
	public static String dataFormatWhole(Date date) {
		return formatw.format(date);
	}

	public static String formatDate(Date date, int style) {
		if (date == null) {
			return "";
		}
		switch (style) {
		case 4:
			return formats.format(date);
		case 3:
			return formatm.format(date);
		case 2:
			return format.format(date);
		default:
			return formatw.format(date);
		}
	}

	public static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	public static final DateFormat formatw = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final DateFormat formatm = new SimpleDateFormat("MM-dd HH:mm");

	public static final DateFormat formats = new SimpleDateFormat("MM-dd");

	public static FilenameFilter DIR_FILE_FILTER = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			if (dir.isDirectory()) {
				return true;
			} else {
				return false;
			}
		}
	};

	/**
	 * 判断某天是星期几
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeekday(String date) {// 必须yyyy-MM-dd
		SimpleDateFormat sdw = new SimpleDateFormat("E");
		Date d = null;
		try {
			d = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdw.format(d);
	}

	public static String getWebPath(HttpServletRequest request) {
		String path = request.getContextPath();
		// String serverName = request.getServerName();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + path + "/";
		return basePath;
	}

	public static Calendar getCalendarDay(String date) {
		Date d = null;
		try {
			d = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar c = Calendar.getInstance();
		c.clear();
		c.setTime(d);
		// 获取是本月的第几周
		// int week = c.get(Calendar.WEEK_OF_MONTH);
		// int week = c.get(Calendar.WEEK_OF_YEAR);
		// 获致是本周的第几天地, 1代表星期天...7代表星期六
		// int day = c.get(Calendar.DAY_OF_WEEK);
		// System.out.println("今天是本月的第" + week + "周");
		// System.out.println("今天是星期"+ (day-1));
		return c;
	}

	public static void main(String[] args) {
		Calendar calendar = getCalendarDay("2013-09-12");
		System.out.println(calendar.get(Calendar.DAY_OF_WEEK));
	}

}
