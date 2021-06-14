package com.example.daoyun

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.daoyun.fragment.*

class ClassTabActivity : AppCompatActivity(), View.OnClickListener {
    private var mMenuRes: LinearLayout? = null
    private var mMenuMember: LinearLayout? = null
    private var mMenuActivity: LinearLayout? = null
    private var mMenuMessage: LinearLayout? = null
    private var mMenuMore: LinearLayout? = null
    private var resImageView: ImageView? = null
    private var memberImageView: ImageView? = null
    private var activityImageView: ImageView? = null
    private var messageImageView: ImageView? = null
    private var moreImageView: ImageView? = null
    private var mResFragment: ResFragment = ResFragment()
    private var mMemberFragment: MemberFragment = MemberFragment()
    private var mActivityFragment: ActivityFragment = ActivityFragment()
    private var mMessageFragment: MessageFragment = MessageFragment()
    private var mMoreFragment: MoreFragment = MoreFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_tab)
        val intent = intent
        courseName = intent.getStringExtra("courseName")
        classId = intent.getStringExtra("classId")
        enterType = intent.getStringExtra("enterType")
        teacherPhone = intent.getStringExtra("teacherPhone")
        initView()
        this.supportFragmentManager
            .beginTransaction()
            .add(R.id.container_class_fragment, mResFragment)
            .add(R.id.container_class_fragment, mMemberFragment)
            .add(R.id.container_class_fragment, mActivityFragment)
            .add(R.id.container_class_fragment, mMessageFragment)
            .add(R.id.container_class_fragment, mMoreFragment)
            .hide(mResFragment)
            .hide(mMemberFragment)
            .hide(mMessageFragment)
            .hide(mMoreFragment) //事物添加  默认：显示首页  其他页面：隐藏
            //提交
            .commit()
    }

    private fun initView() {
        mMenuRes = findViewById(R.id.menu_res)
        mMenuMember = findViewById(R.id.menu_member)
        mMenuActivity = findViewById(R.id.menu_activity)
        mMenuMessage = findViewById(R.id.menu_message)
        mMenuMore = findViewById(R.id.menu_more)
        resImageView = findViewById(R.id.Iv_res)
        memberImageView = findViewById(R.id.Iv_member)
        activityImageView = findViewById(R.id.Iv_activity)
        messageImageView = findViewById(R.id.Iv_message)
        moreImageView = findViewById(R.id.Iv_more)
        mMenuRes?.setOnClickListener(this)
        mMenuMember?.setOnClickListener(this)
        mMenuActivity?.setOnClickListener(this)
        mMenuMessage?.setOnClickListener(this)
        mMenuMore?.setOnClickListener(this)
        activityImageView?.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.nav_activity_pressed
            )
        )
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.menu_res -> {
                this.supportFragmentManager
                    .beginTransaction()
                    .show(mResFragment)
                    .hide(mMemberFragment)
                    .hide(mActivityFragment)
                    .hide(mMessageFragment)
                    .hide(mMoreFragment)
                    .commit()
                resImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_res_pressed
                    )
                )
                memberImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_member_normal
                    )
                )
                activityImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_activity_normal
                    )
                )
                messageImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_message_normal
                    )
                )
                moreImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_more_normal
                    )
                )
            }
            R.id.menu_member -> {
                this.supportFragmentManager
                    .beginTransaction()
                    .show(mMemberFragment)
                    .hide(mResFragment)
                    .hide(mActivityFragment)
                    .hide(mMessageFragment)
                    .hide(mMoreFragment)
                    .commit()
                resImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_res_normal
                    )
                )
                memberImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_member_pressed
                    )
                )
                activityImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_activity_normal
                    )
                )
                messageImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_message_normal
                    )
                )
                moreImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_more_normal
                    )
                )
            }
            R.id.menu_activity -> {
                this.supportFragmentManager
                    .beginTransaction()
                    .show(mActivityFragment)
                    .hide(mResFragment)
                    .hide(mMemberFragment)
                    .hide(mMessageFragment)
                    .hide(mMoreFragment)
                    .commit()
                resImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_res_normal
                    )
                )
                memberImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_member_normal
                    )
                )
                activityImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_activity_pressed
                    )
                )
                messageImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_message_normal
                    )
                )
                moreImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_more_normal
                    )
                )
            }
            R.id.menu_message -> {
                this.supportFragmentManager
                    .beginTransaction()
                    .show(mMessageFragment)
                    .hide(mResFragment)
                    .hide(mActivityFragment)
                    .hide(mMemberFragment)
                    .hide(mMoreFragment)
                    .commit()
                resImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_res_normal
                    )
                )
                memberImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_member_normal
                    )
                )
                activityImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_activity_normal
                    )
                )
                messageImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_message_pressed
                    )
                )
                moreImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_more_normal
                    )
                )
            }
            R.id.menu_more -> {
                this.supportFragmentManager
                    .beginTransaction()
                    .show(mMoreFragment)
                    .hide(mResFragment)
                    .hide(mActivityFragment)
                    .hide(mMessageFragment)
                    .hide(mMemberFragment)
                    .commit()
                resImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_res_normal
                    )
                )
                memberImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_member_normal
                    )
                )
                activityImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_activity_normal
                    )
                )
                messageImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_message_normal
                    )
                )
                moreImageView!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.nav_more_pressed
                    )
                )
            }
        }
    }

    companion object {
        var courseName: String? = null
        var classId: String? = null
        var enterType: String? = null
        var teacherPhone: String? = null
    }
}
