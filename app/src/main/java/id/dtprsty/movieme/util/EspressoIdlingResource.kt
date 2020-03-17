package id.dtprsty.movieme.util

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {
    private val RESOURCE: String? = "GLOBAL"
    private val espressoIdlingTestResource = CountingIdlingResource(RESOURCE)

    fun increment(){
        espressoIdlingTestResource.increment()
    }

    fun decrement(){
        espressoIdlingTestResource.decrement()
    }

    fun getEspressoIdlingResource(): IdlingResource{
        return espressoIdlingTestResource
    }
}