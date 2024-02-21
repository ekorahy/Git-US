package com.ekorahy.gitus.data.remote.retrofit

import com.ekorahy.gitus.data.remote.response.GithubResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getUserByQuery(@Query("q") q: String): Call<GithubResponse>
}