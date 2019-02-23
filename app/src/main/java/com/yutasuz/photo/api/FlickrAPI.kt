package com.yutasuz.photo.api

import com.yutasuz.photo.api.response.FlickrPhotosResultResponse
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object FlickrAPI {

    private const val BASE_URL = "https://api.flickr.com/services/"

    var service: FlickrService? = null

    class RequestInterceptor(private val apiKey: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val url = request.url().newBuilder()
                .addQueryParameter("api_key", apiKey)
                .addQueryParameter("format", "json")
                .addQueryParameter("nojsoncallback", "1")
                .build()
            val newRequest = request.newBuilder().url(url).build()
            return chain.proceed(newRequest)
        }
    }


    fun init(apiKey: String) {

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(RequestInterceptor(apiKey))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addNetworkInterceptor(HttpLoggingInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(httpClient)
            .build()

        service = retrofit.create(FlickrService::class.java)
    }

    interface FlickrService {
        @GET("rest?method=flickr.photos.search")
        fun getSearch(@Query("text") text: String, @Query("page") page: Int)
                : Single<FlickrPhotosResultResponse>

        @GET("rest?method=flickr.photos.getRecent")
        fun getRecent(@Query("page") page: Int)
                : Single<FlickrPhotosResultResponse>
    }
}