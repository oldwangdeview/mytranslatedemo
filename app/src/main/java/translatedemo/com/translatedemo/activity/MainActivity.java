package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.adpater.MainPagerAdapter;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.LoginBean;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.eventbus.OverMainactivty;
import translatedemo.com.translatedemo.eventbus.UpdateMainIndex;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.util.FileUtils;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.PreferencesUtils;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;
import translatedemo.com.translatedemo.view.ForbidScrollViewpager;

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewpager_activity_main)
    public ForbidScrollViewpager mViewPager;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;


    private MainPagerAdapter mMainPagerAdapter;
    private Dialog mLoadingDialog;
    private int type = 0;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    mViewPager.setCurrentItem(0,false);
                    UIUtils.showFullScreen(MainActivity.this,true);
                    return true;
                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(1,false);
                    UIUtils.showFullScreen(MainActivity.this,true);
                    updateactionbar();
                    return true;
                case R.id.navigation_notifications:
                    mViewPager.setCurrentItem(2   ,false);
                    UIUtils.showFullScreen(MainActivity.this,true);
                    updateactionbar();
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
    }



    @Override
    protected void initData() {
        super.initData();

        EventBus.getDefault().register(this);
        UIUtils.showFullScreen(MainActivity.this,true);
        type = getIntent().getIntExtra(Contans.INTENT_TYPE,0);
        mMainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mMainPagerAdapter);
        mViewPager.setCurrentItem(0);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if(type>0){
            getuserinfo(BaseActivity.getuser().id+"");
        }
        creartFilepath();
    }

    public static void startactivity(Context mcontent,int type){
        Intent mIntent = new Intent(mcontent,MainActivity.class);
        mIntent.putExtra(Contans.INTENT_TYPE,type);
        mcontent.startActivity(mIntent);
    }




    public void getuserinfo(String id){


        Observable observable =
                ApiUtils.getApi().getuserinfo(id,BaseActivity.getLanguetype(this))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(MainActivity.this, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<LoginBean>(MainActivity.this) {
            @Override
            protected void _onNext(StatusCode<LoginBean> stringStatusCode) {
                PreferencesUtils.getInstance().putString(BaseActivity.LOGINUSER,new Gson().toJson(stringStatusCode.getData()));
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }

            @Override
            protected void _onError(String message) {
//                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
//
    }

    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    private void creartFilepath(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileUtils.APP_DIR = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/translate";
        } else {
            FileUtils.APP_DIR = this.getFilesDir().getAbsolutePath() + "/translate";
        }
        FileUtils.APP_LOG = FileUtils.APP_DIR + "/log";
        FileUtils.APP_CRASH = FileUtils.APP_DIR + "/crash";
        FileUtils.APK_DIR = FileUtils.APP_DIR + "/apks";
        FileUtils.IMAGE_DIR = FileUtils.APP_DIR + "/imag";

        File appDir = new File(FileUtils.APP_DIR);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        File logDir = new File(FileUtils.APP_LOG);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        File crashDir = new File(FileUtils.APP_CRASH);
        if (!crashDir.exists()) {
            crashDir.mkdirs();
        }
        File apkDir = new File(FileUtils.APK_DIR);
        if (!apkDir.exists()) {
            apkDir.mkdirs();
        }
        File imageDir = new File(FileUtils.IMAGE_DIR);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
    }
    private int mindex = 0;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void steindex(UpdateMainIndex index){

        if(index.index!=0) {
            mindex = index.index;
            mViewPager.setCurrentItem(index.index,false);
            Log.e("index",index.index+"");
            UIUtils.showFullScreen(MainActivity.this, false);
            updateactionbar();
        }else{
            navigation.setSelectedItemId(navigation.getMenu().getItem(index.index).getItemId());
            mOnNavigationItemSelectedListener.onNavigationItemSelected(navigation.getMenu().getItem(index.index));

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (keyCode == KeyEvent.KEYCODE_DEL&& event.getAction() == KeyEvent.ACTION_DOWN){
             return true;
         }

        if (keyCode == event.KEYCODE_BACK) {
             if(mindex!=0){
                 navigation.setSelectedItemId(navigation.getMenu().getItem(0).getItemId());
                 mOnNavigationItemSelectedListener.onNavigationItemSelected(navigation.getMenu().getItem(0));
             }else {
                 exit();
                 return false;
             }
            return true;
        }
        return false;

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finishMainactivity(OverMainactivty over){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    private void exit() {
        if (!isExit) {
            isExit = true;
             ToastUtils.makeText(mcontent.getResources().getString(R.string.exite_dialog));
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }
}
