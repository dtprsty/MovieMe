package id.dtprsty.movieme.di

import android.content.Context
import id.dtprsty.movieme.data.MovieRepository
import id.dtprsty.movieme.data.local.MovieDb

object Injection {
    fun provideRepository(): MovieRepository {
        return MovieRepository.geInstance()
    }

    fun provideMovieDao(context: Context): MovieDb{
        return MovieDb.getInstance(context)
    }
}