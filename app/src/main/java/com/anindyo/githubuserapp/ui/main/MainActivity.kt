package com.anindyo.githubuserapp.ui.main

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.anindyo.githubuserapp.R
import com.anindyo.githubuserapp.databinding.ActivityMainBinding
import com.anindyo.githubuserapp.db.local.entity.User
import com.anindyo.githubuserapp.ui.ViewModelFactory
import com.anindyo.githubuserapp.ui.favorite.FavoriteActivity
import com.anindyo.githubuserapp.ui.preferences.PreferencesActivity
import com.anindyo.githubuserapp.ui.preferences.PreferencesModelFactory
import com.anindyo.githubuserapp.ui.preferences.PreferencesViewModel
import com.anindyo.githubuserapp.ui.preferences.SettingPreferences
import com.anindyo.githubuserapp.ui.userdetail.UserDetailActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var listUserAdapter: ListUserAdapter

    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        val prefViewModel = ViewModelProvider(this, PreferencesModelFactory(pref))[PreferencesViewModel::class.java]
        prefViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val userMainModel = obtainViewModel(this@MainActivity)

        listUserAdapter = ListUserAdapter(list)

        with (binding) {
            rvUsers.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUsers.setHasFixedSize(true)
            rvUsers.adapter = listUserAdapter

            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvUsers.layoutManager = GridLayoutManager(this@MainActivity, 2)
            } else {
                rvUsers.layoutManager = LinearLayoutManager(this@MainActivity)
            }

            btnSearch.setOnClickListener {
                searchUser()
            }

            tfInputEdit.setOnKeyListener { _, i, keyEvent ->
                if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    searchUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }

        userMainModel.getSearchUsers().observe(this) {
            if (it != null) {
                binding.tvWelcome.visibility = View.GONE
                list.clear()
                list.addAll(it)

                val adapter = ListUserAdapter(list)
                binding.rvUsers.adapter = adapter

                adapter.setOnItemClickCallback(object :
                    ListUserAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: User) {
                        val moveUserDetail = Intent(this@MainActivity, UserDetailActivity::class.java)
                        moveUserDetail.putExtra(UserDetailActivity.EXTRA_USER, data.login)
                        startActivity(moveUserDetail)
                    }
                })

                showLoading(false)
            }
        }
    }

    private fun searchUser() {
        val userMainModel = obtainViewModel(this@MainActivity)
        val query = binding.tfInputEdit.text.toString()
        if (query.isEmpty()) return
        showLoading(true)
        userMainModel.setSearchUsers(query)
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserMainModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[UserMainModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            R.id.action_to_favorite_list -> {
                val moveAbout = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(moveAbout)
            }
            R.id.action_to_preferences -> {
                val moveAbout = Intent(this@MainActivity, PreferencesActivity::class.java)
                startActivity(moveAbout)
            }
        }
    }
}