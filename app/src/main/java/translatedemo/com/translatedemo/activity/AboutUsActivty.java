package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

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
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/11 0011.
 */

public class AboutUsActivty  extends BaseActivity {
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.phone)
    TextView phone;
    private Dialog mLoadingDialog;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_aboutus);
    }
    @Override
    protected void onResume() {
        super.onResume();
        UIUtils.showFullScreen(AboutUsActivty.this,false);
        updateactionbar();
    }
    private String mphone = "";

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.myuserinfo_text_titlename));
        getconfig(1);

    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,AboutUsActivty.class);
        mContext.startActivity(mIntent);
    }

    @OnClick(R.id.phone)
    public void callphone(){
          if(!TextUtils.isEmpty(mphone)&&UIUtils.isPhoneNumber(mphone)){
              try {


              Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(mphone));
                  mcontent.startActivity(intent);;
              }catch (Exception e){

              }
          }else{
              ToastUtils.makeText("not PhoneNumber");
          }
    }

    public void getconfig(int type){


        Observable observable =
                ApiUtils.getApi().getconfig(type,BaseActivity.getLanguetype(this))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(AboutUsActivty.this, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<ConfigBean>(AboutUsActivty.this) {
            @Override
            protected void _onNext(StatusCode<ConfigBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                new LogUntil(AboutUsActivty.this,"content",stringStatusCode.getData().content);
                if(!TextUtils.isEmpty(stringStatusCode.getData().content)) {

                    HtmlText.from(stringStatusCode.getData().content)
                            .setImageLoader(new HtmlImageLoader() {
                                @Override
                                public void loadImage(String url, final Callback callback) {
                                    // Glide sample, you can also use other image loader
                                    SimpleTarget<Bitmap> into = Glide.with(AboutUsActivty.this)
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
                                    return ContextCompat.getDrawable(AboutUsActivty.this, R.mipmap.buffer);
                                }

                                @Override
                                public Drawable getErrorDrawable() {
                                    return ContextCompat.getDrawable(AboutUsActivty.this, R.mipmap.buffer);
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
                if(!TextUtils.isEmpty(stringStatusCode.getData().field)){
                    mphone = stringStatusCode.getData().field;
                    phone.setText(stringStatusCode.getData().field);
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

    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }
}
