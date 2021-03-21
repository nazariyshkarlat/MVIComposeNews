package com.example.myapplication.di

import android.content.Context
import android.net.ConnectivityManager
import com.example.myapplication.BuildConfig
import com.example.myapplication.data.net.ConnectionManager
import com.example.myapplication.data.net.ConnectionManagerImpl
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.core.context.GlobalContext
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://newsapi.org/"

val networkModule = module {

    single {
        provideRetrofit(get())
    }

    single {
        ConnectionManagerImpl(
            get()
        )
    } bind(ConnectionManager::class)

    single {
        androidApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    single {
        provideHttpClient(get(named("logging_interceptor")))
    }

    factory(named("logging_interceptor")) { provideHttpLoggingInterceptor() } bind (Interceptor::class)
}

fun <T> getService(className: Class<T>): T = GlobalContext.get().get<Retrofit>().create(className)

fun <T> getService(className: Class<T>, retrofit: Retrofit): T = retrofit.create(className)

private fun provideRetrofit(okHttpClient: OkHttpClient) =
    Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(provideConverterFactory())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

private fun provideConverterFactory() = Json{
    ignoreUnknownKeys=true
    encodeDefaults=true
}.asConverterFactory("application/json".toMediaType())

private fun provideHttpClient(loggingInterceptor: Interceptor) =
    OkHttpClient().newBuilder()
        .addInterceptor(loggingInterceptor)
        .readTimeout(1, TimeUnit.MINUTES)
        .connectTimeout(2, TimeUnit.SECONDS)
        .build()

private fun provideHttpLoggingInterceptor() =
    HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BASIC
        else
            HttpLoggingInterceptor.Level.NONE
    }
