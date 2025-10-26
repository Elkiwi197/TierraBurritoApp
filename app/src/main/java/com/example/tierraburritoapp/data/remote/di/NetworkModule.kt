package com.example.tierraburritoapp.data.remote.di


import com.example.tierraburritoapp.BuildConfig
import com.example.tierraburritoapp.data.remote.apiservices.AuthApiService
import com.example.tierraburritoapp.data.remote.apiservices.GoogleService
import com.example.tierraburritoapp.data.remote.apiservices.LoginSignupService
import com.example.tierraburritoapp.data.remote.apiservices.PedidosService
import com.example.tierraburritoapp.data.remote.apiservices.PlatosService
import com.example.tierraburritoapp.data.remote.apiservices.ProductosService
import com.example.tierraburritoapp.data.remote.repositories.GoogleRepository
import com.example.tierraburritoapp.data.remote.repositories.LoginSignupRepository
import com.example.tierraburritoapp.data.remote.repositories.PedidosRepository
import com.example.tierraburritoapp.data.remote.repositories.PlatosRepository
import com.example.tierraburritoapp.data.remote.repositories.ProductosRepository
import com.example.tierraburritoapp.data.utils.AuthAuthenticator
import com.example.tierraburritoapp.data.utils.AuthInterceptor
import com.example.tierraburritoapp.domain.usecases.coordenadas.GetCoordenadasUseCase
import com.example.tierraburritoapp.domain.usecases.ingredientes.GetExtrasByPlatoUseCase
import com.example.tierraburritoapp.domain.usecases.ingredientes.GetIngredientesByPlatoUseCase
import com.example.tierraburritoapp.domain.usecases.loginsignup.LogInUseCase
import com.example.tierraburritoapp.domain.usecases.loginsignup.SignUpUseCase
import com.example.tierraburritoapp.domain.usecases.pedidos.AnadirPedidoUseCase
import com.example.tierraburritoapp.domain.usecases.pedidos.GetPedidosByCorreoUseCase
import com.example.tierraburritoapp.domain.usecases.pedidos.GetPedidosEnPreparacionUseCase
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
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideAuthApiService(
        @Named("ServidorTierraBurrito")
        retrofit: Retrofit
    ): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

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
        authenticator: AuthAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .authenticator(authenticator)
            .build()
        //todo mandar el access token todo el rato, y poner e refresh token cuando de un 403 (forbidden)
    }

    @Provides
    @Named("ServidorTierraBurrito")
    fun provideRetrofitTierraBurritoServidor(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.URL_TIERRA_BURRITO)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Named("GoogleMaps")
    fun provideRetrofitGoogle(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.URL_API_GOOGLE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideLoginSignupService(@Named("ServidorTierraBurrito") retrofit: Retrofit): LoginSignupService {
        return retrofit.create(LoginSignupService::class.java)
    }

    @Provides
    @Singleton
    fun providePlatosService(@Named("ServidorTierraBurrito") retrofit: Retrofit): PlatosService {
        return retrofit.create(PlatosService::class.java)
    }

    @Provides
    @Singleton
    fun providePedidosService(@Named("ServidorTierraBurrito") retrofit: Retrofit): PedidosService {
        return retrofit.create(PedidosService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductosService(@Named("ServidorTierraBurrito") retrofit: Retrofit): ProductosService {
        return retrofit.create(ProductosService::class.java)
    }

    @Provides
    @Singleton
    fun provideGoogleService(@Named("GoogleMaps") retrofit: Retrofit): GoogleService {
        return retrofit.create(GoogleService::class.java)
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
        fun provideAnadirPedidoUseCase(
            repo: PedidosRepository
        ): AnadirPedidoUseCase {
            return AnadirPedidoUseCase(repo)
        }

        @Provides
        fun provideGetPedidosByCorreoUseCase(
            repo: PedidosRepository
        ): GetPedidosByCorreoUseCase {
            return GetPedidosByCorreoUseCase(repo)
        }

        @Provides
        fun provideGetPedidosEnPreparacionUseCase(
            repo: PedidosRepository
        ): GetPedidosEnPreparacionUseCase {
            return GetPedidosEnPreparacionUseCase(repo)
        }
    }

    @Module
    @InstallIn(ViewModelComponent::class)
    object IngredientesUseCaseModule {
        @Provides
        fun provideGetIngredientesByPlatoUseCase(
            repo: ProductosRepository
        ): GetIngredientesByPlatoUseCase {
            return GetIngredientesByPlatoUseCase(repo)
        }

        @Provides
        fun provideGetExtrasByPlatoUseCase(
            repo: ProductosRepository
        ): GetExtrasByPlatoUseCase {
            return GetExtrasByPlatoUseCase(repo)
        }
    }

    @Module
    @InstallIn(ViewModelComponent::class)
    object CoordenadasGoogleUseCaseModule {
        @Provides
        fun provideGetCoordenadas(
            repo: GoogleRepository
        ): GetCoordenadasUseCase {
            return GetCoordenadasUseCase(repo)
        }
    }


}