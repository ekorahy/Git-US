package com.ekorahy.gitus.view.webview

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ekorahy.gitus.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra(HTML_URL)
        with(binding) {
            wvProfile.loadUrl(url.toString())
            wvProfile.settings.javaScriptEnabled = true
            wvProfile.webViewClient
        }
    }

    companion object {
        const val HTML_URL = "html_url"
    }
}