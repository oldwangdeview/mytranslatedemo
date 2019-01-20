package translatedemo.com.translatedemo.util;

public class LogUtils {
	public static int isDebug = 0;

	public static void v(String tag, String msg) {
		if (isDebug<1)
			android.util.Log.v(tag, msg);
	}

	public static void v(String tag, String msg, Throwable t) {
		if (isDebug<1)
			android.util.Log.v(tag, msg, t);
	}
	
	public static void d(String msg){
		if(isDebug<2)
			android.util.Log.d("MyLog", msg);
	}

	public static void d(String tag, String msg) {
		if (isDebug<2)
			android.util.Log.d(tag, msg);
	}

	public static void d(String tag, String msg, Throwable t) {
		if (isDebug<2)
			android.util.Log.d(tag, msg, t);
	}

	public static void i(String tag, String msg) {
		if (isDebug<3)
			android.util.Log.i(tag, msg);
	}

	public static void i(String tag, String msg, Throwable t) {
		if (isDebug<3)
			android.util.Log.i(tag, msg, t);
	}

	public static void w(String tag, String msg) {
		if (isDebug<4)
			android.util.Log.w(tag, msg);
	}

	public static void w(String tag, String msg, Throwable t) {
		if (isDebug<4)
			android.util.Log.w(tag, msg, t);
	}

	public static void e(String tag, String msg) {
		if (isDebug<5)
			android.util.Log.e(tag, msg);
	}

	public static void e(String tag, String msg, Throwable t) {
		if (isDebug<5)
			android.util.Log.e(tag, msg, t);
	}
}
