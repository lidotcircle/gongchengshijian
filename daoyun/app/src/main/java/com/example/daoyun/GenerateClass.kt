package com.example.daoyun

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.daoyun.databinding.ActivityGenerateClassBinding

class GenerateClass : AppCompatActivity() {
    private lateinit var binding: ActivityGenerateClassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGenerateClassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userData=getSharedPreferences("userData", Context.MODE_PRIVATE)
        val classname=userData.getString("courseExId","error")
        binding.generateClassNumEt.text=classname

        binding.generateClassBt.setOnClickListener{
            finish()
        }
    }
}