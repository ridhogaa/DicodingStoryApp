package com.ergea.dicodingstoryapp.data.remote.model.auth

import com.google.gson.annotations.SerializedName

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

data class RegisterResponse(
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
