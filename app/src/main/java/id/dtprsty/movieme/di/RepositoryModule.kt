package id.dtprsty.movieme.di

import id.dtprsty.movieme.data.MovieRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        MovieRepository (get())
    }

}