package id.dtprsty.movieme.ui.tv_show

import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.nhaarman.mockitokotlin2.mock
import id.dtprsty.movieme.InstantTaskExecutorRule
import id.dtprsty.movieme.data.remote.BaseResponse
import id.dtprsty.movieme.data.remote.tvshow.TvShow
import id.dtprsty.movieme.data.remote.tvshow.TvShowRepository
import id.dtprsty.movieme.shouldBeInstanceOf
import junit.framework.Assert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.mockito.Mockito
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class TvShowViewModelTest : Spek({

    val mainThreadSurrogate = newSingleThreadContext("UI thread")

    InstantTaskExecutorRule(this)

    val repository = Mockito.mock(TvShowRepository::class.java)
    val viewModel by memoized { TvShowViewModel(repository) }

    val observer: Observer<BaseResponse<MutableList<TvShow>>> = mock()

    beforeEachTest {
        Dispatchers.setMain(mainThreadSurrogate)
        viewModel.tvShowResponse.observeForever(observer)
    }

    afterEachTest {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }


    Feature("List Tv Show") {
        Scenario("get and show list tv show properly") {
            val data = BaseResponse<MutableList<TvShow>>()

            Given("Tv Show data") {
                viewModel.viewModelScope.launch {
                    Mockito.`when`(repository.getAiringToday()).thenReturn(data)
                }
            }

            When("Request Tv Show Data") {
                viewModel.viewModelScope.launch {
                    repository.getAiringToday()
                }
            }

            Then("Return tv show data correctly") {
                viewModel.viewModelScope.launch {
                    viewModel.tvShowResponse.value.shouldBeInstanceOf<BaseResponse<MutableList<TvShow>>>()
                    Mockito.verify(repository).getAiringToday()
                    Assert.assertNotNull(viewModel.tvShowResponse.value)
                    Assert.assertNotNull(viewModel.loadingState.value)
                    Mockito.verify(observer).onChanged(repository.getAiringToday())
                }
            }
        }
    }
})