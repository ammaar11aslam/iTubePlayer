package com.demo.itubeplayer

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.demo.itubeplayer.data.AppDatabase
import com.demo.itubeplayer.data.VideoEntity
import com.demo.itubeplayer.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "iTube Player"

        webView = binding.webView
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        // Handle URL passed from PlaylistActivity
        val passedUrl = intent.getStringExtra("url")
        if (!passedUrl.isNullOrEmpty()) {
            binding.etVideoUrl.setText(passedUrl)
        }

        binding.btnPlay.setOnClickListener {
            val url = binding.etVideoUrl.text.toString().trim()
            if (url.isNotEmpty()) {
                val videoId = url.substringAfter("v=")
                val html = getYouTubeEmbedHtml(videoId)
                webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
            } else {
                Toast.makeText(this, "Please enter a URL", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSave.setOnClickListener {
            val url = binding.etVideoUrl.text.toString().trim()
            if (url.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    AppDatabase.getDatabase(applicationContext)
                        .videoDao()
                        .insert(VideoEntity(url = url))
                }
                Toast.makeText(this, "Video saved!", Toast.LENGTH_SHORT).show()
            }
        }

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val url = result.data?.getStringExtra("url")
                binding.etVideoUrl.setText(url)
            }
        }

        binding.btnViewSaved.setOnClickListener {
            val intent = Intent(this, PlaylistActivity::class.java)
            launcher.launch(intent)
        }

        binding.btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun getYouTubeEmbedHtml(videoId: String): String {
        return """
            <html>
            <body style="margin:0;padding:0;">
                <iframe 
                    width="100%" height="100%" 
                    src="https://www.youtube.com/embed/$videoId?autoplay=1&playsinline=1"
                    frameborder="0"
                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                    allowfullscreen>
                </iframe>
            </body>
            </html>
        """.trimIndent()
    }
}
