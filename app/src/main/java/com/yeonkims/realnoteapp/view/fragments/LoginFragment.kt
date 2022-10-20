package com.yeonkims.realnoteapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeonkims.realnoteapp.R
import com.yeonkims.realnoteapp.databinding.FragmentLoginBinding
import com.yeonkims.realnoteapp.logic.viewmodels.auth.LoginViewModel
import com.yeonkims.realnoteapp.util.dev_tools.Logger
import com.yeonkims.realnoteapp.util.extension_functions.hideKeyboard
import com.yeonkims.realnoteapp.view.dialogs.ForgotPasswordDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentLoginBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )

        binding.textForgetPassword.setOnClickListener {
            ForgotPasswordDialog(viewModel.email.value).show(parentFragmentManager, ForgotPasswordDialog.TAG)
        }

        binding.textToSignUp.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            findNavController().navigate(action)
        }

        binding.rootLayout.setOnClickListener {
            hideKeyboard()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.currentUser.observe(viewLifecycleOwner) { currentUser ->
            if(currentUser != null) {
                Log.i(javaClass.simpleName, javaClass.simpleName)
                val action = LoginFragmentDirections.actionLoginFragmentToNoteListFragment()
                findNavController().navigate(action)
            }
        }

        viewModel.email.observe(viewLifecycleOwner) { email ->
            Logger.i(email)
        }

        return binding.root
    }
}