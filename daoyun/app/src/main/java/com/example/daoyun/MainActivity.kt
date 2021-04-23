package com.example.daoyun

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.daoyun.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ActivityCollector.finishAll()
        ActivityCollector.addActivity(this)

        initView()

        this.supportFragmentManager
            .beginTransaction()
            .add(binding.containerContent.id, MainFragment())
            .add(binding.containerContent.id, FindFragment())
            .hide(FindFragment())
            .add(binding.containerContent.id, MeFragment())
            .hide(MeFragment())
            .commit()
    }
    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

    private fun initView(){
        binding.menuMain.setOnClickListener(this)
        binding.menuFind.setOnClickListener(this)
        binding.menuMe.setOnClickListener(this)

        binding.IvMain.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.mipmap.nav_main_click
            )
        )
        binding.mainTv.setTextColor(Color.parseColor("#008CC9"))
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.menu_main->{
                this.supportFragmentManager
                    .beginTransaction()
                    .show(MainFragment())
                    .hide(FindFragment())
                    .hide(MeFragment())
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
                    .hide(MainFragment())
                    .show(FindFragment())
                    .hide(MeFragment())
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
                    .hide(MainFragment())
                    .hide(FindFragment())
                    .show(MeFragment())
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