package com.demo.itubeplayer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.itubeplayer.adapters.PlaylistAdapter
import com.demo.itubeplayer.data.AppDatabase
import com.demo.itubeplayer.data.VideoEntity
import com.demo.itubeplayer.databinding.ActivityPlaylistBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaylistBinding
    private lateinit var adapter: PlaylistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "My Playlist"

        adapter = PlaylistAdapter(
            onClick = { video ->
                val resultIntent = Intent().apply {
                    putExtra("url", video.url)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            },
            onLongClick = { video ->
                showDeleteConfirmation(video)
            }
        )

        binding.recyclerPlaylist.layoutManager = LinearLayoutManager(this)
        binding.recyclerPlaylist.adapter = adapter

        binding.btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        loadPlaylist()
    }

    private fun showDeleteConfirmation(video: VideoEntity) {
        AlertDialog.Builder(this)
            .setTitle("Delete Video")
            .setMessage("Are you sure you want to delete this video?")
            .setPositiveButton("Yes") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    AppDatabase.getDatabase(applicationContext).videoDao().delete(video)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@PlaylistActivity, "Deleted", Toast.LENGTH_SHORT).show()
                        loadPlaylist()
                    }
                }
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun loadPlaylist() {
        CoroutineScope(Dispatchers.IO).launch {
            val videos = AppDatabase.getDatabase(applicationContext).videoDao().getAllVideos()
            withContext(Dispatchers.Main) {
                adapter.submitList(videos)
            }
        }
    }
}
