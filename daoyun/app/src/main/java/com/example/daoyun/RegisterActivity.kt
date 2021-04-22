package com.example.daoyun

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.daoyun.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ActivityCollector.addActivity(this)

        //验证码
        binding.btVeriSubmit.setOnClickListener {
            val randoms = (100000..999999).random()
            val builder = AlertDialog.Builder(this)
                .setTitle("验证码")
                .setMessage("验证码为：$randoms")
                .setPositiveButton("确定", null)
                .show()
            binding.btVeriSubmit.text = "已发送"
            binding.btVeriSubmit.isEnabled=true
        }
    }
}