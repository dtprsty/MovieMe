package id.dtprsty.movieme.ui.movie

import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.nhaarman.mockitokotlin2.mock
import id.dtprsty.movieme.InstantTaskExecutorRule
import id.dtprsty.movieme.data.remote.BaseResponse
import id.dtprsty.movieme.data.remote.movie.Movie
import id.dtprsty.movieme.data.remote.movie.MovieRepository
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.mockito.Mockito.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class MovieViewModelTest : Spek({

    val mainThreadSurrogate = newSingleThreadContext("UI thread")

    InstantTaskExecutorRule(this)

    val repository = mock(MovieRepository::class.java)
    val viewModel by memoized { MovieViewModel(repository) }

    val observer: Observer<BaseResponse<MutableList<Movie>>> = mock()

    beforeEachTest {
        Dispatchers.setMain(mainThreadSurrogate)
        viewModel.movieResponse.observeForever(observer)
    }

    afterEachTest {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }


    Feature("List Movie") {
        Scenario("get and show list movie properly") {
            val data = BaseResponse<MutableList<Movie>>()

            Given("Movie data") {
                viewModel.viewModelScope.launch {
                    `when`(repository.getNowPlaying()).thenReturn(data)
                }
            }

            When("Request Movie Data") {
                viewModel.viewModelScope.launch {
                    repository.getNowPlaying()
                }
            }

            Then("Return movie data correctly") {
                viewModel.viewModelScope.launch {
                    verify(repository).getNowPlaying()
                    verify(repository).getPopular()
                    assertNotNull(viewModel.movieResponse.value)
                    assertNotNull(viewModel.movieHighlight.value)
                    assertNotNull(viewModel.loadingState.value)
                    verify(observer).onChanged(repository.getNowPlaying())
                }
            }
        }
    }
})