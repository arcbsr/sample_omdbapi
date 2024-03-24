package com.rafiur.assesmentproject.domain.models

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("user_id") val nomineeId: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("password") val password: String
)
