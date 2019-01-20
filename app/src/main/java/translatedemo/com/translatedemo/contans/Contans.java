package translatedemo.com.translatedemo.contans;

/**
 * Author wangshifu
 * Dest  常量
 */

public interface Contans {
    /**权限请求码*/
    int  PERMISSION_REQUST_COND=2001;
    /**权限响应码*/
    int  PERMISSION_RESPONS_COND=2002;
    int GALLERY_REQUEST_CODE = 500;
    int CAMERA_REQUEST_CODE = 600;
    int CODE_RESULT_REQUEST = 0xa2;

    /**头像地址*/
     String headPicUrl="http://img.scshushe.com/";

    /**密码验证正则*/
    String PASSWORD_REGEX="^[0-9A-Za-z]{6,20}$";
    /*    *//**密码验证正则*//*
    String PASSWORD_REGEX="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";*/
    /**手机号正则*/
    String PHONE_REGEX="^[1][3,4,5,7,8,9][0-9]{9}$";
    /**注册获取验证码*/
    String SMSSING_REGISTER="1";
    /**忘记密码获取验证码*/
    String SMSSING_FORGETPASSWORD="0";
    /**是否token失效*/
    String ISTOKEN_DISABLE="istoken_disable";
    /**弹窗的选择城市列表*/
    String DIAG_TYPE_CITY="diag_type_city";
    /**弹窗的售卖情况*/
    String DIAG_TYPE_HOUSE_SALE_STATUE="diag_type_house_sale_statue";
    /**弹窗选择房子*/
    String DIAG_TYPE_SELECT_HOUSE="diag_type_select_house";
    /**存储手机号*/
    String username="username";
    /**存储密码*/
        String password="password";
     // 加载默认的状态
      int STATE_UNLOADED = 1;
     // 加载的状态
    int STATE_LOADING = 2;
     // 加载失败的状态
     int STATE_ERROR = 3;
     // 加载空的状态
     int STATE_EMPTY = 4;
     // 加载成功的状态
    int STATE_SUCCEED = 5;

    /**下架*/
     int STATE_SOLDOUT=6;
    /**传递intent数据*/
    String INTENT_DATA="intent_data";
    String OBJECT_DATA = "object_data";
    String INTENT_EXTRA="intent_extra";
    String INTENT_BOOLEAN="intent_boolean";
    /**户型图类型*/
    int BANNER_TYPE=1;
    /**户型图类型*/
    int HOUSE_TYPE=2;
    /**intent类型*/
    String INTENT_TYPE="intent_type";
    /**设置别名*/
    String JPUSH_SET_ALIAS="jpush_set_alias";
    /**优惠券类型*/
    String POPU_TYPE_CLASSIFY="popu_type_classify";
    /**优惠券状态*/
    String POPU_TYPE_state="popu_type_state";
    /**默认token*/
    String defaultToken="scmsw";
    int rows=20;
    /**确定收货*/
    int SURE_ACCEPT_GOODS_TYPE=1;
    /**取消订单*/
    int CANCEL_ORDER_TYPE=2;
    /**删除订单*/
    int DELETE_ORDER_TYPE=3;
    /**城市*/
    String CITY="city";
    /**楼盘预售*/
    int  HOUSE_PRESELL_TYPE=1;
    /**楼盘在售*/
    int HOUSE_ONSALE_TYPE=2;
    /**楼盘售罄*/
    int HOUSE_SELLOUT_TYPE=3;

    String PERFICE_TRANSLATE_TITLE_CLICK = "translate_click_index";

    String HOUSE_SOLD_OUT="该楼盘已下架";
    String SHARE_HOUSE_SOLD_OUT="该样板间已下架";
    String GOODS_SOLD_OUT="该商品已下架";
    String STORE_SOLD_OUT="该店铺已下架";
    /* 1 楼盘  2 店铺  3商品 4 共享家*/
    int HOUSE_SOLDOUT_TYPE=1;
    int STORE_SOLDOUT_TYPE=2;
    int GOODS_SOLDOUT_TYPE=3;
    int SHARHOUSE_SOLDOUT_TYPE=4;
    int CHOICCITY=5;
    /**AESkey*/
    String AESKey="scmsw76449880330";
    boolean isEncrypt=true;
    /***样板间详情分享***/
    int SHARED_FOR_TEMPLATEDETAILS=0X11;
    int cow = 20;

//    boolean banertype = f

    
}
