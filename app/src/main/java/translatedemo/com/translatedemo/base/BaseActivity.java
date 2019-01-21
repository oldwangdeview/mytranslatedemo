package translatedemo.com.translatedemo.base;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.PublishSubject;
import translatedemo.com.translatedemo.bean.LoginBean;
import translatedemo.com.translatedemo.languageutil.LanguageUtil;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.PreferencesUtils;

/**
 * Author oldwang
 * Time  2017/11/27 16:36
 * Dest  Activity的基类
 */

public abstract class BaseActivity extends AppCompatActivity {
    public String TAG=this.getClass().getSimpleName();
    private Unbinder mUnbinder;
    public final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject = PublishSubject.create();
    private Dialog mLoadingDialog;
    public static String LOGINUSER="loginuser";
    public Context mcontent = this;
    public static LoginBean user = null;
    public static int LAGEVAGETYPE = -1;//当前语言默认中文

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        lifecycleSubject.onNext(ActivityLifeCycleEvent.CREATE);
        Log.e("qidongactiity",TAG);
        super.onCreate(savedInstanceState);
    /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }*/
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP&&Build.MANUFACTURER.equals("Xiaomi")) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initView();
        mUnbinder = ButterKnife.bind(this);
        initData();
        initEvent();



    }


    public static LoginBean getuser(){
        try {
            if (user == null) {
                String loginuser = PreferencesUtils.getInstance().getString(LOGINUSER, "");
                if (TextUtils.isEmpty(loginuser)) {
                    return null;
                } else {
                    user = new Gson().fromJson(loginuser, LoginBean.class);
                    if (user != null) {
                        return user;
                    } else {
                        return null;
                    }
                }

            } else {
                return user;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static List<String> gettestdata(){
         List<String> testdata = new ArrayList<>();
         for(int i = 0; i<10;i++){
             testdata.add("1");
         }
         return testdata;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences preferences = newBase.getSharedPreferences("language", Context.MODE_PRIVATE);
        String selectedLanguage = preferences.getString("language", "");
        super.attachBaseContext(LanguageUtil.attachBaseContext(newBase, selectedLanguage));

    }

    /**
     * 展示加载弹框
     *
     * @author wangshifu
     * @version v1.0, 2018/12/29
     */
    protected void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialogUtils.createLoadingDialog(this, "");
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    /**
     * 隐藏加载弹框
     *
     * @author wangshifu
     * @version v1.0, 2018/12/29
     */
    protected void dismissLoadingDialog(){
        LoadingDialogUtils.closeDialog(mLoadingDialog);
    }

    /**
     * 初始化事件
     */
    protected  void initEvent(){};
    /**
     * 初始化数据
     */
    protected  void initData(){};

    /**
     * 初始化界面
     */
    protected abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("qidongactiity","onDestroy"+TAG);
//        lifecycleSubject.onNext(ActivityLifeCycleEvent.DESTROY);
//        if (mUnbinder!=null){
//            mUnbinder.unbind();
//        }

    }
//    @Override
//    protected void onPause() {
//        lifecycleSubject.onNext(ActivityLifeCycleEvent.PAUSE);
//        super.onPause();
//
//    }
//
//    @Override
//    protected void onStop() {
//        lifecycleSubject.onNext(ActivityLifeCycleEvent.STOP);
//
//        super.onStop();
//    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onStart() {
        super.onStart();
       
    }
    /**
     * 刷新小米顶部状态栏图标颜色
     */
    public void updateactionbar(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 获取当前选择语言
     * @param mContext
     * @return
     */

    public static int getLanguetype(Context mContext){
        if(LAGEVAGETYPE<0) {
            SharedPreferences preferences = mContext.getSharedPreferences("language", Context.MODE_PRIVATE);
            String selectedLanguage = preferences.getString("language", "");
            if (!TextUtils.isEmpty(selectedLanguage)) {
                if (selectedLanguage.equals("zh")) {
                    LAGEVAGETYPE = 0;
                } else {
                    LAGEVAGETYPE = 1;
                }
            }
        }
        return  LAGEVAGETYPE;
    }


}
