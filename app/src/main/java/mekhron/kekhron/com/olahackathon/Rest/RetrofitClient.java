package mekhron.kekhron.com.olahackathon.Rest;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by badri on 17/12/17.
 */

public class RetrofitClient {
    private static Retrofit retrofit;

    private static class EmptyResponseConverterFactory extends Converter.Factory {
         @org.jetbrains.annotations.Nullable
         @Override
         public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
             final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
             return new Converter<ResponseBody, Object>() {
                 @Override
                 public Object convert(ResponseBody body) throws IOException {
                     if (body.contentLength() == 0) return null;
                     return delegate.convert(body);
                 }
             };
         }
    }

    public static Retrofit getRetrofit() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.readTimeout(60,TimeUnit.SECONDS);
        httpClient.addInterceptor(httpLoggingInterceptor);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://starlord.hackerearth.com/")
                    .addConverterFactory(new EmptyResponseConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }


}
