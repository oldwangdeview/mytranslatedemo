package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

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
import translatedemo.com.translatedemo.bean.InformationBean;
import translatedemo.com.translatedemo.bean.ListBean_information;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.eventbus.UpdataInfomation;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/6 0006.
 * 资讯详情
 */

public class NoticeDetailActivity extends BaseActivity{
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    ListBean_information data;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.user)
    TextView user;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.isshow)
    TextView isshow;
    @BindView(R.id.zan)
    TextView zan;
    @BindView(R.id.zan_image)
    ImageView zan_image;
    @BindView(R.id.tv_small_title_layout_head)
    TextView tv_small_title_layout_head;

    @BindView(R.id.shred_image)
    ImageView shred_image;
    @Override
    protected void initView() {
      setContentView(R.layout.activity_noticedetail);
    }

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.noticedetail_text_titlename));
        tv_small_title_layout_head.setText("");
        shred_image.setImageResource(R.mipmap.fenxiang3);
        shred_image.setVisibility(View.VISIBLE);
        tv_small_title_layout_head.setVisibility(View.GONE);
        data = (ListBean_information)getIntent().getSerializableExtra(Contans.INTENT_DATA);
        isShow();


    }
    private Dialog mLoadingDialog;
    @OnClick({R.id.zan_image,R.id.zan})
    public void zandata(){
        if(data.isZan==0){
            Log.e("data.id",data.id+"");
            Log.e("data.id",data.type+"");
            Log.e("data.id",BaseActivity.getuser().id+"");

            Observable observable =
                    ApiUtils.getApi().zanInformation(BaseActivity.getLanguetype(NoticeDetailActivity.this),data.id,data.type==0?1:0,BaseActivity.getuser().id+"")
                            .compose(RxHelper.getObservaleTransformer())
                            .doOnSubscribe(new Consumer<Disposable>() {
                                @Override
                                public void accept(Disposable disposable) throws Exception {
                                    try {


                                        if (mLoadingDialog == null) {
                                            mLoadingDialog = LoadingDialogUtils.createLoadingDialog(NoticeDetailActivity.this, "");
                                        }
                                        LoadingDialogUtils.show(mLoadingDialog);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .subscribeOn(AndroidSchedulers.mainThread());

            HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(NoticeDetailActivity.this) {
                @Override
                protected void _onNext(StatusCode<Object> stringStatusCode) {
                    new LogUntil(NoticeDetailActivity.this,TAG+"zixunmessage",new Gson().toJson(stringStatusCode));
                    LoadingDialogUtils.closeDialog(mLoadingDialog);
                    zan_image.setImageResource(R.mipmap.zan2);
                    data.isZan=1;
                    isShow();
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.makeText(message);
                    LoadingDialogUtils.closeDialog(mLoadingDialog);

                }
            }, "", lifecycleSubject, false, true);


        }else{
            ToastUtils.makeText(this.getResources().getString(R.string.noticedatail_text_iszan));
        }
    }

    private void isShow(){

        Observable observable =
                ApiUtils.getApi().readInformation(BaseActivity.getLanguetype(NoticeDetailActivity.this),data.id,BaseActivity.getuser().id+"")
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                try {


                                    if (mLoadingDialog == null) {
                                        mLoadingDialog = LoadingDialogUtils.createLoadingDialog(NoticeDetailActivity.this, "");
                                    }
                                    LoadingDialogUtils.show(mLoadingDialog);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<ListBean_information>(NoticeDetailActivity.this) {
            @Override
            protected void _onNext(StatusCode<ListBean_information> stringStatusCode) {
                new LogUntil(NoticeDetailActivity.this,TAG+"readInformation",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
               if(stringStatusCode!=null&&stringStatusCode.getCode()==0&&stringStatusCode.getData()!=null){
                   data=stringStatusCode.getData();
               }
                initview();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                initview();
            }
        }, "", lifecycleSubject, false, true);

    }


    private void initview(){
        if(!TextUtils.isEmpty(data.title)){
            title.setText(data.title);
        }else{
            title.setText("");
        }

        if(!TextUtils.isEmpty(data.author)){
            user.setText(data.author);
        }else{
            user.setText("");
        }

        if(!TextUtils.isEmpty(data.createTime)){
            time.setText(UIUtils.gettime(data.createTime));
        }else{
            time.setText("");
        }

        if(!TextUtils.isEmpty(data.content)){

            HtmlText.from(data.content)
                    .setImageLoader(new HtmlImageLoader() {
                        @Override
                        public void loadImage(String url, final Callback callback) {
                            // Glide sample, you can also use other image loader
                            SimpleTarget<Bitmap> into = Glide.with(NoticeDetailActivity.this)
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
                            return ContextCompat.getDrawable(NoticeDetailActivity.this, R.mipmap.buffer);
                        }

                        @Override
                        public Drawable getErrorDrawable() {
                            return ContextCompat.getDrawable(NoticeDetailActivity.this, R.mipmap.buffer);
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


        if(!TextUtils.isEmpty(data.lookCount +"")){
            isshow.setText(data.lookCount +"");
        }else{
            isshow.setText("0");
        }
        if(!TextUtils.isEmpty(data.zan+"")){
            zan.setText(data.zan+"");
        }else{
            zan.setText("0");
        }
        if(!TextUtils.isEmpty(data.isZan+"")){
            if(data.isZan==0){
                zan_image.setImageResource(R.mipmap.zan1);
            }else{
                zan_image.setImageResource(R.mipmap.zan2);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UIUtils.showFullScreen(NoticeDetailActivity.this,false);
        updateactionbar();
    }
    public static void startactivity(Context mContext, ListBean_information data){
        Intent mIntent = new Intent(mContext,NoticeDetailActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable)data);
        mContext.startActivity(mIntent);

    }

    /**
     * 分享点击
     */
    @OnClick(R.id.tv_small_title_layout_head)
    public void shard(){

    }
}
