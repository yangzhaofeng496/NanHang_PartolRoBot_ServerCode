package yang.plane.util;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

    /**
     * Base ISO 8601 Date format yyyyMMdd i.e., 20021225 for the 25th day of December in the year 2002
     */
    public static final String ISO_DATE_FORMAT = "yyyyMMdd";

    /**
     * Expanded ISO 8601 Date format yyyy-MM-dd i.e., 2002-12-25 for the 25th day of December in the year 2002
     */
    public static final String ISO_EXPANDED_DATE_FORMAT = "yyyy-MM-dd";

    public static final String DATE_FORMAT = "yyyy/MM/dd";

    /**
     * yyyy-MM-dd hh:mm:ss
     */
    public static String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static String DATE_PATTERN = "yyyyMMddHHmmss";

    /**
     * YYYY年MM月dd日 HH:mm:ss年
     */
    public static String DATE_CHINESE = "YYYY年MM月dd日 HH:mm:ss";

    /**
     * 则个
     */
    private static boolean LENIENT_DATE = false;


    private static Random random = new Random();
    private static final int ID_BYTES = 10;

    public synchronized static String generateId() {
        StringBuffer result = new StringBuffer();
        result = result.append(System.currentTimeMillis());
        for (int i = 0; i < ID_BYTES; i++) {
            result = result.append(random.nextInt(10));
        }
        return result.toString();
    }

    protected static final float normalizedJulian(float JD) {

        float f = Math.round(JD + 0.5f) - 0.5f;

        return f;
    }

    /**
     * Returns the Date from a julian. The Julian date will be converted to noon GMT,
     * such that it matches the nearest half-integer (i.e., a julian date of 1.4 gets
     * changed to 1.5, and 0.9 gets changed to 0.5.)
     *
     * @param JD the Julian date
     * @return the Gregorian date
     */
    public static final Date toDate(float JD) {

        /* To convert a Julian Day Number to a Gregorian date, assume that it is for 0 hours, Greenwich time (so
         * that it ends in 0.5). Do the following calculations, again dropping the fractional part of all
         * multiplicatons and divisions. Note: This method will not give dates accurately on the
         * Gregorian Proleptic Calendar, i.e., the calendar you get by extending the Gregorian
         * calendar backwards to years earlier than 1582. using the Gregorian leap year
         * rules. In particular, the method fails if Y<400. */
        float Z = (normalizedJulian(JD)) + 0.5f;
        float W = (int) ((Z - 1867216.25f) / 36524.25f);
        float X = (int) (W / 4f);
        float A = Z + 1 + W - X;
        float B = A + 1524;
        float C = (int) ((B - 122.1) / 365.25);
        float D = (int) (365.25f * C);
        float E = (int) ((B - D) / 30.6001);
        float F = (int) (30.6001f * E);
        int day = (int) (B - D - F);
        int month = (int) (E - 1);

        if (month > 12) {
            month = month - 12;
        }

        int year = (int) (C - 4715); //(if Month is January or February) or C-4716 (otherwise)

        if (month > 2) {
            year--;
        }

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1); // damn 0 offsets
        c.set(Calendar.DATE, day);

        return c.getTime();
    }

    /**
     * Returns the days between two dates. Positive values indicate that
     * the second date is after the first, and negative values indicate, well,
     * the opposite. Relying on specific times is problematic.
     *
     * @param early the "first date"
     * @param late the "second date"
     * @return the days between the two dates
     */
    public static final int daysBetween(Date early, Date late) {

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(early);
        c2.setTime(late);

        return daysBetween(c1, c2);
    }

    /**
     * Returns the days between two dates. Positive values indicate that
     * the second date is after the first, and negative values indicate, well,
     * the opposite.
     *
     * @param early
     * @param late
     * @return the days between two dates.
     */
    public static final int daysBetween(Calendar early, Calendar late) {

        return (int) (toJulian(late) - toJulian(early));
    }

    /**
     * Return a Julian date based on the input parameter. This is
     * based from calculations found at
     * <a href="http://quasar.as.utexas.edu/BillInfo/JulianDatesG.html">Julian Day Calculations
     * (Gregorian Calendar)</a>, provided by Bill Jeffrys.
     * @param c a calendar instance
     * @return the julian day number
     */
    public static final float toJulian(Calendar c) {

        int Y = c.get(Calendar.YEAR);
        int M = c.get(Calendar.MONTH);
        int D = c.get(Calendar.DATE);
        int A = Y / 100;
        int B = A / 4;
        int C = 2 - A + B;
        float E = (int) (365.25f * (Y + 4716));
        float F = (int) (30.6001f * (M + 1));
        float JD = C + D + E + F - 1524.5f;

        return JD;
    }

    /**
     * Return a Julian date based on the input parameter. This is
     * based from calculations found at
     * <a href="http://quasar.as.utexas.edu/BillInfo/JulianDatesG.html">Julian Day Calculations
     * (Gregorian Calendar)</a>, provided by Bill Jeffrys.
     * @param date
     * @return the julian day number
     */
    public static final float toJulian(Date date) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return toJulian(c);
    }

    /**
     * @param isoString  
     * @param fmt 
     * @param field   Calendar.YEAR/Calendar.MONTH/Calendar.DATE
     * @param amount 
     * @return
     * @throws ParseException
     */
    public static final String dateIncrease(String isoString, String fmt,
                                            int field, int amount) {

        try {
            Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone(
                    "GMT"));
            cal.setTime(stringToDate(isoString, fmt, true));
            cal.add(field, amount);

            return dateToString(cal.getTime(), fmt);

        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Time Field Rolling function.
     * Rolls (up/down) a single unit of time on the given time field.
     *
     * @param isoString
     * @param field the time field.
     * @param up Indicates if rolling up or rolling down the field value.
     * @param expanded use formating char's
     * @exception ParseException if an unknown field value is given.
     */
    public static final String roll(String isoString, String fmt, int field,
                                    boolean up) throws ParseException {

        Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone(
                "GMT"));
        cal.setTime(stringToDate(isoString, fmt));
        cal.roll(field, up);

        return dateToString(cal.getTime(), fmt);
    }

    /**
     * Time Field Rolling function.
     * Rolls (up/down) a single unit of time on the given time field.
     *
     * @param isoString
     * @param field the time field.
     * @param up Indicates if rolling up or rolling down the field value.
     * @exception ParseException if an unknown field value is given.
     */
    public static final String roll(String isoString, int field, boolean up) throws
            ParseException {

        return roll(isoString, DATETIME_PATTERN, field, up);
    }

    /**
     *  java.util.Date
     * @param dateText  
     * @param format  
     * @param lenient  
     * @return
     */
    public static Date stringToDate(String dateText, String format,
                                    boolean lenient) {

        if (dateText == null) {

            return null;
        }

        DateFormat df = null;

        try {

            if (format == null) {
                df = new SimpleDateFormat();
            } else {
                df = new SimpleDateFormat(format);
            }

            // setLenient avoids allowing dates like 9/32/2001
            // which would otherwise parse to 10/2/2001
            df.setLenient(false);

            return df.parse(dateText);
        } catch (ParseException e) {

            return null;
        }
    }

    /**
     * @return Timestamp
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    /** java.util.Date
     * @param dateText  
     * @param format  
     * @return
     */
    public static Date stringToDate(String dateString, String format) {

        return stringToDate(dateString, format, LENIENT_DATE);
    }

    /**
     * java.util.Date
     * @param dateText  
     */
    public static Date stringToDate(String dateString) {
        return stringToDate(dateString, ISO_EXPANDED_DATE_FORMAT, LENIENT_DATE);
    }

    /**  
     * @return 
     * @param pattern 
     * @param date  
     */
    public static String dateToString(Date date, String pattern) {

        if (date == null) {

            return null;
        }

        try {

            SimpleDateFormat sfDate = new SimpleDateFormat(pattern);
            sfDate.setLenient(false);

            return sfDate.format(date);
        } catch (Exception e) {

            return null;
        }
    }

    /**
     * yyyy-MM-dd
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        return dateToString(date, DATE_FORMAT);
    }

    /**  
     * @return  
     */
    public static Date getCurrentDateTime() {
        Calendar calNow = Calendar.getInstance();
        Date dtNow = calNow.getTime();

        return dtNow;
    }

    /**
     *  
     * @param pattern  
     * @return
     */
    public static String getCurrentDateString(String pattern) {
        return dateToString(getCurrentDateTime(), pattern);
    }

    /**
     *   yyyy-MM-dd
     * @return
     */
    public static String getCurrentDateString() {
        return dateToString(getCurrentDateTime(), ISO_EXPANDED_DATE_FORMAT);
    }

    /**
     * 返回固定格式的当前时间
     *   yyyy-MM-dd hh:mm:ss
     * @param date
     * @return
     */
    public static String dateToStringWithTime( ) {

        return dateToString(new Date(), DATETIME_PATTERN);
    }

    
    /**
     *   yyyy-MM-dd hh:mm:ss
     * @param date
     * @return
     */
    public static String dateToStringWithTime(Date date) {

        return dateToString(date, DATETIME_PATTERN);
    }

    /**
     *  
     * @param date
     * @param days
     * @return java.util.Date
     */
    public static Date dateIncreaseByDay(Date date, int days) {

        Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone(
                "GMT"));
        cal.setTime(date);
        cal.add(Calendar.DATE, days);

        return cal.getTime();
    }

    /**
     *  
     * @param date
     * @param days
     * @return java.util.Date
     */
    public static Date dateIncreaseByMonth(Date date, int mnt) {

        Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone(
                "GMT"));
        cal.setTime(date);
        cal.add(Calendar.MONTH, mnt);

        return cal.getTime();
    }

    /**
     *  
     * @param date
     * @param mnt
     * @return java.util.Date
     */
    public static Date dateIncreaseByYear(Date date, int mnt) {

        Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone(
                "GMT"));
        cal.setTime(date);
        cal.add(Calendar.YEAR, mnt);

        return cal.getTime();
    }

    /**
     *  
     * @param date   yyyy-MM-dd
     * @param days
     * @return  yyyy-MM-dd
     */
    public static String dateIncreaseByDay(String date, int days) {
        return dateIncreaseByDay(date, ISO_DATE_FORMAT, days);
    }

    /**
     * @param date  
     * @param fmt  
     * @param days
     * @return
     */
    public static String dateIncreaseByDay(String date, String fmt, int days) {
        return dateIncrease(date, fmt, Calendar.DATE, days);
    }

    /**
     *  
     * @param src  
     * @param srcfmt  
     * @param desfmt 
     * @return
     */
    public static String stringToString(String src, String srcfmt,
                                        String desfmt) {
        return dateToString(stringToDate(src, srcfmt), desfmt);
    }

    /**
     *  
     * @param date  
     * @return string
     */
    public static String getYear(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat(
                "yyyy");
        String cur_year = formater.format(date);
        return cur_year;
    }

    /**
     *  
     * @param date  
     * @return string
     */
    public static String getMonth(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat(
                "MM");
        String cur_month = formater.format(date);
        return cur_month;
    }

    /**
     * @param date  
     * @return string
     */
    public static String getDay(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat(
                "dd");
        String cur_day = formater.format(date);
        return cur_day;
    }
    
    public static int getDayInt(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat(
                "dd");
        String cur_day = formater.format(date);
        return Integer.valueOf(cur_day);
    }
    
    /**
     * @param date  
     * @return string
     */
    public static String getHour(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat(
                "HH");
        String cur_day = formater.format(date);
        return cur_day;
    }    

    public static int getMinsFromDate(Date dt) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dt);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        return ((hour * 60) + min);
    }

    /**
     * Function to convert String to Date Object. If invalid input then current or next day date
     * is returned (Added by Ali Naqvi on 2006-5-16).
     * @param str String input in YYYY-MM-DD HH:MM[:SS] format.
     * @param isExpiry boolean if set and input string is invalid then next day date is returned
     * @return Date
     */
    public static Date convertToDate(String str, boolean isExpiry) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dt = null;
        try {
            dt = fmt.parse(str);
        } catch (ParseException ex) {
            Calendar cal = Calendar.getInstance();
            if (isExpiry) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
            } else {
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
            }
            dt = cal.getTime();
        }
        return dt;
    }

    public static Date convertToDate(String str) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date dt = null;
        try {
            dt = fmt.parse(str);
        } catch (ParseException ex) {
            dt = new Date();
        }
        return dt;
    }

    public static String dateFromat(Date date, int minute) {
        String dateFormat = null;
        int year = Integer.parseInt(getYear(date));
        int month = Integer.parseInt(getMonth(date));
        int day = Integer.parseInt(getDay(date));
        int hour = minute / 60;
        int min = minute % 60;
        dateFormat = String.valueOf(year)
                     +
                     (month > 9 ? String.valueOf(month) :
                      "0" + String.valueOf(month))
                     +
                     (day > 9 ? String.valueOf(day) : "0" + String.valueOf(day))
                     + " "
                     +
                     (hour > 9 ? String.valueOf(hour) : "0" + String.valueOf(hour))
                     +
                     (min > 9 ? String.valueOf(min) : "0" + String.valueOf(min))
                     + "00";
        return dateFormat;
    }
    
    public static String sDateFormat() {
    	return new SimpleDateFormat(DATE_PATTERN).format(Calendar.getInstance().getTime());	
    }
    
    /**
     * 
     * @Description: 获得本月的第一天日期
     * @return
     * 
     * @author leechenxiang
     * @date 2017年5月31日 下午1:37:34
     */
    public static String getFirstDateOfThisMonth() {
    	
    	SimpleDateFormat format = new SimpleDateFormat(ISO_EXPANDED_DATE_FORMAT);
		
		Calendar calendarFirst = Calendar.getInstance();
		calendarFirst = Calendar.getInstance();  
        calendarFirst.add(Calendar.MONTH, 0);  
        calendarFirst.set(Calendar.DAY_OF_MONTH, 1);  
        String firstDate = format.format(calendarFirst.getTime()); 
        
        return firstDate;
    }
    
    /**
     * 
     * @Description: 获得本月的最后一天日期
     * @return
     * 
     * @author leechenxiang
     * @date 2017年5月31日 下午1:37:50
     */
    public static String getLastDateOfThisMonth() {
    	SimpleDateFormat format = new SimpleDateFormat(ISO_EXPANDED_DATE_FORMAT);  
		
		Calendar calendarLast = Calendar.getInstance();
		calendarLast.setTime(new Date());
		calendarLast.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		String lastDate = format.format(calendarLast.getTime());  
		return lastDate;
    }
    
    /**
     * @Description: 判断字符串日期是否匹配指定的格式化日期
     */
	public static boolean isValidDate(String strDate, String formatter) {
		SimpleDateFormat sdf = null;
		ParsePosition pos = new ParsePosition(0);

		if (StringUtils.isBlank(strDate) || StringUtils.isBlank(formatter)) {
			return false;
		}
		try {
			sdf = new SimpleDateFormat(formatter);
			sdf.setLenient(false);
			Date date = sdf.parse(strDate, pos);
			if (date == null) {
				return false;
			} else {
				if (pos.getIndex() > sdf.format(date).length()) {
					return false;
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

    /**
     * 比较date1和date2,date1晚于或等于date2返回true,date1早于date2返回false
     * 日期格斯 YYYY-MM-dd HH:mm:ss
     * @param date1
     * @param date2
     * @return
     */
	public static boolean sortDateString(String  date1,String date2)
    {
        boolean flag1 = DateUtil.isValidDate(date1,DATETIME_PATTERN);
        boolean flag2 = DateUtil.isValidDate(date2,DATETIME_PATTERN);
        if(flag1 == false||flag2 == false)
        {
            throw new InputMismatchException("输入格式错误");
        }

        //比较年份
        int year1Index = date1.indexOf("-");
        int year1 = Integer.parseInt(date1.substring(0,year1Index));

        int year2Index = date2.indexOf("-");
        int year2 = Integer.parseInt(date1.substring(0,year2Index));

//        System.out.println(year1+"   "+year2);

        if(year1>year2)
        {
            return true;
        }else if(year1<year2)
        {
            return false;
        }else
        {
            //年份相同,比较月份
            int month1Index = date1.lastIndexOf("-");
            int month1 = Integer.parseInt(date1.substring(year1Index+1,month1Index));

            int month2Index = date2.lastIndexOf("-");
            int month2 = Integer.parseInt(date2.substring(year2Index+1,month2Index));
            //System.out.println(month1+"   "+month2);

            if(month1>month2)
            {
                return true;
            }else if(month1<month2)
            {
                return false;
            }
            else
            {
                //日份相同比较月份
                int day1Index = date1.lastIndexOf(" ");
                int day1 = Integer.parseInt(date1.substring(month1Index+1,day1Index));

                int day2Index = date2.lastIndexOf(" ");
                int day2 = Integer.parseInt(date2.substring(month2Index+1,day2Index));
                //System.out.println(day1+"   "+day2);


                if(day1>day2)
                {
                    return true;
                }else if(day1<day2)
                {
                    return false;
                }else
                {
                    //年月日都相同比较小时
                    int hour1Index = date1.indexOf(":");
                    int hour1 = Integer.parseInt(date1.substring(day1Index+1,hour1Index));

                    int hour2Index = date2.indexOf(":");
                    int hour2 = Integer.parseInt(date2.substring(day2Index+1,hour2Index));

                    //System.out.println(hour1+"   "+hour2);

                    if(hour1>hour2)
                    {
                        return true;
                    }else if(hour1<hour2)
                    {
                        return false;
                    }else {

                        //比较分钟
                        int minute1Index = date1.lastIndexOf(":");
                        int minute1 = Integer.parseInt(date1.substring(hour1Index+1,minute1Index));

                        int minute2Index = date2.lastIndexOf(":");
                        int minute2 = Integer.parseInt(date2.substring(hour1Index+1,minute2Index));

                        //System.out.println(minute1+"   "+minute2);

                        if(minute1>minute2)
                        {
                            return true;
                        }else if(minute1<minute2)
                        {
                            return false;
                        }else
                        {
                            //比较秒
                            int second1 = Integer.parseInt(date1.substring(minute1Index+1));

                            int second2 = Integer.parseInt(date2.substring(minute2Index+1));

                            //System.out.println(second1+"   "+second2);
                            if(second1>second2)
                            {
                                return true;
                            }else if(second1<second2)
                            {
                                return false;
                            }else
                            {
                                return true;
                            }
                        }


                    }
                }
            }
        }
    }

    public static String datetoString2(Date start,Date end)
    {

        return dateToString(start)+"-"+dateToString(end);
    }

    public static String TimeStamp2Date(String timestampString) {
        String formats = "yyyy-MM-dd HH:mm:ss";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        //日期格式字符串
        String dateStr = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        System.err.println(dateStr);
        Date date = null;
        SimpleDateFormat formater = new SimpleDateFormat();
        formater.applyPattern("yyyy-MM-dd HH:mm:ss");
        try {
            date = formater.parse(dateStr);
            //System.err.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public static void main(String[] args) {
        Date date = new Date(System.currentTimeMillis());
        System.out.println(datetoString2(date,date));
    }
    
}
