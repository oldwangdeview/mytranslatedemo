package translatedemo.com.translatedemo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Author wangshifu
 * Time  2018/5/22 9:35
 * Dest 获取android id
 * Dest 获取android id
 */

public class DeviceUuidFactory {
    protected static final String PREFS_FILE = "device_id";

    protected static final String PREFS_DEVICE_ID = "device_id";

    protected static UUID uuid;

    private static String deviceType = "0";

    private static final String TYPE_ANDROID_ID = "1";

    private static final String TYPE_DEVICE_ID = "2";

    private static final String TYPE_RANDOM_UUID = "3";

    private DeviceUuidFactory(Context context) {
        if (uuid == null) {
            synchronized (DeviceUuidFactory.class) {
                if (uuid == null) {
                    final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null);
                    if (id != null) {
                        uuid = UUID.fromString(id);
                    } else {
                        try {
                                final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                                if (deviceId != null
                                        && !"0123456789abcdef".equals(deviceId.toLowerCase())
                                        && !"000000000000000".equals(deviceId.toLowerCase())) {
                                    deviceType = TYPE_DEVICE_ID;
                                    uuid = UUID.nameUUIDFromBytes(deviceId.getBytes("utf8"));
                                } else {
                                    deviceType = TYPE_RANDOM_UUID;
                                    uuid = UUID.randomUUID();

                            }
                        } catch (UnsupportedEncodingException e) {
                            deviceType = TYPE_RANDOM_UUID;
                            uuid = UUID.randomUUID();
                        } finally {
                            uuid = UUID.fromString(deviceType + uuid.toString());
                        }
                        prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).commit();
                    }
                }
            }
        }
    }
  private  static DeviceUuidFactory mDeviceUuidFactory;
public static DeviceUuidFactory getInstance(Context context){
        if (mDeviceUuidFactory==null){
            mDeviceUuidFactory=new DeviceUuidFactory(context);
        }

        return mDeviceUuidFactory;


}



    public String getDeviceUuid() {
        LogUtils.d("DeviceUuidFactory", "------>获取的设备ID号为：" + uuid.toString().replace("-",""));
        return uuid.toString().replace("-","");
    }
}
