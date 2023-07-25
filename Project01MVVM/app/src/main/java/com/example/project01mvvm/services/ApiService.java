package com.example.project01mvvm.services;

import android.content.Intent;

import com.example.project01mvvm.models.Photo;
import com.example.project01mvvm.models.Topic;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    static String BASE_URL = "https://api.unsplash.com/";
    String token = "XiKGcaTpPyb0ltrlB4c91puuq5UTTWeI1kiEgErXWvc";

    OkHttpClient.Builder builder = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true);

    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Client-ID " + token)
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM--dd HH:mm:ss").setLenient().create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(client)
            .build()
            .create(ApiService.class);


    @GET("photos")
    Observable<List<Photo>> listPhoto(@Query("page") Integer page);

    @GET("photos")
    Observable<List<Photo>> loadMorePhotos(@Query("page") Integer page);

    @GET("topics")
    Observable<List<Topic>> listTopics();

    @GET("topics/{topic}/photos")
    Observable<List<Photo>> listPhotoUponTopic(@Path("topic") String topic, @Query("page") Integer page);

}
