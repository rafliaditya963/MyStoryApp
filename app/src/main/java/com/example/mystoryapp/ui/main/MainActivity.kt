package com.example.mystoryapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.adapter.StoryAdapter
import com.example.mystoryapp.adapter.LoadingStateAdapter
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.helper.ViewModelFactory
import com.example.mystoryapp.ui.add.AddActivity
import com.example.mystoryapp.ui.login.LoginActivity
import com.example.mystoryapp.ui.maps.MapsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

        binding.rvListStory.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        storyAdapter = StoryAdapter()
        binding.rvListStory.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )

        viewModel.getUser().observe(this) { user ->
            val userToken = "Bearer ${user.token}"
            viewModel.getStory(userToken).observe(this) { storyData ->
                if (storyData != null) {
                    showLoading(false)
                    storyAdapter.submitData(lifecycle, storyData)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_logout -> {
                AlertDialog.Builder(this).apply {
                    setTitle(R.string.logout)
                    setMessage(R.string.logout_confirmation)
                    setPositiveButton(R.string.yes) { _, _ ->
                        viewModel.logout()
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    }
                    setNegativeButton(R.string.cancel) { _, _ ->
                    }
                    create()
                    show()
                }
            }

            R.id.app_bar_setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

            R.id.app_bar_maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
            }
        }

        return true
    }

    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                pbLoading.visibility = View.VISIBLE
                binding.rvListStory.visibility = View.GONE
                binding.btnAdd.isEnabled = false
            } else {
                pbLoading.visibility = View.GONE
                binding.rvListStory.visibility = View.VISIBLE
                binding.btnAdd.isEnabled = true
            }
        }
    }
}
