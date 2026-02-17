package com.lovisgod.kozenlib.di


import com.lovisgod.kozenlib.core.data.utilsData.Constants
import com.lovisgod.kozenlib.core.network.AuthInterfaceKozen
//import com.lovisgod.kozenlib.core.network.CardLess.UserStore
import com.lovisgod.kozenlib.core.network.KimonoInterfaceKozen
import com.lovisgod.kozenlib.core.utilities.simplecalladapter.SimpleCallAdapterFactory
import com.pixplicity.easyprefs.library.Prefs
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

const val RETROFIT_KIMONO = "kimono_retrofit"
const val AUTH_INTERCEPTOR = "auth_interceptor"
const val RETROFIT_PAYMENT = "payment_retrofit"

//
//
//val networkModule = module {
//
//    factory(override = true) {
//
//        OkHttpClient.Builder()
//            .connectTimeout(60, TimeUnit.SECONDS)
//            .readTimeout(60, TimeUnit.SECONDS)
//            .writeTimeout(60, TimeUnit.SECONDS)
//    }
//
////    single<UserStore> { UserStoreImpl(get()) }
//
//
////    single {
////
////        // set base url based on env
////        val iswBaseUrl = Constants.ISW_TOKEN_BASE_URL
////
////
////        val builder = Retrofit.Builder()
////            .baseUrl(iswBaseUrl)
////            .addConverterFactory(GsonConverterFactory.create())
////            .addCallAdapterFactory(SimpleCallAdapterFactory.create())
////
////        // getResult the okhttp client for the retrofit
////        val clientBuilder: OkHttpClient.Builder = get()
////
////        val credentials = "${IswApplication.clientId}:${IswApplication.clientSecret}"
////        val encoding = Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
////
////        // add auth interceptor for max services
////        clientBuilder.addInterceptor { chain ->
////            val request = chain.request().newBuilder()
////                .addHeader("Authorization", "Basic $encoding")
////                .build()
////
////            return@addInterceptor chain.proceed(request)
////        }
////
////        // add client to retrofit builder
////        val client = clientBuilder.build()
////        builder.client(client)
////
////
////        val retrofit: Retrofit = builder.build()
////        return@single retrofit.create(IAuthService::class.java)
////    }
//
//    // retrofit interceptor for authentication
////    single(AUTH_INTERCEPTOR) {
////
////        val userManager: UserStore = get()
////        return@single Interceptor { chain ->
////            return@Interceptor userManager.getToken {
////                val request = chain.request().newBuilder()
////                    .addHeader("Content-type", "application/json")
////                    .addHeader("Authorization", "Bearer $it")
////                    .build()
////
////                return@getToken chain.proceed(request)
////            }
////        }
////    }
//
//
//    single {
//
//
//        val inttrr = Interceptor { chain ->
//            Prefs.getString(Constants.TOKEN)
//            val request = chain.request().newBuilder()
//                .addHeader("Content-type", "application/json")
//                .addHeader("Authorization", "Bearer $it")
//                .build()
//
//            return@Interceptor chain.proceed(request)
//
//        }
//        // set base url based on env
//        val iswBaseUrl = Constants.ISW_KIMONO_BASE_URL
//
//        val logger = HttpLoggingInterceptor()
//        logger.level = HttpLoggingInterceptor.Level.BODY
//
//        val authInterceptor: Interceptor = inttrr
//
//        val strategy = AnnotationStrategy()
//        val serializer = Persister(strategy)
//        val builder = Retrofit.Builder()
//            .baseUrl(iswBaseUrl)
//            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(serializer))
//            .addCallAdapterFactory(SimpleCallAdapterFactory.create())
//
//        // getResult the okhttp client for the retrofit
//        val clientBuilder: OkHttpClient.Builder = get()
//
//
//        clientBuilder.addInterceptor(logger)
//        // add auth interceptor for max services
////        clientBuilder.addInterceptor(authInterceptor)
//
//        // add client to retrofit builder
//        val client = clientBuilder.build()
//        builder.client(client)
//
//
//        val retrofit: Retrofit = builder.build()
//        return@single retrofit.create(KimonoInterfaceKozen::class.java)
//    }
//
//
//    // create Auth service with retrofit
//    single {
//
//        // set base url based on env
//        val iswBaseUrl = Constants.ISW_KIMONO_BASE_URL
//
//        val logger = HttpLoggingInterceptor()
//        logger.level = HttpLoggingInterceptor.Level.BODY
//
//        val builder = Retrofit.Builder()
//            .baseUrl(iswBaseUrl)
//            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(SimpleCallAdapterFactory.create())
//
//        // getResult the okhttp client for the retrofit
//        val clientBuilder: OkHttpClient.Builder = get()
//
//
//        clientBuilder.addInterceptor(logger)
//
//        // add client to retrofit builder
//        val client = clientBuilder.build()
//        builder.client(client)
//
//
//        val retrofit: Retrofit = builder.build()
//        return@single retrofit.create(AuthInterfaceKozen::class.java)
//    }
//
//
//    // retrofit isw payment
////    single(RETROFIT_PAYMENT) {
////        val iswBaseUrl = Constants.ISW_USSD_QR_BASE_URL
////
////        val interceptor = HttpLoggingInterceptor()
////        interceptor.level = HttpLoggingInterceptor.Level.BODY
////        // androidContext().getString(R.string.ISW_USSD_QR_BASE_URL)
////        val builder = Retrofit.Builder()
////            .baseUrl(iswBaseUrl)
////            .addConverterFactory(GsonConverterFactory.create())
////            .addCallAdapterFactory(SimpleCallAdapterFactory.create())
////
////        // getResult the okhttp client for the retrofit
////        val clientBuilder: OkHttpClient.Builder = get()
////
////        // getResult auth interceptor for client
////        val authInterceptor: Interceptor = get(AUTH_INTERCEPTOR)
////        // add auth interceptor for max services
////        clientBuilder.addInterceptor(authInterceptor)
////        clientBuilder.addInterceptor(interceptor)
////
////        // add client to retrofit builder
////        val client = clientBuilder.build()
////        builder.client(client)
////
////        return@single builder.build()
////    }
//
//    // create payment Http service with retrofit
////    single {
////        val retrofit: Retrofit = get(RETROFIT_PAYMENT)
////        return@single retrofit.create(IHttpService::class.java)
////    }
//
////    single<HttpService> { HttpServiceImpl(get()) }
//
//}
