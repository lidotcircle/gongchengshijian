package com.example.daoyun

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.daoyun.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ActivityCollector.finishAll()
        ActivityCollector.addActivity(this)

        this.supportFragmentManager
            .beginTransaction()
            .add(binding.containerContent, MainFragment())
    }
    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

}