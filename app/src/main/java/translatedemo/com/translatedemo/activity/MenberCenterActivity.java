package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.adpater.MemberCenterAdpater;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.InformationBean;
import translatedemo.com.translatedemo.bean.LoginBean;
import translatedemo.com.translatedemo.bean.MemberListBean;
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
import translatedemo.com.translatedemo.view.MyGridView;

/**
 * Created by oldwang on 2019/1/6 0006.
 * 会员中心
 */

public class MenberCenterActivity extends BaseActivity {
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.mygridview)
    MyGridView mygridview;
    @BindView(R.id.headimage)
    ImageView headimage;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.vipimage)
    ImageView vipimage;
    @BindView(R.id.notviptext)
    TextView notviptext;

    @BindView(R.id.content)
    TextView content;

    List<MemberListBean> listdata = new ArrayList<>();
    private MemberCenterAdpater madpater;
    @Override
    protected void initView() {
     setContentView(R.layout.activity_membercenter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UIUtils.showFullScreen(MenberCenterActivity.this,false);
        updateactionbar();

    }

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.membercenter_text_title));

        LoginBean myuser = BaseActivity.getuser();
        if(!TextUtils.isEmpty(myuser.headSmallImage)){
            UIUtils.loadImageViewRoud(this,myuser.headSmallImage,headimage,UIUtils.dip2px(40));
        }
        if(!TextUtils.isEmpty(myuser.headBigImage)){
            UIUtils.loadImageViewRoud(this,myuser.headBigImage,headimage,UIUtils.dip2px(40));
        }

        if(!TextUtils.isEmpty(myuser.nickName)){
            name.setText(myuser.nickName);
        }else{
            name.setText("");
        }
        if(!TextUtils.isEmpty(myuser.isMember+"")){
            switch (myuser.isMember){
                case 0:
                    vipimage.setImageResource(R.mipmap.huiyuan1);
                    notviptext.setText(this.getResources().getString(R.string.membercenter_text_botvip));
                    break;
                case 1:
                    vipimage.setImageResource(R.mipmap.huiyuan);
                    if(!TextUtils.isEmpty(myuser.memberEndTime)){
                        notviptext.setText(this.getResources().getString(R.string.membercenter_text_endtime)+myuser.memberEndTime);
                    }
                    break;
            }
        }

        madpater = new MemberCenterAdpater(this,listdata);
        mygridview.setAdapter(madpater);
        getmemberconfig();
        mygridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                content.setText(listdata.get(position).explain);
            }
        });
        madpater.setonitemlistclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                //点击开通
            }
        });
    }
    private Dialog mLoadingDialog;
    private void getmemberconfig(){
        Observable observable =
                ApiUtils.getApi().getMemberConfigList(BaseActivity.getLanguetype(MenberCenterActivity.this))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                try {


                                    if (mLoadingDialog == null) {
                                        mLoadingDialog = LoadingDialogUtils.createLoadingDialog(MenberCenterActivity.this, "");
                                    }
                                    LoadingDialogUtils.show(mLoadingDialog);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<List<MemberListBean>>(MenberCenterActivity.this) {
            @Override
            protected void _onNext(StatusCode<List<MemberListBean>> stringStatusCode) {

                LoadingDialogUtils.closeDialog(mLoadingDialog);
                listdata.clear();
                if(stringStatusCode.getCode()==0&&stringStatusCode.getData()!=null&&stringStatusCode.getData().size()>0) {
                    listdata.addAll(stringStatusCode.getData());
                    content.setText(stringStatusCode.getData().get(0).explain);
                }

                madpater.notifyDataSetChanged();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,MenberCenterActivity.class);
        mContext.startActivity(mIntent);
    }
    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }

}
