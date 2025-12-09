package com.example.tierraburritoapp.data.utils

import com.example.tierraburritoapp.data.remote.apiservices.AuthApiService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val authApiService: AuthApiService,
) : Authenticator {


    override fun authenticate(route: Route?, response: Response): Request? {
        Timber.i("AUTHENTICATOR", "Autenticando...")

        // Evitar bucles infinitos
        if (responseCount(response) >= 2) {
            Timber.e("AUTHENTICATOR", "Demasiados reintentos. Sesión expirada.")
            signalSessionExpired()
            return null
        }

        val refreshToken = runBlocking { tokenManager.getRefreshToken().firstOrNull() }
        if (refreshToken.isNullOrEmpty()) {
            Timber.e("AUTHENTICATOR", "No hay refresh token. Sesión expirada.")
            signalSessionExpired()
            return null
        }

        // --- Intentar refrescar tokens ---
        val newTokens = runBlocking {
            val res = authApiService.refreshToken("Bearer $refreshToken")
            if (res.isSuccessful) {
                res.body()?.also {
                    tokenManager.saveTokens(it.accessToken, it.refreshToken)
                }
            } else {
                null
            }
        }

        // Si refresh falla → sesión expirada
        if (newTokens == null) {
            Timber.e("AUTHENTICATOR", "Refresh fallido. Marcando sesión expirada.")
            signalSessionExpired()
            return null
        }

        // Reintentar con token renovado
        return response.request.newBuilder()
            .header("Authorization", "Bearer ${newTokens.accessToken}")
            .build()
    }

    private fun signalSessionExpired() = runBlocking {
        tokenManager.deleteTokens()
        tokenManager.setSessionExpired()
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}

