package id.dtprsty.movieme.di

import id.dtprsty.movieme.data.remote.review.MovieRepository
import id.dtprsty.movieme.data.remote.tvshow.TvShowRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        MovieRepository(get(), get())
    }
    single {
        TvShowRepository(get())
    }
}