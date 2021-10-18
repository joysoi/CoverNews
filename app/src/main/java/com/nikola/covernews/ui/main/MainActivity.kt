package com.nikola.covernews.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.content.res.AppCompatResources
import androidx.viewpager.widget.ViewPager
import com.nikola.covernews.R
import com.nikola.covernews.data.adapter.PagerAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        supportActionBar?.title = getString(R.string.app_name)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.navigationIcon =
            AppCompatResources.getDrawable(this, R.drawable.ic_info_outline_black_24dp)

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
        builder.setTitle(getString(R.string.app_name))
            .setMessage(getString(R.string.disclosure_msg))
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
