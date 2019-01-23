package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.adpater.CollectionAdpater;
import translatedemo.com.translatedemo.adpater.NoticeAdpater;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.CollectionBean;
import translatedemo.com.translatedemo.bean.CollectionListbean;
import translatedemo.com.translatedemo.bean.NoticeBean;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.eventbus.UpdateMainIndex;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;
import translatedemo.com.translatedemo.view.LoadingPagerHead;
import translatedemo.com.translatedemo.view.YRecycleview;

/**
 * Created by oldwang on 2019/1/7 0007.
 * 我的收藏
 */

public class MyCollectionActivity  extends BaseActivity {
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.yrecycleview_)
    YRecycleview yrecycleview_;
    private CollectionAdpater madpater;
    private List<CollectionListbean> listdata = new ArrayList<>();
    private LoadingPagerHead mLoadingPagerHead;
    private Dialog mLoadingDialog;
    private int countpage = 1;
    private CollectionBean mdata;
    @Override
    protected void initView() {
        mLoadingPagerHead = new LoadingPagerHead(MyCollectionActivity.this) {
            @Override
            protected void close() {

            }

            @Override
            protected View createSuccessView() {
                return UIUtils.inflate(MyCollectionActivity.this, R.layout.activity_mycollection);
            }

            @Override
            protected void reLoading() {
                retry(false);

            }
        };
        setContentView(mLoadingPagerHead);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UIUtils.showFullScreen(MyCollectionActivity.this,false);
        updateactionbar();
    }

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.mycollection_text_titlename));

        madpater = new CollectionAdpater(MyCollectionActivity.this,listdata);
        yrecycleview_.setLayoutManager(new LinearLayoutManager(this));
        yrecycleview_.setItemAnimator(new DefaultItemAnimator());
        yrecycleview_.setAdapter(madpater);
        madpater.setlistOnclickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                EventBus.getDefault().post(listdata.get(position));
                if(0==listdata.get(position).isword){
                    EventBus.getDefault().post(new UpdateMainIndex(1));
                }else{
                    EventBus.getDefault().post(new UpdateMainIndex(3));
                }
                finish();
            }
        });
        retry(false);
    }

    public void retry(final Boolean type){
        if(type){
            countpage++;
            if(countpage>mdata.totalPages){
                yrecycleview_.setloadMoreComplete();
                ToastUtils.makeText(this.getResources().getString(R.string.notice_text_nomore));
                return ;
            }
        }else{
            countpage = 1;
        }
        Observable observable =
                ApiUtils.getApi().getUserCollectionDictionary(BaseActivity.getLanguetype(MyCollectionActivity.this),BaseActivity.getuser().id+"",countpage, Contans.cow)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                try {


                                    if (mLoadingDialog == null) {
                                        mLoadingDialog = LoadingDialogUtils.createLoadingDialog(MyCollectionActivity.this, "");
                                    }
                                    LoadingDialogUtils.show(mLoadingDialog);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<CollectionBean>(MyCollectionActivity.this) {
            @Override
            protected void _onNext(StatusCode<CollectionBean> stringStatusCode) {
                new LogUntil(MyCollectionActivity.this,TAG+"getNotifykList",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if (!type) {
                    yrecycleview_.setReFreshComplete();
                }else{
                    yrecycleview_.setloadMoreComplete();
                }
                mdata = stringStatusCode.getData();
                if(stringStatusCode.getCode()==0&&stringStatusCode.getData()!=null&&stringStatusCode.getData().list.size()>0) {
                    mLoadingPagerHead.showPagerView(Contans.STATE_SUCCEED);
                    if(!type){
                        listdata.clear();
                    }
                    listdata.addAll(stringStatusCode.getData().list);

                }else{
                    if(countpage!=1){
                        mLoadingPagerHead.showPagerView(Contans.STATE_SUCCEED);
                    }else {
                        mLoadingPagerHead.showPagerView(Contans.STATE_EMPTY);
                    }
                }
                madpater.notifyDataSetChanged();
            }

            @Override
            protected void _onError(String message) {
                if (!type) {
                    yrecycleview_.setReFreshComplete();
                }else{
                    yrecycleview_.setloadMoreComplete();
                }
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                mLoadingPagerHead.showPagerView(Contans.STATE_ERROR);
            }
        }, "", lifecycleSubject, false, true);
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,MyCollectionActivity.class);
        mContext.startActivity(mIntent);
    }
    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }
}
