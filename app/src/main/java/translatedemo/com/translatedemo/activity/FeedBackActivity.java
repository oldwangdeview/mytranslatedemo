package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.adpater.Addimageadpater;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.FeedBackBean;
import translatedemo.com.translatedemo.bean.LoginBean;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.eventbus.UpdateUserEvent;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.interfice.DeleteImage;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.util.FileUtils;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.PhotoUtils;
import translatedemo.com.translatedemo.util.PreferencesUtils;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;
import translatedemo.com.translatedemo.view.BottomMenuDialog;
import translatedemo.com.translatedemo.view.MyGridView;

/**
 * Created by oldwang on 2019/1/7 0007.
 * 意见反馈
 */

public class FeedBackActivity extends BaseActivity {
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.mygridview)
    MyGridView mygridview;
    @BindView(R.id.imagesize)
    TextView imagesize;
    @BindView(R.id.input_editext)
    EditText input_editext;
    @BindView(R.id.textszie)
    TextView textsize;
    private Addimageadpater madpater;
    private List<String> filepath = new ArrayList<>();
    private BottomMenuDialog mBottomMenuDialog;
    private Uri imageUri;
    private File fileUri ;
    private String photopath = "";
    public String messagecontent = "";//简要说明
    private static final int MAX_TEXTSIZE=200;//字数限制
    private Dialog mLoadingDialog;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_feedback);
    }
    @Override
    protected void onResume() {
        super.onResume();
        UIUtils.showFullScreen(FeedBackActivity.this,false);
        updateactionbar();
    }

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.feedback_text_titlename));
        filepath.add("addimage");
        madpater = new Addimageadpater(this,filepath);
        madpater.setOnclickItemLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                choiceiamge();
            }
        });
        madpater.setdeteimagelister(new DeleteImage() {
            @Override
            public void deleteiamge(int image) {
                imagesize.setText(getimagesize(filepath));
            }
        });

        mygridview.setAdapter(madpater);
        input_editext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.length() > MAX_TEXTSIZE) {
                        int c = count - (s.length() - MAX_TEXTSIZE);
                        s = s.subSequence(0, start + c).toString() + s.subSequence(start + count, s.length()).toString();
                        input_editext.setText(s);
                        input_editext.setSelection(start + c);
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                messagecontent = input_editext.getText().toString().trim();
                Log.e("textlength","size+"+messagecontent.length()+"");
                textsize.setText(messagecontent.length()+"/"+MAX_TEXTSIZE);
                if(messagecontent.length()>0){
                    //cursorVisible
                    input_editext.setCursorVisible(true);
                }else{
                    input_editext.setCursorVisible(false);
                }
            }
        });
    }

    /**
     * 选择添加图片
     */
    private void choiceiamge(){

            mBottomMenuDialog = new BottomMenuDialog.Builder(FeedBackActivity.this)
                    //.setTitle("更换封面")
                    .addMenu(FeedBackActivity.this.getResources().getString(R.string.tackphoto), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBottomMenuDialog.dismiss();


                            photopath = FileUtils.IMAGE_DIR + "/"+new Date().getTime()+"photo.jpg";
                            fileUri = new File(photopath);
                            imageUri = Uri.fromFile(fileUri);
                            //通过FileProvider创建一个content类型的Uri
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                imageUri = FileProvider.getUriForFile(FeedBackActivity.this, "translatedemo.com.translatedemo", fileUri);
                            }
                            PhotoUtils.takePicture(FeedBackActivity.this, imageUri, Contans.CAMERA_REQUEST_CODE);
                        }
                    }).addMenu(FeedBackActivity.this.getResources().getString(R.string.imagelist), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBottomMenuDialog.dismiss();
                          /*  Intent intent = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(intent, Contans.ALBUM_REQUEST_CODE);*/
                            PhotoUtils.openPic(FeedBackActivity.this, Contans.GALLERY_REQUEST_CODE);
                        }
                    }).create();


        if (!mBottomMenuDialog.isShowing()){

            mBottomMenuDialog.show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            switch (requestCode) {
                case  Contans.CAMERA_REQUEST_CODE:
                    if(filepath.size()<Addimageadpater.MAX_IMAGE) {
                        filepath.add(filepath.size() - 1, photopath);
                    }else{
                        filepath.remove("addimage");
                        filepath.add(filepath.size() - 1, PhotoUtils.getPath(this, data.getData()));
                    }
                    break;
                case Contans.GALLERY_REQUEST_CODE:
                    if(filepath.size()<Addimageadpater.MAX_IMAGE) {
                        filepath.add(filepath.size() - 1, PhotoUtils.getPath(this, data.getData()));
                    }else{
                        filepath.remove("addimage");
                        filepath.add(filepath.size() - 1, PhotoUtils.getPath(this, data.getData()));
                    }
                    break;
            }
            imagesize.setText(getimagesize(filepath));
            madpater.notifyDataSetChanged();
        }
    }

    /**
     * 上传
     * @param
     */
    @OnClick(R.id.uplod_btn)
    public void uploaddata(){

        String content = input_editext.getText().toString().trim();
        List<String> imagepath = madpater.getimagepath();
        if(!TextUtils.isEmpty(content)||imagepath.size()>0){
            List< MultipartBody.Part > imagelistbodey = new ArrayList<>();
            for(int i =0;i<imagepath.size();i++){
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), new File(imagepath.get(i)));
                MultipartBody.Part body = MultipartBody.Part.createFormData("images", new File(imagepath.get(i)).getName(), requestBody);
                imagelistbodey.add(body);
            }
            Observable observable = null;
                   if(imagelistbodey.size()>0) {
                       observable = ApiUtils.getApi().uplodefeedback(BaseActivity.getuser().id, TextUtils.isEmpty(content) ? "" : content, BaseActivity.getLanguetype(FeedBackActivity.this), imagelistbodey.size() > 0 ? imagelistbodey : null)
                               .compose(RxHelper.getObservaleTransformer())
                               .subscribeOn(Schedulers.io())
                               .doOnSubscribe(new Consumer<Disposable>() {
                                   @Override
                                   public void accept(Disposable disposable) throws Exception {
                                       if (mLoadingDialog == null) {
                                           mLoadingDialog = LoadingDialogUtils.createLoadingDialog(FeedBackActivity.this, "正在上传");
                                       }
                                       LoadingDialogUtils.show(mLoadingDialog);
                                   }
                               })
                               .subscribeOn(AndroidSchedulers.mainThread())
                       ;
                   }else{
                       observable = ApiUtils.getApi().uplodefeedback(BaseActivity.getuser().id, TextUtils.isEmpty(content) ? "" : content, BaseActivity.getLanguetype(FeedBackActivity.this))
                               .compose(RxHelper.getObservaleTransformer())
                               .doOnSubscribe(new Consumer<Disposable>() {
                                   @Override
                                   public void accept(Disposable disposable) throws Exception {
                                       if (mLoadingDialog == null) {
                                           mLoadingDialog = LoadingDialogUtils.createLoadingDialog(FeedBackActivity.this, "");
                                       }
                                       LoadingDialogUtils.show(mLoadingDialog);
                                   }
                               })
                               .subscribeOn(AndroidSchedulers.mainThread())
                       ;
                   }


            HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<FeedBackBean>(FeedBackActivity.this) {
                @Override
                protected void _onNext(StatusCode<FeedBackBean> stringStatusCode) {
                    LoadingDialogUtils.closeDialog(mLoadingDialog);
//                    new LogUntil(FeedBackActivity.this,"uplodefeedback",new Gson().toJson(stringStatusCode));
//                    PreferencesUtils.getInstance().putString(BaseActivity.LOGINUSER,new Gson().toJson(stringStatusCode.getData()));
////                    BaseActivity.user=null;
                    EventBus.getDefault().post(new UpdateUserEvent());
                    finish();
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.makeText(message);
                    LoadingDialogUtils.closeDialog(mLoadingDialog);
                }
            }, "", lifecycleSubject, false, true);

        }else{

            return ;
        }

    }





    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,FeedBackActivity.class);
        mContext.startActivity(mIntent);
    }
    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }

    private String getimagesize(List<String> imagepath){
        int size = 0;
        for(String image:imagepath){
            if(!"addimage".equals(image)){
                size++;
            }
        }
        return size+"/4";
    }
}
