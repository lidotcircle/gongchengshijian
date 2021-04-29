package com.example.daoyun

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.daoyun.databinding.ActivityFastLoginActivityBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.regex.Pattern
import kotlin.concurrent.thread


class FastLoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityFastLoginActivityBinding
    private lateinit var messageCode:String
    private var returnMessage:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFastLoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ActivityCollector.addActivity(this)
        val button1=MyCountDownTimer(60000,1000,binding.btVeriSubmit)
        //验证码
        binding.btVeriSubmit.setOnClickListener {
            if(!isChinaPhoneLegal(binding.etLoginUsername.text.toString())){
                Toast.makeText(this, "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
            }
            else{
                //sendMsg()
                button1.start()
                Toast.makeText(this, "验证码已发送", Toast.LENGTH_SHORT).show();
                binding.btVeriSubmit.text = "已发送"
                binding.btVeriSubmit.isEnabled=false
            }
        }
        //FastLogin
        binding.btLoginSubmit.setOnClickListener {
            if(!isChinaPhoneLegal(binding.etLoginUsername.text.toString())){
                Toast.makeText(this, "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
            }
            else if(binding.etRegVericode.text.toString()==""){
                Toast.makeText(this,"请输入验证码！", Toast.LENGTH_SHORT).show();
            }
            else if(binding.etRegVericode.text.toString().length<6){
                Toast.makeText(this,"请输入正确的验证码！", Toast.LENGTH_SHORT).show();
            }
            else{
                quickLogin(
                    binding.etLoginUsername.text.toString(),
                    binding.etRegVericode.text.toString(),
                    messageCode
                )
//                if(returnMessage==""){
//                    Toast.makeText(this, "登陆成功！", Toast.LENGTH_SHORT).show();
//                    //startActivity(Intent(this, MainActivity::class.java))
//                }
//                else{
//                    Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
//                }


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

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

    private fun sendMsg(){
        thread {
            try {
                val json = JSONObject()
                    .put("phone", binding.etLoginUsername.text.toString())
                    .put("type", "login")
                    .put("captcha", " ")
                val stringBody =json.toString().toRequestBody("application/json;charset=utf-8".toMediaType())
                val client=OkHttpClient()
                val request=Request.Builder()
                        .url("https://gcsj.lidotcircle.ltd/apis/message")
                        .post(stringBody)
                        .build()
                val response=client.newCall(request).execute()
                val responseData=response.body?.string()
                messageCode=JSONObject(responseData).getString("codeToken")
                showResponse(responseData.toString())
            }catch (e: Exception){
                Log.e("TAG", Log.getStackTraceString(e));
            }
        }
    }

    private fun quickLogin(phone: String, userMsg: String, msgToken: String){
        thread {
            try {
                val json = JSONObject()
                    .put("phone", phone)
                    .put("messageCode", userMsg)
                    .put("messageCodeToken", msgToken)
                val stringBody =json.toString().toRequestBody("application/json;charset=utf-8".toMediaType())
                val client=OkHttpClient()
                val request=Request.Builder()
                    .url("https://gcsj.lidotcircle.ltd/apis/auth/refresh-token/quick")
                    .post(stringBody)
                    .build()
                val response=client.newCall(request).execute()
                val responseData=response.body?.string()
                returnMessage=JSONObject(responseData).getString("reason")
                showResponse(responseData.toString())
            }catch (e: Exception){
                Log.e("TAG", Log.getStackTraceString(e));
            }
        }
    }

    private fun showResponse(response: String) {
        runOnUiThread {
            // 在这里进行UI操作，将结果显示到界面上
            binding.responseText.text = response
        }
    }

}