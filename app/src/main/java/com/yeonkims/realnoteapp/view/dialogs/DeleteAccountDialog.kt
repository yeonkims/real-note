package com.yeonkims.realnoteapp.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.yeonkims.realnoteapp.R
import com.yeonkims.realnoteapp.databinding.FragmentDeleteAccountDialogBinding
import com.yeonkims.realnoteapp.logic.viewmodels.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAccountDialog(private val authViewModel: AuthViewModel) : DialogFragment() {
    private lateinit var binding: FragmentDeleteAccountDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.fragment_delete_account_dialog, null, false);

        setOnClickListeners()

        binding.lifecycleOwner = this

        return binding.root
    }

    private fun setOnClickListeners() {
        binding.apply {
            okBtn.setOnClickListener {
                authViewModel.deleteAccount()
                dismiss()
            }
            cancelBtn.setOnClickListener {
                dismiss()
            }
        }
    }

    companion object {
        const val TAG = "DeleteAccountDialog"
    }

}