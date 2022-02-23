package com.sam.gogoeat.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sam.gogoeat.CoroutineTestRule
import com.sam.gogoeat.api.usecase.GetMyLocation
import com.sam.gogoeat.api.usecase.GetNearbyFoodsData
import com.sam.gogoeat.api.usecase.InsertHistoryItem
import com.sam.gogoeat.data.GogoPlace
import com.sam.gogoeat.repository.FakeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel : MainViewModel
    private lateinit var repository: FakeRepository
    private lateinit var getNearbyFoodsData: GetNearbyFoodsData
    private lateinit var getMyLocation: GetMyLocation
    private lateinit var insertHistoryItem: InsertHistoryItem

    @Before
    fun setup() {
        repository = FakeRepository()
        getNearbyFoodsData = GetNearbyFoodsData(repository)
        getMyLocation = GetMyLocation(repository)
        insertHistoryItem = InsertHistoryItem(repository)
        viewModel = MainViewModel(getNearbyFoodsData, getMyLocation, insertHistoryItem)
    }

    @Test
    fun `get nearby place success`() = runBlocking {
        viewModel.nearbyFoodResult.test(5000) {
            viewModel.getNearbyFoods()
            val result1 = awaitItem()
            val result2 = awaitItem()
            val result3 = awaitItem()
            assertThat(result1.isNothing()).isTrue()
            assertThat(result2.isLoading()).isTrue()
            assertThat(result3.isSuccess()).isTrue()
            assertThat(result3.data?.isEmpty()).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `get nearby place when net error return net error`() = runBlocking {
        repository.setShouldReturnNetError(true)
        viewModel.nearbyFoodResult.test(5000) {
            viewModel.getNearbyFoods()
            awaitItem()
            awaitItem()
            val result3 = awaitItem()
            assertThat(result3.isFailed()).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `insert history item success`() = runBlocking {
        viewModel.insertHistoryResult.test {
            viewModel.insertHistoryItem(GogoPlace(id = 1))
            val result1 = awaitItem()
            val result2 = awaitItem()
            val result3 = awaitItem()
            assertThat(result1.isNothing()).isTrue()
            assertThat(result2.isLoading()).isTrue()
            assertThat(result3.data).isEqualTo(1L)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `insert history item complete should set flow nothing`() = runBlocking {
        viewModel.completeInsertHistories()
        viewModel.insertHistoryResult.test {
            val result1 = awaitItem()
            assertThat(result1.isNothing()).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

}