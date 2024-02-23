package com.ekorahy.gitus.view.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekorahy.gitus.R
import com.ekorahy.gitus.adapter.UserAdapter
import com.ekorahy.gitus.data.remote.response.ItemsItem
import com.ekorahy.gitus.databinding.FragmentFollowBinding
import com.google.android.material.snackbar.Snackbar

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private val followViewModel by viewModels<FollowViewModel>()
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

        if (followViewModel.usernameFollowers.value.isNullOrEmpty() && position == 1) {
            followViewModel.setUsernameFollowers(username.toString())
            followViewModel.getFollow(username.toString(), true)
        } else if (followViewModel.usernameFollowing.value.isNullOrEmpty() && position == 2) {
            followViewModel.setUsernameFollowing(username.toString())
            followViewModel.getFollow(username.toString(), false)
        }

        followViewModel.warningText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { warningText ->
                Snackbar.make(view.rootView, warningText, Snackbar.LENGTH_SHORT).show()
            }
        }

        if (position == 1) {
            followViewModel.listFollowersUser.observe(viewLifecycleOwner) { user ->
                if (user.isNullOrEmpty()) {
                    with(binding) {
                        rvUsers.visibility = View.GONE
                        tvZeroFollow.visibility = View.VISIBLE
                        tvZeroFollow.text = getString(R.string.zero_followers)
                    }
                } else {
                    setUserData(user)
                }
            }
        } else {
            followViewModel.listFollowingUser.observe(viewLifecycleOwner) { user ->
                if (user.isNullOrEmpty()) {
                    with(binding) {
                        rvUsers.visibility = View.GONE
                        tvZeroFollow.visibility = View.VISIBLE
                        tvZeroFollow.text = getString(R.string.zero_following)
                    }
                } else {
                    setUserData(user)
                }
            }
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUsers.layoutManager = layoutManager

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
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
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}