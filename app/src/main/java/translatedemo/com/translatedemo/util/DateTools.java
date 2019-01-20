package translatedemo.com.translatedemo.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTools {

	public static String getDate(String format) {
		SimpleDateFormat fm = new SimpleDateFormat(format);
		return fm.format(new Date());
	}

    public static String getFileNameStr(){
    	return getDate("yyyyMMdd");
    }
	
	public static String getDate1() {
		return getDate("HHmmss");
	}
	public static String getDate() {
		return getDate("HH:mm:ss");
	}
	public static String getCurrentDay() {
		return getDate("yyyy:MM:dd");
	}
	public static String getCrashDay() {
		return getDate("yyyy-MM-dd");
	}

	public static String getCrashSaveDay() {
		return getDate("yyyy-MM-dd-mm");
	}

	public static String getCoverDate() {
		return getDate("yyyy:MM:dd HH:mm:ss");
	}

	public static String getLineDate() {
		return getDate("yyyy-MM-dd HH-mm-ss");
	}
	public static String getMixedDate() {
		return getDate("yyyy-MM-dd HH:mm:ss");
	}
	public static String getStringTime(){
		return getDate("yyyyMMddHHmmss");
	}


	public static long gettime(String s){
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH时");
		Date date=null;
		try {
			date = df.parse(s);

			return date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return 0;
	}

	public static String getFormat(long longtime){
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH");
		Date date=new Date(longtime);
		String format = fm.format(date);
		return format;
	}


	public static long getcurrentTime(){
		return new Date().getTime();
	}


	public static String getOldDate(int count) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -count); // 得到前一天
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy:MM:dd");
		return df.format(date);
	}
	
	public static String getDateAsFileName(){
		return getDate("yyyyMMdd_HHmmss");
	}

	public static String getDateByString(String dateString) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
		Date date=null;
		try {
			date = df.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dateString=df.format(date);
		return dateString;
	}




}
