package com.example.covernews.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.covernews.R
import com.example.covernews.data.adapter.PagerAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        supportActionBar?.title = "CoverNews"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.navigationIcon = getDrawable(R.drawable.ic_info_outline_black_24dp)

        initPagerAdapter()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                showInfoMessage()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showInfoMessage() {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("CoverNews")
            .setMessage("News and articles from all over the Internet!\nProvided by https://newsapi.org/")
            .show()
    }

    private fun initPagerAdapter() {
        val fragmentPagerAdapter =
            PagerAdapter(
                supportFragmentManager
            )
        view_pager.adapter = fragmentPagerAdapter

        tab_layout.setupWithViewPager(view_pager)
    }
}
