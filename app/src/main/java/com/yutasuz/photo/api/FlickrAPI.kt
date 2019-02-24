package com.yutasuz.photo.api

import com.yutasuz.photo.api.response.FlickrPhotosResultResponse
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import retrofit2.http.GET
import retrofit2.http.Query

object FlickrAPI : KoinComponent {

    const val BASE_URL = "https://api.flickr.com/services/"
    lateinit var apiKey: String

    val service: FlickrService by inject {
        parametersOf(apiKey)
    }

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
        this.apiKey = apiKey
    }

    interface FlickrService {
        @GET("rest?method=flickr.photos.search")
        fun getSearch(@Query("text") text: String, @Query("page") page: Int)
                : Single<FlickrPhotosResultResponse>

        @GET("rest?method=flickr.photos.getRecent")
        fun getRecent(@Query("page") page: Int)
                : Single<FlickrPhotosResultResponse>

        @GET("rest?method=flickr.photos.getSizes")
        fun getSize(@Query("photo_id") photoId: Int)
                : Single<FlickrPhotosResultResponse>
    }
}