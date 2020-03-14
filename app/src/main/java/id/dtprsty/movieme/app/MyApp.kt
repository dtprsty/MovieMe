package id.dtprsty.movieme.app

import android.app.Application
import id.dtprsty.movieme.BuildConfig
import id.dtprsty.movieme.data.local.MovieDao
import id.dtprsty.movieme.data.local.MovieDb
import timber.log.Timber
import timber.log.Timber.DebugTree

class MyApp : Application() {

    private lateinit var movieDao: MovieDao

    companion object {
        lateinit var mInstance: MyApp
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this

        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }

    fun getMovieDao(): MovieDao{
        return MovieDb.getInstance(this).movieDao()
    }

}