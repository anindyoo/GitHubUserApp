package com.anindyo.githubuserapp.ui.userdetail.followers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.anindyo.githubuserapp.R
import com.anindyo.githubuserapp.databinding.FragmentFollowersFollowingBinding
import com.anindyo.githubuserapp.db.local.entity.User
import com.anindyo.githubuserapp.ui.ViewModelFactory
import com.anindyo.githubuserapp.ui.main.ListUserAdapter
import com.anindyo.githubuserapp.ui.userdetail.UserDetailActivity

class FollowersFragment : Fragment() {
    private var _binding: FragmentFollowersFollowingBinding? = null
    private val binding get() = _binding!!

    private lateinit var listFollowerAdapter: ListUserAdapter
    private var username : String = ""

    private val list = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowersFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)

        username = arguments?.getString(ARG_SECTION_USERNAME).toString()

        listFollowerAdapter = ListUserAdapter(list)

        with (binding) {
            rvFollow.layoutManager = LinearLayoutManager(activity)
            rvFollow.setHasFixedSize(true)
            rvFollow.adapter = listFollowerAdapter
        }

        showLoadingFollowFragment(true)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        val followersViewModel: FollowersViewModel by viewModels {
            factory
        }

        when (index) {
            1 -> {
                followersViewModel.setFollowersList(username)
                followersViewModel.getFollowersList().observe(viewLifecycleOwner) {
                    if (it != null) {
                        if (it.isEmpty()) {
                            binding.tvFollowIndicator.text = getString(R.string.no_followers, username)
                            binding.tvFollowIndicator.visibility = View.VISIBLE
                        }
                        else {
                            list.clear()
                            list.addAll(it)
                        }

                        val adapter = ListUserAdapter(list)
                        binding.rvFollow.adapter = adapter

                        adapter.setOnItemClickCallback(object :
                            ListUserAdapter.OnItemClickCallback {
                            override fun onItemClicked(data: User) {
                                val moveUserDetail = Intent(activity, UserDetailActivity::class.java)
                                moveUserDetail.putExtra(UserDetailActivity.EXTRA_USER, data.login)
                                startActivity(moveUserDetail)
                            }
                        })

                        showLoadingFollowFragment(false)
                    }
                }
            }
            2 -> {
                followersViewModel.setFollowingList(username)
                followersViewModel.getFollowingList().observe(viewLifecycleOwner) {
                    if (it != null) {
                        if (it.isEmpty()) {
                            binding.tvFollowIndicator.text = getString(R.string.no_following, username)
                            binding.tvFollowIndicator.visibility = View.VISIBLE
                        }
                        else {
                            list.clear()
                            list.addAll(it)
                        }

                        val adapter = ListUserAdapter(list)
                        binding.rvFollow.adapter = adapter

                        adapter.setOnItemClickCallback(object :
                            ListUserAdapter.OnItemClickCallback {
                            override fun onItemClicked(data: User) {
                                val moveUserDetail = Intent(activity, UserDetailActivity::class.java)
                                moveUserDetail.putExtra(UserDetailActivity.EXTRA_USER, data.login)
                                startActivity(moveUserDetail)
                            }
                        })

                        showLoadingFollowFragment(false)
                    }
                }
            }
        }
    }

    private fun showLoadingFollowFragment(isLoading: Boolean) {
        binding.progressBarFollow.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_SECTION_USERNAME = "section_username"
    }
}