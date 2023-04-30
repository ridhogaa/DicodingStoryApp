package com.ergea.dicodingstoryapp.data.remote.model.error

import com.google.gson.annotations.SerializedName

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

data class ResponseError(
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
