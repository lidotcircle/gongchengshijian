package com.example.daoyun

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.daoyun.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding

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

        //登录
        binding.btLoginSubmit.setOnClickListener {

            if(binding.etLoginUsername.text.toString() == ""){
                showAlertDialog("请输入账号!")
            }
            else if(binding.etLoginPwd.text.toString()=="")
                showAlertDialog("请输入密码!")
            else{
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

    }

    private fun showAlertDialog(msg: String?) {
        runOnUiThread {
            val builder = AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("确定", null)
            builder.show()
        }
    }

    private fun sendRequestWithHttpURLConnection(username:String?, password:String?){

    }
    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }
}