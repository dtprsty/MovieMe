package id.dtprsty.movieme.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import id.dtprsty.movieme.BuildConfig
import id.dtprsty.movieme.data.local.FavoriteMovie
import id.dtprsty.movieme.data.remote.ApiService
import id.dtprsty.movieme.data.remote.movie.Movie
import id.dtprsty.movieme.data.remote.movie.MovieResponse
import id.dtprsty.movieme.data.remote.movie.MovieRepository
import id.dtprsty.movieme.ui.movie.MovieViewModel
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
class MovieViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Mock
    private lateinit var movieRepository: MovieRepository

    @Mock
    private lateinit var observer: Observer<MovieResponse>

    @Mock
    private lateinit var viewModel: MovieViewModel

    @Mock
    private lateinit var apiService: ApiService

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        viewModel =
            MovieViewModel(movieRepository)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun getMovieResponse() {
        val movies: MutableList<Movie> = mutableListOf()
        val response = MovieResponse(movies)

        viewModel.viewModelScope.launch {
            Mockito.`when`(movieRepository.getNowPlaying()).thenReturn(response)
            Mockito.`when`(
                apiService.getMovies(
                    ArgumentMatchers.anyString(),
                    Locale.getDefault().toString(),
                    BuildConfig.API_KEY
                )
            ).thenReturn(response)

            viewModel.movieResponse.observeForever(observer)
            Mockito.verify(observer).onChanged(movieRepository.getNowPlaying())

            val movie = viewModel.movieResponse.value
            assertNotNull(movie)
            assertEquals(movieRepository.getNowPlaying(), movie)
        }
    }

    @Test
    fun getMovieHighlight() {
        var movies: MutableList<Movie> = mutableListOf()
        val response = MovieResponse(movies)

        viewModel.viewModelScope.launch {
            Mockito.`when`(movieRepository.getPopular()).thenReturn(response)
            Mockito.`when`(
                apiService.getMovies(
                    ArgumentMatchers.anyString(),
                    Locale.getDefault().toString(),
                    BuildConfig.API_KEY
                )
            ).thenReturn(response)
            viewModel.movieHighlight.observeForever(observer)
            Mockito.verify(observer).onChanged(movieRepository.getPopular())

            val movie = viewModel.movieHighlight.value
            assertNotNull(movie)
            assertEquals(movieRepository.getPopular(), movie?.listMovie?.size)
        }
    }
}