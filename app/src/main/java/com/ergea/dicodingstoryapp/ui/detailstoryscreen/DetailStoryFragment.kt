package com.ergea.dicodingstoryapp.ui.detailstoryscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ergea.dicodingstoryapp.data.remote.model.story.Story
import com.ergea.dicodingstoryapp.databinding.FragmentDetailStoryBinding
import com.ergea.dicodingstoryapp.wrapper.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailStoryFragment : Fragment() {

    private var _binding: FragmentDetailStoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailStoryViewModel by viewModels()
    private val navArgs: DetailStoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailStoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        back()
    }

    private fun back() {
        binding.icBackBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    private fun observeData() {
        viewModel.getToken().observe(viewLifecycleOwner) {
            viewModel.getDetailStory(it, navArgs.id)
        }
        viewModel.detailStory.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.animationLoading.isVisible = true
                }
                is Resource.Error -> {
                    binding.animationLoading.isVisible = false
                    view?.let { view -> showSnackBar(view, it.message.toString()) }
                }
                is Resource.Success -> {
                    binding.animationLoading.isVisible = false
                    it.data?.story?.let { story -> bindView(story) }
                }
                is Resource.Empty -> {
                    binding.animationLoading.isVisible = false
                }
            }
        }
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    private fun bindView(story: Story) {
        binding.apply {
            Glide.with(requireContext()).load(story.photoUrl).into(backdrop)
            tvName.text = story.name
            tvDetailDescription.text = story.description
            tvDate.text = story.createdAt?.substring(0, 10)
            tvClock.text = story.createdAt?.substring(11, 16)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}