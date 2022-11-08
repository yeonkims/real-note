package com.yeonkims.realnoteapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
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

    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )
        val navController = findNavController()

        observeIsLoggedIn(navController)
        setOnClickListeners(navController)
        hideKeyboardWhenTouchedOutside()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    private fun hideKeyboardWhenTouchedOutside() {
        binding.rootLayout.setOnClickListener {
            hideKeyboard()
        }
    }

    private fun setOnClickListeners(navController: NavController) {
        binding.apply {
            textToSignUp.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
                navController.navigate(action)
            }
            textForgetPassword.setOnClickListener {
                ForgotPasswordDialog(viewModel!!.email.value).show(
                    parentFragmentManager,
                    ForgotPasswordDialog.TAG
                )
            }
        }
    }

    private fun observeIsLoggedIn(navController: NavController) {
        viewModel.currentUser.observe(viewLifecycleOwner) { currentUser ->
            if (currentUser != null) {
                Log.i(javaClass.simpleName, javaClass.simpleName)
                val action = LoginFragmentDirections.actionLoginFragmentToNoteListFragment()
                navController.navigate(action)
            }
        }
    }
}