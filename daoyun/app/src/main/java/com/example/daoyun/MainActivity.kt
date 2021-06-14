package com.example.daoyun

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.daoyun.databinding.ActivityMainBinding
import com.example.daoyun.fragment.FindFragment
import com.example.daoyun.fragment.MainFragment
import com.example.daoyun.fragment.MeFragment

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    private var mMenuMain: LinearLayout? = null
    private var mMenuFind: LinearLayout? = null
    private var mMenuMe: LinearLayout? = null
    private var mainImageView: ImageView? = null
    private var findImageView: ImageView? = null
    private var meImageView: ImageView? = null
    private var mainTV: TextView? = null
    private var findTV: TextView? = null
    private var meTV: TextView? = null
    private var mMainFragment = MainFragment() //首页

    private var mFindFragment = FindFragment() //发现

    private var mMeFragment = MeFragment() //我的

    var userName: String? = null
    var icon: String? = null
    var loginType: String? = null
    var name: String? = null
    var phoneNumber: String? = null
    var BUFFER_SIZE = 8192

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ActivityCollector.finishAll()
        ActivityCollector.addActivity(this)

        initView()

        //获取管理类
        this.supportFragmentManager
            .beginTransaction()
            .add(R.id.container_content, mMainFragment)
            .add(R.id.container_content, mFindFragment)
            .hide(mFindFragment)
            .add(R.id.container_content, mMeFragment)
            .hide(mMeFragment)
            .commit()
        val userData=getSharedPreferences("userData",Context.MODE_PRIVATE)
        val toke=userData.getString("token","error")
        Toast.makeText(this,toke,Toast.LENGTH_LONG).show()

    }
    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

    private fun initView(){
        mMenuMain = findViewById(R.id.menu_main)
        mMenuFind = findViewById(R.id.menu_find)
        mMenuMe = findViewById(R.id.menu_me)
        mainImageView = findViewById(R.id.Iv_main)
        findImageView = findViewById(R.id.Iv_find)
        meImageView = findViewById(R.id.Iv_me)
        mainTV = findViewById(R.id.main_Tv)
        findTV = findViewById(R.id.find_Tv)
        meTV = findViewById(R.id.me_Tv)

        mMenuMain?.setOnClickListener(this)
        mMenuFind?.setOnClickListener(this)
        mMenuMe?.setOnClickListener(this)

        mainImageView?.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.mipmap.nav_main_click
            )
        )
        mainTV?.setTextColor(Color.parseColor("#008CC9"))
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.menu_main->{
                this.supportFragmentManager
                    .beginTransaction()
                    .show(mMainFragment)
                    .hide(mFindFragment)
                    .hide(mMeFragment)
                    .commit()
                binding.IvMain.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.mipmap.nav_main_click
                    )
                )
                binding.IvFind.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.mipmap.nav_find_normal
                    )
                )
                binding.IvMe.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.mipmap.nav_me_normal
                    )
                )
                binding.mainTv.setTextColor(Color.parseColor("#008CC9"))
                binding.findTv.setTextColor(Color.parseColor("#000000"))
                binding.meTv.setTextColor(Color.parseColor("#000000"))
            }
            R.id.menu_find->{
                this.supportFragmentManager
                    .beginTransaction()
                    .hide(mMainFragment)
                    .show(mFindFragment)
                    .hide(mMeFragment)
                    .commit()
                binding.IvMain.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.mipmap.nav_main_normal
                    )
                )
                binding.IvFind.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.mipmap.nav_find_click
                    )
                )
                binding.IvMe.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.mipmap.nav_me_normal
                    )
                )
                binding.mainTv.setTextColor(Color.parseColor("#000000"))
                binding.findTv.setTextColor(Color.parseColor("#008CC9"))
                binding.meTv.setTextColor(Color.parseColor("#000000"))
            }
            R.id.menu_me->{
                this.supportFragmentManager
                    .beginTransaction()
                    .hide(mMainFragment)
                    .hide(mFindFragment)
                    .show(mMeFragment)
                    .commit()
                binding.IvMain.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.mipmap.nav_main_normal
                    )
                )
                binding.IvFind.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.mipmap.nav_find_normal
                    )
                )
                binding.IvMe.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.mipmap.nav_me_click
                    )
                )
                binding.mainTv.setTextColor(Color.parseColor("#000000"))
                binding.findTv.setTextColor(Color.parseColor("#000000"))
                binding.meTv.setTextColor(Color.parseColor("#008CC9"))
            }
        }
    }

}