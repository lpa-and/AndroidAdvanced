package at.technikumwien.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import at.technikumwien.maps.data.model.DrinkingFountain
import at.technikumwien.maps.data.remote.DrinkingFountainApi
import at.technikumwien.maps.ui.maps.MapsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class MapsViewModelTest {

    // Setup LiveData to work in Unit tests
    @get:Rule var rule = InstantTaskExecutorRule()

    private val drinkingFountainApi: DrinkingFountainApi = mockk()

    private val viewModel = MapsViewModel(drinkingFountainApi)

    @Before
    fun setup() {
        // Setup Coroutines to work in Unit tests
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun testSuccess() {
        // Given (define mock behavior)
        val drinkingFountain = DrinkingFountain("ID", "DrinkingFountain", 1.0, 2.0)
        val drinkingFountainList = listOf(drinkingFountain)
        coEvery { drinkingFountainApi.getDrinkingFountains() } returns drinkingFountainList

        // When (call method under test)
        viewModel.syncDrinkingFountains()

        // Then (verify behavior)
        assertEquals(drinkingFountainList, viewModel.drinkingFountains.value)
        assertNull(viewModel.errors.value)
    }

    @Test
    fun testError() {
        val exception = IOException("Error")
        coEvery { drinkingFountainApi.getDrinkingFountains() } throws exception

        viewModel.syncDrinkingFountains()

        assertNull(viewModel.drinkingFountains.value)
        assertEquals(exception, viewModel.errors.value)
    }
}