package com.ekorahy.gitus.view.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekorahy.gitus.adapter.UserAdapter
import com.ekorahy.gitus.data.remote.response.ItemsItem
import com.ekorahy.gitus.databinding.ActivityFavoriteBinding
import com.ekorahy.gitus.view.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()

        binding.rvFavoriteUsers.layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteUsers.adapter = adapter

        favoriteViewModel.getFavoriteUsers().observe(this) { favoriteList ->
            if (favoriteList.isNullOrEmpty()) {
                binding.rvFavoriteUsers.visibility = View.GONE
                binding.tvEmptyFavoriteUser.visibility = View.VISIBLE
            } else {
                val items = arrayListOf<ItemsItem>()
                favoriteList.map {
                    val item = ItemsItem(
                        login = it.username.toString(),
                        avatarUrl = it.avatarUrl.toString(),
                        type = it.type.toString()
                    )
                    items.add(item)
                }
                adapter.submitList(items)
            }
        }

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}