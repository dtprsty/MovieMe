package id.dtprsty.movieme.di

import androidx.room.Room
import id.dtprsty.movieme.data.local.MovieDb
import id.dtprsty.movieme.util.Constant
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val DbModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            MovieDb::class.java,
            Constant.DB_NAME
        ).fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
    single {
        get<MovieDb>().movieDao()
    }
}