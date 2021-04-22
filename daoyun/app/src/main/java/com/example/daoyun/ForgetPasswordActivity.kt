package com.example.daoyun

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.daoyun.databinding.ActivityForgetPasswordBinding
import java.util.regex.Pattern

class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding
    private var randoms:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ActivityCollector.addActivity(this)
        binding.btVeriSubmit.setOnClickListener {
            randoms = (100000..999999).random()
            val builder = AlertDialog.Builder(this)
                .setTitle("验证码")
                .setMessage("验证码为：$randoms")
                .setPositiveButton("确定", null)
                .show()
            binding.btVeriSubmit.text = "已发送"
            binding.btVeriSubmit.isEnabled=false
        }
        binding.btLoginSubmit.setOnClickListener {
            if (binding.etRegVericode.text.toString() != "$randoms") {
                val builder = AlertDialog.Builder(this)
                    .setMessage("验证码错误！")
                    .setPositiveButton("确定", null)
                    .show()
                binding.btVeriSubmit.text = "发送验证码"
                binding.btVeriSubmit.isEnabled = true
            } else if (binding.etLoginPwd.text.toString() != binding.etRegConfPwd.text.toString()) {
                val builder = AlertDialog.Builder(this)
                    .setMessage("两次密码输入不一致！")
                    .setPositiveButton("确定", null)
                    .show()
            } else if (binding.etLoginPwd.text.toString().length < 6) {
                val builder = AlertDialog.Builder(this)
                    .setMessage("密码最低为6位！")
                    .setPositiveButton("确定", null)
                    .show()
            }
            else if(!isChinaPhoneLegal(binding.etLoginUsername.text.toString())){
                val builder = AlertDialog.Builder(this)
                    .setMessage("请输入正确的手机号码！")
                    .setPositiveButton("确定", null)
                    .show()
            }
            else{
                showAlertDialog("注册成功！")
            }
        }
    }
    private fun isChinaPhoneLegal(str: String): Boolean {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        val regExp = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])" +"|(18[0-9])|(19[8,9]))\\d{8}$"
        val p = Pattern.compile(regExp)
        val m = p.matcher(str)
        return m.matches()
    }

    private fun showAlertDialog(msg: String){

        val builder = AlertDialog.Builder(this)
            .setMessage(msg)
        if (msg == "注册成功！") {
            builder.setPositiveButton("确定",
                DialogInterface.OnClickListener { _, _ ->
                    startActivity(
                        Intent(
                            this,
                            LoginActivity::class.java
                        )
                    )
                })
        }
        else {
            builder.setPositiveButton("确定", null)
        }
        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }
}