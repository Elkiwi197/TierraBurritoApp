package com.example.tierraburritoapp.data.remote.di


import com.example.tierraburritoapp.BuildConfig
import com.example.tierraburritoapp.data.remote.apiservices.LoginSignupService
import com.example.tierraburritoapp.data.remote.apiservices.PedidosService
import com.example.tierraburritoapp.data.remote.apiservices.PlatosService
import com.example.tierraburritoapp.data.remote.repositories.LoginSignupRepository
import com.example.tierraburritoapp.data.remote.repositories.PedidosRepository
import com.example.tierraburritoapp.data.remote.repositories.PlatosRepository
import com.example.tierraburritoapp.data.utils.AuthInterceptor
import com.example.tierraburritoapp.domain.usecases.loginsignup.LogInUseCase
import com.example.tierraburritoapp.domain.usecases.loginsignup.SignUpUseCase
import com.example.tierraburritoapp.domain.usecases.pedidos.AnadirPedidoUseCase
import com.example.tierraburritoapp.domain.usecases.platos.GetPlatoByIdUseCase
import com.example.tierraburritoapp.domain.usecases.platos.GetPlatosUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        //  authenticator: AuthAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            //  .authenticator(authenticator)
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideLoginSignupService(retrofit: Retrofit): LoginSignupService {
        return retrofit.create(LoginSignupService::class.java)
    }

    @Provides
    @Singleton
    fun providePlatosService(retrofit: Retrofit): PlatosService {
        return retrofit.create(PlatosService::class.java)
    }

    @Provides
    @Singleton
    fun providePedidosService(retrofit: Retrofit): PedidosService {
        return retrofit.create(PedidosService::class.java)
    }

    @Module
    @InstallIn(ViewModelComponent::class)
    object LoginSignupUseCaseModule {
        @Provides
        fun provideLoginUseCase(
            repo: LoginSignupRepository
        ): LogInUseCase {
            return LogInUseCase(repo)
        }

        @Provides
        fun provideSignupUseCase(
            repo: LoginSignupRepository
        ): SignUpUseCase {
            return SignUpUseCase(repo)
        }
    }

     @Module
     @InstallIn(ViewModelComponent::class)
     object PlatosUseCaseModule {
         @Provides
         fun provideGetPlatosUseCase(
             repo: PlatosRepository
         ): GetPlatosUseCase {
             return GetPlatosUseCase(repo)
         }

         @Provides
         fun provideGetPlatoById(
             repo: PlatosRepository
         ): GetPlatoByIdUseCase {
             return GetPlatoByIdUseCase(repo)
         }
     }

    @Module
    @InstallIn(ViewModelComponent::class)
    object PedidosUseCaseModule {
        @Provides
        fun providePedidoUseCase(
            repo: PedidosRepository
        ): AnadirPedidoUseCase {
            return AnadirPedidoUseCase(repo)
        }

    }

}