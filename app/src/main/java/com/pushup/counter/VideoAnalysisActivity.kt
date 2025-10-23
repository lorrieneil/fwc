package com.pushup.counter

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class VideoAnalysisActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_VIDEO_URI = "extra_video_uri"
        private const val FRAME_INTERVAL_MS = 100L
    }

    private lateinit var videoView: PlayerView
    private lateinit var tvCount: TextView
    private lateinit var tvStatus: TextView
    private lateinit var progressBar: ProgressBar
    
    private var player: ExoPlayer? = null
    private lateinit var poseDetector: PoseDetector
    private val pushUpDetector = PushUpDetector()
    private var isAnalyzing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_analysis)

        videoView = findViewById(R.id.videoView)
        tvCount = findViewById(R.id.tvCount)
        tvStatus = findViewById(R.id.tvStatus)
        progressBar = findViewById(R.id.progressBar)

        val videoUriString = intent.getStringExtra(EXTRA_VIDEO_URI)
        if (videoUriString == null) {
            finish()
            return
        }

        val videoUri = Uri.parse(videoUriString)
        
        initPoseDetector()
        setupVideoPlayer(videoUri)
        startAnalysis(videoUri)
    }

    private fun initPoseDetector() {
        val options = AccuratePoseDetectorOptions.Builder()
            .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
            .build()
        poseDetector = PoseDetection.getClient(options)
    }

    private fun setupVideoPlayer(videoUri: Uri) {
        player = ExoPlayer.Builder(this).build()
        videoView.player = player
        
        val mediaItem = MediaItem.fromUri(videoUri)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        
        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        progressBar.visibility = View.GONE
                    }
                    Player.STATE_ENDED -> {
                        tvStatus.text = "分析完成"
                    }
                }
            }
        })
    }

    private fun startAnalysis(videoUri: Uri) {
        if (isAnalyzing) return
        isAnalyzing = true
        
        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                tvStatus.text = "正在分析视频..."
                
                analyzeVideo(videoUri)
                
                player?.play()
                progressBar.visibility = View.GONE
                tvStatus.text = "播放中..."
                
            } catch (e: Exception) {
                e.printStackTrace()
                progressBar.visibility = View.GONE
                tvStatus.text = "分析出错: ${e.message}"
            } finally {
                isAnalyzing = false
            }
        }
    }

    private suspend fun analyzeVideo(videoUri: Uri) = withContext(Dispatchers.IO) {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(this@VideoAnalysisActivity, videoUri)
            
            val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val videoDuration = durationString?.toLongOrNull() ?: 0L
            
            if (videoDuration <= 0) {
                withContext(Dispatchers.Main) {
                    tvStatus.text = "无法获取视频时长"
                }
                return@withContext
            }
            
            pushUpDetector.reset()
            var currentTime = 0L
            
            while (currentTime < videoDuration) {
                val frameTimeUs = currentTime * 1000
                
                val bitmap = retriever.getFrameAtTime(
                    frameTimeUs,
                    MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                )
                
                if (bitmap != null) {
                    val inputImage = InputImage.fromBitmap(bitmap, 0)
                    
                    try {
                        val pose = com.google.android.gms.tasks.Tasks.await(
                            poseDetector.process(inputImage)
                        )
                        
                        val result = pushUpDetector.detectPushUp(pose)
                        
                        withContext(Dispatchers.Main) {
                            updateUI(result)
                        }
                        
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    
                    bitmap.recycle()
                }
                
                currentTime += FRAME_INTERVAL_MS
            }
            
        } finally {
            retriever.release()
        }
    }

    private fun updateUI(result: PushUpDetector.PushUpResult) {
        tvCount.text = result.count.toString()
        
        val statusText = when (result.state) {
            PushUpDetector.PushUpState.UP -> "向上"
            PushUpDetector.PushUpState.DOWN -> "向下"
            PushUpDetector.PushUpState.TRANSITION -> "过渡中"
            PushUpDetector.PushUpState.UP_COUNTED -> "完成一次！"
            PushUpDetector.PushUpState.POOR_FORM -> "姿势需要改进"
            PushUpDetector.PushUpState.UNKNOWN -> "检测中..."
        }
        
        tvStatus.text = "$statusText (角度: ${String.format("%.1f", result.angle)}°)"
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
        poseDetector.close()
    }
}
