package com.logicielhouse.ca.source.remote

import android.content.Context
import com.google.gson.GsonBuilder
import com.logicielhouse.ca.source.remote.interceptors.AuthenticationInterceptor

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class RetrofitClient(ctx: Context) {

    private var retrofit: Retrofit? = null
    private val httpClient = OkHttpClient.Builder()
    var context: Context = ctx

    private val BASE_URL = "https://peakupjj.zattutelage.com/"

    init {
        if (retrofit == null) {
            initRetrofit()
        }
    }


    private fun initRetrofit() {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        httpClient.connectTimeout(180, TimeUnit.SECONDS)
        httpClient.readTimeout(180, TimeUnit.SECONDS)
        httpClient.writeTimeout(180, TimeUnit.SECONDS)

        val language = Locale.getDefault().language
        val interceptor: Interceptor =
            AuthenticationInterceptor(
                language,
                context
            )
        httpClient.addInterceptor(interceptor)

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())

        retrofit = retrofitBuilder.build()

    }

    fun getService(): ApiCaller = retrofit!!.create(ApiCaller::class.java)
}