package com.ekorahy.gitus.view.follow

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekorahy.gitus.adapter.UserAdapter
import com.ekorahy.gitus.data.remote.response.ItemsItem
import com.ekorahy.gitus.data.remote.retrofit.ApiConfig
import com.ekorahy.gitus.databinding.FragmentFollowBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = arguments?.getInt(ARG_POSITION, 0)
        val username = arguments?.getString(ARG_USERNAME)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUsers.layoutManager = layoutManager

        if (position == 1) {
            getFollow(username.toString(), true)
        } else {
            getFollow(username.toString(), false)
        }
    }

    private fun getFollow(username: String, isFollowers: Boolean) {
        showLoading(true)
        val client = if (isFollowers) ApiConfig.getApiService()
            .getFollowers(username) else ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    response.body()?.let { setUserData(it) }
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                showLoading(false)
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private fun setUserData(user: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(user)
        binding.rvUsers.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.lpiLoading.visibility = View.VISIBLE
        } else {
            binding.lpiLoading.visibility = View.GONE
        }
    }

    companion object {
        const val TAG = "fragmentfollow"
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

}