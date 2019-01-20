package translatedemo.com.translatedemo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * @autor wangshifu
 * @time 2017/3/2313:41
 * @des 网络工具类
 */
public class NetUtils<T> {
    private static final String TAG = "NetUtils";

    /**
     *
     * 网络是否可用
     * @param context
     * @return
     */
    public static boolean NetwrokIsUseful(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {

            Log.v("error", e.toString());
        }
        return false;
    }

    /**
     * 是否使用wifi连接
     * @param context
     * @return
     */
    public static boolean isWIFINetWork(Context context) {
        try {

            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivity != null) {
                NetworkInfo wifi = connectivity
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI); // wifi
                // NetworkInfo gprs =
                // connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                // // gprs
                if (wifi != null && wifi.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.v("error", e.toString());
        }
        return false;
    }
    /**
     * 本地ip
     * @return
     */
    public static String getlocalip(Context context){
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        Log.i(TAG, "int ip "+ipAddress);
        if(ipAddress==0)return "";
        return ((ipAddress & 0xff)+"."+(ipAddress>>8 & 0xff)+"."
                +(ipAddress>>16 & 0xff)+"."+(ipAddress>>24 & 0xff));
    }

    /**
     * 获取mac
     * @param context
     * @return
     */
    public static String getMac(Context context) {

        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);

        WifiInfo info = wifi.getConnectionInfo();

        return info.getMacAddress();

    }










}
