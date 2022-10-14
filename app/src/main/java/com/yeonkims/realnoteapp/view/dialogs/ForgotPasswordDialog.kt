package com.yeonkims.realnoteapp.view.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.yeonkims.realnoteapp.R
import com.yeonkims.realnoteapp.databinding.FragmentForgotPasswordDialogBinding
import com.yeonkims.realnoteapp.logic.viewmodels.auth.ForgotPasswordDialogViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForgotPasswordDialog(private val email: String?) : DialogFragment() {
    private lateinit var binding: FragmentForgotPasswordDialogBinding

    @Inject
    lateinit var viewModel : ForgotPasswordDialogViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.fragment_forgot_password_dialog, null, false);

        if(email != null) {
            viewModel.email.value = email
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.sendResetLinkBtn.setOnClickListener {
            viewModel.sendResetLink()
            dismiss()
        }

        binding.cancelBtn.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    companion object {
        const val TAG = "ForgotPasswordDialog"
    }

}