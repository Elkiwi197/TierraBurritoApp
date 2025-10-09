package com.example.tierraburritoapp.data.utils

import com.example.tierraburritoapp.common.Constantes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenManager.getRefreshToken().first()
        }
        val request = chain.request().newBuilder()
        request.addHeader(Constantes.AUTHORIZATION, Constantes.BEARER + token)
        return chain.proceed(request.build())
    }
}