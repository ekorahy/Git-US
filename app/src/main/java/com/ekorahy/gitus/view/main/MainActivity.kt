package com.ekorahy.gitus.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekorahy.gitus.R
import com.ekorahy.gitus.adapter.UserAdapter
import com.ekorahy.gitus.data.remote.response.ItemsItem
import com.ekorahy.gitus.databinding.ActivityMainBinding
import com.ekorahy.gitus.view.favorite.FavoriteActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager

        mainViewModel.listUser.observe(this) { user ->
            setUserData(user)
        }

        mainViewModel.warningText.observe(this) {
            it.getContentIfNotHandled()?.let { warningText ->
                Snackbar.make(window.decorView.rootView, warningText, Snackbar.LENGTH_SHORT).show()
            }
        }

        with(binding) {
            svUser.setupWithSearchBar(sbUser)
            svUser
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    if (svUser.text.toString().isEmpty()) {
                        svUser.editText.error = getString(R.string.empty_input_warning)
                    } else {
                        svUser.hide()
                        mainViewModel.findUsers(svUser.text.toString())
                    }
                    sbUser.setText(svUser.text)
                    true
                }
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.totalCount.observe(this) {
            showTextNotFound(it)
        }

        binding.btnFavorite.setOnClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUserData(user: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(user)
        binding.rvUsers.adapter = adapter
    }

    private fun showTextNotFound(totalCount: Int) {
        if (totalCount == 0) {
            with(binding) {
                rvUsers.visibility = View.GONE
                tvNotfound.visibility = View.VISIBLE
            }
        } else {
            with(binding) {
                rvUsers.visibility = View.VISIBLE
                tvNotfound.visibility = View.GONE
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbLoading.visibility = View.VISIBLE
        } else {
            binding.pbLoading.visibility = View.GONE
        }
    }

    companion object {
        const val Q = "a"
    }
}