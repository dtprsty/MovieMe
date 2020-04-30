package id.dtprsty.movieme.di

import id.dtprsty.movieme.data.local.FavoriteRepository
import id.dtprsty.movieme.data.remote.movie.MovieRepository
import id.dtprsty.movieme.data.remote.detail.DetailRepository
import id.dtprsty.movieme.data.remote.tvshow.TvShowRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        MovieRepository(get())
    }
    single {
        TvShowRepository(get())
    }
    single {
        FavoriteRepository(get())
    }
    single {
        DetailRepository(get())
    }
}