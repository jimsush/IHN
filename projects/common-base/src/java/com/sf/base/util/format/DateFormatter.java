package com.sf.base.util.format;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * Title: DateFormatter
 * </p>
 * <p>
 * Description: ����ʱ����ص�ת������
 * </p>
 * 
 * @author sufeng
 * created 2015-3-30 ����09:40:07
 * modified [who date description]
 * check [who date description]
 */
public class DateFormatter {
    
	public final static SimpleDateFormat LONG_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final static SimpleDateFormat MID_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public final static SimpleDateFormat SHORT_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public final static SimpleDateFormat COMMON_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	
	/**
	 * 3��ĺ�����
	 */
	public final static long THREE_DAY_MILLSEC = 3*24*3600*1000;

	/**
	 * 1��ĺ�����
	 */
	public final static long ONE_DAY_MILLSEC = 24*3600*1000;
	
	/**
	 * 1Сʱ�ĺ�����
	 */
	public final static long ONE_HOUR_MILLSEC = 3600*1000;
	
	/**
	 * 3Сʱ�ĺ�����
	 */
	public final static long THREE_HOURS_MILLSEC = 3*3600*1000;
	
	/**
	 * 12Сʱ�ĺ�����
	 */
	public final static long TWELVE_HOURS_MILLSEC = 12*3600*1000;
	
	/**
	 * һ��ķ�����
	 */
	public final static int ONE_DAY_MINUTE=24*60;
	
	/**
	 * һ�ܵ�Сʱ��
	 */
	public final static int ONE_WEEK_HOUR=7*24;
	
	/**
	 * ת��Ϊdate����
	 * @param longDateFormatString
	 * @return
	 * @throws Exception
	 */
	public static Date getDateByString(String longDateFormatString) throws Exception{
	    if(longDateFormatString==null || longDateFormatString.length()==0)
	        return new Date(0);
	    else
	        return LONG_FORMAT.parse(longDateFormatString);
	}
	
	/**
	 * ���ص�ǰ���������ַ�������ʽΪ: yyyy-MM-dd hh:mm:ss
	 * @return
	 */
	public static String getLongCurrentDate() {
		return String.valueOf(LONG_FORMAT.format(new Date()));
	}
	
	/**
     * ���ص�ǰ���������ַ�������ʽΪ: yyyyMMddhhmmss
     * @return
     */
	public static String getCommonFormatDate(){
	    return String.valueOf(COMMON_FORMAT.format(new Date()));
	}
	
	/**
	 * ��������(Date)�����ظ�ʽΪ: yyyy-MM-dd hh:mm:ss���ַ���
	 * @param date
	 * @return
	 */
	public static String getLongDate(Date date) {
		if (null == date)
			return getLongCurrentDate();
		return String.valueOf(LONG_FORMAT.format(date));
	}
	
	/**
	 * ��������(long:ms)�����ظ�ʽΪ: yyyy-MM-dd hh:mm:ss���ַ���
	 * @param value
	 * @return
	 */
	public static String getLongDate(long value) {
		return String.valueOf(LONG_FORMAT.format(new Date(value)));
	}
	
	/**
	 * ���ص�ǰ���ڼ�д�ַ�������ʽΪ: yyyy-MM-dd
	 * @return
	 */
	public static String getShortCurrentDate() {
		return String.valueOf(SHORT_FORMAT.format(new Date()));
	}
	
	/**
	 * ��������(Date)�����ص�ǰ���ڼ�д�ַ�������ʽΪ: yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String getShortDate(Date date) {
		if (null == date)
			return getShortCurrentDate();
		return String.valueOf(SHORT_FORMAT.format(date));
		
	}
	
	/**
	 * ��������(long:ms)�����ص�ǰ���ڼ�д�ַ�������ʽΪ: yyyy-MM-dd
	 * @param value
	 * @return
	 */
	public static String getShortDate(long value) {
		return String.valueOf(SHORT_FORMAT.format(new Date(value)));		
	}
	
	/**
	 * ���ص�ǰ�����еȸ��ӳ̶ȵ��ַ�������ʽΪ: yyyy-MM-dd hh:mm
	 * @return
	 */
	public static String getMidCurrentDate() {
		return String.valueOf(MID_FORMAT.format(new Date()));
	}
	
	/**
	 * ��������(Date)�����ص�ǰ�����еȸ��ӳ̶ȵ��ַ�������ʽΪ: yyyy-MM-dd hh:mm
	 * @param date
	 * @return
	 */
	public static String getMidDate(Date date) {
		if (null == date)
			return getMidCurrentDate();
		return String.valueOf(MID_FORMAT.format(date));
	}
	
