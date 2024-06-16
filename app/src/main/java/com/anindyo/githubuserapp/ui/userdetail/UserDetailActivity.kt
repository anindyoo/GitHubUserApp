package com.anindyo.githubuserapp.ui.userdetail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.anindyo.githubuserapp.R
import com.anindyo.githubuserapp.databinding.ActivityUserDetailBinding
import com.anindyo.githubuserapp.db.local.entity.User
import com.anindyo.githubuserapp.ui.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding

    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val detailUserViewModel: DetailUserViewModel by viewModels {
        factory
    }

    private var nameShare : String = ""
    private var unameShare : String = ""
    private var urlShare : String = ""
    private var isFav : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val detailUser = intent.getStringExtra(EXTRA_USER) as String

        showLoadingDetail(true)

        detailUserViewModel.setUserDetail(detailUser)
        detailUserViewModel.detailUser.observe(this) {
            if (it != null) {
                with(binding) {
                    lifecycleScope.launch {
                        isFav = detailUserViewModel.isFavorite(it.id)

                        fabAddFavorite.setImageDrawable(
                            ContextCompat.getDrawable(
                                fabAddFavorite.context,
                                if (isFav) R.drawable.ic_baseline_star_24 else R.drawable.ic_baseline_star_border_24
                            )
                        )
                    }
                    val loginParam = it.login
                    val favoriteUser = User(
                        it.id,
                        it.login,
                        it.avatarUrl,
                        it.htmlUrl,
                        true
                    )

                    title = it.login
                    tvDetailName.text = if (!it.name.isNullOrBlank()) it.name else it.login
                    tvDetailBio.text = if (!it.bio.isNullOrBlank()) it.bio else "${it.login} doesn\'t have bio."
                    tvDetailTwitter.text = if (!it.twitterUsername.isNullOrBlank()) "@${it.twitterUsername}" else "Unavailable"
                    tvDetailBlog.text = if (!it.blog.isNullOrBlank()) it.blog else "Unavailable"
                    tvDetailLocation.text = if (!it.location.isNullOrBlank()) it.location else "Unavailable"
                    tvDetailRepository.text = it.publicRepos.toString()
                    tvDetailFollowers.text = it.followers.toString()
                    tvDetailFollowing.text = it.following.toString()

                    nameShare = tvDetailName.text.toString()
                    unameShare = it.login
                    urlShare = it.htmlUrl

                    Glide.with(this@UserDetailActivity)
                        .load(it.avatarUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(imgDetailAvatar)

                    fabAddFavorite.apply {
                        setOnClickListener {
                            lifecycleScope.launch {
                                if (isFav) {
                                    isFav = false
                                    detailUserViewModel.deleteUserFavorite(favoriteUser)

                                    fabAddFavorite.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            fabAddFavorite.context,
                                            R.drawable.ic_baseline_star_border_24
                                        )
                                    )
                                    Toast.makeText(this@UserDetailActivity, "$loginParam has been deleted to Favorite.", Toast.LENGTH_SHORT).show()
                                }
                                else {
                                    isFav = true
                                    detailUserViewModel.insertUserFavorite(favoriteUser)

                                    fabAddFavorite.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            fabAddFavorite.context,
                                            R.drawable.ic_baseline_star_24
                                        )
                                    )
                                    Toast.makeText(this@UserDetailActivity, "$loginParam has been saved to Favorite.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
                // tab layout + viewpager2
                val sectionsPagerAdapter = SectionsPagerAdapter(this, it.login)

                val viewPager : ViewPager2 = binding.viewPager
                viewPager.adapter = sectionsPagerAdapter

                val tabs : TabLayout = binding.tabs
                TabLayoutMediator(tabs, viewPager) { tab, position ->
                    tab.text = resources.getString(TAB_TITLES[position])
                }.attach()
                showLoadingDetail(false)
            }
        }

    }

    private fun showLoadingDetail(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_share_user -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Discover more about $nameShare ($unameShare) on GitHubUser App! $urlShare"
                    )
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )

        const val EXTRA_USER = "extra_user"
    }
}