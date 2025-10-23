package com.pushup.counter

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var btnSelectVideo: MaterialButton
    private lateinit var btnStartCamera: MaterialButton

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            Toast.makeText(this, "权限已授予", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "需要所有权限才能使用应用", Toast.LENGTH_LONG).show()
        }
    }

    private val videoPickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                startVideoAnalysis(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSelectVideo = findViewById(R.id.btnSelectVideo)
        btnStartCamera = findViewById(R.id.btnStartCamera)

        btnSelectVideo.setOnClickListener {
            if (checkPermissions()) {
                selectVideo()
            } else {
                requestPermissions()
            }
        }

        btnStartCamera.setOnClickListener {
            if (checkPermissions()) {
                startCamera()
            } else {
                requestPermissions()
            }
        }

        if (!checkPermissions()) {
            showPermissionRationale()
        }
    }

    private fun checkPermissions(): Boolean {
        val permissions = mutableListOf(
            Manifest.permission.CAMERA
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_VIDEO)
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.CAMERA
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_VIDEO)
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        requestPermissionLauncher.launch(permissions.toTypedArray())
    }

    private fun showPermissionRationale() {
        MaterialAlertDialogBuilder(this)
            .setTitle("需要权限")
            .setMessage("此应用需要相机和存储权限来分析俯卧撑视频。")
            .setPositiveButton("授予权限") { _, _ ->
                requestPermissions()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun selectVideo() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type = "video/*"
        videoPickerLauncher.launch(intent)
    }

    private fun startCamera() {
        Toast.makeText(this, "摄像功能即将推出", Toast.LENGTH_SHORT).show()
    }

    private fun startVideoAnalysis(videoUri: Uri) {
        val intent = Intent(this, VideoAnalysisActivity::class.java)
        intent.putExtra(VideoAnalysisActivity.EXTRA_VIDEO_URI, videoUri.toString())
        startActivity(intent)
    }
}
