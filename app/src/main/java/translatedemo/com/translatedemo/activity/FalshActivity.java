package translatedemo.com.translatedemo.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.util.CheckPermission;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by Administrator on 2018/12/25 0025.
 */

public class FalshActivity  extends AppCompatActivity{

    static final String[] PERMISSION = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,  //读取权限
            Manifest.permission.CAMERA,//相机
            Manifest.permission.RECORD_AUDIO,//相机
            Manifest.permission.ACCESS_COARSE_LOCATION,//定位
            Manifest.permission.ACCESS_FINE_LOCATION,//定位
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE

    };
//    @Override
//    protected void initView() {
//
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtils.showFullScreen(FalshActivity.this,true);
        if (!UIUtils.isMarshmallow()) {
            ChoiceLanguageActivity.startactivity(FalshActivity.this);
            finish();
        } else {
            CheckPermission checkPermission = new CheckPermission(FalshActivity.this);
            if (checkPermission.permissionSet(PERMISSION)) {

                PermissionActivity.startActivityForResult(FalshActivity.this, Contans.PERMISSION_REQUST_COND, PERMISSION);
            } else {

                ChoiceLanguageActivity.startactivity(FalshActivity.this);
                finish();
            }
        }

    }
//
//    @Override
//    protected void initData() {
//        super.initData();
//
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Contans.PERMISSION_REQUST_COND) {
            if (resultCode == PermissionActivity.PERMISSION_DENIEG) {
                //没有权限
                finish();
            } else if (resultCode == PermissionActivity.PERMISSION_GRANTED) {
                //有权限
                ChoiceLanguageActivity.startactivity(this);
                finish();
            }
        }
    }

}
