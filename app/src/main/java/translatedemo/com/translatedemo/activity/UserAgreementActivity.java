package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.wcy.htmltext.HtmlImageLoader;
import me.wcy.htmltext.HtmlText;
import me.wcy.htmltext.OnTagClickListener;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.ConfigBean;
import translatedemo.com.translatedemo.bean.LoginBean;
import translatedemo.com.translatedemo.bean.NoticeListBean;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.LogUtils;
import translatedemo.com.translatedemo.util.PreferencesUtils;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by oldwang on 2019/1/4 0004.
 */

public class UserAgreementActivity extends BaseActivity {


    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.title_layout)
    LinearLayout title_layout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.user)
    TextView user;
    @BindView(R.id.time)
    TextView time;
    private Dialog mLoadingDialog;
    private int type = 2;
    private NoticeListBean mcontent = null;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_useragreement);
    }

    @Override
    protected void initData() {
        super.initData();
        title_name.setText(getResources().getString(R.string.user_agreementment_text_tiltlemessage));
        iv_back_activity_text.setVisibility(View.VISIBLE);
        type = getIntent().getIntExtra(Contans.INTENT_TYPE,2);
        mcontent =(NoticeListBean)getIntent().getSerializableExtra(Contans.INTENT_DATA);
        content.setMovementMethod(LinkMovementMethod.getInstance());
        title_layout.setVisibility(View.GONE);
        if(type==2&&mcontent==null) {
            getconfig(2);
        }else if(mcontent!=null){

            title_name.setText(getResources().getString(R.string.user_agreementment_text_tiltlemessage1));
            title_layout.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(mcontent.field)){
                title.setText(mcontent.field);
            }
            user.setText("");
            if(!TextUtils.isEmpty(mcontent.createTime)){
                time.setText(UIUtils.gettime(mcontent.createTime));
            }
            HtmlText.from(mcontent.content)
                    .setImageLoader(new HtmlImageLoader() {
                        @Override
                        public void loadImage(String url, final Callback callback) {
                            // Glide sample, you can also use other image loader
                            SimpleTarget<Bitmap> into = Glide.with(UserAgreementActivity.this)
                                    .load(url)
                                    .asBitmap()
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(Bitmap resource,
                                                                    GlideAnimation<? super Bitmap> glideAnimation) {
                                            callback.onLoadComplete(resource);
                                        }

                                        @Override
                                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                            callback.onLoadFailed();
                                        }
                                    });
                        }

                        @Override
                        public Drawable getDefaultDrawable() {
                            return ContextCompat.getDrawable(UserAgreementActivity.this, R.mipmap.buffer);
                        }

                        @Override
                        public Drawable getErrorDrawable() {
                            return ContextCompat.getDrawable(UserAgreementActivity.this, R.mipmap.buffer);
                        }

                        @Override
                        public int getMaxWidth() {
                            return content.getMaxWidth();
                        }

                        @Override
                        public boolean fitWidth() {
                            return false;
                        }
                    })
                    .setOnTagClickListener(new OnTagClickListener() {
                        @Override
                        public void onImageClick(Context context, List<String> imageUrlList, int position) {
                            // image click
                        }

                        @Override
                        public void onLinkClick(Context context, String url) {
                            // link click
                        }
                    })
                    .into(content);
        }else{
            title_name.setText(getResources().getString(R.string.seting_text_aboutus));
            getconfig(type);
        }

    }
    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overActivity(){
        finish();
    }

    public static void statrtactivity(Context mContext){
        Intent mintent = new Intent(mContext,UserAgreementActivity.class);
        mContext.startActivity(mintent);
    }

    public static void  statrtactivity(Context mContext,int type){
        Intent mintent = new Intent(mContext,UserAgreementActivity.class);
        mintent.putExtra(Contans.INTENT_TYPE,type);
        mContext.startActivity(mintent);
    }
    public static void  statrtactivity(Context mContext,NoticeListBean content){
        Intent mintent = new Intent(mContext,UserAgreementActivity.class);
        mintent.putExtra(Contans.INTENT_DATA,(Serializable) content);
        mContext.startActivity(mintent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateactionbar();
    }

    public void getconfig(int type){


        Observable observable =
                ApiUtils.getApi().getconfig(type,BaseActivity.getLanguetype(this))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(UserAgreementActivity.this, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<ConfigBean>(UserAgreementActivity.this) {
            @Override
            protected void _onNext(StatusCode<ConfigBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                new LogUntil(UserAgreementActivity.this,"content",stringStatusCode.getData().content);
                if(!TextUtils.isEmpty(stringStatusCode.getData().content)) {
                    HtmlText.from(stringStatusCode.getData().content)
                            .setImageLoader(new HtmlImageLoader() {
                                @Override
                                public void loadImage(String url, final Callback callback) {
                                    // Glide sample, you can also use other image loader
                                    SimpleTarget<Bitmap> into = Glide.with(UserAgreementActivity.this)
                                            .load(url)
                                            .asBitmap()
                                            .into(new SimpleTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(Bitmap resource,
                                                                            GlideAnimation<? super Bitmap> glideAnimation) {
                                                    callback.onLoadComplete(resource);
                                                }

                                                @Override
                                                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                                    callback.onLoadFailed();
                                                }
                                            });
                                }

                                @Override
                                public Drawable getDefaultDrawable() {
                                    return ContextCompat.getDrawable(UserAgreementActivity.this, R.mipmap.buffer);
                                }

                                @Override
                                public Drawable getErrorDrawable() {
                                    return ContextCompat.getDrawable(UserAgreementActivity.this, R.mipmap.buffer);
                                }

                                @Override
                                public int getMaxWidth() {
                                    return content.getMaxWidth();
                                }

                                @Override
                                public boolean fitWidth() {
                                    return false;
                                }
                            })
                            .setOnTagClickListener(new OnTagClickListener() {
                                @Override
                                public void onImageClick(Context context, List<String> imageUrlList, int position) {
                                    // image click
                                }

                                @Override
                                public void onLinkClick(Context context, String url) {
                                    // link click
                                }
                            })
                            .into(content);
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
//
    }
}
