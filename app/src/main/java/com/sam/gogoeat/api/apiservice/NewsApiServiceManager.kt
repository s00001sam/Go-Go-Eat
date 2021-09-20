package com.sam.gogoeat.api.apiservice

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sam.gogoeat.MyApplication
import com.sam.gogoeat.R
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val API_URL = "https://newsapi.org/v2/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val client = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
    .addInterceptor { chain -> return@addInterceptor addNewsApiKeyToRequests(chain) }
    .build()
private val lifeRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(API_URL)
    .client(client)
    .build()

private fun addNewsApiKeyToRequests(chain: Interceptor.Chain): Response {
    val request = chain.request().newBuilder()
    val originalHttpUrl = chain.request().url
    val newUrl = originalHttpUrl.newBuilder()
            .addQueryParameter("apiKey", MyApplication.appContext.getString(R.string.news_key)).build()
    request.url(newUrl)
    return chain.proceed(request.build())
}

object NewsApi {
    val apiService by lazy { lifeRetrofit.create(LifeApi::class.java) }
}