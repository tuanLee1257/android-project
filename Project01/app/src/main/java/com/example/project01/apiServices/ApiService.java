package com.example.project01.apiServices;

import com.example.project01.mvp.model.Photo;
import com.example.project01.mvp.model.Topic;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("photos")
    Call<List<Photo>> listPhoto();

    @GET("topics")
    Call<List<Topic>> listTopics();

    @GET("topics/{topic}/photos")
    Call<List<Photo>> listPhotoUponTopic(@Path("topic") String topic);

}