	/**
	 * ��������(long:ms)�����ص�ǰ�����еȸ��ӳ̶ȵ��ַ�������ʽΪ: yyyy-MM-dd hh:mm
	 * @param value
	 * @return
	 */
	public static String getMidDate(long value) {
		return String.valueOf(MID_FORMAT.format(new Date(value)));		
	}
	
	/**
	 * �������������x��xʱx��x��x����
	 * @param ms
	 * @return
	 */
	public static String getWellTimeFromMilliSecond(long ms) {
		int oneSecond = 1000;
		int oneMinute = oneSecond * 60;
		int oneHour = oneMinute * 60;
		int oneDay = oneHour * 24;

		long day = ms / oneDay;
		long hour = (ms - day * oneDay) / oneHour;
		long minute = (ms - day * oneDay - hour * oneHour) / oneMinute;
		long second = (ms - day * oneDay - hour * oneHour - minute * oneMinute) / oneSecond;
		long milliSecond = ms - day * oneDay - hour * oneHour - minute * oneMinute - second	* oneSecond;

		String strDay = day < 10 ? "0" + day : "" + day;
		String strHour = hour < 10 ? "0" + hour : "" + hour;
		String strMinute = minute < 10 ? "0" + minute : "" + minute;
		String strSecond = second < 10 ? "0" + second : "" + second;
		String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;
		strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
		StringBuffer timeBuffer = new StringBuffer();
		timeBuffer.append(strDay);
		timeBuffer.append("d");
		timeBuffer.append(strHour);
		timeBuffer.append("h");
		timeBuffer.append(strMinute);
		timeBuffer.append("m");
		timeBuffer.append(strSecond);
		timeBuffer.append("s");
		timeBuffer.append(strMilliSecond);
		timeBuffer.append("ms");
		
		return timeBuffer.toString();
	}

	/**
	 * �õ��������ڵ������䣬���罫2008-04-09����2008-04-07_2008-04-13���������
	 * @param dateString ʱ���ַ���
	 * @return
	 */
    public static String getWeekFromDate(String dateString){
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = format.parse(dateString);
			Date mondy = new Date();
			Date sundy = new Date();
			int day = date.getDay();
			if (day == 0) {
				mondy.setTime(date.getTime() - 1000L * 60 * 60 * 24 * 6);
				sundy.setTime(date.getTime());
			} else {
				mondy.setTime(date.getTime() - 1000L * 60 * 60 * 24 * (day-1));
				sundy.setTime(date.getTime() + 1000L * 60 * 60 * 24 * (7 - day));
			}
			sb.append(format.format(mondy));
			sb.append("_");
			sb.append(format.format(sundy));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
     * ��ʱ�����ʱ��ת��ΪDateAndTime��ʽ��������������ʱ���ʱ����OAM����
     * 
     * @param date
     * @param timeZone
     * @return
     */
    public static byte[] getDateAndTimeByDate(Date date, int timeZone) {
        byte[] bytes = new byte[11];
        if (date == null)
            date = new Date();
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        int day = date.getDate();
        int hour = date.getHours();
        int minute = date.getMinutes();
        int second = date.getSeconds();
        bytes[0] = Integer.valueOf(year / 236).byteValue();
        bytes[1] = Integer.valueOf(year % 256).byteValue();
        bytes[2] = Integer.valueOf(month).byteValue();
        bytes[3] = Integer.valueOf(day).byteValue();
        bytes[4] = Integer.valueOf(hour).byteValue();
        bytes[5] = Integer.valueOf(minute).byteValue();
        bytes[6] = Integer.valueOf(second).byteValue();
        bytes[7] = 0;
        if (timeZone > 0) {
            bytes[8] = 43;
            bytes[9] = Integer.valueOf(timeZone).byteValue();
        } else {
            bytes[8] = 45;
            bytes[9] = Integer.valueOf(-timeZone).byteValue();
        }
        bytes[10] = 0;
        return bytes;
    }

    /**
     * ����ʱ���ʱ���õ�����2015-6-24,16:16:46.0,+7:0�ַ�����byte����
     * 
     * @param date
     * @param timeZone
     * @return
     */
    public static byte[] getDateAndTimeStringByDate(Date date, int timeZone) {
        if (date == null)
            date = new Date();
        StringBuffer buffer = new StringBuffer();
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        int day = date.getDate();
        int hour = date.getHours();
        int minute = date.getMinutes();
        int second = date.getSeconds();
        buffer.append(year).append("-").append(month).append("-").append(day);
        buffer.append(",").append(hour).append(":").append(minute).append(":").append(second);
        buffer.append(".0,");
        if (timeZone >= 0)
            buffer.append("+").append(timeZone);
        else
            buffer.append("-").append(-timeZone);
        buffer.append(":0");
        return buffer.toString().getBytes();
    }

}
