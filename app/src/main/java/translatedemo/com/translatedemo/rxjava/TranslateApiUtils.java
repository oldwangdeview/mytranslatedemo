package translatedemo.com.translatedemo.rxjava;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by oldwang on 2019/1/18 0018.
 */

public class TranslateApiUtils {
    private static OkHttpClient.Builder sBuilder = new OkHttpClient().newBuilder();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create(new Gson());
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();
    private static TranslateApi sApi;
    public static TranslateApi getApi() {

        if (sApi == null) {
            sBuilder.readTimeout(10, TimeUnit.SECONDS);
            sBuilder.connectTimeout(10, TimeUnit.SECONDS);
            Retrofit retrofit = new Retrofit.Builder()
                    .client(sBuilder.build())
                    .baseUrl(TranslateApi.isRelease ? TranslateApi.baseUrl : TranslateApi.testBaseUrl)

                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sApi = retrofit.create(TranslateApi.class);
        }
        return sApi;
    }
}
