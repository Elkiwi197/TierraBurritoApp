package com.example.tierraburritoapp.data.utils

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking {
            tokenManager.getAccessToken().firstOrNull()
        }

        val request = chain.request().newBuilder()
            .apply {
                if (!accessToken.isNullOrEmpty()) {
                    header("Authorization", "Bearer $accessToken")
                }
            }
            .build()

        return chain.proceed(request)
    }
}

