package translatedemo.com.translatedemo.activity;

import android.Manifest;
import android.content.Intent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.util.CheckPermission;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by Administrator on 2018/12/25 0025.
 */

public class FalshActivity  extends BaseActivity{

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
    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        super.initData();

        if (!UIUtils.isMarshmallow()) {
            ChoiceLanguageActivity.startactivity(this);
            finish();
        } else {
            CheckPermission checkPermission = new CheckPermission(this);
            if (checkPermission.permissionSet(PERMISSION)) {
//                startPermissionActivity();
                PermissionActivity.startActivityForResult(this, Contans.PERMISSION_REQUST_COND, PERMISSION);
            } else {

                ChoiceLanguageActivity.startactivity(this);
                finish();
            }
        }
    }

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
