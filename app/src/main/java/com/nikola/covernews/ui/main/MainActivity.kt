package com.nikola.covernews.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.content.res.AppCompatResources
import com.nikola.covernews.R
import com.nikola.covernews.data.adapter.PagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

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
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.app_name))
        val inflater = this.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.custom_alert_dialog_layout, null)
        val text = layout.findViewById<TextView>(R.id.alertTextView)
        val s = SpannableString(getString(R.string.disclosure_msg))
        Linkify.addLinks(s, Linkify.ALL)
        text.text = s
        text.movementMethod = LinkMovementMethod.getInstance()
        builder.setView(layout)
        builder.show()
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
