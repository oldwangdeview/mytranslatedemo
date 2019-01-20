package translatedemo.com.translatedemo.rxjava;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.bean.TranslateBean;

/**
 * Created by oldwang on 2019/1/18 0018.
 */

public interface TranslateApi {
    boolean isRelease=true;
    /**发布地址*/
    String baseUrl="https://nmt.xmu.edu.cn";
    /**测试地址*/
    String testBaseUrl="http://39.104.188.55:8001/";

    @GET("/nmt")
    Observable<String> translateconttent(
            @Query("lang") String lang,
            @Query("src")String src

    );
}
