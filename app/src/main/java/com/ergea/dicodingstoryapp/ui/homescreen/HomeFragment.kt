package com.ergea.dicodingstoryapp.ui.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ergea.dicodingstoryapp.R
import com.ergea.dicodingstoryapp.databinding.FragmentHomeBinding
import com.ergea.dicodingstoryapp.ui.homescreen.adapter.LoadingStateAdapter
import com.ergea.dicodingstoryapp.ui.homescreen.adapter.StoriesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var storiesAdapter: StoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllStories()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        observeData()
        settings()
        addStory()
        maps()
    }

    private fun maps() {
        binding.btnLocation.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_mapsFragment)
        }
    }

    private fun addStory() {
        binding.fabAddStory.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addStoryFragment)
        }
    }

    private fun settings() {
        binding.btnSettings.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
        }
    }

    private fun initList() {
        storiesAdapter = StoriesAdapter()
        binding.rvStory.apply {
            adapter =
                storiesAdapter.withLoadStateFooter(footer = LoadingStateAdapter { storiesAdapter.retry() })
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
        storiesAdapter.addLoadStateListener {
            when (it.source.refresh) {
                is LoadState.NotLoading -> {
                    binding.apply {
                        animationLoading.visibility = View.GONE
                        rvStory.visibility = View.VISIBLE
                        errorState.visibility = View.GONE
                    }
                }
                is LoadState.Loading -> {
                    binding.apply {
                        animationLoading.visibility = View.VISIBLE
                        rvStory.visibility = View.GONE
                        errorState.visibility = View.GONE
                    }
                }
                is LoadState.Error -> {
                    binding.apply {
                        animationLoading.visibility = View.GONE
                        rvStory.visibility = View.GONE
                        errorState.visibility = View.VISIBLE
                    }
                    val errorState = it.source.refresh as LoadState.Error
                    Toast.makeText(requireContext(), errorState.error.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun observeData() {
        viewModel.story.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach(storiesAdapter::submitData)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        (view?.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}