package com.sam.gogoeat.view.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.android.gms.maps.model.LatLng
import com.google.common.truth.Truth.assertThat
import com.sam.gogoeat.CoroutineTestRule
import com.sam.gogoeat.data.SettingData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SearchViewModel
    private lateinit var fakeUerSetting: SettingData

    @Before
    fun setup() {
        fakeUerSetting = SettingData(
            LatLng(121.2, 24.5), "food", 2000, isOpen = false)
        viewModel = SearchViewModel()
        viewModel.setCurrentValues(fakeUerSetting)
    }

    @Test
    fun `check current settings is set correct`() = runBlocking {
        viewModel.keyWordStr.test {
            assertThat(awaitItem()).isEqualTo("food")
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.onlyOpen.test {
            assertThat(awaitItem()).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `check reset data is reset correct`() = runBlocking {
        viewModel.resetData()
        viewModel.keyWordStr.test {
            assertThat(awaitItem()).isEqualTo("")
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.onlyOpen.test {
            assertThat(awaitItem()).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.distanceValue.test {
            assertThat(awaitItem()).isEqualTo(1000)
            cancelAndIgnoreRemainingEvents()
        }
    }


}