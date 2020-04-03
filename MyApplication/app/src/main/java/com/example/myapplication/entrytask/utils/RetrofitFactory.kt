package com.example.myapplication.entrytask.utils

import com.shopee.sdk.ShopeeSDK
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * author: beitingsu
 * created on: 2020/3/31 3:12 PM
 */
class RetrofitFactory {

    companion object {

        private const val DEFAULT_HOST = "https://live.test.shopee.co.id/"

        @JvmStatic
        fun getRetrofit(): Retrofit {
            val okHttpClient = ShopeeSDK.registry().networkModule().client
            return Retrofit.Builder()
                .baseUrl(DEFAULT_HOST) // 设置网络请求的公共Url地址
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
                .build()
        }
    }
}