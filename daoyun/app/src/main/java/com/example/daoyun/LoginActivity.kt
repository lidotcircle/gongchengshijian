package com.example.daoyun

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.daoyun.databinding.ActivityLoginBinding
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import kotlin.concurrent.thread


class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    private var reason: String=""
    private var token:String=""
    private var jwtToken:String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ActivityCollector.finishAll()
        ActivityCollector.addActivity(this)

        //记住密码
        val preferences = getSharedPreferences("remember_user", MODE_PRIVATE)
        if(!preferences.getString("userName", "").equals("")&&!preferences.getString("password", "").equals("")){
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
                getJWT(token)
                Toast.makeText(this,"$token\n@@@@@\n$jwtToken",Toast.LENGTH_LONG).show()
                if(token!=null){
                    Toast.makeText(this, "登陆成功！", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                }
                else
                    Toast.makeText(this,"密码错误或者账号不存在",Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun getJWT(token:String){
        thread {
            try {
                val url = HttpUrl.Builder()
                    .scheme("https")
                    .host("gcsj.lidotcircle.ltd")
                    .addPathSegment("apis")
                    .addPathSegment("auth")
                    .addPathSegment("jwt")
                    .addQueryParameter("refreshToken", token)
                    .build()
                val client= OkHttpClient()
                val request= Request.Builder()
                    .url(url)
                    .get()
                    .build()
                val response=client.newCall(request).execute()
                val responseData=response.body?.string()
                //showResponse(responseData.toString())
                jwtToken = JSONObject(responseData).getString("jwtToken")
                val userData =getSharedPreferences("userData", Context.MODE_PRIVATE)
                val editor = userData.edit()
                editor.putString("jwtToken",jwtToken)
                editor.commit()
            }catch (e: Exception){
                Log.e("TAG", Log.getStackTraceString(e));
            }
        }.join()
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
                token=JSONObject(responseData)?.getString("token")
                //showResponse(token)
                val userData =getSharedPreferences("userData", Context.MODE_PRIVATE)
                val editor = userData.edit()
                editor.putString("token",token)
                editor.commit()
                //showResponse(responseData.toString())
            }catch (e: Exception){
                Log.e("TAG", Log.getStackTraceString(e));
            }
        }.join()
    }

    private fun showResponse(response: String) {
        runOnUiThread {
            // 在这里进行UI操作，将结果显示到界面上
            binding.responseText.text = "$response"
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