package com.example.daoyun

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.daoyun.databinding.ActivityFastLoginActivityBinding
import com.example.daoyun.databinding.ActivityRegisterBinding
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception
import java.util.regex.Pattern
import kotlin.concurrent.thread

class FastLoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFastLoginActivityBinding
    private var codeToken:String ?= "no receive"
    private var testt:String?="no"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFastLoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ActivityCollector.addActivity(this)

        //验证码
        binding.btVeriSubmit.setOnClickListener {
            if(!isChinaPhoneLegal(binding.etLoginUsername.text.toString())){
                AlertDialog.Builder(this)
                        .setMessage("请输入正确的手机号码！")
                        .setPositiveButton("确定", null)
                        .show()
            }
            else{
                sendRequestWithOkHttp()
                AlertDialog.Builder(this)
                        .setTitle("验证码")
                        .setMessage("验证码为：$codeToken$testt")
                        .setPositiveButton("确定", null)
                        .show()
                binding.btVeriSubmit.text = "已发送"
                binding.btVeriSubmit.isEnabled=false
            }
        }
        //FastLogin
        binding.btLoginSubmit.setOnClickListener {
            if (binding.etRegVericode.text.toString() != "$codeToken") {
                AlertDialog.Builder(this)
                    .setMessage("验证码错误！")
                    .setPositiveButton("确定", null)
                    .show()
                binding.btVeriSubmit.text = "发送验证码"
                binding.btVeriSubmit.isEnabled = true
            }
            else if(!isChinaPhoneLegal(binding.etLoginUsername.text.toString())){
                AlertDialog.Builder(this)
                    .setMessage("请输入正确的手机号码！")
                    .setPositiveButton("确定", null)
                    .show()
            }
            else{
                showAlertDialog("登录成功！")
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
        if (msg == "登录成功！") {
            builder.setPositiveButton("确定",
                DialogInterface.OnClickListener { _, _ ->
                    startActivity(
                        Intent(
                            this,
                            MainActivity::class.java
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

    private fun sendRequestWithOkHttp(){
        thread {
            try {
                val client=OkHttpClient()
                val requestBody=FormBody.Builder()
                        .add("phone",binding.etLoginUsername.text.toString())
                        .add("type","login")
                        .add("captcha","")
                        .build()
                val request=Request.Builder()
                        .url("https://gcsj.lidotcircle.ltd/apis/message")
                        .post(requestBody)
                        .build()
                val response=client.newCall(request).execute()
                codeToken= response.body?.string()
                testt="yes"
            }catch (e:Exception){
                Log.e("TAG", Log.getStackTraceString(e));
            }
        }
    }
}