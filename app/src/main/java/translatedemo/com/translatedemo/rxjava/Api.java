package translatedemo.com.translatedemo.rxjava;


import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import translatedemo.com.translatedemo.bean.CollectionBean;
import translatedemo.com.translatedemo.bean.ConfigBean;
import translatedemo.com.translatedemo.bean.DictionaryBean;
import translatedemo.com.translatedemo.bean.DucationBean;
import translatedemo.com.translatedemo.bean.FeedBackBean;
import translatedemo.com.translatedemo.bean.GetCouponListBean;
import translatedemo.com.translatedemo.bean.HelpDataBean;
import translatedemo.com.translatedemo.bean.InformationBean;
import translatedemo.com.translatedemo.bean.ListBean_information;
import translatedemo.com.translatedemo.bean.LoginBean;
import translatedemo.com.translatedemo.bean.MemberListBean;
import translatedemo.com.translatedemo.bean.NoticeBean;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.bean.TranslateBean;

/**
 * Author wangshifu
 * Dest  访问网络的方法
 *
 */
public interface Api {
    boolean isRelease=false;
    /**发布地址*/
    String baseUrl="http://120.79.136.125/";
    /**测试地址*/
     String testBaseUrl="http://39.104.188.55:8001/";


     @POST("/user/thridLogin")
     Observable<StatusCode<LoginBean>> thridLogin(
             @Query("weixinOpenid") String weixinOpenid,
             @Query("qqOpenid")String qqOpenid,
             @Query("languageType") String languageType);


    /**
     * 获取系统配置
     * @param type 1关于我们 2注册协议
     * @param languageType
     * @return
     */
     @GET("/user/getConfig")
     Observable<StatusCode<ConfigBean>> getconfig(
             @Query("type") int type,
             @Query("languageType") int languageType);
     /**
      * 获取用户信息
      */
     @GET("/user/getInfo")
     Observable<StatusCode<LoginBean>> getuserinfo(
             @Query("id") String weixinOpenid,
             @Query("languageType") int languageType);

    /**
     * 第三方注册
     * @param code 验证码
     * @param nickName 昵称（微信或qq获取的资料）
     * @param weixinOpenid  微信用户唯一标识
     * @param qqOpenid qq用户唯一标识
     * @param sex 性别 0未知1男2女3保密（微信或qq获取的资料）
     * @param imagePath 头像图片地址（微信或qq获取的资料）
     * @param languageType 语言类型 0 汉语 1 藏语
     * @param phone 电话号码
     * @return
     */
     @POST("/user/thridLoginRegister")
     Observable<StatusCode<LoginBean>> threereguest(
             @Query("code") String code,
             @Query("nickName")String nickName,
             @Query("weixinOpenid") String weixinOpenid,
             @Query("qqOpenid") String qqOpenid,
             @Query("sex")String sex,
             @Query("imagePath") String imagePath,
             @Query("languageType")String languageType,
             @Query("phone") String phone);

    /**手机号登录**/
    @POST("user/login")
    Observable<StatusCode<LoginBean>> loginforphone(
            @Query("phone") String phone,
            @Query("code")String code,
            @Query("languageType") int languageType,
            @Query("deviceId")String deviceId);

    /**
     * 获取资讯
     * @param languageType 语言类型
     * @param page 页码
     * @param size 数据返回条数
     * @return
     */
    @GET("/information/getInformationList")
    Observable<StatusCode<InformationBean>> getinformationlist(
            @Query("languageType") int languageType,
            @Query("page")int page,
            @Query("size") int size,
            @Query("userId")String userid,
            @Query("type")int type,
            @Query("advLocation")String advLocation);



    /**
     * 咨询赞
     * @param languageType
     * @param informationId
     * @param type
     * @param userId
     * @return
     */
    @POST("/information/zanInformation")
    Observable<StatusCode<Object>> zanInformation(
            @Query("languageType") int languageType,
            @Query("informationId")int informationId,
            @Query("type") int type,
            @Query("userId")String userId);

    /**
     * 获取咨询详情
     * @param languageType
     * @param informationId
     * @param userId
     * @return
     */
    @POST("/information/readInformation")
    Observable<StatusCode<ListBean_information>> readInformation(
            @Query("languageType") int languageType,
            @Query("informationId")int informationId,
            @Query("userId")String userId);


    /**
     * 获取会员资费列表
     */
    @GET("/member/getMemberConfigList")
    Observable<StatusCode<List<MemberListBean>>> getMemberConfigList(
            @Query("languageType") int languageType);

