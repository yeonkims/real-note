package com.yeonkims.realnoteapp.util.aliases

import com.yeonkims.realnoteapp.data.enums.AuthState

typealias AuthStateFunction = (AuthState) -> Unit
typealias BooleanFunction = (Boolean) -> Unit
typealias BooleanStringFunction = (Boolean, String?) -> Unit
