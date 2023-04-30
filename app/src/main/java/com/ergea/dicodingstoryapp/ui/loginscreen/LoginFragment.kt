package com.ergea.dicodingstoryapp.ui.loginscreen

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ergea.dicodingstoryapp.R
import com.ergea.dicodingstoryapp.data.remote.model.auth.LoginBody
import com.ergea.dicodingstoryapp.databinding.FragmentLoginBinding
import com.ergea.dicodingstoryapp.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toSignUp()
        signIn()
        playAnimation()
    }

    private fun playAnimation() {
        val email = ObjectAnimator.ofFloat(binding.tilEmail, View.ALPHA, 1f).setDuration(400)
        val password = ObjectAnimator.ofFloat(binding.tilPassword, View.ALPHA, 1f).setDuration(400)
        val btn = ObjectAnimator.ofFloat(binding.btnSignIn, View.ALPHA, 1f).setDuration(400)

        AnimatorSet().apply {
            playSequentially(email, password, btn)
            start()
        }
    }

    private fun observeResponse() {
        viewModel.login.observe(viewLifecycleOwner) {
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
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }
        }
    }

    private fun signIn() {
        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (validateInput()) {
                viewModel.login(LoginBody(email, password))
                observeResponse()
            }
        }
    }

    private fun toSignUp() {
        binding.tvSignUp.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun validateInput(): Boolean {
        var flag = true
        binding.apply {
            if (etEmail.text.toString().isEmpty()) {
                flag = false
                tilEmail.error = getString(R.string.email_empty)
                etEmail.requestFocus()
            } else if (etPassword.text.toString().isEmpty()) {
                flag = false
                tilPassword.error = getString(R.string.password_empty)
                etPassword.requestFocus()
            }
            if (tilEmail.isErrorEnabled) {
                flag = false
            } else if (tilPassword.isErrorEnabled) {
                flag = false
            }
        }

        return flag
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}