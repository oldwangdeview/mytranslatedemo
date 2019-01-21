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
import translatedemo.com.translatedemo.adpater.GetCouponAdpater;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.GetCouponListBean;
import translatedemo.com.translatedemo.bean.InformationBean;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.eventbus.UpdateCuponEvent;
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
 * 领券中心
 */

public class GetCouponActivty  extends BaseActivity {
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.yrecycleview_)
    YRecycleview yrecycleview_;
    private LoadingPagerHead mLoadingPagerHead;
    private GetCouponAdpater madpater;
    private Dialog mLoadingDialog;
    private List<GetCouponListBean> listdata = new ArrayList<>();
    @Override
    protected void initView() {
        mLoadingPagerHead = new LoadingPagerHead(this) {
            @Override
            protected void close() {

            }

            @Override
            protected View createSuccessView() {
                return UIUtils.inflate(GetCouponActivty.this, R.layout.activity_getcoupon);
            }

            @Override
            protected void reLoading() {
                retry();

            }
        };
        setContentView(mLoadingPagerHead);
    }

    private void retry(){

        Observable observable =
                ApiUtils.getApi().getWaitReceiveCouponList(BaseActivity.getuser().id+"")
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                try {


                                    if (mLoadingDialog == null) {
                                        mLoadingDialog = LoadingDialogUtils.createLoadingDialog(GetCouponActivty.this, "");
                                    }
                                    LoadingDialogUtils.show(mLoadingDialog);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<List<GetCouponListBean>>(GetCouponActivty.this) {
            @Override
            protected void _onNext(StatusCode<List<GetCouponListBean>> stringStatusCode) {
                new LogUntil(GetCouponActivty.this,TAG+"GetCouponActivty",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                mLoadingPagerHead.showPagerView(Contans.STATE_SUCCEED);
                if(stringStatusCode.getCode()==0&&stringStatusCode.getData()!=null) {
                    listdata.clear();
                    listdata.addAll(stringStatusCode.getData());
                    madpater.notifyDataSetChanged();
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                mLoadingPagerHead.showPagerView(Contans.STATE_ERROR);
            }
        }, "", lifecycleSubject, false, true);

    }

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.getcoupon_text_titlename));
        madpater = new GetCouponAdpater(GetCouponActivty.this,listdata);
        yrecycleview_.setLayoutManager(new LinearLayoutManager(GetCouponActivty.this));
        yrecycleview_.setItemAnimator(new DefaultItemAnimator());
        yrecycleview_.setAdapter(madpater);
        madpater.setlistOnclickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                if(listdata.get(position).isReceive==0){
                    getcoupon(listdata.get(position).id+"");
                }else{
                    ToastUtils.makeText(GetCouponActivty.this.getResources().getString(R.string.getcoupon_text_getover));
                }
            }
        });
        retry();
        yrecycleview_.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                yrecycleview_.setReFreshComplete();
                retry();
            }

            @Override
            public void onLoadMore() {
                yrecycleview_.setloadMoreComplete();
                retry();
            }
        });
    }


    /**
     * 领取优惠券
     */
    private void getcoupon(String id){

        Observable observable =
                ApiUtils.getApi().receiveCoupon(id,BaseActivity.getuser().id+"",BaseActivity.getLanguetype(GetCouponActivty.this))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                try {


                                    if (mLoadingDialog == null) {
                                        mLoadingDialog = LoadingDialogUtils.createLoadingDialog(GetCouponActivty.this, "");
                                    }
                                    LoadingDialogUtils.show(mLoadingDialog);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(GetCouponActivty.this) {
            @Override
            protected void _onNext(StatusCode<Object> stringStatusCode) {
                new LogUntil(GetCouponActivty.this,TAG+"getcoupon",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                ToastUtils.makeText(GetCouponActivty.this.getResources().getString(R.string.getcoupon_text_getover));
                EventBus.getDefault().post(new UpdateCuponEvent());
                retry();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,GetCouponActivty.class);
        mContext.startActivity(mIntent);
    }
    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }
}
