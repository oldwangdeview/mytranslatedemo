package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
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

    @Override
    protected void initView() {
      setContentView(R.layout.activity_noticedetail);
    }

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.noticedetail_text_titlename));
        data = (ListBean_information)getIntent().getSerializableExtra(Contans.INTENT_DATA);
        isShow();


    }
    private Dialog mLoadingDialog;
    @OnClick({R.id.zan_image,R.id.zan})
    public void zandata(){
        if(data.isZan==0){

            Observable observable =
                    ApiUtils.getApi().zanInformation(BaseActivity.getLanguetype(NoticeDetailActivity.this),data.id,data.type,BaseActivity.getuser().id+"")
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
                new LogUntil(NoticeDetailActivity.this,TAG+"zixunmessage",new Gson().toJson(stringStatusCode));
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
            content.setText(data.content);
        }else{
            content.setText("");
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
}