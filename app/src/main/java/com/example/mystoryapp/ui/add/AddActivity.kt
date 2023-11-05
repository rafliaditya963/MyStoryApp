package com.example.mystoryapp.ui.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import com.example.mystoryapp.R
import com.example.mystoryapp.data.api.ApiConfig
import com.example.mystoryapp.databinding.ActivityAddBinding
import com.example.mystoryapp.helper.ViewModelFactory
import com.example.mystoryapp.model.FileUploadResponse
import com.example.mystoryapp.ui.camera.CameraActivity
import com.example.mystoryapp.ui.camera.CameraActivity.Companion.CAMERAX_RESULT

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private var currentImageUri: Uri? = null
    private val viewModel by viewModels<AddViewModel> {
        ViewModelFactory.getInstance(this)
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        viewModel.getUser().observe(this) {
            viewModel.token = it.token
        }

        viewModel.loading.observe(this) {
            showLoading(it)
        }

        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener {
            val description = binding.etDescription.text.toString()
            val image = currentImageUri

            if (image != null && description.isNotBlank()) {
                viewModel.uploadImage(image, description, this)
            } else {
                // Handle case when image or description is missing
                Toast.makeText(this, "Image or description is missing", Toast.LENGTH_SHORT).show()
            }
        }

        val upArrow = ResourcesCompat.getDrawable(resources, R.drawable.baseline_arrow_back_ios_24, theme) // Ganti dengan nama file gambar Anda
        upArrow?.setTint(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(upArrow)

        viewModel.getMessageLiveData().observe(this) { (success, message) ->
            val title = if (success) R.string.camera_success_title else R.string.camera_failed_title
            AlertDialog.Builder(this).apply {
                setTitle(title)
                setMessage(message)
                setPositiveButton(R.string.ok) { _, _ ->
                    if (success) {
                        finish()
                    }
                }
                create()
                show()
            }
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
        startActivity(intent)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivStory.setImageURI(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                pbLoading.visibility = View.VISIBLE
            } else {
                pbLoading.visibility = View.INVISIBLE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}

