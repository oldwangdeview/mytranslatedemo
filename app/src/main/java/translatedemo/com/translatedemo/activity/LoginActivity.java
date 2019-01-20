package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.application.BaseApplication;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.LoginBean;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.util.DeviceUuidFactory;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.PreferencesUtils;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;

import static translatedemo.com.translatedemo.application.BaseApplication.mContext;

/**
 * Created by Administrator on 2018/12/27 0027.
 */

public class LoginActivity  extends BaseActivity {
    @BindView(R.id.login_inputeditext_phone)
    EditText inoutphone;

    @BindView(R.id.login_inoputeditext_code)
    EditText inputcode;

    private Dialog mLoadingDialog;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initData() {
        super.initData();
        if(BaseActivity.getuser()!=null){
            MainActivity.startactivity(LoginActivity.this,1);
            finish();
        }
    }


    @OnClick({R.id.login_text_getcode,R.id.login_button_reture})
    public void mOnclick(View v){
        switch (v.getId()){
            case R.id.login_text_getcode:

                break;
            case R.id.login_button_reture:
                String phone = inoutphone.getText().toString().trim();
                String code = inputcode.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    ToastUtils.makeText(getResources().getString(R.string.login_error_notphonenumber));
                    return;
                }
//                if(!UIUtils.isPhoneNumber(phone)){
//                    ToastUtils.makeText(getResources().getString(R.string.login_error_isnotphone));
//                    return;
//                }
                if(TextUtils.isEmpty(code)){
                    ToastUtils.makeText(getResources().getString(R.string.login_error_notcode));
                    return;
                }
                login(phone,code);


                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateactionbar();
    }

    /**
     * 查看用户协议
     */
    @OnClick(R.id.login_text_useragremeet)
    public void gotouseragreement (){
        UserAgreementActivity.statrtactivity(this);
    }

    /**
     * QQ登陆
     */

    @OnClick(R.id.login_image_qq)
    public void logininQQ(){
        UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, authListener);
        BindPhoneActivity.statrtactivity(this);
        finish();
    }

    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }
        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(mContext, "成功了", Toast.LENGTH_LONG).show();
        }
        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(mContext, "失败：" + t.getMessage(),                                     Toast.LENGTH_LONG).show();
        }
        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(mContext, "取消了", Toast.LENGTH_LONG).show();
        }
    };
    /**
     * 微信登陆
     */
    @OnClick(R.id.login_image_wechart)
    public void loginwechart(){
        UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, authListener);
        BindPhoneActivity.statrtactivity(this);
        finish();
    }


    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,LoginActivity.class);
        mContext.startActivity(mIntent);

    }

    public void login(String phone,String code){


        Observable observable =
                ApiUtils.getApi().loginforphone(phone,code,BaseActivity.getLanguetype(this), DeviceUuidFactory.getInstance(LoginActivity.this).getDeviceUuid())
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(LoginActivity.this, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<LoginBean>(LoginActivity.this) {
            @Override
            protected void _onNext(StatusCode<LoginBean> stringStatusCode) {
                new LogUntil(LoginActivity.this,TAG+"loginreturn",new Gson().toJson(stringStatusCode));

                PreferencesUtils.getInstance().putString(BaseActivity.LOGINUSER,new Gson().toJson(stringStatusCode.getData()));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                MainActivity.startactivity(LoginActivity.this,0);
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
//
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
