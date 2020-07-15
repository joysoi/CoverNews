package com.nikola.covernews

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.nikola.covernews.data.network.*
import com.nikola.covernews.ui.main.viewmodel.CoverViewModelFactory
import com.nikola.covernews.ui.main.viewmodel.SharedViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class NewsApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@NewsApplication))
        bind<ConnectivityInterceptor>() with singleton {
            ConnectivityInterceptorImpl(
                instance()
            )
        }
        bind<CacheInterceptor>() with singleton {
            CacheInterceptorImpl(
                instance()
            )
        }
        bind<NetworkInterceptor>() with singleton {
            NetworkInterceptorImpl()
        }
        bind() from singleton {
            NewsApiResponseService(
                instance(),
                instance(),
                instance()
            )
        }
        bind() from provider { CoverViewModelFactory(instance()) }
        bind() from provider { SharedViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}