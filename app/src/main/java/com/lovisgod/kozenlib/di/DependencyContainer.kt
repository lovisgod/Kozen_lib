package com.lovisgod.kozenlib.di

import android.content.Context
import com.lovisgod.kozenlib.core.data.dataInteractor.IswConfigSourceInteractor
import com.lovisgod.kozenlib.core.data.dataInteractor.IswDetailsAndKeySourceInteractor
import com.lovisgod.kozenlib.core.data.dataInteractor.IswPrinterInteractor
import com.lovisgod.kozenlib.core.data.dataInteractor.IswTransactionInteractor
import com.lovisgod.kozenlib.core.data.dataSourceImpl.IswDataConfig
import com.lovisgod.kozenlib.core.data.dataSourceImpl.IswDetailsAndKeysImpl
import com.lovisgod.kozenlib.core.data.dataSourceImpl.IswPrinterImpl
import com.lovisgod.kozenlib.core.data.dataSourceImpl.IswTransactionImpl
import com.lovisgod.kozenlib.core.data.datasource.IswConfigDataSource
import com.lovisgod.kozenlib.core.data.datasource.IswDetailsAndKeyDataSource
import com.lovisgod.kozenlib.core.data.datasource.IswPrinterDataSource
import com.lovisgod.kozenlib.core.data.datasource.IswTransactionDataSource
import com.lovisgod.kozenlib.core.data.utilsData.Constants
import com.lovisgod.kozenlib.core.network.AuthInterfaceKozen
import com.lovisgod.kozenlib.core.network.KimonoInterfaceKozen
import com.lovisgod.kozenlib.core.utilities.CardCheckerHandler2
import com.lovisgod.kozenlib.core.utilities.EmvHandler
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

object DependencyContainer {
    private lateinit var applicationContext: Context

    private val okHttpClientBuilder by lazy {
        OkHttpClient.Builder()
    }

    private val kimonoInterfaceKozen by lazy {

        val interceptor = Interceptor { chain ->
            val token = Prefs.getString(Constants.TOKEN)
            val request = chain.request().newBuilder()
                .addHeader("Content-type", "application/json")
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }

        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val strategy = AnnotationStrategy()
        val serializer = Persister(strategy)

        val clientBuilder = okHttpClientBuilder
            .addInterceptor(logger)
        // Uncomment the next line if you want to add the auth interceptor
        // .addInterceptor(interceptor)

        Retrofit.Builder()
            .baseUrl(Constants.ISW_KIMONO_BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(serializer))
            .addCallAdapterFactory(SimpleCallAdapterFactory.create())
            .client(clientBuilder.build())
            .build()
            .create(KimonoInterfaceKozen::class.java)
    }

    private val authInterfaceKozen by lazy {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val clientBuilder = okHttpClientBuilder
            .addInterceptor(logger)

        Retrofit.Builder()
            .baseUrl(Constants.ISW_KIMONO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(SimpleCallAdapterFactory.create())
            .client(clientBuilder.build())
            .build()
            .create(AuthInterfaceKozen::class.java)
    }

    // Lazy initialization for singletons
    private val depIswDataConfig by lazy { IswDataConfig(applicationContext) }
    private val depIswDetailsAndKeys by lazy { IswDetailsAndKeysImpl(authInterfaceKozen, kimonoInterfaceKozen) }
    private val depIswConfigSourceInteractor by lazy { IswConfigSourceInteractor(depIswDataConfig) }
    private val depIswDetailsAndKeySourceInteractor by lazy { IswDetailsAndKeySourceInteractor(depIswDetailsAndKeys) }
    private val depIswPrinter by lazy { IswPrinterImpl() }
    private val depIswPrinterInteractor by lazy { IswPrinterInteractor(depIswPrinter) }
    private val depEmvHandler by lazy { EmvHandler() }
    private val depCardCheckerHandler2 by lazy { CardCheckerHandler2() }
    private val depIswTransactionInteractor by lazy { IswTransactionInteractor(createIswTransactionDataSource()) }
    private val depIswTransactionDataSource by lazy { createIswTransactionDataSource() }

    fun init(context: Context) {
        applicationContext = context.applicationContext
    }

    // Singleton accessors
    fun getIswDataConfig(): IswDataConfig = depIswDataConfig
    fun getIswDetailsAndKeys(): IswDetailsAndKeysImpl = depIswDetailsAndKeys
    fun getIswConfigSourceInteractor(): IswConfigSourceInteractor = depIswConfigSourceInteractor
    fun getIswDetailsAndKeySourceInteractor(): IswDetailsAndKeySourceInteractor = depIswDetailsAndKeySourceInteractor
    fun getIswPrinter(): IswPrinterImpl = depIswPrinter
    fun getIswPrinterInteractor(): IswPrinterInteractor = depIswPrinterInteractor
    fun getIswTransactionInteractor(): IswTransactionInteractor = depIswTransactionInteractor
    fun getIswTransactionDataSource(): IswTransactionDataSource = depIswTransactionDataSource

    // Factory methods
    fun createIswConfigDataSource(): IswConfigDataSource = IswDataConfig(applicationContext)
    fun createIswDetailsAndKeyDataSource(): IswDetailsAndKeyDataSource = IswDetailsAndKeysImpl(
        authInterfaceKozen, kimonoInterfaceKozen)
    fun createIswPrinterDataSource(): IswPrinterDataSource = IswPrinterImpl()
    fun createIswTransactionDataSource(): IswTransactionDataSource = IswTransactionImpl(depEmvHandler, depCardCheckerHandler2)
}