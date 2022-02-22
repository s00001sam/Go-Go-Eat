package com.sam.gogoeat.room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.sam.gogoeat.data.GogoPlace
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class PlaceDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var roomDB : RoomDB
    private lateinit var dao: PlaceDao

    @Before
    fun setup() {
        roomDB = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RoomDB::class.java
        ).allowMainThreadQueries().build()
        dao = roomDB.placeDao()
    }

    @After
    fun teardown() {
        roomDB.close()
    }

    @Test
    @ExperimentalCoroutinesApi
    fun insertOne() = runTest {
        val fakePlace = GogoPlace(
            id = 1,
            name = "sam store",
            place_id = "123456",
            rating = 4.5,
            user_ratings_total = 100,
            lat = 121.0,
            lng = 24.3,
            open_now = true
        )
        val insertResult = dao.insertOne(fakePlace)
        val places = dao.getAll()

        assertThat(insertResult).isEqualTo(1)
        assertThat(places).contains(fakePlace)
    }

}