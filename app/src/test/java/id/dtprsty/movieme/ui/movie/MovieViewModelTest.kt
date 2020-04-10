package id.dtprsty.movieme.ui.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.dtprsty.movieme.data.remote.movie.MovieRepository
import org.mockito.Mockito.mock
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class MovieViewModelTest: Spek({

    InstantTaskExecutorRule(this)

    val repository = mock(MovieRepository::class.java)
    val viewModel = MovieViewModel(repository)

    Feature("List Movie"){
        Scenario("get and show list movie properly"){

        }
    }
})