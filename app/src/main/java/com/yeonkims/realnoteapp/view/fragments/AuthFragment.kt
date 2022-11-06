package com.yeonkims.realnoteapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeonkims.realnoteapp.R
import com.yeonkims.realnoteapp.data.enums.AuthState
import com.yeonkims.realnoteapp.logic.viewmodels.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment() {

    @Inject
    lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.loadInitialAuthState()

        viewModel.authState.observe(viewLifecycleOwner) {

            when(it) {
                AuthState.LOGGED_IN -> {
                    val action = AuthFragmentDirections.actionAuthFragmentToNoteListFragment()
                    findNavController().navigate(action)
                }
                AuthState.AUTH_NOT_FOUND -> {
                    val action = AuthFragmentDirections.actionAuthFragmentToLoginFragment()
                    findNavController().navigate(action)
                }
            }
        }

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

}