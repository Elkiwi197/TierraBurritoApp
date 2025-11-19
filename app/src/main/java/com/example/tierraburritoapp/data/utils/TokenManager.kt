package com.example.tierraburritoapp.data.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.tierraburritoapp.common.Constantes
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val Context.dataStore by preferencesDataStore(name = Constantes.DATASTORE)

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey(Constantes.ACCESS_TOKEN)
        private val REFRESH_TOKEN = stringPreferencesKey(Constantes.REFRESH_TOKEN)
    }

    private val _sessionExpired = MutableStateFlow(false)
    val sessionExpired: StateFlow<Boolean> get() = _sessionExpired

    fun setSessionExpired() {
        _sessionExpired.value = true
    }

    fun clearSessionExpired() {
        _sessionExpired.value = false
    }

    fun getAccessToken(): Flow<String?> {
        return context.dataStore.data.map { preferences -> preferences[ACCESS_TOKEN] }
    }

    fun getRefreshToken(): Flow<String?> {
        return context.dataStore.data.map { preferences -> preferences[REFRESH_TOKEN] }
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun deleteTokens() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
            preferences.remove(REFRESH_TOKEN)
        }
    }

}
