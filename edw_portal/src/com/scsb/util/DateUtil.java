package com.scsb.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.scsb.util.Padder;

public abstract class DateUtil {

	private final static SimpleDateFormat hh24mmFormat = new SimpleDateFormat("HHmm");

	private final static SimpleDateFormat mmddyyyyWithSlashFormat = new SimpleDateFormat("MM/dd/yyyy");

	private final static DateFormat standardDateFormat = DateFormat.getDateInstance();

	private final static SimpleDateFormat yyyymmddhhmmssWithSlashFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	private final static SimpleDateFormat yyyymmddhhmmssNoSlashFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
	
	private final static SimpleDateFormat yyyymmddhhmmssNoSlashNoSpaceFormat = new SimpleDateFormat("yyyyMMddHHmmss");	

	private final static SimpleDateFormat yyyymmddWithoutSlashFormat = new SimpleDateFormat("yyyyMMdd");

	private final static SimpleDateFormat yyyymmddWithSlashFormat = new SimpleDateFormat("yyyy/MM/dd");

	public static final String[] ANNUAL_MONTH_START_DATE = { "0101", "0201",
			"0301", "0401", "0501", "0601", "0701", "0801", "0901", "1001",
			"1101", "1201" };

	public static final String[] ANNUAL_MONTH_END_DATE = { "0131", "0229",
			"0331", "0430", "0531", "0630", "0731", "0831", "0930", "1031",
			"1130", "1231" };

	private static Calendar createTodayWithoutTimeField() {
		Calendar c = Calendar.getInstance();
		trimTimeField(c);
		return c;
	}

	public static Date getCurrentTimeWithoutMinisec() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date getDate(int dayRange) {
		Calendar c = createTodayWithoutTimeField();
		c.add(Calendar.DATE, dayRange);
		return c.getTime();
	}