    /**
     * 获取待领取优惠券列表
     * @param userId
     * @return
     */
    @GET("/coupon/getWaitReceiveCouponList")
    Observable<StatusCode<List<GetCouponListBean>>> getWaitReceiveCouponList(
            @Query("userId") String userId);

    /**
     * 领取优惠券
     * @param id
     * @return
     */
    @POST("/coupon/receiveCoupon")
    Observable<StatusCode<Object>> receiveCoupon(
            @Query("id") String id,
            @Query("userId") String userId,
            @Query("languageType")int languageType);

    /**
     * 优惠券列表
     * @param userId
     * @return
     */
    @GET("/coupon/getCouponList")
    Observable<StatusCode<List<GetCouponListBean>>> getCouponList(
            @Query("status") int status,
            @Query("userId") String userId,
            @Query("languageType")int languageType);

    @GET("/user/getNotifykList")
    Observable<StatusCode<NoticeBean>> getNotifykList(
            @Query("page") int page,
            @Query("size") int size,
            @Query("languageType")int languageType);

    /***
     * 修改用户信息
     */
    @Multipart
    @POST("/user/editUserInfo")
    Observable<StatusCode<LoginBean>> editUserInfo(
            @Query("id") int id,
            @Query("nickName") String nickName,
            @Query("sex")int sex,
            @Query("age") String age,
            @Query("educationId")String educationId,
            @Query("languageType")int languageType,
            @Part MultipartBody.Part imgs);

    /**
     * 获取学历信息
     */
    @GET("/user/getDucation")
    Observable<StatusCode<List<DucationBean>>> getDucation();

    /**
     * 反馈
     * @param userId
     * @param content
     * @param languageType
     * @param imgs
     * @return
     */
    @Multipart
    @POST("/user/feedback")
    Observable<StatusCode<FeedBackBean>> uplodefeedback(
            @Query("userId") int userId,
            @Query("content") String content,
            @Query("languageType")int languageType,
            @Part List<MultipartBody.Part> imgs);
    @POST("/user/feedback")
    Observable<StatusCode<FeedBackBean>> uplodefeedback(
            @Query("userId") int userId,
            @Query("content") String content,
            @Query("languageType")int languageType
            );


    /**
     * 更换手机号
     */
    @POST("/user/changePhone")
    Observable<StatusCode<LoginBean>> changePhone(
            @Query("userId") String userId,
            @Query("nowPhone")String nowPhone,
            @Query("newPhone") String newPhone,
            @Query("code")String code,
            @Query("languageType")int languageType);
    /**
     * 获取所有词典
     */

    @GET("/dictionary/getAllDictionary")
    Observable<StatusCode<List<DictionaryBean>>> getAllDictionary(
            @Query("userId") String userId,
            @Query("languageType")int languageType
           );
    @GET("/dictionary/getAllDictionary")
    Observable<StatusCode<List<DictionaryBean>>> getAllDictionary(
            @Query("userId") String userId,
            @Query("languageType")int languageType,
            @Query("type")int type
    );
    /**
     * 翻译单词
     */
    @GET("/dictionary/translate")
    Observable<StatusCode<TranslateBean>> translateconttent(
            @Query("userId") String userId,
            @Query("languageType")int languageType,
            @Query("type")int type,
            @Query("content") String content,
            @Query("dictionaryId")int dictionaryId
    );
    /**
     * 获取帮助中心列表
     */
    @GET("/user/getHelpCenterList")
    Observable<StatusCode<HelpDataBean>> getHelpCenterList(
            @Query("page")int page,
            @Query("size")int size
    );

    /**
     * 收藏单词句子
     */
    @POST("/dictionary/collectionDictionary")
    Observable<StatusCode<Object>> collectionDictionary(
            @Query("languageType")int languageType,
            @Query("userId")String userId,
            @Query("type")int type,
            @Query("content")String content,
            @Query("translateContent")String translateContent,
            @Query("dictionaryId")String dictionaryId,
            @Query("isWord")String isWord
    );

    /**
     * 获取收藏
     * @param languageType
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @GET("/dictionary/getUserCollectionDictionary")
    Observable<StatusCode<CollectionBean>> getUserCollectionDictionary(
            @Query("languageType")int languageType,
            @Query("userId")String userId,
            @Query("page")int page,
            @Query("size")int size
    );

}


