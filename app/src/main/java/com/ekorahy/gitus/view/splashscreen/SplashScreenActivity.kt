package com.ekorahy.gitus.view.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ekorahy.gitus.R
import com.ekorahy.gitus.databinding.ActivitySplashScreenBinding
import com.ekorahy.gitus.view.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            ivLogo.alpha = 0f
            ivLogo.animate().setDuration(2500).alpha(1f).withEndAction {
                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                startActivity(intent)
                @Suppress("DEPRECATION")
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                ivLogo.setImageResource(R.drawable.logo_dark)
                llLogo.setBackgroundColor(getColor(R.color.md_theme_primary))
                finish()
            }
        }
    }
}