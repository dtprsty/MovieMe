package id.dtprsty.movieme.feature.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import id.dtprsty.movieme.BuildConfig
import id.dtprsty.movieme.data.MovieRepository
import id.dtprsty.movieme.data.local.FavoriteMovie
import id.dtprsty.movieme.data.remote.ApiService
import id.dtprsty.movieme.data.remote.movie.Movie
import id.dtprsty.movieme.data.remote.movie.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner.Silent::class)
class MainViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Mock
    private lateinit var movieRepository: MovieRepository

    @Mock
    private lateinit var observer: Observer<MovieResponse>

    @Mock
    private lateinit var observerFavorit: Observer<MutableList<FavoriteMovie>>

    @Mock
    private lateinit var viewModel: MainViewModel
    @Mock
    private lateinit var apiService: ApiService

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp(){
        viewModel = MainViewModel(movieRepository)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun getMovieResponse() {
        val movies: MutableList<Movie> = mutableListOf()
        val response = MovieResponse(movies)

        viewModel.viewModelScope.launch{
            Mockito.`when`(movieRepository.getNowPlaying()).thenReturn(response)
            Mockito.`when`(apiService.getMovies(ArgumentMatchers.anyString(), Locale.getDefault().toString(), BuildConfig.API_KEY)).thenReturn(response)

            viewModel.movieResponse.observeForever(observer)
            Mockito.verify(observer).onChanged(movieRepository.getNowPlaying())

            val movies = viewModel.movieResponse.value
            assertNotNull(movies)
            assertEquals(movieRepository.getNowPlaying(), movies)
        }
    }

    @Test
    fun getMovieFavorite() {
        val favoriteMovie: MutableList<FavoriteMovie> = mutableListOf()
        viewModel.viewModelScope.launch{
            Mockito.`when`(movieRepository.movies()).thenReturn(favoriteMovie)
            Mockito.verify(movieRepository).movies()
            assertNotNull(viewModel.getMovies(3)) //Now Playing

            viewModel.movieFavorite.observeForever(observerFavorit)
            Mockito.verify(observerFavorit).onChanged(movieRepository.movies())
        }

    }

    @Test
    fun getMovieHighlight() {
        val movies: MutableList<Movie> = mutableListOf()
        val response = MovieResponse(movies)

        viewModel.viewModelScope.launch{
            Mockito.`when`(movieRepository.getPopular()).thenReturn(response)
            Mockito.`when`(apiService.getMovies(ArgumentMatchers.anyString(), Locale.getDefault().toString(), BuildConfig.API_KEY)).thenReturn(response)
            viewModel.movieResponse.observeForever(observer)
            Mockito.verify(observer).onChanged(movieRepository.getPopular())

            val movies = viewModel.movieResponse.value
            assertNotNull(movies)
            assertEquals(5, movies?.listMovie?.size)
        }
    }
}