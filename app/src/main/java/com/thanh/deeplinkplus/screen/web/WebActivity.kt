package com.thanh.deeplinkplus.screen.web

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.thanh.deeplinkplus.R
import kotlinx.android.synthetic.main.activity_webview.*

class WebActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        var url: String = intent?.extras?.getString("URL")?:""

        if (!url.contains("http")){
            url = "https://$url"
        }
        wv?.loadUrl(url)
    }
}