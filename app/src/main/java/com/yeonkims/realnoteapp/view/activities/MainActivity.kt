package com.yeonkims.realnoteapp.view.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.yeonkims.realnoteapp.R
import com.yeonkims.realnoteapp.logic.viewmodels.AlertViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var alertViewModel: AlertViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alertViewModel.snackbarMessage.observe(this) { message ->
            Snackbar.make(findViewById(R.id.nav_host), message, Snackbar.LENGTH_LONG).show();
        }
    }

    private var backPressedTime: Long = 0

    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        Log.i(javaClass.simpleName, "${navController.previousBackStackEntry}")
        if(navController.previousBackStackEntry == null) {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                finish()
            } else {
                Toast.makeText(this, "Press back again to leave the app.", Toast.LENGTH_LONG).show()
            }
            backPressedTime = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }
    }

}