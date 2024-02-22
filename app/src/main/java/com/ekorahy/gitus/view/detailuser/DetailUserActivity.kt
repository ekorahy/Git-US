package com.ekorahy.gitus.view.detailuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.ekorahy.gitus.R
import com.ekorahy.gitus.adapter.SectionsPagerAdapter
import com.ekorahy.gitus.data.remote.response.DetailUserResponse
import com.ekorahy.gitus.data.remote.retrofit.ApiConfig
import com.ekorahy.gitus.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayoutMediator
import jp.wasabeef.glide.transformations.BlurTransformation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(USERNAME)

        detailUser(username)

        val sectionPagerAdapter = SectionsPagerAdapter(this)
        sectionPagerAdapter.username = intent.getStringExtra(USERNAME).toString()
        val viewPager = binding.vpUser
        viewPager.adapter = sectionPagerAdapter
        val tabs = binding.tbFollow
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun detailUser(username: String?) {
        showLoading(true)
        val client = username?.let { ApiConfig.getApiService().getDetailUser(it) }
        client?.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    response.body()?.let { setUserData(it) }
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                showLoading(false)
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private fun setUserData(user: DetailUserResponse) {
        Glide.with(this)
            .load(user.avatarUrl)
            .apply(bitmapTransform(BlurTransformation(120, 3)))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.ivBg)

        Glide.with(this)
            .load(user.avatarUrl)
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.ivAvatar)
        with(binding) {
            if (user.name !== null) {
                tvFullName.text = user.name
            } else {
                tvFullName.text = getString(R.string.default_value_string)
            }
            tvUsername.text = getString(R.string.username, user.login)
            tvFollowers.text = user.followers.toString()
            tvRepos.text = user.publicRepos.toString()
            tvFollowing.text = user.following.toString()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.lpiLoading.visibility = View.VISIBLE
        } else {
            binding.lpiLoading.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "detailuseractivity"
        const val USERNAME = "username"
        private val TAB_TITLES = intArrayOf(
            R.string.label_followers,
            R.string.label_following
        )
    }
}