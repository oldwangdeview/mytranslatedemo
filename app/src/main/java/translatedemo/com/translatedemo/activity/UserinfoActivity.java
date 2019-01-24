package translatedemo.com.translatedemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.wcy.htmltext.HtmlImageLoader;
import me.wcy.htmltext.HtmlText;
import me.wcy.htmltext.OnTagClickListener;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.adpater.DucationAdpater;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.ConfigBean;
import translatedemo.com.translatedemo.bean.DucationBean;
import translatedemo.com.translatedemo.bean.LoginBean;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.eventbus.UpdateUserEvent;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
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
import translatedemo.com.translatedemo.view.ChoiceDucationDialog;
import translatedemo.com.translatedemo.view.ChoiceSexDialog;

/**
 * Created by oldwang on 2019/1/6 0006.
 * 我的资料
 */

public class UserinfoActivity extends BaseActivity{
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.headimage)
    ImageView headimage;
    /**学历**/
    @BindView(R.id.ducation_editext)
    EditText ducation_editext;
    @BindView(R.id.nickname_editext)
    TextView nickname_editext;
    @BindView(R.id.sex_editext)
    EditText sex_editext;
    @BindView(R.id.age_editext)
    EditText age_editext;

    private String headimagepath = FileUtils.getSDRoot()+"/translate/imag/crop_photo.jpg";
    private BottomMenuDialog mBottomMenuDialog;
    private Uri imageUri;
    private Uri cropImageUri;
    private File fileUri = new File(FileUtils.IMAGE_DIR + "/photo.jpg");
    private File fileCropUri = new File(FileUtils.IMAGE_DIR+ "/crop_photo.jpg");
    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;
    static int CODE_RESULT_REQUEST = 0xa2;

    /**
     * 选择学历
     */
    private List<DucationBean> mducatlistdata = new ArrayList<>();
    private DucationAdpater mDucationadpater ;
    private ChoiceDucationDialog choiceducationdialog;
    private int mDucationid = -1;

    /**
     * 选择性别
     */
    private ChoiceSexDialog choicesexdiag;
    private int sextype = -1;
    @Override
    protected void initView() {
     setContentView(R.layout.activity_userinfo);
    }
    @Override
    protected void onResume() {
        super.onResume();
        UIUtils.showFullScreen(UserinfoActivity.this,false);
        updateactionbar();
    }
    private Dialog mLoadingDialog;
    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.myuserinfo_text_titlename));
        if(mDucationadpater==null){
            mDucationadpater = new DucationAdpater(UserinfoActivity.this,mducatlistdata);
            mDucationadpater.setOnclickItemLister(new ListOnclickLister() {
                @Override
                public void onclick(View v, int position) {
                    ducation_editext.setText(mducatlistdata.get(position).name);
                    mDucationid = mducatlistdata.get(position).id;
                    if(choiceducationdialog!=null){
                        choiceducationdialog.dismiss();
                    }
                }
            });
        }
        updatedata();
        getDucation();
    }

    private void updatedata(){
        LoginBean user = BaseActivity.getuser();
        if(!TextUtils.isEmpty(user.headBigImage)){
            UIUtils.loadImageView(this,user.headBigImage,headimage);
        }else if(!TextUtils.isEmpty(user.headSmallImage)){
            UIUtils.loadImageView(this,user.headSmallImage,headimage);
        }else{
            headimage.setImageResource(R.mipmap.mrtx);
        }

        if(!TextUtils.isEmpty(user.nickName)){
            nickname_editext.setText(user.nickName);
        }else{
            nickname_editext.setText("");
        }

        if(!TextUtils.isEmpty(user.sex)){
            switch (Integer.parseInt(user.sex)){
                case 1:
                    sex_editext.setText(this.getResources().getString(R.string.myuserinfo_text_man));
                    sextype = 1;
                    break;
                case 2:
                    sex_editext.setText(this.getResources().getString(R.string.myuserinfo_text_wuman));
                    sextype = 2;
                    break;
                case 3:

                    break;
            }
        }
        if(!TextUtils.isEmpty(user.age)){
            age_editext.setText(user.age);
        }
        if(!TextUtils.isEmpty(user.education)){
            ducation_editext.setText(user.education);
        }

    }

    @OnClick(R.id.headimage)
    public void gteheadimage(){
        if (mBottomMenuDialog==null) {

            mBottomMenuDialog = new BottomMenuDialog.Builder(UserinfoActivity.this)
                    //.setTitle("更换封面")
                    .addMenu(UserinfoActivity.this.getResources().getString(R.string.tackphoto), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBottomMenuDialog.dismiss();


/*
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            startActivityForResult(intent, Contans.CAMERA_REQUEST_CODE);*/

                            imageUri = Uri.fromFile(fileUri);
                            //通过FileProvider创建一个content类型的Uri
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                imageUri = FileProvider.getUriForFile(UserinfoActivity.this, "translatedemo.com.translatedemo", fileUri);
                            }
                            PhotoUtils.takePicture(UserinfoActivity.this, imageUri, Contans.CAMERA_REQUEST_CODE);
                        }
                    }).addMenu(UserinfoActivity.this.getResources().getString(R.string.imagelist), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBottomMenuDialog.dismiss();
                          /*  Intent intent = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(intent, Contans.ALBUM_REQUEST_CODE);*/
                            PhotoUtils.openPic(UserinfoActivity.this, Contans.GALLERY_REQUEST_CODE);
                        }
                    }).create();

        }
        if (!mBottomMenuDialog.isShowing()){

            mBottomMenuDialog.show();
        }
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            switch (requestCode) {
                case  Contans.CAMERA_REQUEST_CODE:
                    //相机
                    FileUtils.deleteFile(headimagepath);
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, Contans.CODE_RESULT_REQUEST);

                    break;
                case Contans.GALLERY_REQUEST_CODE:
                    //相册
                    cropImageUri = Uri.fromFile(fileCropUri);
                    Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        newUri = FileProvider.getUriForFile(this, "translatedemo.com.translatedemo", new File(newUri.getPath()));
                    }
                    PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, Contans.CODE_RESULT_REQUEST);


                    break;

                case Contans.CODE_RESULT_REQUEST:

                    new LogUntil(this,"imageurl",headimagepath);
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        headimage.setImageBitmap(bitmap);

                    }
                    break;
            }
        }
    }


    @OnClick(R.id.updatauserdata_btn)
    public void updateuserinfo(){
        String nickname = nickname_editext.getText().toString().trim();
        if(TextUtils.isEmpty(nickname)){
            return ;
        }
        String age = age_editext.getText().toString().trim();



        uplodeuserinf0(nickname,age,headimagepath);
    }


    public void uplodeuserinf0(String nickname,String age,String headimagepath){
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), new File(headimagepath));
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", new File(headimagepath).getName(), requestBody);


        Observable observable =
                ApiUtils.getApi().editUserInfo(BaseActivity.getuser().id,nickname,sextype>0?sextype:3,age,mDucationid>0?mDucationid+"":0+"",BaseActivity.getLanguetype(this),body)
                        .compose(RxHelper.getObservaleTransformer())
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(UserinfoActivity.this, "正在修改");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        ;


        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<LoginBean>(UserinfoActivity.this) {
            @Override
            protected void _onNext(StatusCode<LoginBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                new LogUntil(UserinfoActivity.this,"content",new Gson().toJson(stringStatusCode));
                PreferencesUtils.getInstance().putString(BaseActivity.LOGINUSER,new Gson().toJson(stringStatusCode.getData()));
                BaseActivity.user=null;
                EventBus.getDefault().post(new UpdateUserEvent());
                ToastUtils.makeText(UserinfoActivity.this.getResources().getString(R.string.update_seccese));
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }


    /**
     * 选择学历
     */
    @OnClick(R.id.ducation_layout)
    public void openducationDialog(){



        if (choiceducationdialog==null) {

            choiceducationdialog = new ChoiceDucationDialog.Builder(UserinfoActivity.this).setCanCancel(mDucationadpater).create();
        }
        if (!choiceducationdialog.isShowing()){

            choiceducationdialog.show();
        }
    }

    /**
     * 选择性别
     */
    @OnClick(R.id.sex_layout)
    public void choicesex(){
        if (choicesexdiag==null) {

            choicesexdiag = new ChoiceSexDialog.Builder(UserinfoActivity.this)
                    //.setTitle("更换封面")
                    .addMenu(mcontent.getResources().getString(R.string.myuserinfo_text_man), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            choicesexdiag.dismiss();
                             sextype = 1;
                            sex_editext.setText(mcontent.getResources().getString(R.string.myuserinfo_text_man));
                        }
                    }).addMenu(mcontent.getResources().getString(R.string.myuserinfo_text_wuman), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            choicesexdiag.dismiss();
                            sextype = 2;
                            sex_editext.setText(mcontent.getResources().getString(R.string.myuserinfo_text_wuman));
                        }
                    }).create();

        }
        if (!choicesexdiag.isShowing()){

            choicesexdiag.show();
        }

    }


    /**
     * 获取学历信息
     */
    private void getDucation(){
        Observable observable =
                ApiUtils.getApi().getDucation()
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                try {


                                    if (mLoadingDialog == null) {
                                        mLoadingDialog = LoadingDialogUtils.createLoadingDialog(UserinfoActivity.this, "");
                                    }
                                    LoadingDialogUtils.show(mLoadingDialog);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<List<DucationBean>>(UserinfoActivity.this) {
            @Override
            protected void _onNext(StatusCode<List<DucationBean>> stringStatusCode) {
                new LogUntil(UserinfoActivity.this,TAG+"getDucation",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                 if(stringStatusCode.getData()!=null&&stringStatusCode.getData().size()>0){
                    mducatlistdata.clear();
                    mducatlistdata.addAll(stringStatusCode.getData());
                }
                mDucationadpater.notifyDataSetChanged();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);
    }




    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,UserinfoActivity.class);
        mContext.startActivity(mIntent);
    }
    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }
}
