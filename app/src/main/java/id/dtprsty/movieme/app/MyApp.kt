package id.dtprsty.movieme.app

import androidx.multidex.MultiDexApplication
import id.dtprsty.movieme.BuildConfig
import id.dtprsty.movieme.di.DbModule
import id.dtprsty.movieme.di.networkModule
import id.dtprsty.movieme.di.repositoryModule
import id.dtprsty.movieme.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree

class MyApp : MultiDexApplication() {

    companion object {
        lateinit var mInstance: MyApp
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(networkModule, repositoryModule, viewModelModule, DbModule))
        }

        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }
}