package com.sam.gogoeat.view.lotteryhistory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.sam.gogoeat.CoroutineTestRule
import com.sam.gogoeat.api.usecase.GetHistories
import com.sam.gogoeat.api.usecase.GetMyLocation
import com.sam.gogoeat.api.usecase.GetNearbyFoodsData
import com.sam.gogoeat.api.usecase.InsertHistoryItem
import com.sam.gogoeat.data.GogoPlace
import com.sam.gogoeat.repository.FakeRepository
import com.sam.gogoeat.view.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LotteryHistoryViewModelTest {

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LotteryHistoryViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var repository: FakeRepository
    private lateinit var getHistories: GetHistories
    private lateinit var getNearbyFoodsData: GetNearbyFoodsData
    private lateinit var getMyLocation: GetMyLocation
    private lateinit var insertHistoryItem: InsertHistoryItem

    @Before
    fun setup() {
        repository = FakeRepository()
        getHistories = GetHistories(repository)
        insertHistoryItem = InsertHistoryItem(repository)
        getMyLocation = GetMyLocation(repository)
        getNearbyFoodsData = GetNearbyFoodsData(repository)
        viewModel = LotteryHistoryViewModel(getHistories)
        mainViewModel = MainViewModel(getNearbyFoodsData, getMyLocation, insertHistoryItem)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `check the historic items is Empty`() = runBlocking {
        viewModel.getHistoriesResult.test {
            val result1 = awaitItem()
            val result2 = awaitItem()
            val result3 = awaitItem()
            Truth.assertThat(result1.isNothing()).isTrue()
            Truth.assertThat(result2.isLoading()).isTrue()
            Truth.assertThat(result3.data?.isEmpty()).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `check the historic items are same as mainViewModel insert`() = runBlocking {
        viewModel.getHistoriesResult.test {
            mainViewModel.insertHistoryItem(GogoPlace(id = 1))
            delay(200)
            viewModel.getHistories()
            delay(200)
            val result = expectMostRecentItem()
            Truth.assertThat(result.data?.size).isEqualTo(1)
            Truth.assertThat(result.data?.get(0)?.id).isEqualTo(1)
            cancelAndIgnoreRemainingEvents()
        }
    }
}