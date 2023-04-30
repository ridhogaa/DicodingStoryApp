package com.ergea.dicodingstoryapp.ui.registerscreen

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ergea.dicodingstoryapp.R
import com.ergea.dicodingstoryapp.data.remote.model.auth.RegisterBody
import com.ergea.dicodingstoryapp.databinding.FragmentRegisterBinding
import com.ergea.dicodingstoryapp.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        handleOnBackPressed()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toSignIn()
        signUp()
        observeData()
        playAnimation()
    }

    private fun playAnimation() {
        val username = ObjectAnimator.ofFloat(binding.tilUsername, View.ALPHA, 1f).setDuration(400)
        val email = ObjectAnimator.ofFloat(binding.tilEmail, View.ALPHA, 1f).setDuration(400)
        val password = ObjectAnimator.ofFloat(binding.tilPassword, View.ALPHA, 1f).setDuration(400)
        val btn = ObjectAnimator.ofFloat(binding.btnSignUp, View.ALPHA, 1f).setDuration(400)

        AnimatorSet().apply {
            playSequentially(username, email, password, btn)
            start()
        }
    }

    private fun observeData() {
        viewModel.register.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.animationLoading.isVisible = true
                }
                is Resource.Error -> {
                    binding.animationLoading.isVisible = false
                    Toast.makeText(
                        requireContext(),
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Empty -> {
                    binding.animationLoading.isVisible = false
                }
                is Resource.Success -> {
                    binding.animationLoading.isVisible = false
                    Toast.makeText(
                        requireContext(),
                        it.data?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.loginFragment)
                }
            }
        }
    }

    private fun signUp() {
        binding.btnSignUp.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (validateInput()) {
                viewModel.register(RegisterBody(username, email, password))
            }
        }
    }

    private fun validateInput(): Boolean {
        var flag = true
        binding.apply {
            if (etUsername.text.toString().isEmpty()) {
                flag = false
                tilUsername.error = getString(R.string.username_empty)
                etUsername.requestFocus()
            } else if (etEmail.text.toString().isEmpty()) {
                flag = false
                tilEmail.error = getString(R.string.email_empty)
                etEmail.requestFocus()
            } else if (etPassword.text.toString().isEmpty()) {
                flag = false
                tilPassword.error = getString(R.string.password_empty)
                etPassword.requestFocus()
            }
            if (tilUsername.isErrorEnabled) {
                flag = false
            } else if (tilEmail.isErrorEnabled) {
                flag = false
            } else if (tilPassword.isErrorEnabled) {
                flag = false
            }
        }
        return flag
    }

    private fun toSignIn() {
        binding.tvSignIn.setOnClickListener {
            it.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun handleOnBackPressed() {
        val callbacks: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callbacks)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}