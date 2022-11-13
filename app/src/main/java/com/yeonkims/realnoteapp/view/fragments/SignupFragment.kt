package com.yeonkims.realnoteapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeonkims.realnoteapp.R
import com.yeonkims.realnoteapp.databinding.FragmentSignupBinding
import com.yeonkims.realnoteapp.logic.viewmodels.auth.SignupViewModel
import com.yeonkims.realnoteapp.util.extension_functions.hideKeyboard
import com.yeonkims.realnoteapp.util.extension_functions.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignupFragment : Fragment() {

    @Inject
    lateinit var viewModel: SignupViewModel
    lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_signup, container, false
        )

        observeIsLoggedIn()
        setTextToLoginOnClick()
        hideKeyboardWhenTouchedOutside()

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    private fun setTextToLoginOnClick() {
        binding.textToLogin.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeIsLoggedIn() {
        viewModel.currentUser.observe(viewLifecycleOwner) { currentUser ->
            if (currentUser != null) {
                val action = SignupFragmentDirections.actionSignupFragmentToNoteListFragment()
                findNavController().safeNavigate(action)
            }
        }
    }

    private fun hideKeyboardWhenTouchedOutside() {
        binding.rootLayout.setOnClickListener {
            hideKeyboard()
        }
    }
}