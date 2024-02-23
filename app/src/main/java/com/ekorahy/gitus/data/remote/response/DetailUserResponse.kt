package com.ekorahy.gitus.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailUserResponse(

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("public_repos")
    val publicRepos: Int,

    @field:SerializedName("html_url")
    val htmlUrl: String,

    @field:SerializedName("followers")
    val followers: Int,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,


    @field:SerializedName("following")
    val following: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("type")
    val type: String,
)
