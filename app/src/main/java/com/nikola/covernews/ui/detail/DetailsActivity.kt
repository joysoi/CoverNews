package com.nikola.covernews.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.*
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.nikola.covernews.R
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.item_layout.view.*
import java.lang.Exception

class DetailsActivity : AppCompatActivity() {

    private var title: String? = ""
    private var url: String? = ""
    private var source: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbarDetails)
        title = intent.getStringExtra("TitleString")
        url = intent.getStringExtra("UrlString")
        title?.let {
            supportActionBar?.title = it
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        url?.let {
            initWebUrl(it)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.view_web -> openWithWebBrowser()
            R.id.share -> shareURL()
        }

        return super.onOptionsItemSelected(item!!)
    }

    private fun openWithWebBrowser(): Boolean {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
        return true
    }

    private fun shareURL(): Boolean {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            source?.let {
                intent.putExtra(Intent.EXTRA_SUBJECT, source)
            }
            val body = "$title \n $url \n Share from app \n"
            intent.putExtra(Intent.EXTRA_TEXT, body)
            startActivity(Intent.createChooser(intent, "Share with: "))
        } catch (e: Exception) {
            Toast.makeText(this, "Cannot be shared!", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebUrl(url: String) {

        val webView: WebView = findViewById(R.id.webView)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(url)
                return true
            }
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBarDetails.visibility = View.GONE

            }
        }

        webView.loadUrl(url)
        webView.settings.javaScriptEnabled = true
        webView.settings.allowContentAccess = true
        webView.settings.domStorageEnabled = true
        webView.settings.useWideViewPort = true
        webView.settings.setAppCacheEnabled(true)

    }

}
