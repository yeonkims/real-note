package com.yeonkims.realnoteapp.view.bindingAdapters

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("showPageNavigation")
fun isVisible(view: View, showPageNavigation: Boolean) {
     view.visibility = if(showPageNavigation) View.VISIBLE else View.GONE
}