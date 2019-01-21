package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.adpater.OffLineAdpater;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.DictionaryBean;
import translatedemo.com.translatedemo.bean.NoticeBean;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.eventbus.UpdateUserEvent;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.rxjava.Api;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.util.FileUtils;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.PreferencesUtils;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/7 0007.
 * 离线数据
 */

public class OffLineActivity extends BaseActivity {
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.recyclerview)
    RecyclerView yrecycleview_;
    private Dialog mLoadingDialog;
    private OffLineAdpater madpater;
    private final int UPDATE_COMPLETE = 3;
    private final int UPDATE_PRO = 2;
    private String dolodpath="shushe.apk";
    private List<DictionaryBean> listdata = new ArrayList<>();
    @Override
    protected void initView() {
        setContentView(R.layout.activity_offline);
    }
    @Override
    protected void onResume() {
        super.onResume();
        UIUtils.showFullScreen(OffLineActivity.this,false);
        updateactionbar();
    }

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.offlin_text_titlename));
        madpater = new OffLineAdpater(OffLineActivity.this,listdata);
        yrecycleview_.setLayoutManager(new LinearLayoutManager(OffLineActivity.this));
        yrecycleview_.setItemAnimator(new DefaultItemAnimator());
        yrecycleview_.setAdapter(madpater);
        madpater.setlistOnclickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                dolodpath = FileUtils.getSDRoot()+"/translate/"+listdata.get(position).name+".json";
                showLoadingDialog();
                downFile((Api.isRelease? Api.baseUrl:Api.testBaseUrl)+"/dictionary/downThesaurus?languageType="+BaseActivity.getLanguetype(OffLineActivity.this)+"&id="+listdata.get(position).id+"&userId="+BaseActivity.getuser().id);
            }
        });
        getdata();

    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_PRO:
                    Log.e("jind",msg.arg1+"");
                    break;
                case UPDATE_COMPLETE:
                   dismissLoadingDialog();
                   getdata();
                    break;
            }
        }
    };


    private void getdata(){

        Observable observable =
                ApiUtils.getApi().getAllDictionary(BaseActivity.getuser().id+"",BaseActivity.getLanguetype(OffLineActivity.this),0)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                try {


                                    if (mLoadingDialog == null) {
                                        mLoadingDialog = LoadingDialogUtils.createLoadingDialog(OffLineActivity.this, "");
                                    }
                                    LoadingDialogUtils.show(mLoadingDialog);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<List<DictionaryBean>>(OffLineActivity.this) {
            @Override
            protected void _onNext(StatusCode<List<DictionaryBean>> stringStatusCode) {
                new LogUntil(OffLineActivity.this,TAG+"getAllDictionary",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getData()!=null&&stringStatusCode.getData().size()>0){
                    listdata.clear();
//                    listdata.addAll(stringStatusCode.getData());
                    for(int i=0;i<stringStatusCode.getData().size();i++){
                        DictionaryBean datab = stringStatusCode.getData().get(i);
                        datab.islode = new File(FileUtils.getSDRoot()+"/translate/"+datab.name+".json").exists();
                        listdata.add(datab);
                    }
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
        Intent mIntent = new Intent(mContext,OffLineActivity.class);
        mContext.startActivity(mIntent);
    }
    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }
    private long length;
    public void downFile(final String url) {
        Log.e("dizhi",url);
        new Thread() {
            public void run() {
                URL url2 = null;
                try {
                    url2 = new URL(url);

                    HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept-Encoding", "identity");
                    conn.connect();
					/*if (conn.getResponseCode() == 200) {
   					maxLength = conn.getContentLength();
   					mPbar.setMax(maxLength);
   					InputStream is = conn.getInputStream();  */
                    if (conn.getResponseCode()==200){
                        length=		conn.getContentLength();

                        InputStream is = conn.getInputStream();
                        File file = new File(dolodpath);

                        if(file!=null&&file.exists()){
                            //本地已经存在文件
                            if(file.length()==length){
//                                file.delete();
                              return ;
                            }else{
                                file.delete();
                            }
                        }else{

                            file.createNewFile();
                        }


                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        byte[] buf = new byte[1024];
                        int ch = -1;
                        int count = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            count += ch;
                            Message message = mHandler.obtainMessage();
                            message.what = UPDATE_PRO;
                            message.arg1 = count/10000;

                            mHandler.sendMessage(message);



                        }
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        is.close();
                        Message message = mHandler.obtainMessage();
                        message.what = UPDATE_COMPLETE;
                        mHandler.sendMessage(message);
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
