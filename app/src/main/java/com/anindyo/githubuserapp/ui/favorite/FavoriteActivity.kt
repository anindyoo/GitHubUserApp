package com.anindyo.githubuserapp.ui.favorite

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.anindyo.githubuserapp.R
import com.anindyo.githubuserapp.databinding.ActivityFavoriteBinding
import com.anindyo.githubuserapp.db.local.entity.User
import com.anindyo.githubuserapp.ui.ViewModelFactory
import com.anindyo.githubuserapp.ui.main.ListUserAdapter
import com.anindyo.githubuserapp.ui.preferences.PreferencesActivity
import com.anindyo.githubuserapp.ui.userdetail.UserDetailActivity


class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var listUserAdapter: ListUserAdapter

    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        showLoading(true)

        val favoriteViewModel = obtainViewModel(this@FavoriteActivity)

        title = "Favorite Users"

        listUserAdapter = ListUserAdapter(list)

        with (binding) {
            rvFavUsers.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvFavUsers.setHasFixedSize(true)
            rvFavUsers.adapter = listUserAdapter

            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvFavUsers.layoutManager = GridLayoutManager(this@FavoriteActivity, 2)
            } else {
                rvFavUsers.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            }
        }

        favoriteViewModel.getFavoriteList().observe(this) {
            if (it != null) {
                binding.tvFavListWelcome.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                list.clear()
                list.addAll(it)

                val adapter = ListUserAdapter(list)
                binding.rvFavUsers.adapter = adapter

                adapter.setOnItemClickCallback(object :
                    ListUserAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: User) {
                        val moveUserDetail = Intent(this@FavoriteActivity, UserDetailActivity::class.java)
                        moveUserDetail.putExtra(UserDetailActivity.EXTRA_USER, data.login)
                        startActivity(moveUserDetail)
                    }
                })

                showLoading(false)
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private lateinit var menuHide : MenuItem
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        if (menu != null) {
            menuHide = menu.findItem(R.id.action_to_favorite_list)
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menuHide.isVisible = false
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_to_preferences -> {
                val moveAbout = Intent(this@FavoriteActivity, PreferencesActivity::class.java)
                startActivity(moveAbout)
            }
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}