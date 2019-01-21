package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.adpater.MyCuponAdpater;
import translatedemo.com.translatedemo.adpater.NoticeAdpater;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.GetCouponListBean;
import translatedemo.com.translatedemo.bean.NoticeBean;
import translatedemo.com.translatedemo.bean.NoticeListBean;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.contans.Contans;
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
 * 通知公告
 */

public class NoticeActivity extends BaseActivity {
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.yrecycleview_)
    YRecycleview yrecycleview_;

    private LoadingPagerHead mLoadingPagerHead;
    private Dialog mLoadingDialog;
    private int countpage = 1;
    private NoticeBean mdata;
    public List<NoticeListBean> listdata = new ArrayList<>();
    private NoticeAdpater madpater;
    @Override
    protected void initView() {
        mLoadingPagerHead = new LoadingPagerHead(NoticeActivity.this) {
            @Override
            protected void close() {

            }

            @Override
            protected View createSuccessView() {
                return UIUtils.inflate(NoticeActivity.this, R.layout.activty_notice);
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
        UIUtils.showFullScreen(NoticeActivity.this,false);
        updateactionbar();
    }

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.notice_text_titlename));


        madpater = new NoticeAdpater(NoticeActivity.this,listdata);
        yrecycleview_.setLayoutManager(new LinearLayoutManager(this));
        yrecycleview_.setItemAnimator(new DefaultItemAnimator());
        yrecycleview_.setAdapter(madpater);

        yrecycleview_.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                retry(false);
            }

            @Override
            public void onLoadMore() {

                retry(true);
            }
        });
        madpater.setlistOnclickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                if(!TextUtils.isEmpty(listdata.get(position).content)) {
                    UserAgreementActivity.statrtactivity(NoticeActivity.this, listdata.get(position));
                }
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
                ApiUtils.getApi().getNotifykList(countpage, Contans.cow,BaseActivity.getLanguetype(NoticeActivity.this))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                try {


                                    if (mLoadingDialog == null) {
                                        mLoadingDialog = LoadingDialogUtils.createLoadingDialog(NoticeActivity.this, "");
                                    }
                                    LoadingDialogUtils.show(mLoadingDialog);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<NoticeBean>(NoticeActivity.this) {
            @Override
            protected void _onNext(StatusCode<NoticeBean> stringStatusCode) {
                new LogUntil(NoticeActivity.this,TAG+"getNotifykList",new Gson().toJson(stringStatusCode));
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
        Intent mIntent = new Intent(mContext,NoticeActivity.class);
        mContext.startActivity(mIntent);
    }
    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }
}
