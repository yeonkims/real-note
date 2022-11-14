package com.yeonkims.realnoteapp.view.fragments

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.yeonkims.realnoteapp.R
import com.yeonkims.realnoteapp.databinding.FragmentSettingsBinding
import com.yeonkims.realnoteapp.logic.viewmodels.auth.AuthViewModel
import com.yeonkims.realnoteapp.util.extension_functions.safeNavigate
import com.yeonkims.realnoteapp.view.dialogs.DeleteNoteDialog
import com.yeonkims.realnoteapp.view.dialogs.LogoutDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    @Inject
    lateinit var authViewModel: AuthViewModel

    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_settings, container, false
        )
        val navController = findNavController()

        observeIsLoggedIn(navController)
        setToolbar()
        setBackButtonOnClick(navController)

        binding.lifecycleOwner = this

        return binding.root
    }

    private fun observeIsLoggedIn(navController: NavController) {
        authViewModel.currentUser.observe(viewLifecycleOwner) { currentUser ->
            if (currentUser == null) {
                val action = SettingsFragmentDirections.actionSettingsFragmentToLoginFragment()
                navController.safeNavigate(action)
            }
        }
    }

    private fun setToolbar() {
        val activity = requireActivity()
        activity.setActionBar(binding.toolbar)
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
    }

    private fun setBackButtonOnClick(navController: NavController) {
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.logoutBtn.setOnClickListener {
            val fragmentManger = parentFragmentManager
            LogoutDialog().show(
                fragmentManger, LogoutDialog.TAG
            )
        }

        binding.deleteAccountBtn.setOnClickListener {
            authViewModel.deleteAccount()
        }
    }

}