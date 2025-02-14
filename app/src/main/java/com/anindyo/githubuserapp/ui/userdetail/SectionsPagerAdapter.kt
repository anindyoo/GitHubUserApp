package com.anindyo.githubuserapp.ui.userdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.anindyo.githubuserapp.ui.userdetail.followers.FollowersFragment

class SectionsPagerAdapter(activity: AppCompatActivity, private val username : String) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowersFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowersFragment.ARG_SECTION_NUMBER, position + 1)
            putString(FollowersFragment.ARG_SECTION_USERNAME, username)
        }
        return fragment
    }
}