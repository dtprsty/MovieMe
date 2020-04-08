package id.dtprsty.movieme.app

import android.app.Application
import androidx.multidex.MultiDexApplication
import id.dtprsty.movieme.BuildConfig
import id.dtprsty.movieme.data.local.MovieDao
import id.dtprsty.movieme.data.local.MovieDb
import id.dtprsty.movieme.di.networkModule
import id.dtprsty.movieme.di.repositoryModule
import id.dtprsty.movieme.di.viewMOdelModule
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
            modules(listOf(networkModule, repositoryModule, viewMOdelModule))
        }

        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }

    fun getMovieDao(): MovieDao {
        return MovieDb.getInstance(this).movieDao()
    }

}