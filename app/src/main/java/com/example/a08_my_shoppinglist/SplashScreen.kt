package com.example.a08_my_shoppinglist

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import com.example.a08_my_shoppinglist.databinding.LogoLayoutBinding

import java.lang.Exception

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: LogoLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LogoLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val background = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(500)
                    val intent = Intent(baseContext, ScrollingActivity::class.java)
                    //val p1 = androidx.core.util.Pair.create<View, String>(ivLogo, "logoTrans")
                    //val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@SplashScreen, p1)

                    startActivity(intent)//,options.toBundle())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}