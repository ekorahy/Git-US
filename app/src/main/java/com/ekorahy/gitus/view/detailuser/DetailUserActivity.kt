package com.ekorahy.gitus.view.detailuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.ekorahy.gitus.R
import com.ekorahy.gitus.adapter.SectionsPagerAdapter
import com.ekorahy.gitus.data.local.entity.FavoriteUser
import com.ekorahy.gitus.data.remote.response.DetailUserResponse
import com.ekorahy.gitus.databinding.ActivityDetailUserBinding
import com.ekorahy.gitus.view.ViewModelFactory
import com.ekorahy.gitus.view.webview.ProfileActivity
import com.ekorahy.gitus.view.webview.ProfileActivity.Companion.HTML_URL
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import jp.wasabeef.glide.transformations.BlurTransformation

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(application)
    }

    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(USERNAME)

        if (detailViewModel.username.value.isNullOrEmpty()) {
            detailViewModel.setUsername(username.toString())
            detailViewModel.findDetailUser(username.toString())
        }

        detailViewModel.getFavoriteUserByUsername(username.toString()).observe(this) {
            isFavorite = if (it != null) {
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_fill)
                true
            } else {
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
                false
            }
        }

        detailViewModel.detailUser.observe(this) { user ->
            setUserData(user)
        }

        detailViewModel.warningText.observe(this) {
            it.getContentIfNotHandled()?.let { warningText ->
                Snackbar.make(window.decorView.rootView, warningText, Snackbar.LENGTH_SHORT).show()
            }
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
            tvLabelDetail.text = getString(R.string.label_detail_user, user.type)
            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
            btnProfileWebview.setOnClickListener {
                val intent = Intent(this@DetailUserActivity, ProfileActivity::class.java)
                intent.putExtra(HTML_URL, user.htmlUrl)
                startActivity(intent)
            }
            btnShare.setOnClickListener {
                val shareUrl = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    @Suppress("UselessCallOnNotNull")
                    putExtra(
                        Intent.EXTRA_TEXT,
                        getString(
                            R.string.user_content,
                            user.login,
                            if (user.name.isNullOrEmpty()) {
                                getString(R.string.default_value_string)
                            } else {
                                user.name
                            },
                            user.followers.toString(),
                            user.publicRepos.toString(),
                            user.following.toString()
                        )
                    )
                }
                startActivity(Intent.createChooser(shareUrl, getString(R.string.share_via)))
            }
            fabFavorite.setOnClickListener {
                val favoriteUser = FavoriteUser(
                    username = user.login,
                    avatarUrl = user.avatarUrl,
                    type = user.type
                )
                if (isFavorite) {
                    detailViewModel.deleteByUsername(user.login)
                } else {
                    detailViewModel.insert(favoriteUser)
                }
            }
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
        const val USERNAME = "username"
        private val TAB_TITLES = intArrayOf(
            R.string.label_followers,
            R.string.label_following
        )
    }
}