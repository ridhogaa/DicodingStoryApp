package com.ergea.dicodingstoryapp.ui.homescreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.test
import com.ergea.dicodingstoryapp.data.repository.StoryRepository
import com.ergea.dicodingstoryapp.ui.homescreen.adapter.StoriesAdapter
import com.ergea.dicodingstoryapp.utils.DataDummy
import com.ergea.dicodingstoryapp.utils.MainDispatcherRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(storyRepository)
    }

    @Test
    fun `When successfully loading story data`() = runTest {
        val dummyListStory = DataDummy.generateDummyStory()
        val data = PagingData.from(dummyListStory)
        val expectedStory = flow { emit(data) }
        `when`(storyRepository.getAllStories()).thenReturn(expectedStory)
        homeViewModel.story.test {
            homeViewModel.getAllStories()
            Mockito.verify(storyRepository).getAllStories()
            val differ = AsyncPagingDataDiffer(
                diffCallback = StoriesAdapter.callback,
                updateCallback = noopListUpdateCallback,
                workerDispatcher = Dispatchers.Main
            )
            awaitItem()
            val actual = awaitItem()
            differ.submitData(actual)
            advanceUntilIdle()
            assertNotNull(differ.snapshot()) // data != null
            assertEquals(dummyListStory, differ.snapshot().items) // data same return type
            assertEquals(dummyListStory.size, differ.snapshot().size) // data size same
            assertEquals(dummyListStory[0], differ.snapshot()[0]) // first data same return type
        }
    }

    @Test
    fun `When there is no story data`() = runTest {
        val dummyListStory = DataDummy.emptyStory()
        val data = PagingData.from(dummyListStory)
        val expectedStory = flow { emit(data) }

        `when`(storyRepository.getAllStories()).thenReturn(expectedStory)

        homeViewModel.story.test {
            homeViewModel.getAllStories()
            Mockito.verify(storyRepository).getAllStories()

            val differ = AsyncPagingDataDiffer(
                diffCallback = StoriesAdapter.callback,
                updateCallback = noopListUpdateCallback,
                workerDispatcher = Dispatchers.Main
            )
            awaitItem()
            val actual = awaitItem()
            differ.submitData(actual)

            advanceUntilIdle()
            assertNotNull(differ.snapshot()) // data != null
            assertEquals(0, differ.snapshot().size) // data size same return
        }
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}