package com.example.daoyun

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.daoyun.databinding.ActivityLoginBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.security.cert.CertPathValidatorException
import kotlin.concurrent.thread

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    private var reason: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ActivityCollector.finishAll()
        ActivityCollector.addActivity(this)

        //记住密码
        val preferences = getSharedPreferences("remember_user", MODE_PRIVATE)
        if(!preferences.getString("userName", "").equals("")&&!preferences.getString("password", "").equals(
                ""
            )){
            binding.etLoginUsername.setText(preferences.getString("userName", ""))
            binding.etLoginPwd.setText(preferences.getString("password", ""))
        }

        //忘记密码
        binding.tvLoginForgetPwd.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }

        //注册
        binding.btLoginRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.fastRegister.setOnClickListener{
            startActivity(Intent(this,FastLoginActivity::class.java))
        }

        //登录
        binding.btLoginSubmit.setOnClickListener {
            if(binding.etLoginUsername.text.toString() == ""){
                showAlertDialog("请输入账号!")
            }
            else if(binding.etLoginPwd.text.toString()=="")
                showAlertDialog("请输入密码!")
            else{
                login(binding.etLoginUsername.text.toString(),binding.etLoginPwd.text.toString())
                showAlertDialog(reason)
                when(reason){
                    "手机号未注册"->showAlertDialog(reason)
                        else -> showAlertDialog("登录成功！")
                }
            }
        }
    }

    private fun login(phone: String,  password: String){
        thread {
            try {
                val json = JSONObject()
                    .put("userName", phone)
                    .put("password", password)
                val stringBody =json.toString().toRequestBody("application/json;charset=utf-8".toMediaType())
                val client= OkHttpClient()
                val request= Request.Builder()
                    .url("https://gcsj.lidotcircle.ltd/apis/auth/refresh-token")
                    .post(stringBody)
                    .build()
                val response=client.newCall(request).execute()
                val responseData=response.body?.string()
                reason=JSONObject(responseData).getString("reason")
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
}