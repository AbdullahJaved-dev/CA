package com.logicielhouse.ca.source.remote.interceptors

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthenticationInterceptor(language: String?, context: Context) : Interceptor {

    private var language: String? = null
    private var context: Context? = null

    init {
        this.language = language
        this.context = context
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Accept", "application/json")
            .method(originalRequest.method, originalRequest.body)

        if (!language.isNullOrEmpty()) {
            requestBuilder.addHeader("Language", language!!)
        }

        return chain.proceed(requestBuilder.build())
    }
}