package com.example.mystoryapp.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapp.R
import com.example.mystoryapp.ui.login.LoginActivity

class splashScreen : AppCompatActivity() {
    private val SPLASH_SCREEN_DELAY = 2000L //L = long
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@splashScreen, LoginActivity::class.java))
            finish()
        }, SPLASH_SCREEN_DELAY)

        val iconIMG = findViewById<ImageView>(R.id.icon_Github)
        val scaleAnimation = AnimationUtils.loadAnimation(this@splashScreen, R.anim.scale_animation)
        iconIMG.startAnimation(scaleAnimation)

    }
}