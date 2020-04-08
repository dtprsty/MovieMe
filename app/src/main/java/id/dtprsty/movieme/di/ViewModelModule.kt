package id.dtprsty.movieme.di

import id.dtprsty.movieme.ui.detail.DetailViewModel
import id.dtprsty.movieme.ui.movie.MovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewMOdelModule = module {
    viewModel { MovieViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}