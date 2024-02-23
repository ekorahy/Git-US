package com.ekorahy.gitus.view.detailuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.ekorahy.gitus.R
import com.ekorahy.gitus.adapter.SectionsPagerAdapter
import com.ekorahy.gitus.data.remote.response.DetailUserResponse
import com.ekorahy.gitus.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayoutMediator
import jp.wasabeef.glide.transformations.BlurTransformation

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[DetailViewModel::class.java]

        val username = intent.getStringExtra(USERNAME)

        if (detailViewModel.username.value.isNullOrEmpty()) {
            detailViewModel.setUsername(username.toString())
            detailViewModel.findDetailUser(username.toString())
        }

        detailViewModel.detailUser.observe(this) { user ->
            setUserData(user)
        }

        val sectionPagerAdapter = SectionsPagerAdapter(this)
        sectionPagerAdapter.username = username.toString()
        val viewPager = binding.vpUser
        viewPager.adapter = sectionPagerAdapter
        val tabs = binding.tbFollow
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
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
            @Suppress("SENSELESS_COMPARISON")
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
        const val TAG = "DetailUserActivity"
        const val USERNAME = "username"
        private val TAB_TITLES = intArrayOf(
            R.string.label_followers,
            R.string.label_following
        )
    }
}