package com.example.tierraburritoapp.data.utils

import com.example.tierraburritoapp.data.model.TokenResponse
import com.example.tierraburritoapp.data.remote.apiservices.AuthApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val authApiService: AuthApiService
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


//    override fun intercept(chain: Interceptor.Chain): Response {
//        val originalRequest = chain.request()
//
//        val accessToken = runBlocking { tokenManager.getAccessToken().firstOrNull() }
//
//        val requestWithToken = originalRequest.newBuilder()
//            .header("Authorization", "Bearer $accessToken")
//            .build()
//
//        // --- PRIMERA RESPUESTA ---
//        val response = chain.proceed(requestWithToken)
//
//        if (response.code != 401) {
//            return response // ✔️ no lo cierres, LoggingInterceptor lo leerá
//        }
//
//        // TOKEN 401 → reintentar
//        // ✔️ cerramos SOLO su body, no la respuesta completa
//        response.body?.close()
//
//        val refreshToken = runBlocking { tokenManager.getRefreshToken().firstOrNull() }
//        if (refreshToken == null) {
//            return response // ✔️ no cerrar -> dejar que logging lo procese
//        }
//
//        val newTokens = runBlocking {
//            authApiService.refreshToken("Bearer $refreshToken").body()
//        } ?: return response
//
//        runBlocking {
//            tokenManager.saveTokens(newTokens.accessToken, newTokens.refreshToken)
//        }
//
//        val newRequest = originalRequest.newBuilder()
//            .header("Authorization", "Bearer ${newTokens.accessToken}")
//            .build()
//
//        // IMPORTANTÍSIMO: NO cerrar la respuesta aquí,
//        // ya se cerró su body arriba y LoggingInterceptor no lo verá
//
//        return chain.proceed(newRequest) // ✔️ retry seguro
//    }


//    override fun intercept(chain: Interceptor.Chain): Response {
//        val request = chain.request()
//        val accessToken = runBlocking { tokenManager.getAccessToken().firstOrNull() }
//
//        val requestWithToken = request.newBuilder()
//            .header("Authorization", "Bearer $accessToken")
//            .build()
//
//        val response = chain.proceed(requestWithToken)
//
//        if (response.code == 401) {
//            response.body?.close() // Cierra la respuesta anterior
//
//            val refreshToken = runBlocking { tokenManager.getRefreshToken().firstOrNull() }
//            val newTokens = runBlocking {
//                refreshToken?.let { authApiService.refreshToken("Bearer $it").body() }
//            } ?: return response // Si no hay refresh, devuelve 401
//
//            runBlocking {
//                tokenManager.saveTokens(
//                    newTokens.accessToken,
//                    newTokens.refreshToken
//                )
//            }
//
//            val newRequest = request.newBuilder()
//                .header("Authorization", "Bearer ${newTokens.accessToken}")
//                .build()
//            response.close()
//            return chain.proceed(newRequest) // Reintenta con token nuevo
//        }
//
//        return response
//    }


//    private suspend fun refreshToken(): TokenResponse? {
//        return try {
//            val refreshToken = tokenManager.getRefreshToken().first() ?: return null
//            val response = authApiService.refreshToken("Bearer $refreshToken")
//            return response.body()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
//}

/*
val token = runBlocking {
    tokenManager.getRefreshToken().first()
}
val request = chain.request().newBuilder()
request.addHeader(Constantes.AUTHORIZATION, Constantes.BEARER + token)
return chain.proceed(request.build())
 */
