package id.dtprsty.movieme.di

import id.dtprsty.movieme.ui.detail.DetailViewModel
import id.dtprsty.movieme.ui.favorite.FavoriteViewModel
import id.dtprsty.movieme.ui.movie.MovieViewModel
import id.dtprsty.movieme.ui.tv_show.TvShowViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MovieViewModel(get()) }
    viewModel { TvShowViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }
}