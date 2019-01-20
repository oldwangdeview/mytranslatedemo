package translatedemo.com.translatedemo.util;

import android.content.Context;
import android.content.SharedPreferences;

import translatedemo.com.translatedemo.application.BaseApplication;


/**
 * @autor wangshifu
 * @time 2017/3/2218:09
 * @des ${TODO}
 */
public class PreferencesUtils {
   private static PreferencesUtils mPreferences;
    private final SharedPreferences mSharedPreferences;

    private PreferencesUtils(){
        mSharedPreferences = BaseApplication.mContext.getSharedPreferences("info", Context.MODE_PRIVATE);
    }
    public static PreferencesUtils getInstance(){
        if (mPreferences==null ){
            mPreferences= new PreferencesUtils();
        }
        return mPreferences;
    }
    public void putString (String key, String value){
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();

    }
    public String getString (String key, String defaultValue){
        String value = mSharedPreferences.getString(key, defaultValue);
        return value;
    }
    public void putBoolean (String key, boolean value){
       // mPreferences.putBoolean(key, value);
        SharedPreferences.Editor edit = mSharedPreferences.edit();
         edit.putBoolean(key, value);
        edit.commit();

    }
    public boolean getBoolean(String key, boolean defValue){
        boolean aBoolean = mSharedPreferences.getBoolean(key, defValue);
        return aBoolean;
    }
    public void putInt(String key, int  value){
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putInt(key,value);
        edit.commit();

    }

    public int getInt(String key, int devalue){
        int anInt = mSharedPreferences.getInt(key, devalue);
        return anInt;

    }
}
