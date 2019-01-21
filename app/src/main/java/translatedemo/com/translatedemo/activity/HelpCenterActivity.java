package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
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
import translatedemo.com.translatedemo.adpater.HelpcenterAdpater;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.DucationBean;
import translatedemo.com.translatedemo.bean.HelpDataBean;
import translatedemo.com.translatedemo.bean.Helpcenter_ListBean;
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
import translatedemo.com.translatedemo.view.YRecycleview;

/**
 * Created by oldwang on 2019/1/7 0007.
 * 帮助中心
 */

public class HelpCenterActivity extends BaseActivity {
    @BindView(R.id.yrecycleview_)
    YRecycleview yrecycleview_;
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    private Dialog mLoadingDialog;
    public List<Helpcenter_ListBean> listdata = new ArrayList<>();
    HelpcenterAdpater madpatr ;
    private HelpDataBean mdata;
    @Override
    protected void initView() {
     setContentView(R.layout.activity_helpcenter);
    }
    protected void onResume() {
        super.onResume();
        UIUtils.showFullScreen(HelpCenterActivity.this,false);
        updateactionbar();
    }

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.helpcenter_text_titlename));
        madpatr = new HelpcenterAdpater(this,listdata);
        yrecycleview_.setLayoutManager(new LinearLayoutManager(this));
        yrecycleview_.setItemAnimator(new DefaultItemAnimator());
        yrecycleview_.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                getdata(false);
            }

            @Override
            public void onLoadMore() {
                getdata(true);
            }
        });
        yrecycleview_.setAdapter(madpatr);
        madpatr.setlistOnclickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                HelpDetailActivity.startactivty(HelpCenterActivity.this,listdata.get(position));
            }
        });
        getdata(false);
    }

    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }

    @Override
    protected void onDestroy() {
        UIUtils.showFullScreen(HelpCenterActivity.this,true);
        super.onDestroy();
    }
    private int countpage = 1;
    private void getdata(final boolean type){
        if(type){

            countpage++;
            if(mdata!=null&&countpage>mdata.totalPages){
                yrecycleview_.setloadMoreComplete();
              return;
            }
        }else{
            countpage = 1;
        }

        Observable observable =
                ApiUtils.getApi().getHelpCenterList(countpage, Contans.rows)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                try {


                                    if (mLoadingDialog == null) {
                                        mLoadingDialog = LoadingDialogUtils.createLoadingDialog(HelpCenterActivity.this, "");
                                    }
                                    LoadingDialogUtils.show(mLoadingDialog);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<HelpDataBean>(HelpCenterActivity.this) {
            @Override
            protected void _onNext(StatusCode<HelpDataBean> stringStatusCode) {
               LoadingDialogUtils.closeDialog(mLoadingDialog);
               if(type){
                   yrecycleview_.setloadMoreComplete();
               }else{
                   listdata.clear();
                   yrecycleview_.setReFreshComplete();
               }
               if(stringStatusCode.getData().list.size()>0){
                   listdata.addAll(stringStatusCode.getData().list);
               }
                madpatr.notifyDataSetChanged();;

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);
    }

    public static void startactivity(Context mContext){
        Intent mintent = new Intent(mContext,HelpCenterActivity.class);
        mContext.startActivity(mintent);
    }
}
