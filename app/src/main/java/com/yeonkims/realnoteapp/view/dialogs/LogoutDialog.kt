package com.yeonkims.realnoteapp.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.yeonkims.realnoteapp.R
import com.yeonkims.realnoteapp.databinding.FragmentLogoutDialogBinding
import com.yeonkims.realnoteapp.logic.viewmodels.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LogoutDialog : DialogFragment() {
    private lateinit var binding: FragmentLogoutDialogBinding

    @Inject
    lateinit var authViewModel : AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.fragment_logout_dialog, null, false)

        setOnClickListeners()

        binding.authViewModel = authViewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    private fun setOnClickListeners() {
        binding.apply {
            yesBtn.setOnClickListener {
                authViewModel!!.logout()
                dismiss()
            }
            noBtn.setOnClickListener {
                dismiss()
            }
        }
    }

    companion object {
        const val TAG = "LogoutDialog"
    }

}