	/**
	 * @param i
	 * @param testDate
	 */
	public static Date getDate(int dayRange, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, dayRange);
		return calendar.getTime();
	}

	public static Date getEndDateOfWeek(Date date) {

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		trimTimeField(c);

		int weekday = c.get(Calendar.DAY_OF_WEEK);
		int daysBetweenEndOfWeekDay = 7 - weekday;

		c.add(Calendar.DATE, daysBetweenEndOfWeekDay);
		return c.getTime();
	}

	// MM/dd/yyyy -- 12/30/2004
	public static Date getMMDDYYYY(String mmddyyyyString) {
		try {
			return mmddyyyyWithSlashFormat.parse(mmddyyyyString);
		} catch (ParseException e) {
			throw new RuntimeException("date parse failed", e);
		}
	}

	/**
	 * @param baseDate
	 * @param noOfMonth
	 * @return
	 */
	public static Date getRollYear(Date baseDate, int noOfYear) {
		Calendar c = Calendar.getInstance();
		c.setTime(baseDate);
		c.add(Calendar.YEAR, noOfYear);
		return c.getTime();
	}

	public static Date getRollMonth(Date baseDate, int noOfMonth) {
		Calendar c = Calendar.getInstance();
		c.setTime(baseDate);
		c.add(Calendar.MONTH, noOfMonth);
		return c.getTime();
	}

	public static Date getRollDay(Date baseDate, int noOfDay) {
		Calendar c = Calendar.getInstance();
		c.setTime(baseDate);
		c.add(Calendar.DATE, noOfDay);
		return c.getTime();
	}

	public static Date getRollWeek(Date baseDate, int noOfweek) {
		Calendar c = createTodayWithoutTimeField();
		c.setTime(baseDate);
		c.add(Calendar.DATE, 7 * noOfweek);
		return c.getTime();
	}

	public static Date getRollWeeks(int noOfweek) {
		Calendar c = createTodayWithoutTimeField();
		c.add(Calendar.DATE, 7 * noOfweek);
		return c.getTime();
	}

	public static Date getTomorrow() {
		Calendar c = createTodayWithoutTimeField();
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}

	/**
	 * @param yyyymmddWithSlashString
	 *            in "yyyy/mm/dd" -- 2004/12/03
	 * @return
	 */
	public static Date getYMD(String yyyymmddWithSlashString) {
		try {
			return standardDateFormat.parse(yyyymmddWithSlashString);
		} catch (ParseException e) {
			throw new RuntimeException("date parse failed", e);
		}
	}

	public static Date getYMD(String year, String month, String day) {
		Calendar c = createTodayWithoutTimeField();
		c.set(Calendar.YEAR, Integer.parseInt(year));
		c.set(Calendar.MONTH, Integer.parseInt(month) - 1);
		c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));

		return c.getTime();
	}

	/**
	 * @param year
	 * @param solarDay
	 * 
	 * @return Date
	 */
	public static Date getYMD(String year, String solarDay) {
		Calendar c = createTodayWithoutTimeField();
		c.set(Calendar.YEAR, Integer.parseInt(year));
		c.set(Calendar.DAY_OF_YEAR, Integer.parseInt(solarDay));

		return c.getTime();
	}

	
	/**
	 * @param yyyymmddhhmmssWithSlash
	 *            in "20040213123044"
	 * @return
	 */
	public static Date getYYYYmmddHHMMSS(String yyyymmddhhmmssWithSlash) {
		String year = yyyymmddhhmmssWithSlash.substring(0, 4);
		String month = yyyymmddhhmmssWithSlash.substring(4, 6);
		String day = yyyymmddhhmmssWithSlash.substring(6, 8);
		String hour = yyyymmddhhmmssWithSlash.substring(8,10);
		String minute = yyyymmddhhmmssWithSlash.substring(10,12);
		String second = yyyymmddhhmmssWithSlash.substring(12,14);
		Calendar c = Calendar.getInstance();
		c.setTime(getYMD( year,  month,  day));
		c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
		c.set(Calendar.MINUTE, Integer.parseInt(minute));
		c.set(Calendar.SECOND, Integer.parseInt(second));

		return c.getTime();
	}	
	/**
	 * @param yyyymmdd_hhmmssWithSlash
	 *            in "2004/02/13 123044"
	 * @return
	 */
	public static Date getYMDHMS(String yyyymmdd_hhmmssWithSlash) {
		String datePart = yyyymmdd_hhmmssWithSlash.substring(0, 10);
		String hour = yyyymmdd_hhmmssWithSlash.substring(11, 13);
		String minute = yyyymmdd_hhmmssWithSlash.substring(13, 15);
		String second = yyyymmdd_hhmmssWithSlash.substring(15, 17);
		Calendar c = Calendar.getInstance();
		c.setTime(getYMD(datePart));
		c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
		c.set(Calendar.MINUTE, Integer.parseInt(minute));
		c.set(Calendar.SECOND, Integer.parseInt(second));

		return c.getTime();
	}

	public static Date getYMDHMS(String year, String month, String day,
			String hour, String minute, String second) {
		Calendar c = createTodayWithoutTimeField();
		c.set(Calendar.YEAR, Integer.parseInt(year));
		c.set(Calendar.MONTH, Integer.parseInt(month) - 1);
		c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
		c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
		c.set(Calendar.MINUTE, Integer.parseInt(minute));
		c.set(Calendar.SECOND, Integer.parseInt(second));
		return c.getTime();
	}

	// yyyyMMdd HHmmss -- "20040213 123044"
	public static Date getYMDHMSNoSlash(String yyyymmddhhmmssNoSlashString) {
		try {
			return yyyymmddhhmmssNoSlashFormat
					.parse(yyyymmddhhmmssNoSlashString);
		} catch (ParseException e) {
			throw new RuntimeException("date parse failed", e);
		}
		
	}
	// yyyyMMdd HHmmss -- "20040213 123044"
	public static Date getYMDHMSNoSlashNoSpace(String yyyymmddhhmmssNoSlashString) {
		try {
			return yyyymmddhhmmssNoSlashNoSpaceFormat
					.parse(yyyymmddhhmmssNoSlashString);
		} catch (ParseException e) {
			throw new RuntimeException("date parse failed", e);
		}
	}
	// get yyyyMMdd -- 20040111
	public static Date getYMDNoSlash(String yyyymmddNoSlashString) {
		try {
			return yyyymmddWithoutSlashFormat.parse(yyyymmddNoSlashString);
		} catch (ParseException e) {
			throw new RuntimeException("date parse failed", e);
		}
	}

	// HH24mm -- 2359
	public static String nullSafeFormatHH24MM(Date date) {
		if (date == null) {
			return "";
		}
		return hh24mmFormat.format(date);
	}

	// MM/dd/yyyy -- 05/30/2004
	public static String nullSafeFormatMMDDYYYY(Date date) {
		return date == null ? "" : mmddyyyyWithSlashFormat.format(date);
	}

	// yyyyMMdd -- 20040503
	public static String nullSafeFormatNoSlashYYYYMMDD(Date date) {
		if (date == null) {
			return "";
		}
		return yyyymmddWithoutSlashFormat.format(date);
	}

	// yyyy/MM/dd -- 2004/05/11
	public static String nullSafeFormatYYYYMMDD(Date date) {
		if (date == null) {
			return "";
		}
		return yyyymmddWithSlashFormat.format(date);
	}

	// yyyy/MM/dd HH:mm:ss -- 2004/12/03 13:49:50
	public static String nullSafeFormatYYYYMMDDHHMMSS(Date date) {
		return date == null ? "" : yyyymmddhhmmssWithSlashFormat.format(date);
	}

	private static void trimTimeField(Calendar c) {
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.AM_PM, Calendar.AM);
	}

	/**
	 * 民國年轉西元年 yyymmdd --> YYYYMMDD
	 * 
	 * @param yyymmdd
	 * @return
	 */
	public static String getWesterDate(String yyymmdd) {
		yyymmdd = yyymmdd.trim();

		if (yyymmdd == null || yyymmdd.length() != 7) {// 員日期格式必須為"0960510"
			// throw new RuntimeException("date parse failed");
			return "";
		}
		return yyymmdd = (Integer.parseInt(yyymmdd.substring(0, 3)) + 1911)
				+ yyymmdd.substring(3);
	}

	/**
	 * 民國年轉西元年 yyy/mm/dd --> YYYYMMDD
	 * 
	 * @param yyymmddWithSlash
	 * @return
	 */
	public static String getYYYYMMDD(String yyymmddWithSlash) {
		yyymmddWithSlash = yyymmddWithSlash.trim();
		if (yyymmddWithSlash == null || yyymmddWithSlash.length() == 0) {
			return "";
		}
		String[] date = yyymmddWithSlash.split("/");
		if (date.length != 3) {
			return "";
		} else {
			Padder pd = new Padder(Integer.parseInt(date[0]) + "", 3,
					Padder.RIGHT, "0");
			return (Integer.parseInt(pd.getOutput()) + 1911) + date[1]
					+ date[2];
		}
	}

	/**
	 * 轉換格式 ex: 20070520 --> 2007/05/20
	 * 
	 * @param yyyymmdd
	 * @return
	 */
	public static String nullSafeFormatWithSlash(String yyyymmdd) {
		if (yyyymmdd == null || yyyymmdd.length() == 0) {
			return "";
		}
		if (yyyymmdd.length() == 8) {
			String year = yyyymmdd.substring(0, 4);
			String month = yyyymmdd.substring(4, 6);
			String day = yyyymmdd.substring(6, 8);
			return year + "/" + month + "/" + day;
		} else {
			return yyyymmdd;
		}
	}

	/**
	 * 轉換格式 ex: 2007/05/20 --> 20070520
	 * 
	 * @param yyyymmddWithSlash
	 * @return
	 */
	public static String nullSafeFormatNoSlash(String yyyymmddWithSlash) {
		if (yyyymmddWithSlash == null || yyyymmddWithSlash.length() == 0) {
			return "";
		}
		if (yyyymmddWithSlash.length() == 10) {
			String year = yyyymmddWithSlash.substring(0, 4);
			String month = yyyymmddWithSlash.substring(5, 7);
			String day = yyyymmddWithSlash.substring(8, 10);
			return year + month + day;
		} else {
			return yyyymmddWithSlash;
		}
	}

	/**
	 * 自動調整合理日期 ex: 2009/02/30 --> 20090228
	 * 
	 * @param yyyymmdd
	 * @return
	 */
	public static String getReasonableDate(String yyyymmdd) {
		String reasonableDate = yyyymmdd;

		int yyyy = Integer.parseInt(yyyymmdd.substring(0, 4));
		int mm = Integer.parseInt(yyyymmdd.substring(4, 6));
		int dd = Integer.parseInt(yyyymmdd.substring(6));

		int maxDay = getMaxDayByMonth(yyyy, mm);
		if (dd > maxDay) {
			String sdd = (maxDay + 100 + "").substring(1);
			reasonableDate = yyyymmdd.substring(0, 6) + sdd;
		}

		return reasonableDate;
	}

	/**
	 * 取得該月份最大天數
	 * 
	 * @param yy
	 * @param mm
	 * @return
	 */
	public static int getMaxDayByMonth(int yy, int mm) { // 記算該月份最大天數
		int lest_dd = 0;

		if (mm == 1 || mm == 3 || mm == 5 || mm == 7 || mm == 8 || mm == 10
				|| mm == 12) {
			lest_dd = 31;
		} else if (mm == 4 || mm == 6 || mm == 9 || mm == 11) {
			lest_dd = 30;
		} else if (mm == 2 && (yy % 4 == 0)
				&& (!((yy % 100 == 0) & (yy % 400 != 0)))) {// 以下閏年
			lest_dd = 29;
		} else {
			lest_dd = 28;
		}
		return lest_dd;
	}

	public static Date getSmartRollMonth(Date baseDate, int noOfMonth) {
		Calendar c = Calendar.getInstance();
		c.setTime(baseDate);
		c.add(Calendar.MONTH, noOfMonth);

		String staDate = nullSafeFormatNoSlashYYYYMMDD(baseDate);
		int yyyy = Integer.parseInt(staDate.substring(0, 4));
		int mm = Integer.parseInt(staDate.substring(4, 6));
		int dd = Integer.parseInt(staDate.substring(6));
		if (mm == 2 || mm == 4 || mm == 6 || mm == 9 || mm == 11) {
			int maxDay = getMaxDayByMonth(yyyy, mm);
			if (maxDay == dd) {
				String endDate = nullSafeFormatNoSlashYYYYMMDD(c.getTime());
				yyyy = Integer.parseInt(endDate.substring(0, 4));
				mm = Integer.parseInt(endDate.substring(4, 6));
				maxDay = getMaxDayByMonth(yyyy, mm);
				return getYMD(yyyy + "", mm + "", maxDay + "");
			}
		}

		return c.getTime();
	}

	// public static Date emptySafeGetYMD(String yyyymmddWithSlashString) {
	// if (StringUtils.trimToNull(yyyymmddWithSlashString) == null) {
	// return null;
	// }
	// return getYMD(yyyymmddWithSlashString);
	// }

	/**
	 * 取得民國年格式 YYYYMMDD --> yy年mm月dd日
	 * 
	 * @param YYYYMMDD
	 * @return
	 */
	public static String getChineseCalendarFormat(String YYYYMMDD) {
		if (YYYYMMDD == null || YYYYMMDD.length() != 8) {
			return "";
		}
		int year = Integer.valueOf(YYYYMMDD.substring(0, 4)) - 1911;
		int month = Integer.valueOf(YYYYMMDD.substring(4, 6));
		int day = Integer.valueOf(YYYYMMDD.substring(6, 8));

		return "" + year + "年" + month + "月" + day + "日";
	}

	/**
	 * 取得民國年格式 YYYYMMDD --> yy.mm.dd
	 * 
	 * @param YYYYMMDD
	 * @return
	 */
	public static String getChineseCalendarFormatWithPoint(String YYYYMMDD) {
		if (YYYYMMDD == null || YYYYMMDD.length() != 8) {
			return "";
		}
		int year = Integer.valueOf(YYYYMMDD.substring(0, 4)) - 1911;
		int month = Integer.valueOf(YYYYMMDD.substring(4, 6));
		int day = Integer.valueOf(YYYYMMDD.substring(6, 8));

		return "" + year + "." + month + "." + day;
	}

	/**
	 * 取得民國年格式 YYYYMMDD --> yy/mm/dd
	 * 
	 * @param YYYYMMDD
	 * @return
	 */
	public static String getChineseCalendarFormatWithSlash(String YYYYMMDD) {
		if (YYYYMMDD == null || YYYYMMDD.length() != 8) {
			return "";
		}
		int year = Integer.valueOf(YYYYMMDD.substring(0, 4)) - 1911;
		int month = Integer.valueOf(YYYYMMDD.substring(4, 6));
		int day = Integer.valueOf(YYYYMMDD.substring(6, 8));

		return "" + year + "/" + month + "/" + day;
	}

	/**
	 * 日期位移
	 * 
	 * @param timeLength
	 * @param timeUnit
	 * @param baseDate
	 * @return
	 */
	public static Date getShiftDate(int timeLength, String timeUnit,
			String baseDate) {
		if (baseDate == null || baseDate.length() == 0) {
			return null;
		}
		Date resultDate = null;
		if (timeUnit.equals("Y")) {// 以年份位移
			resultDate = getRollYear(getYMDNoSlash(baseDate), timeLength);
		} else if (timeUnit.equals("M")) {// 以月份位移
			resultDate = getRollMonth(getYMDNoSlash(baseDate), timeLength);
		} else if (timeUnit.equals("W")) {// 以週數位移
			resultDate = getRollWeek(getYMDNoSlash(baseDate), timeLength);
		} else {// 以天數位移
			resultDate = getRollDay(getYMDNoSlash(baseDate), timeLength);
		}
		return resultDate;
	}

	/**
	 * 傳回日期位移值
	 * 
	 * @param timeLength
	 * @param timeUnit
	 * @param baseDate
	 * @return
	 */
	public static int getTimeLength(String timeUnit, String staDate,
			String endDate) {
		int timeLength = 0;

		if (staDate == null || staDate.length() == 0 || endDate == null
				|| endDate.length() == 0) {
			return timeLength;
		}

		int count = 0;
		Date resultDate = null;
		while (true) {
			count++;
			if (timeUnit.equals("Y")) {// 以年份位移
				resultDate = getRollYear(getYMDNoSlash(staDate), count);
			} else if (timeUnit.equals("M")) {// 以月份位移
				resultDate = getRollMonth(getYMDNoSlash(staDate), count);
			} else if (timeUnit.equals("W")) {// 以週數位移
				resultDate = getRollWeek(getYMDNoSlash(staDate), count);
			} else {// 以天數位移
				resultDate = getRollDay(getYMDNoSlash(staDate), count);
			}
			String checkDate = nullSafeFormatNoSlashYYYYMMDD(resultDate);
			if (checkDate.compareTo(endDate) > 0) {
				timeLength = count - 1 <= 0 ? 0 : count - 1;
				break;
			}

		}
		return timeLength;
	}

	/**
	 * 取得兩日期之差異天數
	 * 
	 * @param yyyymmdd_from
	 * @param yyyymmdd_to
	 * @return
	 */
	public static int compareDayCount(String yyyymmdd_from, String yyyymmdd_to) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				"yyyyMMdd");
		int count = 0;
		try {
			java.util.Date date1 = formatter.parse(yyyymmdd_from);
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			calendar.setTime(date1);
			java.util.Date date2 = formatter.parse(yyyymmdd_to);
			while (calendar.getTime().compareTo(date2) < 0) {
				// System.out.println(formatter.format(calendar.getTime()));
				count++;
				calendar.add(java.util.Calendar.DAY_OF_MONTH, 1);
			}
			while (calendar.getTime().compareTo(date2) > 0) {
				count--;
				calendar.add(java.util.Calendar.DAY_OF_MONTH, -1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 取得兩日期之差異月數
	 * 
	 * @param yyyymmdd_from
	 * @param yyyymmdd_to
	 * @return
	 */
	public static int compareMonthCount(String yyyymmdd_from, String yyyymmdd_to) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				"yyyyMMdd");
		int count = 0;
		try {
			java.util.Date date1 = formatter.parse(yyyymmdd_from);
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			calendar.setTime(date1);
			java.util.Date date2 = formatter.parse(yyyymmdd_to);
			while (calendar.getTime().compareTo(date2) < 0) {
				// System.out.println(formatter.format(calendar.getTime()));
				count++;
				calendar.add(java.util.Calendar.MONTH, 1);
			}
			while (calendar.getTime().compareTo(date2) > 0) {
				count--;
				calendar.add(java.util.Calendar.MONTH, -1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 檢核西元年日期格式
	 * 
	 * @param yyyymmdd
	 * @return
	 */
	public static boolean checkWesternDateFormat(String yyyymmdd) {

		if (yyyymmdd == null || yyyymmdd.length() != 8) {
			return false;
		} else {
			if (Double.isNaN(Double.parseDouble(yyyymmdd))) {
				return false;
			} else {
				// 檢核日期範圍
				int yy = Integer.parseInt(yyyymmdd.substring(2, 4));
				int mm = Integer.parseInt(yyyymmdd.substring(4, 6));
				if (mm < 1 || mm > 12) {
					return false;
				}
				int dd = getMaxDayByMonth(yy, mm);
				if (Integer.parseInt(yyyymmdd.substring(6, 8)) < 1
						|| Integer.parseInt(yyyymmdd.substring(6, 8)) > dd) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 取得今天日期的西元年
	 * @return
	 */
	public static String getY4(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String yyyy = sdf.format(new Date());		
		return yyyy; 
	}
	
	public static String getCYear(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String yyyy = sdf.format(new Date());
		String cYear = String.valueOf(Integer.parseInt(yyyy)-1911);		
		return cYear; 
	}	

	public static String getYYYYMMDD(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());		
		return ymd; 
	}
	
	public static String getDT(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String result = sdf.format(new Date());		
		return result; 
	}
	public static String getDTS(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String result = sdf.format(new Date());		
		return result; 
	}	
	
	/**
	 * 取得基準日的年齡
	 * @return
	 */
	public static int getAge(Date birthDt ,Date countDt){
		String yyyymmdd1 =DateUtil.nullSafeFormatNoSlashYYYYMMDD(birthDt);
		String yyyymmdd2 =DateUtil.nullSafeFormatNoSlashYYYYMMDD(countDt);
        //取得生日的西元年
        int year = Integer.parseInt(yyyymmdd1.substring(0,4));
        //取得生日的月份
        int month = Integer.parseInt(yyyymmdd1.substring(4,6));
        //取得生日的日
        int day = Integer.parseInt(yyyymmdd1.substring(6,8));
        //求得基準日的西元年
        int yearNow = Integer.parseInt(yyyymmdd2.substring(0,4));
        //求得基準日的月份
        int monthNow = Integer.parseInt(yyyymmdd2.substring(4,6));
        //求得基準日的日期
        int dayNow =Integer.parseInt(yyyymmdd2.substring(6,8));		
		
		//計算實際年齡
        int iAge=0;
        if( monthNow > month )
        	iAge = yearNow - year ;
        else if( (monthNow == month)&&(dayNow>=day) )
        	iAge = yearNow - year;
        else
            iAge = yearNow - year - 1  ;           
		return iAge; 
	}	
	
	public static void main(String[] args){
		String xx=  getY4();
		System.out.println(xx);
	}
}