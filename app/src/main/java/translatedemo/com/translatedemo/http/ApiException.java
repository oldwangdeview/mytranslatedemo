package translatedemo.com.translatedemo.http;


import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import translatedemo.com.translatedemo.activity.LoginActivity;
import translatedemo.com.translatedemo.application.BaseApplication;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.eventbus.ExitLoginSuccess;

/**
 * Created by oldwang on 2016/10/10 11:52.
 */

public class ApiException extends RuntimeException {
    private static String message;


    public ApiException(int number, StatusCode resultCode) {
        this(getApiExceptionMessage(number, resultCode));

    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     *
     * @param code
     * @param resultCode
     * @param
     * @return
     */
    private static String getApiExceptionMessage(int code, StatusCode resultCode) {
        Log.e("resultCode",new Gson().toJson(resultCode));
        switch (code) {
            case 2001:
                message = resultCode.getDetailMsg();
                break;
            case 2002:
                message = "未知错误";
                break;
            case 2003:
                message = "参数错误";
                break;
            case 3001:
                message = "无效的token";
                break;
            case 3002:
                message = "登陆已失效，请重新登陆";
                BaseApplication.isLoginSuccess=false;
              //  BaseApplication.sUserInfoBean=null;
                EventBus.getDefault().post(new ExitLoginSuccess());
//                LoginActivity.startActivity(BaseApplication.mContext,true);
                break;
            case 5000:
                message = "网络开了一个小差";
                break;
            case 2101:
                message = Contans.HOUSE_SOLD_OUT;
                break;
            case 2102:
                message = Contans.SHARE_HOUSE_SOLD_OUT;
                break;
            case 2103:
                message = Contans.GOODS_SOLD_OUT;
                break;
            case 2104:
                message = Contans.STORE_SOLD_OUT;
                break;
            case 5004:
                message = "您的账户已经在其他地方登录";
             //   BaseApplication.isLoginSuccess=false;
                //  BaseApplication.sUserInfoBean=null;
//                EventBus.getDefault().post(new ExitLoginSuccess());
//                OtherLoginDialogActivity.myStartActivity(BaseApplication.mContext);

                break;
            case 1:
                message = resultCode.getMsg();
                break;
            default:
                message = "未知错误";
        }
        return message;
    }
}
