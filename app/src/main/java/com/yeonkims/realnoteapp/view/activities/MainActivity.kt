package com.yeonkims.realnoteapp.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.yeonkims.realnoteapp.R
import com.yeonkims.realnoteapp.logic.viewmodels.ErrorViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var errorViewModel: ErrorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        errorViewModel.snackbarMessage.observe(this) { message ->
            Snackbar.make(findViewById(R.id.nav_host), message, Snackbar.LENGTH_LONG).show();
        }
    }

}